//var mongoose = require('mongoose');
console.log("App starting...");
var createError = require('http-errors');
var express = require('express');
console.log("Express app initialized");
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');

//var Connection = require('./controllers/mongoControllers.js') ;
var home = require('./routes/home.js') ; 


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

app.use((req, res, next) => {
    next(createError(404, 'Page non trouvée'));
});

app.use(error);

module.exports = app;

var port = 3000;
app.listen(port, () => {
  console.log(`Server running on http://localhost:${port}`);
});
