var express = require('express');
var router = express.Router();
var Promise = require('bluebird');
var gp = require('googleplaces');

var helpers = require('../helpers');
var Place = require('../models/place');
var config = require('../config');

// promisify the entire mongoose Model
// Place = Promise.promisifyAll(Place);

/* GET place listing. */
router.get('/all', function(req, res, next) {
  // get all the places
  Place.find({}, function(err, result) {
    if (err){
    	response = helpers.respond(0, err);
    	console.log(response);
    }else{
    	response = helpers.respond(1, "all places", result);
    }

    // save the result into the response object.
    res.json(response);
  });
});

// async dummy data generation
router.get('/dummy', function(req, res, next){

	var goo = gp(config.googlePlaceKey, config.googlePlaceFormat);
  goo.nearBySearch({location: "41.0780754,29.0227915", radius:1000}, function(err, data){
    if (err){
    	response = helpers.respond(0, "googleplaces api error");
      console.log(response);
    }else{
    		var results = data.results;
		    for (var i = 0; i < results.length; i++){
		    	var title = results[i].name;
		    	var lat = parseFloat(results[i].geometry.location.lat);
		    	var lon = parseFloat(results[i].geometry.location.lng);
		    	var newPlace = Place({title: title, lat:lat, lon:lon});
					newPlace.save(function(err, place){
						if (err) throw err;
						if (i == 9){
							console.log('added 10 places');
						}
					});
		    }
		    if (results.length == 0){
					response = helpers.respond(2,"no close place exist", results);
		    }else{
		    	response = helpers.respond(1,"success to fetch close places", results);
		    }
    }
    res.json(response);
  });

	/*var loaded = new Promise(function(resolve, reject){
		Promise.each(places, function(p){
			var title = "place " + p;
			var lat = p * p + p; 		// just a random number
			var lon = 2*p*p + 1;		// just a random number
			var newPlace = Place({title: title, lat:lat, lon:lon});
			cnt++;
			if (cnt == places.length-1){
				resolve();
			}
			return newPlace.save()
				.then(function(place){
					console.log(place)	
				})
		});
	});

	loaded.then(function(){
		response = helpers.respond(1, "done", places);
	})
	.then(undefined, function(err){
    //Handle error
    if (err){
    	response = helpers.respond(0, 'error in dummy places');
    	console.log(response);	
    }
  });

  res.json(response);*/
});

// delete all the places
router.get('/clear', function(req, res, next){
	Place.remove({},function(err, removed){
  	if(err){
  		response = helpers.respond(0,err);
  	}else{	
  		response = helpers.respond(1,'clear places success',[]);
  	}
  	res.json(response);
  });
});

// get places by name (title)
router.get('/byName/:title', function(req, res, next){
	Place.find({title:req.params.title}, function(err, place){
		if (err){
			response = helpers.respond(1,err);
			console.log(response);
		}else{
			response = helpers.respond(1,"got the place",place);
		}
		res.json(response);
	});
});

// get places by name (title)
router.get('/searchByName/:title', function(req, res, next){
	Place.find({title: {$regex:req.params.title, $options:"i"}}, function(err, places){
		if(err){
			response = helpers.respond(1,err);
		}else{
			response = helpers.respond(1,"search by name succes",places);	
		}

		res.json(response);
	});
});

router.get('/closePlaces/:lon/:lat/:radius', function(req, res, next){
	var lon = "" + (req.params.lon);
  var lat = "" + (req.params.lat);
  var radius = parseFloat(req.params.radius);

  var goo = gp(config.googlePlaceKey, config.googlePlaceFormat);
  goo.nearBySearch({location: lat+","+lon, radius:radius}, function(err, data){
    if (err){
      response = helpers.respond(0, "googleplaces api error");
      console.log(response);
    }else{
  		var results = data.results;
  		if (results.length == 0){
				response = helpers.respond(2,"no close place exist", results);
	    }else{
	    	response = helpers.respond(1,"success to fetch close places", results);
	    }
		}
    res.json(response);
  });
});

module.exports = router;