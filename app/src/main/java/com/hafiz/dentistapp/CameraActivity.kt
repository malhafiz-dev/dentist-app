package com.hafiz.dentistapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.hafiz.dentistapp.Constants.LABELS_PATH
import com.hafiz.dentistapp.Constants.MODEL_PATH
import com.hafiz.dentistapp.databinding.ActivityCameraBinding
import com.hafiz.dentistapp.model.BaseResponse
import com.hafiz.dentistapp.network.ApiClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity(), Detector.DetectorListener {
    private lateinit var binding: ActivityCameraBinding
    private var isFrontCamera = false

    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null
    private lateinit var detector: Detector

    private lateinit var cameraExecutor: ExecutorService

    private var lastDetectedBitmap: Bitmap? = null
    private var lastDetectedType: String? = null
    private var lastBoundingBoxes: List<BoundingBox> = emptyList()
    private var currentUserId: Int = -1
    private var currentUsername: String? = null
    private var isFromGallery = false

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { processGalleryImage(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentUserId = intent.getIntExtra("USER_ID", -1)
        currentUsername = intent.getStringExtra("USERNAME")

        detector = Detector(baseContext, MODEL_PATH, LABELS_PATH, this)
        detector.setup()

        if (allPermissionsGranted()) {
            binding.viewFinder.post { startCamera() }
        } else {
            requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        binding.captureButton.setOnClickListener {
            saveDetectionResult()
        }

        binding.switchCameraButton.setOnClickListener {
            isFrontCamera = !isFrontCamera
            startCamera()
        }

        binding.galleryButton.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
    }

    private fun processGalleryImage(uri: Uri) {
        try {
            val bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }.copy(Bitmap.Config.ARGB_8888, true)

            synchronized(this) {
                lastDetectedBitmap = bitmap
            }
            isFromGallery = true
            detector.detect(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Gagal memuat gambar dari galeri", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()
            bindCameraUseCases()
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindCameraUseCases() {
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")

        val rotation = binding.viewFinder.display.rotation

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(if (isFrontCamera) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK)
            .build()

        preview = Preview.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setTargetRotation(rotation)
            .build()

        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetAspectRatio(AspectRatio.RATIO_4_3)
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
            .build()

        imageAnalyzer?.setAnalyzer(cameraExecutor) { imageProxy ->
            val bitmapBuffer = Bitmap.createBitmap(imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888)
            imageProxy.use { bitmapBuffer.copyPixelsFromBuffer(imageProxy.planes[0].buffer) }

            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                if (isFrontCamera) {
                    postScale(-1f, 1f, imageProxy.width.toFloat(), imageProxy.height.toFloat())
                }
            }

            val rotatedBitmap = Bitmap.createBitmap(bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height, matrix, true)

            synchronized(this) {
                lastDetectedBitmap = rotatedBitmap
            }

            detector.detect(rotatedBitmap)
        }

        cameraProvider.unbindAll()

        try {
            camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
            Toast.makeText(this, "Use case binding failed: ${exc.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDetect(boundingBoxes: List<BoundingBox>, inferenceTime: Long) {
        runOnUiThread {
            binding.inferenceTime.text = "${inferenceTime}ms"
            binding.overlay.apply {
                setResults(boundingBoxes)
                invalidate()
            }

            synchronized(this) {
                lastDetectedType = boundingBoxes.firstOrNull()?.clsName
                lastBoundingBoxes = boundingBoxes
            }

            if (isFromGallery) {
                saveDetectionResult()
                isFromGallery = false
            }
        }
    }

    override fun onEmptyDetect() {
        runOnUiThread {
            binding.overlay.invalidate()
            synchronized(this) {
                lastDetectedType = null
                lastBoundingBoxes = emptyList()
            }
            if (isFromGallery) {
                Toast.makeText(this, "Tidak ada penyakit yang terdeteksi pada gambar yang dipilih.", Toast.LENGTH_SHORT).show()
                isFromGallery = false
            }
        }
    }

    private fun saveDetectionResult() {
        val bitmapToSave: Bitmap?
        val typeToSave: String?
        val bboxesToSave: List<BoundingBox>

        synchronized(this) {
            bitmapToSave = lastDetectedBitmap
            typeToSave = lastDetectedType
            bboxesToSave = lastBoundingBoxes
        }

        if (currentUserId == -1) {
            Toast.makeText(this, "Error: User ID tidak valid.", Toast.LENGTH_SHORT).show()
            return
        }

        if (bitmapToSave == null || typeToSave == null) {
            Toast.makeText(this, "Tidak ada penyakit terdeteksi untuk disimpan", Toast.LENGTH_SHORT).show()
            return
        }

        val finalBitmap = drawOverlayOnBitmap(bitmapToSave, bboxesToSave)

        val file = bitmapToFile(finalBitmap)
        if (file == null) {
            Toast.makeText(this, "Gagal membuat file gambar", Toast.LENGTH_SHORT).show()
            return
        }

        val userIdRequestBody = currentUserId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val typeRequestBody = typeToSave.toRequestBody("text/plain".toMediaTypeOrNull())
        val imageRequestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, imageRequestBody)

        val call = ApiClient.userService.saveDetection(userIdRequestBody, typeRequestBody, imagePart)
        call.enqueue(object : Callback<BaseResponse<Any>> {
            override fun onResponse(call: Call<BaseResponse<Any>>, response: Response<BaseResponse<Any>>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
                    val intent = Intent(this@CameraActivity, ResultActivity::class.java).apply {
                        putExtra(ResultActivity.EXTRA_IMAGE_PATH, file.absolutePath)
                        putExtra(ResultActivity.EXTRA_DISEASE_TYPE, typeToSave)
                        putParcelableArrayListExtra(ResultActivity.EXTRA_BOUNDING_BOXES, ArrayList(bboxesToSave))
                        putExtra("USER_ID", currentUserId)
                        putExtra("USERNAME", currentUsername)
                        putExtra("TIMESTAMP", timestamp)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CameraActivity, "Gagal menyimpan: ${response.body()?.message}", Toast.LENGTH_LONG).show()
                    file.delete()
                }
            }

            override fun onFailure(call: Call<BaseResponse<Any>>, t: Throwable) {
                Toast.makeText(this@CameraActivity, "Gagal terhubung ke server: ${t.message}", Toast.LENGTH_LONG).show()
                file.delete()
            }
        })
    }

    private fun drawOverlayOnBitmap(bitmap: Bitmap, results: List<BoundingBox>): Bitmap {
        val outputBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(outputBitmap)
        val overlayView = OverlayView(this)
        overlayView.setResults(results)
        overlayView.draw(canvas)
        return outputBitmap
    }

    private fun bitmapToFile(bitmap: Bitmap): File? {
        return try {
            val file = File(cacheDir, "detection_image_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) {
        if (it[Manifest.permission.CAMERA] == true) {
            binding.viewFinder.post { startCamera() }
        } else {
            Toast.makeText(this, "Izin kamera ditolak.", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.clear()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraActivity"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
