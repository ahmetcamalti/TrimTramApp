var express = require('express');
var router = express.Router();
var Promise = require('bluebird');

var Place = require('../models/place');

// promisify the entire mongoose Model
Place = Promise.promisifyAll(Place)

/* GET place listing. */
router.get('/all', function(req, res, next) {
  // get all the places
  Place.find({}, function(err, result) {
    if (err) throw err;

    // save the result into the response object.
    res.json(result);
  });
});

// async dummy data generation
router.get('/dummy', function(req, res, next){

	var cnt = 0;
	var places = [1,2,3,4,5,6,7,8,9,10];

	var loaded = new Promise(function(resolve, reject){
		Promise.each(places, function(p){
			var title = "place " + p;
			var lat = p * p + p + "";
			var long = 2*p*p + 1 + "";
			var newPlace = Place({title: title, lat:lat, long:long});
			cnt++;
			if (cnt == places.length){
				resolve();
			}
			return newPlace.save()
				.then(function(place){
					console.log(place)	
				})
		});
	});

	loaded.then(function(){
		res.json(places);
	})
	.then(undefined, function(err){
    //Handle error
    if (err){
    	console.log('error in dummy travels');
    	res.json('error in dummy');	
    }
  });

 	/*for (var i = 0; i < 10; i++){
 		var title = "place " + i;
 		var lat = i* i + i + "";
		var long = 2*i*i + 1 + "";
		var newPlace = Place({title: title, lat:lat, long:long});
		newPlace.save(function(err, place){
			if (err) throw err;
			if (i == 9){
				console.log('added 10 places');
			}
		});
 	}*/


 	res.json('adding 10 places');
});

router.get('/clear', function(req, res, next){
	Place.remove({},function(err, removed){
  	if(err) throw err;
  	res.json(removed);
  });
});

router.get('/:title', function(req, res, next){
	Place.find({title:req.params.title}, function(err, place){
		res.json(place);
	});
});

module.exports = router;