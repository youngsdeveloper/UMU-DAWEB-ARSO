var express = require('express');
var router = express.Router();

var jwt_middleware = require("../middleware/jwt")


const mysql = require('mysql2/promise');

/* GET incidencias */
router.post('/', jwt_middleware,async function(req, res, next) {


    // create the pool
    const promisePool = mysql.createPool(
        {
            host:'localhost',
            port: "6033",
            user: 'daweb',
            password: 'daweb',
            database: 'daweb'
        });

    // now get a Promise instance of that pool
    //const promisePool = pool.promise();

    // query database using promises
    const user_id = req.user.usuario;
    const restaurante_id = req.body.restaurante_id;
    const plato = req.body.plato;
    const fecha_apertura = new Date().toISOString().split('T')[0];
    const comentarios = req.body.comentario;


    const query = `INSERT INTO incidencias (user_id, restaurante_id, plato, fecha_apertura, comentarios) VALUES ("${user_id}","${restaurante_id}","${plato}","${fecha_apertura}", "${comentarios}")`;
    
    try {
        const [rows,fields] = await promisePool.query(query);

        res.redirect("back");

    } catch (error){
        res.status(500);
    }

    
});


router.get('/', jwt_middleware, async function(req, res, next) {

    // create the pool
    const promisePool = mysql.createPool(
        {
            host:'localhost',
            port: "6033",
            user: 'daweb',
            password: 'daweb',
            database: 'daweb'
        });

    const query = `SELECT * FROM incidencias ORDER BY fecha_apertura DESC`;
    try {
        const [rows,fields] = await promisePool.query(query);

        res.json(rows);

    } catch (error){
        res.status(500);
    }

    
});

router.get('/:id', jwt_middleware, async function(req, res, next) {

    const id = req.params.id;

    // create the pool
    const promisePool = mysql.createPool(
        {
            host:'localhost',
            port: "6033",
            user: 'daweb',
            password: 'daweb',
            database: 'daweb'
        });

    const query = `SELECT * FROM incidencias WHERE restaurante_id="${id}" ORDER BY fecha_apertura,id DESC`;
    try {
        const [rows,fields] = await promisePool.query(query);
        console.log("Llega")
        res.json(rows);

    } catch (error){
        console.log(error)
        res.status(500);
    }

    
});

module.exports = router;
