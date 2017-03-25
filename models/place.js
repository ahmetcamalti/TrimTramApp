// grab the things we need
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// create a schema
var placeSchema = new Schema({
  name: String,
  events: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'event'
  }];
});

// the schema is useless so far
// we need to create a model using it
var Event = mongoose.model('Event', travelSchema);

// make this available in our Node applications
module.exports = Event;
