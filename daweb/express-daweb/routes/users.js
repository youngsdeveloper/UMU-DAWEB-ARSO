var express = require('express');
var router = express.Router();

const jwt_middleware = require("../middleware/jwt")



/* GET users listing. */
router.get('/', function(req, res, next) {
  res.send('respond with a resource');
});

router.get("/auth", [jwt_middleware], function(req,res,next){

  if(!req.user){
    res.json({message: "Debes inciar sesión"})
    res.status(401);
    return;
  }

  res.json(req.user);
  res.status(200);
  return;

})

module.exports = router;
