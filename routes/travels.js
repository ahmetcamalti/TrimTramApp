var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var Travel = require('../models/travel');
var Place = require('../models/place');
var helpers = require('../helpers');

/* GET travel listing. */
router.get('/all', function(req, res, next) {
  // get all the travels
  Travel.find({}, function(err, result) {
    if (err) {
      // error feedback for client
      response = {
        success: 0,
        message: "There is an error in database!",
      };
    } else {
      // save the result into the response object for client.
      response = {
        success: 1,
        message: "Fetched all travels data with success",
        travels: result
      };
    }
    res.json(response);
  })
  .populate('Place')
  .populate('User');
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
router.get('/clear', function(req, res, next) {

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

  console.log(places);

  for (var i = 0; i < data.places.length; i++){
    places[i] = mongoose.Types.ObjectId(data.places[i]);
  }

  console.log(times);
  console.log(places);

  Travel.find({time: {$in: times}, place: {$in:places}}, function(err, result){
    if(err) throw err;
    console.log(result);

    res.json(result);
  });
});

router.get('/add/:travel_id/:uid', function(req, res, next){
  var user_id = req.params.uid;

  Travel.findById(req.params.travel_id).exec()
  .then(function(travel){

    if (travel.users.indexOf(user_id) == -1){
      travel.users.push(user_id);
      travel.going_cnt = travel.going_cnt + 1;
      travel.save(function(err, tr){
        if (err) throw err;
        console.log('added user to travel');
        res.json(tr);
      });
    }else{
      console.log('user already going to event');
      res.json('user already going to event');
    }
  })
  .then(undefined, function(err){
    //Handle error
    console.log(err);
  })
});

router.get('/remove/:travel_id/:uid', function(req, res, next){
  var user_id = req.params.uid;

  Travel.findById(req.params.travel_id).exec()
  .then(function(travel){

    if (travel.users.indexOf(user_id) == -1){
      console.log('user already not going to event');
      res.json('user already going to event');
    }else{
      travel.users.pull(user_id);
      travel.going_cnt = travel.going_cnt - 1;
      travel.save(function(err, tr){
        if (err) throw err;
        console.log('removed user from travel');
        res.json(tr);
      });
    }

  })
  .then(undefined, function(err){
    //Handle error
    console.log(err);
  })
});

// generates dummy travels
router.get('/dummy', function(req, res, next){
  
  for (var i = 0; i < 10; i++){
    
    Place.findRandom({},{},{limit:1},function(err, results){
      if (err) throw err;
      var tit = helpers.dummy_event_names[helpers.getRandomInt(0,3)];
      var time = helpers.getRandomInt(0,23);
      var travel = Travel({title: tit, time: time, place: mongoose.Types.ObjectId(results._id), going_cnt:0});
      travel.save(function(err, t){
        if (err) {
          console.log('error in dummy travel generation');
        }
        if (i == 9){
          console.log("added 10 travels");
        }
      });
    });
  }
  res.json('adding travels');
});

module.exports = router;
