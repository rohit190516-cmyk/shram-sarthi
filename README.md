# Shram Sarthi

Shram Sarthi is an Android application designed to connect workers with job opportunities and provide work records, profile management, and grievance support.

## Key Features

- Role-based dashboards for workers, contractors, builders, and customers.
- Phone authentication using Firebase Authentication.
- User profiles and job data using Cloud Firestore.
- Job discovery and applications.
- Work records and complaint-support screens.
- Voice assistance, subject to the implemented functionality and backend configuration.

## Technology Stack

- Kotlin
- Android
- Jetpack Compose
- Material 3
- Firebase Authentication
- Cloud Firestore

## Setup

1. Install Android Studio with a compatible JDK and Android SDK.
2. Clone this repository.
3. Register the Android application in your Firebase project using package ID `com.example.shramsarthi`.
4. Add your Firebase configuration as `app/google-services.json`.
5. Configure Firebase Authentication and Firestore.
6. Open the project in Android Studio and complete Gradle sync.

## Build

On Windows, run:

```powershell
.\gradlew.bat assembleDebug
```

The debug APK is generated at `app/build/outputs/apk/debug/app-debug.apk`.

## Limitations

OTP delivery, role switching, job search, profile persistence, complaint handling, and voice/AI functionality must be tested before being considered fully operational.

## Security

Never commit service-account credentials, private keys, signing keys, access tokens, or personal user data. Configure and verify Firestore security rules before deployment.
