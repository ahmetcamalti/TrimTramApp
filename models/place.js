// grab the things we need
var mongoose = require('mongoose');
var Schema = mongoose.Schema;
var random = require('mongoose-simple-random');

// create a schema
var placeSchema = new Schema({
  title: String,
  lat:Number,
  lon:Number
});

// the schema is useless so far
// we need to create a model using it
placeSchema.plugin(random);
var Place = mongoose.model('Place', placeSchema);


// make this available in our Node applications
module.exports = Place;
