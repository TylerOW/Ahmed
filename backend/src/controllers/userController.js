const { User } = require("../models");
const { isMongoId } = require("../utils/mongoUtil");
const { successMessage, errorMessage } = require("../utils/responseUtil");

/**
 *  Auth Token Required
 * */ 
module.exports.getUser = async(req,res)=>{
    try{
        const {_id : userId} = req.user;
        if(!userId){
            throw new Error("userId is required");
        }
        if(!isMongoId(userId)){
            throw new Error("Invalid userId");
        }
        let result = await User.findById(userId);
        if(!result){
            throw new Error("User does not exist");
        }
        return res.json(successMessage({data : result}));
    }catch(e){
        console.error("getUser Error : ", e);
        return res.status(400).json(errorMessage(e.message || e));
    }
}

module.exports.getUserById = async(req,res)=>{
    try{
        const {userId} = req.params;
        if(!userId){
            throw new Error("userId is required");
        }
        if(!isMongoId(userId)){
            throw new Error("Invalid userId");
        }
        let result = await User.findById(userId);
        if(!result){
            throw new Error("User does not exist");
        }
        return res.json(successMessage({data : result}));
    }catch(e){
        console.error("getUser Error : ", e);
        return res.status(400).json(errorMessage(e.message || e));
    }
}


module.exports.updateUser = async(req,res) =>{
    try{
        const {id, name, email, phone} = req.body;
        if(!id){
            throw new Error("userId is required");
        }
        if(!isMongoId(id)){
            throw new Error("Invalid userId");
        }let result = await User.findByIdAndUpdate(id,{
            name,
            email,
            phone
        },{runValidators : true, new : true});
        
        if(!result){
            throw new Error("User does not exist");
        }
        return res.json(successMessage({
            message : "User updated successfully",
            data : result
        }));
    }catch(e){
        console.error("updateUser Error : ", e);
        return res.json(errorMessage(e.message || e));
    }
}


module.exports.deleteUser = async(req,res) =>{
    try{
        const {userId} = req.params;
        if(!userId){
            throw new Error("userId is required");
        }
        if(!isMongoId(userId)){
            throw new Error("Invalid userId");
        }

        let result = await User.findByIdAndDelete(userId);
        if(!result){
            throw new Error("User does not exist");
        }

        return res.json(successMessage({
            message : "User deleted successfully",
            data : result
        }));
    }catch(e){
        console.error("deleteUser Error : ", e);
        return res.json(errorMessage(e.message || e));
    }
}

// Favorite Bus Stop APIs
module.exports.addFavoriteStop = async (req, res) => {
    try {
        const { stopNo } = req.body;
        if (!stopNo) {
            throw new Error("stopNo is required");
        }
        const userId = req.user?._id;
        if (!isMongoId(userId)) {
            throw new Error("Invalid userId");
        }
        const { BusStop } = require("../models");
        const stop = await BusStop.findOne({ stopNo });
        if (!stop) {
            throw new Error("Bus Stop does not exist");
        }
        let user = await User.findById(userId);
        if (!user) {
            throw new Error("User does not exist");
        }
        if (!user.favorites) {
            user.favorites = [];
        }
        if (user.favorites.find((id) => id.equals(stop._id))) {
            throw new Error("Stop already added to favorites");
        }
        user.favorites.push(stop._id);
        await user.save();
        return res.json(
            successMessage({ message: "Stop added to favorites", data: user })
        );
    } catch (e) {
        console.error("addFavoriteStop Error : ", e);
        return res.status(400).json(errorMessage(e.message || e));
    }
};

module.exports.removeFavoriteStop = async (req, res) => {
    try {
        const { stopNo } = req.body;
        if (!stopNo) {
            throw new Error("stopNo is required");
        }
        const userId = req.user?._id;
        if (!isMongoId(userId)) {
            throw new Error("Invalid userId");
        }
        const { BusStop } = require("../models");
        const stop = await BusStop.findOne({ stopNo });
        if (!stop) {
            throw new Error("Bus Stop does not exist");
        }
        let user = await User.findById(userId);
        if (!user) {
            throw new Error("User does not exist");
        }
        if (user.favorites) {
            const index = user.favorites.findIndex((id) => id.equals(stop._id));
            if (index >= 0) {
                user.favorites.splice(index, 1);
                await user.save();
            }
        }
        return res.json(
            successMessage({ message: "Stop removed from favorites", data: user })
        );
    } catch (e) {
        console.error("removeFavoriteStop Error : ", e);
        return res.status(400).json(errorMessage(e.message || e));
    }
};

module.exports.getFavoriteStops = async (req, res) => {
    try {
        const userId = req.user?._id;
        if (!isMongoId(userId)) {
            throw new Error("Invalid userId");
        }
        let user = await User.findById(userId).populate("favorites");
        if (!user) {
            throw new Error("User does not exist");
        }
        return res.json(successMessage({ data: user.favorites }));
    } catch (e) {
        console.error("getFavoriteStops Error : ", e);
        return res.status(400).json(errorMessage(e.message || e));
    }
};
