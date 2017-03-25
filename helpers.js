/**
 * Returns a random integer between min (inclusive) and max (inclusive)
 * Using Math.round() will give you a non-uniform distribution!
 */
exports.getRandomInt = function(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}

exports.dummy_event_names = ["sport", "game", "image", "football"];

exports.respond = function(success, msg, data){
	if (data){
		return {success: success, message: msg, data: data};
	}else{
		return {success: success, message: msg};
	}
}