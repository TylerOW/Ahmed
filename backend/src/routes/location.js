const router = require('express').Router();
const controller = require('../controllers/locationController');

router.post('/bus', controller.upsertBusLocation);
router.get('/bus/:busId', controller.getBusLocation);

module.exports = router;
