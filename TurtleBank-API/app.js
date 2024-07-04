const express = require("express");
const app = express();
const index = require('./routes/index');
var logger = require('morgan');


global.file_path = "../file/";
// global.file_path = "D:/RFinal/ShieldBank-master/AWS-ShieldBank/file/";
global.different_api = 'http://127.0.0.1:4000/';

app.use(express.json());

app.use('/api', index);
app.use(logger('dev'));

module.exports = app;
