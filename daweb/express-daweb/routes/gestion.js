var express = require('express');

var router = express.Router();

var is_admin = require("../middleware/is_admin")

const csrf = require('csurf');
const csrfProtection = csrf({ cookie: true });


const axios = require("axios")
const mysql = require('mysql2/promise');

router.get('/', [is_admin,csrfProtection],async function(req, res, next) {

    api = await axios.get("http://localhost:8090/restaurantes/gestor")
    json = api.data;

    res.render('gestion', {restaurantes:json, active:"gestion",_csrf:req.csrfToken()});

});

router.get('/create', [is_admin,csrfProtection],async function(req, res, next) {


    res.render('gestion_crear_restaurante', {active:"gestion",_csrf:req.csrfToken()});

});

router.post('/create', [is_admin,csrfProtection],async function(req, res, next) {


    
    data = req.body;

    console.log(req)
    
    delete data._csrf;

    api = await axios.post("http://localhost:8090/restaurantes", data)
    
    
    if(api.status==201){
        // Exito, redirigir a gestion
        res.redirect("/gestion?status=restaurante_creado");
    }else{
        // Fallo, redirigir al formulario
        res.redirect("back");
    }
});

router.get('/:id/edit', [is_admin,csrfProtection],async function(req, res, next) {

    const id = req.params.id;

    api = await axios.get("http://localhost:8090/restaurantes/"+id)
    restaurante_data = api.data;

    api = await axios.get("http://localhost:8090/restaurantes/"+id+"/sitios-turisticos")
    sitios_data = api.data;


    // Filtar sitios que ya hayan sido seleccionados
    sitios_elegibles = sitios_data.filter(s => !restaurante_data.sitioTuristicos.some(s2 => s.title==s2.title));

    res.render('gestion_editar_restaurante', {restaurante:restaurante_data, sitios_elegibles: sitios_elegibles, active:"gestion",_csrf:req.csrfToken()});

});

router.post('/:id/update', [is_admin,csrfProtection],async function(req, res, next) {

    const id = req.params.id;


    data = req.body;

    data.id = id;

    delete data._csrf; //Delete CSRF token

    api = await axios.put("http://localhost:8090/restaurantes/"+id, data)
    
    
    if(api.status==204){
        // Exito, redirigir a gestion
        res.redirect("/gestion/"+id+"/edit?status=restaurante_actualizado");
    }else{
        // Fallo, redirigir al formulario
        res.redirect("back");
    }
});

router.post('/:id/update/add-sitio',[is_admin],async function(req, res, next) {

    const id = req.params.id;

    data = req.body;


    api = await axios.get("http://localhost:8090/restaurantes/"+id)
    restaurante_data = api.data;

    sitios = restaurante_data.sitioTuristicos;

    sitios.push(data);

    api = await axios.put("http://localhost:8090/restaurantes/"+id+"/sitios-turisticos", sitios)
    if(api.status==204){
        api = await axios.get("http://localhost:8090/restaurantes/"+id+"/sitios-turisticos")
        sitios_data = api.data;
    
    
        // Filtar sitios que ya hayan sido seleccionados
        sitios_elegibles = sitios_data.filter(s => !sitios.some(s2 => s.title==s2.title));
    
        res.json({sitios_elegidos: sitios, sitios_elegibles: sitios_elegibles});
    
    }

});

router.post('/:id/update/delete-sitio',[is_admin],async function(req, res, next) {

    const id = req.params.id;

    data = req.body;


    api = await axios.get("http://localhost:8090/restaurantes/"+id)
    restaurante_data = api.data;

    sitios = restaurante_data.sitioTuristicos.filter(s => s.title != data.title);

    api = await axios.put("http://localhost:8090/restaurantes/"+id+"/sitios-turisticos", sitios)
    if(api.status==204){
        api = await axios.get("http://localhost:8090/restaurantes/"+id+"/sitios-turisticos")
        sitios_data = api.data;
    
    
        // Filtar sitios que ya hayan sido seleccionados
        sitios_elegibles = sitios_data.filter(s => !sitios.some(s2 => s.title==s2.title));
    
        res.json({sitios_elegidos: sitios, sitios_elegibles: sitios_elegibles});
    
    }

});

