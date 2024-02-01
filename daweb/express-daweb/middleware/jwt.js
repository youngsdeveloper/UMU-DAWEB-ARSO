
const jwt = require('jsonwebtoken');
var axios = require('axios');

const secret = 'daweb_arso_mola'

const authenticateJWT = (req, res, next) => {
  const authCookie = req.cookies.jwt;

  if(authCookie){
      const token = authCookie;
        console.log(token)
      jwt.verify(token, secret, (err, user) => {

          if (err) {
              return res.sendStatus(403);
          }

          req.user = user;
          res.locals.user = req.user;

          axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

          next();
      });
  } else {
      res.redirect("/login")
      res.sendStatus(401);
  }
};

module.exports = authenticateJWT;