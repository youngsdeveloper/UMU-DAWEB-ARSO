using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;
using System.Collections.Generic;

namespace Opiniones.Modelo
{
    public class Opinion
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
         public string? id { get; set; }
         public string? recurso {get;set;}

        public int numValoraciones { get; set; }
        public double avgCalificacion { get; set; }

        public void updateStats(){
            this.numValoraciones = valoraciones.Count;
            this.avgCalificacion = valoraciones.Select(valoracion => valoracion.calificacion).Average();

        }


         //DONE: Realizar modelo valoraciones


         public List<Valoracion> valoraciones {get; set;}
         
         public Opinion(){
            valoraciones = new List<Valoracion>();
         }

         public void addValoracion(Valoracion v){

            Valoracion v_to_delete = valoraciones.Find(_v => v.email==_v.email);
            if(v_to_delete!=null){
                valoraciones.Remove(v_to_delete);
            }
            valoraciones.Add(v);
         }
    }

    //DONE: Modelo valoraciones

    public class Valoracion
    {
        public string? email {get; set;}
        public string? fechaRegistro {get; set;}
        public float calificacion {get; set;}
        public String? comentario {get; set;}

    }

    public class ValoracionDTO
    {
        public string? email {get; set;}
        public string? fechaRegistro {get; set;}
        public float calificacion {get; set;}
        public String? comentario {get; set;}

    }

    public class EventoValoracionCreada
    {
    public string opinionID {get; set;}
    public Valoracion nuevaValoracion {get; set;}
    public int numValoraciones {get; set;}
    public double avgValoraciones {get; set;}
    }
}