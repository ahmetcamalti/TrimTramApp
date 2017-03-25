// grab the things we need
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// create a schema
var userSchema = new Schema({
  username: String,
  private_key: String,
  travels: [{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'travel'
  }]
});

// the schema is useless so far
// we need to create a model using it
var User = mongoose.model('User', userSchema);

// make this available in our Node applications
module.exports = User;
