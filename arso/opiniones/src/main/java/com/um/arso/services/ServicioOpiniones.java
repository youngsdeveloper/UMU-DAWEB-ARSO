package com.um.arso.services;

import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.um.arso.events.EventoValoracionCreada;
import com.um.arso.models.Opinion;
import com.um.arso.models.Valoracion;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.FactoriaRepositorios;
import com.um.arso.repositories.Repositorio;
import com.um.arso.repositories.RepositorioException;

public class ServicioOpiniones implements IServicioOpiniones {

    private Repositorio<Opinion,String> repositorio = FactoriaRepositorios.getRepositorio(Opinion.class);

    private final static String EXCHANGE_NAME = "evento.valoracion";
    

    public ServicioOpiniones(){
        createExchange();
    }

    

    @Override
    public String createRecurso(String nombre) throws RepositorioException {
        Opinion opinion = new Opinion();
        opinion.setRecurso(nombre);
        return repositorio.add(opinion);
    }

    @Override
    public boolean registrarValoracion(String id, String email, double calificacion, String comentario)
            throws RepositorioException, EntidadNoEncontrada {

        if(calificacion>5 || calificacion<0){
            throw new RepositorioException("La calificación debe estar entre 0 y 5");
        }

        Valoracion v = new Valoracion();
        v.setEmail(email);
        v.setCalificacion(calificacion);
        v.setComentario(comentario);
        v.setFechaRegistro(LocalDate.now().toString());
        
        Opinion opinion = repositorio.getById(id);
        opinion.addValoracion(v);


        repositorio.update(opinion);

        EventoValoracionCreada eventoValoracionCreada = new EventoValoracionCreada();
        eventoValoracionCreada.setOpinionID(id);
        eventoValoracionCreada.setNuevaValoracion(v);
        eventoValoracionCreada.setAvgValoraciones(opinion.getAvgCalificacion());
        eventoValoracionCreada.setNumValoraciones(opinion.getNumValoraciones());

        notificarEvento(eventoValoracionCreada);

        return true;
    }

    @Override
    public Opinion recuperarOpinion(String id) throws RepositorioException, EntidadNoEncontrada {
        return repositorio.getById(id);
    }

    @Override
    public boolean eliminarOpinion(String id) throws RepositorioException, EntidadNoEncontrada {
        repositorio.delete(recuperarOpinion(id));
        return true;
    }

    @Override
    public List<Opinion> findAll() throws RepositorioException {
        return repositorio.getAll();
    }


    /* Rabbit MQ */
    private void createExchange(){

        ConnectionFactory factory = new ConnectionFactory();
		try {
            factory.setUri("amqps://gdfsrihu:wN9-9EhkyZnXh5PXhUS3DbcJwVTL7axi@rat.rmq2.cloudamqp.com/gdfsrihu");
        
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
            
            boolean durable = true;
            channel.exchangeDeclare(EXCHANGE_NAME, "direct", durable);

            channel.close();
            connection.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    protected void notificarEvento(EventoValoracionCreada evento) {
		
		try {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqps://gdfsrihu:wN9-9EhkyZnXh5PXhUS3DbcJwVTL7axi@rat.rmq2.cloudamqp.com/gdfsrihu");
        Connection connection = factory.newConnection();

		Channel channel = connection.createChannel();
		
		/** Envío del mensaje **/
		
		ObjectMapper mapper = new ObjectMapper(); // Jackson

		StringWriter writer = new StringWriter();
		mapper.writeValue(writer, evento);

		String mensaje = writer.toString();
		
		String routingKey = "nueva_valoracion";
        
		channel.basicPublish(EXCHANGE_NAME, routingKey, new AMQP.BasicProperties.Builder()
				.contentType("application/json")
				.build(), mensaje.getBytes());

		channel.close();
		connection.close();
        
		} catch(Exception e) {
			
			throw new RuntimeException(e);
		}
	}
}
