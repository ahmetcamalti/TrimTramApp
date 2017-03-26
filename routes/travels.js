var express = require('express');
var router = express.Router();
var mongoose = require('mongoose');

var Travel = require('../models/travel');
var Place = require('../models/place');
var helpers = require('../helpers');

/* GET travel listing. */
router.get('/all', function(req, res, next) {
  // get all the travels
  Travel.find({})
  .populate('place')
  .populate('users')
  .exec(function(err, result) {
    if (err) {
      // error feedback for client
      response = helpers.respond(0, "There is an error in database!");
      console.log(response);
    } else {
      // save the result into the response object for client.
      response = helpers.respond(1, "Fetched all travels data with success", result);
    }
    res.json(response);
  });
});

/* add new travel */
router.get('/addTravel/:travelData', function(req, res, next) {
  // check is there a param?
  if (!req.params.travelData) {
    response = helpers.respond(0,"There isn't any travel parameter!");
    console.log(response);
  } else {
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
      response = helpers.respond(1,"Travel added to database with success!");
    });
  }

  res.json(response);
});

/* get travel by title */
router.get('/getTravelByTitle/:title', function(req, res, next) {
  // create json objset from string
  var query = req.params.title;
  
  // get all the travels filtering by places
  Travel.find({title: {$regex:query, $options:"i"}}).exec(function(err, result){
    if (err){
      response = helpers.respond(0, "There isn't any travel parameter!");
      console.log(response);
    }else{
      esponse = helpers.respond(1,"OK", result);
    }
    res.json(response);
  });
});

/* get travel by its place name */
router.get('/getTravelByPlace/:title', function(req, res, next) {
  // create json objset from string
  var query = req.params.title;
  
  // get all the travels filtering by places
  Travel.find({}).populate('place').exec()
  .then(function(travels){
    var result = [];

    for (var i = 0; i < travels.length; i++){
      var curr = travels[i].place.title;
      if (curr.indexOf(query) !== -1){
        result.push(travels[i]);
      }
    }
    response = helpers.respond(1, "OK", result);
  })
  .then(undefined, function(err){
    //Handle error
    response = helpers.respond(0, 'err get travel from its name');
  })

  res.json(response);
});

/* remove all travels */
router.get('/clear', function(req, res, next) {

  Travel.remove({}, function(err, result) {
    // remove all travels from database
    if (err) {
      // response message for client
      response = helpers.respond(0,"clear travel error");
    } else {
      response = helpers.respond(1,"clear travel OK", []);
    }

    res.json(response);
  });
});

// place and time filtering for travels
router.get('/candidates/:specs', function(req, res, next){
  var data = JSON.parse(req.params.specs);
  var times = data.times;
  var places = new Array(data.places.length);

  for (var i = 0; i < data.places.length; i++){
    places[i] = mongoose.Types.ObjectId(data.places[i]);
  }

  Travel.find({time: {$in: times}, place: {$in:places}})
  .populate('place').populate('users').exec(function(err, result){
    if(err){
      response = helpers.respond(0, err);
      console.log(response);
    }else{
      if (result.length == 0){
        response = helpers.respond(2, "success", result);
      }else{
        response = helpers.respond(1, "success", result);
      }
    }
    res.json(response);
  });
});

// subscribe a user to a travel
router.get('/add/:travel_id/:uid', function(req, res, next){
  var user_id = req.params.uid;

  Travel.findById(req.params.travel_id).exec()
  .then(function(travel){

    if (travel.users.indexOf(user_id) == -1){
      travel.users.push(user_id);
      travel.going_cnt = travel.going_cnt + 1;
      var response;
      travel.save(function(err, tr){
        if (err){
          response = helpers.respond(0, err);
          console.log(response);
        }else{
          response = helpers.respond(1, 'added user to travel', tr);  
        }
      });
    }else{
      response = helpers.respond(0, 'user already going to event');
    }
  })
  .then(undefined, function(err){
    //Handle error
    response = helpers.respond(0, err);
    console.log(response);
  })

  res.json(response);
});

// unsubscribe a user from a travel
router.get('/remove/:travel_id/:uid', function(req, res, next){
  var user_id = req.params.uid;

  Travel.findById(req.params.travel_id).exec()
  .then(function(travel){
    var response;
    if (travel.users.indexOf(user_id) == -1){
      response = respond(0, 'user already going to event');
      console.log(response);
    }else{
      travel.users.pull(user_id);
      travel.going_cnt = travel.going_cnt - 1;
      travel.save(function(err, tr){
        if (err){
          response = respond(0, err);
          console.log(response);
        }
        else{
          response = respond(1, err, tr);
        }
      });
    }

  })
  .then(undefined, function(err){
    //Handle error
    response = respond(0, err);
    console.log(err);
  })

  res.json(response);
});

// generates dummy travels
router.get('/dummy', function(req, res, next){
  
  for (var i = 0; i < 10; i++){
    
    Place.findRandom({},{},{limit:1},function(err, results){
      if (err){
        response = respond(0, err);
        console.log(response);
      }else{
        var tit = helpers.dummy_event_names[helpers.getRandomInt(0,3)];
        var time = helpers.getRandomInt(0,23);
        
        results = results[0];

        var travel = Travel({title: tit, time: time, place: results, going_cnt:0});
        travel.save(function(err, t){
          if (err) {
            response = respond(0, err);
            console.log(response);
          }
          if (i == 9){
            response = respond(1, "added 10 travels", []);
          }
        });  
      }
      
    });
  }
  res.json(response);
});

// get close events, inside the specified radius
router.get('/inrange/:lon/:lat/:radius', function(req, res, next){
  var lon = parseFloat(req.params.lon);
  var lat = parseFloat(req.params.lat);
  var radius = parseFloat(req.params.radius);

  console.log(lon +' ' + lat + ' ' + radius);

  Travel.find({}).populate('place').exec(function(err,travels){

    if (err){
      response = helpers.respond(0, err);
    }else{
      var closeTravels = [];
      for (var i = 0; i < travels.length; i++){
        var p = travels[i].place;
        if (p){
          if ( (p.lon-lon)*(p.lon-lon) + (p.lat-lat)*(p.lat-lat) < radius*radius ){
            closeTravels.push(travels[i]);
          }  
        }
      }
      response = helpers.respond(1, "success", closeTravels);  
    }
    res.json(response);
  });
});

module.exports = router;
