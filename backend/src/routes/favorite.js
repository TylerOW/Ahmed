const router = require('express').Router();
const controller = require('../controllers/favoriteController');
const { userAuth } = require('../middlewares/auth.middleware');

router.get('/', userAuth, controller.getFavorites);
router.post('/', userAuth, controller.addFavorite);
router.delete('/:stopNo', userAuth, controller.removeFavorite);

module.exports = router;
