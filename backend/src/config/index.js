// src/config/index.js
/**
 * Central place to expose all environment variables
 * required by the backend.  It loads .env once, then
 * exports typed constants other files can import.
 */

require("dotenv").config();

module.exports = {
  /** Express listening port (default 5000) */
  PORT:        process.env.PORT || 5000,

  /** Full MongoDB connection string */
  MONGO_URI:   process.env.MONGO_URI,

  /** Secret key used to sign JWTs */
  JWT_SECRET:  process.env.JWT_SECRET,
};
