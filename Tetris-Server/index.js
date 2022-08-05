const express = require('express');
const bodyParser = require('body-parser');
const app = express();
app.use(bodyParser());

var rooms = {};
var current_id = 0;

app.post('/create/', function(req, res) {
	var roomKey = req.body.roomKey;
	if (roomKey in rooms) {
		res.send("room is already taken");
	}
	else {
		rooms[roomKey] = {
			"player1": current_id,
			"player2": null,
			"player1_tetrion": null,
			"player2_tetrion": null,
			"gamestate": "waiting_for_player",
			"player1_lines": 0,
			"player2_lines": 0,
			"loser": null
		};
		res.send(current_id.toString());
		current_id ++;
	}
});

app.post('/join/', function(req, res) {
	var roomKey = req.body.roomKey;

	if (!(roomKey in rooms)) {
		res.send("room does not exist");
	} else {
		if (rooms[roomKey]["player2"] == null) rooms[roomKey]["player2"] = current_id;
		else if (rooms[roomKey]["player1"] == null) rooms[roomKey]["player1"] = current_id;
		else {
			res.send("room full");
		}

		rooms[roomKey]["gamestate"] = "game";

		res.send(current_id.toString());
		current_id ++;
	}
});

app.post('/lose/', function(req, res) {
	var roomKey = req.body.roomKey;
	var id = req.body.id;

	if (!(roomKey in rooms)) {
		res.send("room does not exist");
	} else {
		rooms[roomKey]["loser"] = id;
		rooms[roomKey]["gamestate"] = "game_over";
		res.send("success");
	}
});

app.post('/info/', function(req, res) {
	var roomKey = req.body.roomKey;
	var id = req.body.id;

	if (!(roomKey in rooms)) {
		res.send("room does not exist");
		return;
	}

	var playerToReq;

	if (rooms[roomKey]["player1"] == id) playerToReq = "player2";
	else if (rooms[roomKey]["player2"] == id) playerToReq = "player1";
	else {
		res.send("you are not in this game");
		return;
	}

	var payload = {};
	payload["gamestate"] = rooms[roomKey]["gamestate"];
	payload["loser"] = rooms[roomKey]["loser"];
	payload["lines"] = rooms[roomKey][playerToReq + "_lines"];
	payload["tetrion"] = rooms[roomKey][playerToReq + "_tetrion"];

	res.send(payload);
});

app.post('/give/', function(req, res) {
	var roomKey = req.body.roomKey;
	var id = req.body.id;
	var lines = req.body.lines;
	var tetrion = req.body.tetrion;

	if (!(roomKey in rooms)) {
		res.send("invalid room");
	} else {
		var playerToReq;

		if (rooms[roomKey]["player1"] == id) playerToReq = "player1";
		else if (rooms[roomKey]["player2"] == id) playerToReq = "player2";
		else {
			res.send("invalid id");
			return;
		}

		rooms[roomKey][playerToReq + "_lines"] = lines;
		rooms[roomKey][playerToReq + "_tetrion"] = tetrion;

		res.send("success");
	}
});

app.post('/disconnect/', function(req, res) {
	var roomKey = req.body.roomKey;
	var id = req.body.id;

	if (!(roomKey in rooms)) {
		res.send("invalid room");
	} else {
		if (rooms[roomKey]["player1"] == id) {
			rooms[roomKey]["player1"] = null;
			rooms[roomKey]["loser"] = id;
			rooms[roomKey]["gamestate"] = "game_over";
			res.send("success");
			check(roomKey);
		}
		else if (rooms[roomKey]["player2"] == id) {
			rooms[roomKey]["player2"] = null;
			rooms[roomKey]["loser"] = id;
			rooms[roomKey]["gamestate"] = "game_over";
			res.send("success");
			check(roomKey);
		} else {
			res.send("you are not in this room");
		}
	}
});

function check(roomKey) {
	if (rooms[roomKey]["player1"] == null && rooms[roomKey]["player2"] == null) {
		delete rooms[roomKey];
	}
}

app.listen(4444, () => console.log('Tetris server started on port 4444!'));
