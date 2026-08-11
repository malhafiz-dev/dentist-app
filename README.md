# Dentist App

Android application for detecting common dental conditions using a YOLOv8-based computer vision model.

## Overview

Dentist App is an Android application developed as part of a collaborative Capstone Project in the Informatics undergraduate program at Universitas Andalas.

The application is designed to help users identify several common dental conditions through image-based detection. It integrates a YOLOv8 object detection model into an Android application and also provides a chatbot feature for dental-related information.

The project was developed by a three-member team with different responsibilities across mobile application development, computer vision, and chatbot development.

## Detected Dental Conditions

The application is designed to detect the following dental conditions:

- Gingivitis
- Dental Caries
- Calculus
- Mouth Ulcer
- Tooth Discoloration

## Features

- User authentication
- User registration
- Dental condition detection using YOLOv8
- Image-based dental condition analysis
- YOLOv8 model integration into an Android application
- Dental-related chatbot
- Healthcare facility information
- Dental examination history
- Detailed results for previous examinations

## Application Pages

The application consists of the following main pages:

1. **Login** — User authentication.
2. **Register** — New user registration.
3. **Homepage** — Main application dashboard and access to core features.
4. **Dental Scan** — Allows users to perform dental image scanning and detection.
5. **Detection Result** — Displays the result of the dental condition detection.
6. **Healthcare Facilities** — Provides information about available healthcare facilities.
7. **Dental History** — Displays previous dental examination records in a grid layout.
8. **History Result** — Displays detailed detection results for a selected history record.
9. **Chatbot** — Provides a conversational interface for dental-related information.

## My Contribution

As a member of the development team, my primary responsibilities included:

- Developing the Android application frontend using Kotlin.
- Developing the application backend and supporting application logic.
- Integrating the YOLOv8 detection model into the Android application.
- Connecting the mobile application with the dental detection functionality.
- Implementing application features required for the capstone project.

## Team

The project was developed by a three-member team with the following responsibilities:

| Role | Responsibility |
| --- | --- |
| Mobile Application Developer | Android frontend, backend, and YOLOv8 model integration |
| Computer Vision Developer | YOLOv8 model development and training |
| Chatbot Developer | Chatbot model and chatbot feature development |

## Tech Stack

### Mobile Development

- Kotlin
- Android Studio
- Android SDK

### Machine Learning

- YOLOv8
- Computer Vision
- Object Detection

### Backend

- Backend services
- REST API integration

### Development Tools

- Git
- GitHub
- Android Studio

## Application Preview

Screenshots of the application interface will be added to this section.


Suggested screenshots:

### Authentication

<p align="center">
  <img width="200" alt="Login" src="https://github.com/user-attachments/assets/7b8a7673-26b2-4ca1-b525-e322cc862133" />
  <img width="200" alt="Register" src="https://github.com/user-attachments/assets/eb40ca1b-be78-4717-bbd0-3ed722341b0b" />
</p>

### Main Application
<p align="center">
  <img width="200" alt="Homepage" src="https://github.com/user-attachments/assets/2a2598bc-f3d4-49d9-bb80-5c203a5757f4" />
  <img width="200" alt="Dental Scan" src="https://github.com/user-attachments/assets/0bf31049-d9ea-4eba-9872-0fcefd154ec4" />
  <img width="200" alt="Detection Result" src="https://github.com/user-attachments/assets/81e8ec1c-f3ab-4e4a-9b77-d990b6bd4981" />
</p>

### History & Healthcare

<p align="center">
  <img width="200" alt="Healthcare Facilities" src="https://github.com/user-attachments/assets/744b7182-043e-4a78-935f-5a4aa2517416" />
  <img width="200" alt="Dental History" src="https://github.com/user-attachments/assets/285ca456-08f1-4b90-9784-43de25e753b3" />
  <img width="200" alt="History Result" src="https://github.com/user-attachments/assets/ed3b8409-da9f-4cfb-b9eb-a647554b7844" />
</p>

### Chatbot

<p align="center">
  <img width="200" alt="Chatbot" src="https://github.com/user-attachments/assets/b3a45600-7d7c-4d83-9a44-21b9a66cf0a2" />
</p>


## Project Structure

The project follows a standard Android application structure:

```text
dentist-app/
├── app/
├── gradle/
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── settings.gradle.kts
└── README.md
```

## Project Status

**Academic Capstone Project**

The application was developed and tested primarily in a local development environment.

The backend service is not currently deployed, so features that require an active server connection may not be fully accessible.

The project is maintained as an academic and portfolio project demonstrating Android application development and the integration of computer vision models into a mobile application.

## Limitations

- The backend is not currently hosted in a production environment.
- Some application features require an active backend/server connection.
- The YOLOv8 detection model is intended for academic and demonstration purposes.
- The application should not be considered a substitute for professional dental diagnosis.

## Learning Outcomes

Through this project, I gained practical experience in:

- Android application development using Kotlin.
- Frontend and backend development for mobile applications.
- Integrating machine learning models into mobile applications.
- Working with YOLOv8 for object detection.
- Developing applications collaboratively in a team.
- Connecting application components with machine learning functionality.

## Acknowledgements

This project was developed as part of the Capstone Project course at Universitas Andalas.

The project utilizes YOLOv8-based object detection technology as part of its computer vision functionality.

## License

This project was developed for academic and portfolio purposes.
