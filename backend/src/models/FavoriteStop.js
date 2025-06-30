const mongoose = require('mongoose');

const favoriteStopSchema = new mongoose.Schema({
    userId: {
        type: mongoose.Schema.Types.ObjectId,
        ref: 'User',
        required: true
    },
    stopNo: {
        type: String,
        required: true
    }
}, {
    timestamps: true
});

module.exports = mongoose.model('FavoriteStop', favoriteStopSchema);
