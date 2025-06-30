const mongoose = require('mongoose');

const busLocationSchema = new mongoose.Schema({
  busId: {
    type: String,
    required: true,
    unique: true,
  },
  latitude: {
    type: Number,
    required: true,
  },
  longitude: {
    type: Number,
    required: true,
  },
  timestamp: {
    type: Date,
    required: true,
  },
});

module.exports = mongoose.model('BusLocation', busLocationSchema, 'bus_locations');
