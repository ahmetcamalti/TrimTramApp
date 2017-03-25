var express = require('express');
var router = express.Router();

var Travel = require('../models/travel');

/* GET travel listing. */
router.get('/getAllTravels', function(req, res, next) {
  // get all the travels
  Travel.find({}, function(err, result) {
    if (err) throw err;

    // save the result into the response object.
    res.json(result);
  }).populate('Place');
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

/* get travel by places */
router.get('/getTravelByPlace/:place', function(req, res, next) {
  // create json objset from string
  var thePlace = req.params.place;

  // get all the travels filtering by places
  Travel.find({place: thePlace}).exec()
  .then(function(travels){
    res.json(travels);
  })
  .then(undefined, function(err){
    //Handle error
  })
});

/* remove all travels */
router.get('/removeAllTravels', function(req, res, next) {

  Travel.remove({}, function(err, result) {
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

router.get('/candidates/:specs', function(req, res, next){
  var data = JSON.parse(req.params.specs);
  var times = data.times;
  var places = data.places;


  Travel.find({time: {$in: times}, place: {$in:places}}, function(err, result){
    if(err) throw err;
    console.log(result);

    res.json(result);
  });
});

router.get('/addUser/:uid', function(req, res, next){
  var user_id = req.params.uid;

  Travel.findById(user_id).exec()
  .then(function(travel){

    if (travel.users.indexOf(user_id) == -1){
      travel.users.push(user_id);
      travel.going_cnt = travel.going_cnt + 1;
      travel.save(function(err, tr){
        if (err) throw err;
        console.log()
        res.json(tr);
      });
    }else{
      going_cnt
    }

  })
  .then(undefined, function(err){
    //Handle error
    console.log(err);
  })
});

module.exports = router;
