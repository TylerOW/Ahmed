# BusTrackr Backend

This module provides the Node.js backend for the BusTrackr application. It exposes REST APIs and real-time Socket.IO channels for tracking buses and user interactions.

## Getting Started

1. Install dependencies:

```bash
cd backend
npm install
```

2. Copy `.env.example` to `.env` and adjust the values as needed. The following variables are required:

- `PORT` – Port the Express server will listen on (default `5000`).
- `MONGO_URI` – MongoDB connection string.
- `JWT_SECRET` – Secret key used to sign JSON Web Tokens.

3. Launch the development server:

```bash
npm run dev
```

This runs the server with `nodemon` and loads variables from `.env`.

### Favorite Stops API
The backend supports managing a passenger's favorite bus stops.

- `POST /api/user/addFavoriteStop` – Add a stop to the authenticated user's favorites. Provide `{ "stopNo": "<stop number>" }` in the request body.
- `POST /api/user/removeFavoriteStop` – Remove a stop from favorites using the same payload.
- `GET /api/user/getFavoriteStops` – Retrieve a list of the user's favorite bus stops. Requires the `Authorization` header with a valid token.

### Driver Accounts

Create a driver account by setting `isDriver` to `true` when calling `POST /api/auth/registerUser`.

Alternatively, you can run the interactive script in `scripts/createDriver.js` to insert a driver directly into MongoDB:

```bash
cd backend
node scripts/createDriver.js
```

The script will prompt for the driver's name, phone number, email and password, then create the record using the `MONGO_URI` from your `.env` file.

Create a driver account by setting `isDriver` to `true` when calling `POST /api/auth/registerUser`. You can also run the script in `scripts/createDriver.js` to insert a sample driver directly into MongoDB.

