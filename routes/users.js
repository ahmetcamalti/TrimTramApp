var express = require('express');
var rand = require("random-key");
var random_name = require('node-random-name');
var router = express.Router();

var User = require('../models/user');
var Travel = require('../models/travel');

const crypto = require('crypto');

function generateNewUser(username, travels){
  if (travels)
    return User({username: username, private_key: rand.generate(), travels:travels});
  else
    return User({username: username, private_key: rand.generate()});
}

/* GET users listing. */
router.get('/allUser', function(req, res, next) {
  // get all the users
  User.find({}, function(err, result) {
    if (err) throw err;

    // save the result into the response object.
    res.json(result);
  });
});

/* add new user */
router.get('/addUser/:username', function(req, res, next) {
  // check is there a param?
  if (!req.params.username) {
    response = {
      success: 0,
      message: "There isn't any user parameter!"
    };

    res.json(response);
  }

  // create a new user
  var newUser = generateNewUser(username);

  // save the user
  newUser.save(function(err, user) {
    if (err) throw err;

    // message for server
    console.log('User created!');

    // response for client
    response = {
      success: 1,
      message: "User added to database with success!",
      user: user
    }

    // response error
    res.json(response);
  });
});

/* remove all users */
router.get('/removeAllUser', function(req, res, next) {
  // get all the users
  User.remove({}, function(err, result) {
    // remove all budgeds data
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
      var message = "All users removed succesfully!";

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

// get my travels
router.get('/myTravels/:u_name/:p_key', function(req, res, next){

  var username = req.params.u_name;
  var private_key = req.params.p_key;

  User.findOne({username: username, private_key:private_key}).populate('travels').exec()
  .then(function(user){
    res.json(user.travels);
  })
  .then(undefined, function(err){
    //Handle error
    console.log('error in myTravels/:u_name:/:p_key');
    res.json('error in myTravels');
  });
});

// generates dummy users
router.get('/dummy', function(req, res, next){

  for (var i = 0; i < 10; i++){
    
    Travel.findRandom({},{},{limit:3},function(err, results){
      if (err) throw err;
      var uname = random_name();
      var u = generateNewUser(uname, results);
      u.save(function(err, user){
        if (err) {
          console.log('error in dummy user generation');
        }
        if (i == 9){
          console.log("added 10 users");
        }
      });
    });
  }

  res.json('adding 10 users');

});

module.exports = router;
