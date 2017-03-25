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
