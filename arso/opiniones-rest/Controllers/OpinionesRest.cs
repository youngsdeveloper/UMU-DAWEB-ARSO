using Microsoft.AspNetCore.Mvc;
using Opiniones.Modelo;
using Opiniones.Servicio;
using System;
using System.Collections.Generic;
using Microsoft.Extensions.Primitives;
using System.Text.RegularExpressions;

namespace opiniones_rest.Controllers;

[ApiController]
[Route("api/opiniones")]
public class OpinionesController : ControllerBase
{
    private readonly IServicioOpiniones _servicio;

    public OpinionesController(IServicioOpiniones servicio)
    {
        _servicio=servicio;
    }

    /**
    Crea un nuevo recurso
    */
    [HttpPost]
    public ActionResult<Opinion> Create()
    {
        // Verificamos que se incluye los datos

        if (!Request.HasFormContentType ||
        (Request.ContentType.Contains("multipart/form-data") &&
         Request.ContentType.Contains("application/x-www-form-urlencoded")))
        {
            return BadRequest("La petición debe tener un body");
        }

        // Verificamos que se incluye el campo "nombre"
        StringValues nombre;
        if(!Request.Form.TryGetValue("nombre", out nombre))
        {
            return BadRequest("El campo nombre es requerido.");
        }
        String id = _servicio.CreateRecurso(nombre.ToString());
        return GetOpinion(id);
    }

    //TODO Terminar de hacer las rutas

    /**
    Lista todos los recursos
    */
    [HttpGet]
    public ActionResult<List<Opinion>> GetAll()
    {
        return _servicio.FindAll();
    }

    [HttpGet("{opinionID}")]
    public ActionResult<Opinion> GetOpinion(string opinionID)
    {
        return _servicio.RecuperarOpinion(opinionID);
    }

    [HttpGet("recurso/{recurso}")]
    public ActionResult<Opinion> GetOpinionByRecurso(string recurso)
    {
        return _servicio.RecuperarOpinionByRecurso(recurso);
    }

    [HttpDelete("{opinionID}")]
    public ActionResult<Opinion> DeleteOpinion(string opinionID)
    {
        _servicio.EliminarOpinion(opinionID);
        return NoContent();
    }

    [HttpPost("{opinionID}/valorar")]
    public ActionResult<String> Valorar(string opinionID, [FromBody] ValoracionDTO valoracion)
    {

        if(valoracion.calificacion<0||valoracion.calificacion>5){
            return BadRequest("La valoración debe estar entre 0 y 5");
        }

        string email_pattern = @"^[^@\s]+@[^@\s]+\.[^@\s]+$";

        if(!Regex.IsMatch(valoracion.email, email_pattern)){
            return BadRequest("El email no tiene un formato adecuado");
        }


        Boolean result = _servicio.RegistrarValoracion(opinionID, valoracion);

        if(result){
            return "Valoración creada correctamente";

        }else{
            return StatusCode(500, new { error = "Ha ocurrido un error en el servidor" });
        }
        
    }



}
