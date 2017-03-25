// grab the things we need
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// create a schema
var placeSchema = new Schema({
  title: String,
  lat:String,
  long:String
});

// the schema is useless so far
// we need to create a model using it
var Place = mongoose.model('Place', placeSchema);

// make this available in our Node applications
module.exports = Place;
