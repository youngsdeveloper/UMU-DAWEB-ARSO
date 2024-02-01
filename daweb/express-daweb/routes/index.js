var express = require('express');
var router = express.Router();

var jwt_middleware = require("../middleware/jwt")


/* GET home page. */
router.get('/', jwt_middleware, function(req, res, next) {
  res.render('index', { title: 'Express',active:"inicio" });
});


router.get('/login', function(req, res, next) {
  res.render('login');
});

router.get('/profile',jwt_middleware ,function(req, res, next) {
  res.render('profile');
});

router.post('/logout',jwt_middleware ,function(req, res, next) {
    res.clearCookie("jwt")
    res.status(200);
    res.json({"message": "Cierre de sesión exitoso"})
});

router.get('/api/login/github', function(req, res, next) {
  res.redirect('http://localhost:8090/oauth2/authorization/github');
});

module.exports = router;
