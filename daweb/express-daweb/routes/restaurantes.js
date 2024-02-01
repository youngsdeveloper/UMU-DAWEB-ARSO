var express = require('express');
var router = express.Router();

const jwt_middleware = require("../middleware/jwt")


const axios = require("axios")

const csrf = require('csurf');
const csrfProtection = csrf({ cookie: true });


/* GET restaurantes */
router.get('/api/', jwt_middleware,async function(req, res, next) {

    api = await axios.get("http://localhost:8090/restaurantes")
    json = api.data;
    res.send(json)
    
});

/* Search */

/* GET restaurantes */
router.get('/api/search', jwt_middleware,async function(req, res, next) {

    api = await axios.get("http://localhost:8090/restaurantes/search?"+encodeURI(new URLSearchParams(req.query)))

    json = api.data;

    res.send(json)
    
});

router.get('/', jwt_middleware, async function(req, res, next) {



    api = await axios.get("http://localhost:8090/restaurantes")
    json = api.data;


    res.render('restaurantes', {restaurantes:json, active:"restaurantes"});
    
});

router.get('/:id', jwt_middleware, async function(req, res, next) {

    const id = req.params.id;


    api = await axios.get(`http://localhost:8090/restaurantes/${id}`)
    restaurante_data = api.data;


    
    api = await axios.get(`http://localhost:8090/opiniones/${restaurante_data.idOpinion}`)
    opinion_data = api.data;

    api = await axios

                .get(`http://localhost:3000/incidencias/${id}`, {
                    headers: {
                        Cookie: `jwt=${req.cookies.jwt}`
                    }
                })
                
    incidencias_data = api.data;


    res.render('restaurante', {restaurante:restaurante_data, opinion: opinion_data,incidencias: incidencias_data, active:"restaurantes"});

    
});

module.exports = router;
