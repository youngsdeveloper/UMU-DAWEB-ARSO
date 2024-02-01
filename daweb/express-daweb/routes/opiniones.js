var express = require('express');
var router = express.Router();

const jwt_middleware = require("../middleware/jwt")

const csrf = require('csurf');
const csrfProtection = csrf({ cookie: true });


const axios = require("axios");

/* GET opiniones */
router.get('/', jwt_middleware,async function(req, res, next) {

    api = await axios.get("http://localhost:8090/opiniones")
    json = api.data;
    res.send(json)
    
});


/* GET opinion */
router.get('/:id', jwt_middleware,async function(req, res, next) {

    const id = req.params.id;

    api = await axios.get("http://localhost:8090/opiniones/"+id)

    json = api.data;
    res.send(json)
    
});

router.post('/:id', [jwt_middleware],async function(req, res, next) {

    const id = req.params.id;


    api = await axios.post("http://localhost:8090/opiniones/"+id+"/valorar",{
        email: req.body.email,
        calificacion: req.body.calificacion,
        comentario: req.body.comentario
    })

    json = api.data;
    res.send(json);
    
});

module.exports = router;
