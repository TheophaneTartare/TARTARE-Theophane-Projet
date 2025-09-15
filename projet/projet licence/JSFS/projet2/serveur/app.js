var mongoose = require('mongoose');
var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

var Connection = require('./controllers/mongoControllers.js') ;
var home = require('./routes/home.js') ; 
var students = require('./routes/students.js') ; 
var groups = require('./routes/groups.js') ;
var task = require('./routes/task.js');
var error = require('./middlewares/error.middleware.js');

var app = express();

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'pug');


app.use((req, res, next) => {
    res.setHeader('Cache-Control', 'no-store'); 
    next();
  });

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', home);
app.use('/task', task);
app.use('/students',students) ;
app.use('/groups',groups) ;


app.use((req, res, next) => {
    next(createError(404, 'Page non trouvée'));
});

app.use(error);

module.exports = app;
