using System;
using System.Collections.Generic;
using System.Text.RegularExpressions;
using Opiniones.Modelo;
using Repositorio;
using System.Text;
using RabbitMQ.Client;
using Newtonsoft.Json;


namespace Opiniones.Servicio
{

    public interface IServicioOpiniones
    {

        /** 
	 * Obtener todos los recursos
	 * 
	 * @return Lista de opiniones
	 */
	List<Opinion> FindAll();

    /** 
	 * Alta de un recurso
	 * 
	 * @param nombre Nombre del recurso
	 * @return identificador de la opinion
	 */
	String CreateRecurso(String nombre);

    /** 
	 * Registrar valoracion
	 * 
	 * @param ValoracionDTO valoracion
	 */
	bool RegistrarValoracion(String opinionID, ValoracionDTO valoracionDTO);


    /** 
	 * Recuperar opinión
	 * 
	 * @param id id de la opinion
	 * @return Opinion

	 */
	Opinion RecuperarOpinion(String id);



    /** 
	 * Recuperar opinión por nombre de recurso
	 * 
	 * @param recurso nombre del recurso
	 * @return Opinion

	 */
	Opinion RecuperarOpinionByRecurso(String recurso);


    /** 
	 * Eliminar opinión
	 * 
	 * @param id id de la opinion
	 * @return Opinion

	 */
	void EliminarOpinion(String id);

    }

    public class ServicioOpiniones : IServicioOpiniones
    {

        const String EXCHANGE_NAME = "evento.valoracion";

        private Repositorio<Opinion, String> repositorio;

        public ServicioOpiniones(Repositorio<Opinion, String> repositorio)
        {
            this.repositorio = repositorio;
            createExchange();

        }
        public string CreateRecurso(string nombre)
        {
            var opinion =new Opinion{
                recurso = nombre
            };

            return repositorio.Add(opinion);
        }

        public void EliminarOpinion(string id)
        {
            Opinion opinion = repositorio.GetById(id);
            repositorio.Delete(opinion);

        }
        
        //TODO terminar implementacion
        public List<Opinion> FindAll()
        {
            return repositorio.GetAll();
        }
        //TODO terminar implementacion
        public Opinion RecuperarOpinion(string id)
        {
            return repositorio.GetById(id);
        }

        //DONE Realizar funcion despues de implementar valoraciones
        public bool RegistrarValoracion(String opinionID, ValoracionDTO valoracionDTO)
        {

            Opinion opinion = repositorio.GetById(opinionID);

            if(opinion==null){
                return false;
            }

            if(valoracionDTO.calificacion<0||valoracionDTO.calificacion>5){
                return false;
            }

            string email_pattern = @"^[^@\s]+@[^@\s]+\.[^@\s]+$";

            if(!Regex.IsMatch(valoracionDTO.email, email_pattern)){
                return false;
            }


            DateOnly today = DateOnly.FromDateTime(DateTime.Now);

            var valoracion = new Valoracion{
                email = valoracionDTO.email,
                calificacion = valoracionDTO.calificacion,
                comentario = valoracionDTO.comentario,
                fechaRegistro = today.ToString("yyyy-MM-dd")
            };            

            opinion.addValoracion(valoracion);
            opinion.updateStats();

            EventoValoracionCreada eventoValoracionCreada = new EventoValoracionCreada{
                opinionID = opinion.id,
                nuevaValoracion = valoracion,
                numValoraciones = opinion.numValoraciones,
                avgValoraciones = opinion.avgCalificacion
            };

            notificarEvento(eventoValoracionCreada);
            
            repositorio.Update(opinion);

            return true;
        }

        /* Rabbit MQ */ 
        private void createExchange(){

            var factory = new ConnectionFactory();

            try
            {
                factory.Uri = new Uri("amqps://gdfsrihu:wN9-9EhkyZnXh5PXhUS3DbcJwVTL7axi@rat.rmq2.cloudamqp.com/gdfsrihu");

                using (var connection = factory.CreateConnection())
                using (var channel = connection.CreateModel())
                {
                    bool durable = true;
                    channel.ExchangeDeclare(EXCHANGE_NAME, "direct", durable);

                    channel.Close();
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        }

        protected void notificarEvento(EventoValoracionCreada evento){

            var factory = new ConnectionFactory();

            try
            {
                factory.Uri = new Uri("amqps://gdfsrihu:wN9-9EhkyZnXh5PXhUS3DbcJwVTL7axi@rat.rmq2.cloudamqp.com/gdfsrihu");

                using (var connection = factory.CreateConnection())
                using (var channel = connection.CreateModel())
                {
                    // Serializar el objeto a JSON
                    string mensaje = JsonConvert.SerializeObject(evento);
                    var body = Encoding.UTF8.GetBytes(mensaje);

                    string routingKey = "nueva_valoracion";

                    IBasicProperties basicProperties = channel.CreateBasicProperties();
                    basicProperties.ContentType = "application/json";


                    channel.BasicPublish(EXCHANGE_NAME, routingKey, basicProperties, body: body);
                }

                
            }
            catch (Exception e)
            {
                Console.WriteLine(e);
            }
        } 



        public Opinion RecuperarOpinionByRecurso(String recurso){
            return repositorio.GetByRecurso(recurso);
        }

    }
}