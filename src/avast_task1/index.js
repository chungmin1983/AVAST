var express = require('express');
var bodyParser = require('body-parser');
var app = express();

app.use(bodyParser.urlencoded({
	extended: true
}));
app.use(bodyParser.json());

var nameHash = {};
framework = '<script language="javascript">';
framework = framework + 'function Button(theButton) {';
framework = framework +		'var theForm = theButton.form;';
framework = framework +		'if (theButton.name=="GET"){';
framework = framework +			'theForm.action = "http://localhost:3000/get";';
framework = framework +			'theForm.method = "get";';
framework = framework +		'} else if (theButton.name=="DEL"){';
framework = framework +			'theForm.action = "http://localhost:3000/delete";';
framework = framework +			'theForm.method = "get";';
framework = framework +		'} else if (theButton.name=="UPDATE"){';
framework = framework +			'theForm.action = "http://localhost:3000/post";';
framework = framework +			'theForm.method = "post";';
framework = framework +		'}';
framework = framework +	'}';
framework = framework + '</script>';
framework = framework + '<form action="http://localhost:3000/put" method="post" id="myPost">';
framework = framework + 'Name:<input name="name"/><br>';
framework = framework + 'Age:<input type="password" name="age"/><br>';
framework = framework + '<input type="submit" name="ADD" value="ADD"/>';
framework = framework + '<input type="submit" name="UPDATE" value="UPDATE" onclick="Button(this);"/>';
framework = framework + '<input type="submit" name="GET" value="GET" onclick="Button(this);"/>';
framework = framework + '<input type="submit" name="DEL" value="DEL" onclick="Button(this);"/>';
framework = framework + '</form>';

app.get('/', function (req, res) {
	res.send(framework);
	res.end();
});

app.get('/get', function(req, res) {
	console.log(Date() + '\t[GET] Name: ' + req.query.name);
	var message = "";
	var name = req.query.name;
	if (name && nameHash[name]) {
		message = message + "<h2>"+name+"&nbsp;&nbsp;&nbsp;&nbsp;"+nameHash[name]['age']+"&nbsp;&nbsp;&nbsp;&nbsp;"+nameHash[name]['time']+"</h2>";
	}else{
		var i;
		for (i in nameHash){
			message = message + "<h2>"+i+"&nbsp;&nbsp;&nbsp;&nbsp;"+nameHash[i]['age']+"&nbsp;&nbsp;&nbsp;&nbsp;"+nameHash[i]['time']+"</h2>";
		}
	}
	message = framework + message;
	res.send(message);
	res.end();
});

app.get('/get/:id', function (req, res) {
	console.log(Date() + '\t[GET] All');
	res.send(nameHash);
	res.end();
});

app.post('/put', function (req, res) {
	console.log(Date() + '\t[PUT] Add: ' + req.body.name);
	var age = 0;
	if (req.body.name){
		if (req.body.age){
			age=req.body.age;
		}
		nameHash[req.body.name] = {'time':Date(), 'age':age};
	}
	message = "<h2>ADD "+ req.body.name+"</h2>";
	message = framework + message;
	res.send(message);
	res.end();
});

app.post('/a_put', function (req, res) {
	for (i in req.query) {
		console.log(Date() + '\t[PUT] Add: ' + i);
		nameHash[i] = {'time':Date(), 'age':req.query[i]};
	}
	res.send(JSON.stringify(req.query));
	res.end();
});

app.post('/post', function (req, res) {
	console.log(Date() + '\t[POST]  Updated ' + req.body.name);
	if (req.body.name && req.body.age && nameHash[req.body.name]){
		nameHash[req.body.name] = {'time':Date(), 'age':req.body.age};
	}
	message = "<h2>UPDATE "+ req.body.name+"</h2>";
	message = framework + message;
	res.send(message);
	res.end();
});

app.post('/a_post', function (req, res) {
	for (i in req.query) {
		if (nameHash[i] && nameHash[i]["age"]){
			console.log(Date() + '\t[POST] Update: ' + i);
			nameHash[i] = {'time':Date(), 'age':req.query[i]};
		}
	}
	res.send(JSON.stringify(req.query));
	res.end();
});

app.get('/delete', function (req, res) {
	console.log(Date() + '\t[DELETE] ' + req.query.name);
	if (req.query.name){
		delete nameHash[req.query.name];
	}
	message = "<h2>DELETE "+ req.query.name+"</h2>";
	message = framework + message;
	res.send(message);
	res.end();
});

app.get('/delete/:id', function (req, res) {
	if (nameHash[req.params.id]){
		console.log(Date() + '\t[DELETE] ' + req.params.id);
		delete nameHash[req.params.id];
	}
	res.send(nameHash);
	res.end();
});

app.listen(3000, function () {
	console.log('AVAST_TASK1 listening on port 3000!');
});
