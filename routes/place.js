var express = require('express');
var router = express.Router();

var Place = require('../models/place');

/* GET place listing. */
router.get('/allPlace', function(req, res, next) {
  // get all the places
  Place.find({}, function(err, result) {
    if (err) throw err;

    // save the result into the response object.
    res.json(result);
  });
});
