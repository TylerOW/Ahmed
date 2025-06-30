# BusTrackr Monorepo

This repository contains the frontend Android application and backend Ktor service.

## Prerequisites

Make sure JDK 17 is installed and `JAVA_HOME` points to it. The backend can be
built from the repository root while the Android app uses its own wrapper in the
`frontend` directory.

## Build Frontend

Run the following command from the `frontend` directory to build the Android app:

```bash
cd frontend
./gradlew assembleDebug
```

You can also open the `frontend` folder in Android Studio.

## Run Backend

Run the backend service using:

```bash
./gradlew :backend:run
```

(Ensure you have the backend module sources inside `backend`.)

## Develop Locally

Start the backend and run the Android app on an emulator or device. Both modules share the same Gradle wrapper at the repo root.
