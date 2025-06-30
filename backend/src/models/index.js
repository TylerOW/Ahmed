const User = require("./User");
const Driver = require("./Driver");
const Bus = require("./Bus");
const BusRoute = require("./BusRoute");
const BusStop = require("./BusStop");
const FavoriteStop = require("./FavoriteStop");
const RouteFeedback = require("./Feedback");
const locationSchema = require("./locationSchema");
const BusLocation = require("./BusLocation");

module.exports = {
    User,
    Driver,
    Bus,
    BusRoute,
    BusStop,
    FavoriteStop,
    RouteFeedback,
    locationSchema,
    BusLocation
}