
const jwt = require('jsonwebtoken');
var axios = require('axios');

const secret = 'daweb_arso_mola'

const isAdmin = (req, res, next) => {
  const authCookie = req.cookies.jwt;

  console.log(authCookie)

  if(authCookie){
      const token = authCookie;

      jwt.verify(token, secret, (err, user) => {

          if (err) {
            console.log(err)

              return res.sendStatus(403);
          }

          if(user.rol!="GESTOR"){
            return res.sendStatus(401);
          }

          req.user = user;
          res.locals.user = req.user;

          axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

          next();
      });
  } else {
      res.sendStatus(401);
  }
};

module.exports = isAdmin;