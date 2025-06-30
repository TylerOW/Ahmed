const { User, BusStop } = require('../models');
const { isMongoId } = require('../utils/mongoUtil');
const { successMessage, errorMessage } = require('../utils/responseUtil');

module.exports.getFavorites = async (req, res) => {
  try {
    const userId = req.user?._id;
    if (!isMongoId(userId)) throw new Error('Invalid userId');
    const user = await User.findById(userId).populate('favorites');
    if (!user) throw new Error('User does not exist');
    const stops = user.favorites?.map(stop => stop.stopNo) || [];
    return res.json(successMessage({ data: stops }));
  } catch (e) {
    console.error('getFavorites Error : ', e);
    return res.status(400).json(errorMessage(e.message || e));
  }
};

module.exports.addFavorite = async (req, res) => {
  try {
    const { stopNo } = req.body;
    if (!stopNo) throw new Error('stopNo is required');
    const userId = req.user?._id;
    if (!isMongoId(userId)) throw new Error('Invalid userId');
    const stop = await BusStop.findOne({ stopNo });
    if (!stop) throw new Error('Bus Stop does not exist');
    const user = await User.findById(userId);
    if (!user) throw new Error('User does not exist');
    if (!user.favorites) user.favorites = [];
    if (user.favorites.find(id => id.equals(stop._id))) {
      throw new Error('Stop already added to favorites');
    }
    user.favorites.push(stop._id);
    await user.save();
    return res.json(successMessage({ message: 'Stop added to favorites' }));
  } catch (e) {
    console.error('addFavorite Error : ', e);
    return res.status(400).json(errorMessage(e.message || e));
  }
};

module.exports.removeFavorite = async (req, res) => {
  try {
    const { stopNo } = req.params;
    if (!stopNo) throw new Error('stopNo is required');
    const userId = req.user?._id;
    if (!isMongoId(userId)) throw new Error('Invalid userId');
    const stop = await BusStop.findOne({ stopNo });
    if (!stop) throw new Error('Bus Stop does not exist');
    const user = await User.findById(userId);
    if (!user) throw new Error('User does not exist');
    if (user.favorites) {
      const index = user.favorites.findIndex(id => id.equals(stop._id));
      if (index >= 0) {
        user.favorites.splice(index, 1);
        await user.save();
      }
    }
    return res.json(successMessage({ message: 'Stop removed from favorites' }));
  } catch (e) {
    console.error('removeFavorite Error : ', e);
    return res.status(400).json(errorMessage(e.message || e));
  }
};
