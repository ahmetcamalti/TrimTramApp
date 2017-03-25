var express = require('express');
var router = express.Router();

var Travel = require('../models/travel');

/* GET travel listing. */
router.get('/allTravel', function(req, res, next) {
  // get all the travels
  Travel.find({}, function(err, result) {
    if (err) throw err;

    // save the result into the response object.
    res.json(result);
  });
});

/* add new travel */
router.get('/addTravel/:travelData', function(req, res, next) {
  // check is there a param?
  if (!req.params.travelData) {
    response = {
      success: 0,
      message: "There isn't any travel parameter!"
    };

    // response error
    res.json(response);
  }

  // create json objset from string
  var theTravelData = JSON.parse(req.params.travelData);

  // create a new travel
  var newTravel = Travel(theTravelData);

  // save the travel
  newTravel.save(function(err, theTravel) {
    if (err) throw err;

    // message for server
    console.log('New Travel created!');

    // response for client
    response = {
      success: 1,
      message: "Travel added to database with success!",
      travel: theTravel
    }

    res.json(response);
  });
});

/* remove all travels */
router.get('/removeAllTravel', function(req, res, next) {

  User.remove({}, function(err, result) {
    // remove all travels from database
    if (err) {
      var message = "There is an error on database";

      // message for server
      console.log(message);

      // response message for client
      response = {
        success: 0,
        message: message
      };
    } else {
      var message = "All travels removed succesfully from database!";

      // message for server
      console.log(message);

      // response message for client
      response = {
        success: 1,
        message: message
      };
    }

    res.json(response);
  });
});

module.exports = router;
