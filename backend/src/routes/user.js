const router = require("express").Router();
const controller = require("../controllers/userController");
const {userAuth} = require("../middlewares/auth.middleware")

router.get("/getUser", userAuth, controller.getUser); // Using token
router.get("/getUserById/:userId", controller.getUserById);
router.post("/updateUser", controller.updateUser);
router.delete("/deleteUser/:userId", controller.deleteUser);
router.post("/addFavoriteStop", userAuth, controller.addFavoriteStop);
router.post("/removeFavoriteStop", userAuth, controller.removeFavoriteStop);
router.get("/getFavoriteStops", userAuth, controller.getFavoriteStops);

module.exports=router;