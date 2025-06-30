const { BusLocation, BusStop, User } = require('../models');
const { successMessage, errorMessage } = require('../utils/responseUtil');
const { calculateDistance } = require('../utils/locationUtil');
const { getIO } = require('../services/socket/io');

// simple cooldown map to avoid spamming notifications
const lastNotified = {};

module.exports.upsertBusLocation = async (req, res) => {
  try {
    const { busId, latitude, longitude, timestamp } = req.body;
    if (!busId || latitude === undefined || longitude === undefined) {
      throw new Error('busId, latitude and longitude are required');
    }
    const ts = timestamp ? new Date(timestamp) : new Date();
    const doc = await BusLocation.findOneAndUpdate(
      { busId },
      { busId, latitude, longitude, timestamp: ts },
      { upsert: true, new: true, setDefaultsOnInsert: true }
    );

    try {
      const io = getIO();
      if (io) {
        const stops = await BusStop.find();
        for (const stop of stops) {
          const dist = calculateDistance(
            latitude,
            longitude,
            stop.location.lat,
            stop.location.lng
          );
          if (dist <= 500) {
            const key = `${busId}_${stop._id}`;
            const last = lastNotified[key];
            if (last && Date.now() - last < 3 * 60 * 1000) {
              continue;
            }
            const users = await User.find({ favorites: stop._id }).select('_id');
            for (const u of users) {
              io.of('/user').to(u._id.toString()).emit('busNearStop', {
                stopNo: stop.stopNo,
                stopName: stop.name,
                busId,
                distance: dist,
              });
            }
            lastNotified[key] = Date.now();
          }
        }
      }
    } catch (notifyErr) {
      console.error('notify users error', notifyErr);
    }
    return res.json(successMessage({ message: 'Location updated', data: doc }));
  } catch (e) {
    console.error('upsertBusLocation Error : ', e);
    return res.status(400).json(errorMessage(e.message || e));
  }
};

module.exports.getBusLocation = async (req, res) => {
  try {
    const { busId } = req.params;
    if (!busId) {
      throw new Error('busId is required');
    }
    const doc = await BusLocation.findOne({ busId });
    if (!doc) {
      return res.status(404).json(errorMessage('Bus location not found'));
    }
    return res.json({
      latitude: doc.latitude,
      longitude: doc.longitude,
      timestamp: doc.timestamp.toISOString(),
    });
  } catch (e) {
    console.error('getBusLocation Error : ', e);
    return res.status(400).json(errorMessage(e.message || e));
  }
};
