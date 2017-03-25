// grab the things we need
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// create a schema
var travelSchema = new Schema({
  title: String,
  time: Number,
  place: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Place'
  },
  users: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User'
  }],
  going_cnt: Number
});

// the schema is useless so far
// we need to create a model using it
var Travel = mongoose.model('Travel', travelSchema);

// make this available in our Node applications
module.exports = Travel;
