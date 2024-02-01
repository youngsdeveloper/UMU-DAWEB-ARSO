var createError = require('http-errors');
var express = require('express');
var path = require('path');

const csrf = require('csurf');


var cookieParser = require('cookie-parser');
var logger = require('morgan');



const bodyParser = require('body-parser');


var daweb_helpers = require("./daweb-helpers")


var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var restaurantesRouter = require('./routes/restaurantes'); 
var gestionRouter = require("./routes/gestion")
var incidenciasRouter = require("./routes/incidencias")
var opinionesRouter = require("./routes/opiniones")

var app = express();

const {engine} = require('express-handlebars');

const cors = require('cors');

const corsOoptions = {
  origin: "http://localhost:3001",
  methods: "GET,HEAD,PUT,PATCH,POST,DELETE",
  credentials: true
}
app.use(cors(corsOoptions));


// view engine setup
app.set('views', path.join(__dirname, 'views'));

app.set('view engine', 'hbs');

app.engine('hbs', engine({
    extname: 'hbs',
    partialsDir: path.join(__dirname + '/views/partials'),
    layoutsDir: path.join(__dirname + '/views/layouts'),
    defaultLayout: 'layout',
    helpers: daweb_helpers
}));


// Body Parser
app.use(bodyParser.json());




// Enviar Query Parameters to Locals
app.use(function(req, res, next) {
  res.locals.query = req.query;
  next();
});


app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


// JWT Middleware


// Redireccionar si no autentificado  
/*
app.use((req, res, next) => {
  if (!req.cookies.jwt && req.path !== '/login') {
    // Redirigir a /login si no está autenticado
    return res.redirect(302, '/login');
  }

  next();
});
*/




app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/restaurantes', restaurantesRouter);
app.use("/gestion",gestionRouter)
app.use("/incidencias",incidenciasRouter)
app.use("/opiniones",opinionesRouter)


// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


module.exports = app;
