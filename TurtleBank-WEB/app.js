var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var {api_url, file_path, server_url} = require('./config')

var indexRouter = require('./routes/index');
var app = express();

global.api_url = api_url;
global.file_path = file_path;
global.server_url = server_url;


app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({extended: false}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);

app.use(function (req, res, next) {
	    next(createError(404));
});

app.use(function (err, req, res, next) {
	    res.locals.message = err.message;
	    res.locals.error = req.app.get('env') === 'development' ? err : {};

	    res.status(err.status || 500);
	    res.render('error');
});

module.exports = app;