router.post('/:id/update/remove-plato',[is_admin],async function(req, res, next) {

    const id = req.params.id;


    nombre = req.body.nombre;

    api = await axios.delete(encodeURI("http://localhost:8090/restaurantes/"+id+"/platos/"+nombre))
    if(api.status==204){


        // create the pool
        const promisePool = mysql.createPool(
            {
                host:'localhost',
                port: "6033",
                user: 'daweb',
                password: 'daweb',
                database: 'daweb'
            });

        const query = `DELETE FROM incidencias WHERE restaurante_id='${id}' and plato='${nombre}'`;
        const [rows,fields] = await promisePool.query(query);
        

        api = await axios.get("http://localhost:8090/restaurantes/"+id)
        
        restaurante_data = api.data;
    
        platos_data = restaurante_data.platos
    
        res.json({platos:platos_data});
    
    }

});

router.post('/:id/delete', [is_admin,csrfProtection],async function(req, res, next) {

    const id = req.params.id;

    api = await axios.delete("http://localhost:8090/restaurantes/"+id)

    if(api.status==204){

        // Eliminamos tambien de incidencias

        // create the pool
        const promisePool = mysql.createPool(
            {
                host:'localhost',
                port: "6033",
                user: 'daweb',
                password: 'daweb',
                database: 'daweb'
            });

        const query = `DELETE FROM incidencias WHERE restaurante_id='${id}'`;
        const [rows,fields] = await promisePool.query(query);


        // Exito, redirigir a gestion
        res.redirect("/gestion?status=restaurante_eliminado");
    }else{
        // Fallo, redirigir al formulario
        res.redirect("back");
    }
});

router.get('/:id/edit/plato/crear', [is_admin,csrfProtection],async function(req, res, next) {

    const id =req.params.id;
    res.render('gestion_crear_plato', {res_id:id, active:"gestion",_csrf:req.csrfToken()});


});

router.post('/:id/edit/plato/crear', [is_admin,csrfProtection],async function(req, res, next) {

    data = req.body;

    delete data._csrf;

    const id =req.params.id;

    data.precio=parseFloat(data.precio)

    api = await axios.post(encodeURI("http://localhost:8090/restaurantes/"+id+"/platos/"),data)

    if(api.status==204){
        // Exito, redirigir a gestion
        res.redirect("/gestion/"+id+"/edit?status=plato_creado");
    }else{
        // Fallo, redirigir al formulario
        res.redirect("back");
    }


});

router.get('/:id/edit/plato/:nombre', [is_admin,csrfProtection],async function(req, res, next) {

    const nombre = req.params.nombre;

    const id =req.params.id;

    api = await axios.get("http://localhost:8090/restaurantes/"+id)
    restaurante_data = api.data;

    // Filtar sitios que ya hayan sido seleccionados
    plato_data = restaurante_data.platos.filter(p => p.nombre==nombre)[0];

    res.render('gestion_editar_plato', {res_id:id,plato:plato_data, active:"gestion",_csrf:req.csrfToken()});

});

router.post('/:id/edit/plato/:nombre/update-plato', [is_admin,csrfProtection],async function(req, res, next) {

    data = req.body;

    delete data._csrf;

    const id =req.params.id;

    const nombre =req.params.nombre;

    data.disponibilidad=(data.disponibilidad=="true")
    data.precio=parseFloat(data.precio)

    api = await axios.put(encodeURI("http://localhost:8090/restaurantes/"+id+"/platos/"+nombre),data)

    if(api.status==204){

        // create the pool
        const promisePool = mysql.createPool(
            {
                host:'localhost',
                port: "6033",
                user: 'daweb',
                password: 'daweb',
                database: 'daweb'
            });

        const query = `UPDATE incidencias SET plato='${data.nombre}' WHERE restaurante_id='${id}' and plato='${nombre}'`;
        const [rows,fields] = await promisePool.query(query);
        
        // Exito, redirigir a gestion
        res.redirect("/gestion/"+id+"/edit/plato/"+data.nombre+"?status=plato_actualizado");
    }else{
        // Fallo, redirigir al formulario
        res.redirect("back");
    }


});

module.exports = router;
