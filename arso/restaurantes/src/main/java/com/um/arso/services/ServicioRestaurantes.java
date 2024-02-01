package com.um.arso.services;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.um.arso.dto.PlaceDetailsDTO;
import com.um.arso.dto.PlatoDTO;
import com.um.arso.dto.RestauranteDTO;
import com.um.arso.dto.SitioTuristicoDTO;
import com.um.arso.events.EventoValoracionCreada;
import com.um.arso.models.PlaceDetails;
import com.um.arso.models.Plato;
import com.um.arso.models.Restaurante;
import com.um.arso.models.SitioTuristico;
import com.um.arso.models.Valoracion;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.FactoriaRepositorios;
import com.um.arso.repositories.Repositorio;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.repositories.especificacion.EqualsSpecification;

public class ServicioRestaurantes implements IServicioRestaurantes {

    private Repositorio<Restaurante,String> repositorio = FactoriaRepositorios.getRepositorio(Restaurante.class);


    public ServicioRestaurantes(){
        configRabbitMQ();
    }

    @Override
    public String create(RestauranteDTO restauranteDTO) throws RepositorioException {

        
        Restaurante restaurante = fromDTO(restauranteDTO);

        restaurante.setFechaAlta(LocalDate.now());
        String id = repositorio.add(restaurante);

        restaurante.setId(id);
    
        try {
            altaOpiniones(toDTO(restaurante));
        } catch (EntidadNoEncontrada e) {
        }


        return id;
    }

    @Override
    public void update(RestauranteDTO restaurante) throws RepositorioException, EntidadNoEncontrada {

        
        Restaurante r2 = repositorio.getById(restaurante.getId());

        

        // Campos editables
        if(restaurante.getNombre()!=null)
            r2.setNombre(restaurante.getNombre());

        if(restaurante.getCiudad()!=null)
            r2.setCiudad(restaurante.getCiudad());

        if(restaurante.getLat()!=null)
            r2.setLat(restaurante.getLat());

        if(restaurante.getLng()!=null)
            r2.setLng(restaurante.getLng());

        repositorio.update(r2);
    }

    @Override
    public List<SitioTuristicoDTO> getSitiosTuristicosCercanos(String id)
            throws RepositorioException, EntidadNoEncontrada {

        Restaurante restaurante = repositorio.getById(id);

        List<SitioTuristico> sitioTuristicos = SitiosTuristicosServices.getInstance().findByLocation(restaurante.getLat(), restaurante.getLng());

        List<SitioTuristicoDTO> sitioTuristicoDTOs = new ArrayList<>();
        for (SitioTuristico sitioTuristico : sitioTuristicos) {
            sitioTuristicoDTOs.add(toDTO(sitioTuristico));
        }

        return sitioTuristicoDTOs;
    }

    @Override
    public void setSitiosTuristicos(String id, List<SitioTuristicoDTO> sitioTuristicos)
            throws RepositorioException, EntidadNoEncontrada {

        Restaurante restaurante = repositorio.getById(id);
        
        List<SitioTuristico> sitios = new ArrayList<>();
        for (SitioTuristicoDTO sitio : sitioTuristicos) {
            sitios.add(fromDTO(sitio));
        }
        restaurante.setSitioTuristicos(sitios);
        repositorio.update(restaurante);

    }

    @Override
    public void addPlato(String id, PlatoDTO platoDTO) throws RepositorioException, EntidadNoEncontrada {
        Restaurante restaurante = repositorio.getById(id);

        restaurante.addPlato(fromDTO(platoDTO));

        repositorio.update(restaurante);
    }

    @Override
    public void removePlato(String id, String plato) throws RepositorioException, EntidadNoEncontrada {
        Restaurante restaurante = repositorio.getById(id);

        if(!restaurante.containsPlato(plato)){
            throw new EntidadNoEncontrada("El plato no existe");
        }
    
        restaurante.removePlato(plato);
        repositorio.update(restaurante);
    }

    @Override
    public void removeRestaurante(String id) throws RepositorioException, EntidadNoEncontrada {
        Restaurante restaurante = repositorio.getById(id);
        repositorio.delete(restaurante);
    }

    @Override
    public List<RestauranteDTO> getAllRestaurantes() throws RepositorioException {

        return toDTO(repositorio.getAll());
    }

    @Override
    public RestauranteDTO findRestaurante(String id) throws RepositorioException, EntidadNoEncontrada {
        Restaurante r= repositorio.getById(id);
        return toDTO(r);
    }

    @Override
    public void updatePlato(String id, String nombrePlato, PlatoDTO platoDTO) throws RepositorioException, EntidadNoEncontrada {

        Restaurante restaurante = repositorio.getById(id);

        if(!restaurante.containsPlato(nombrePlato)){
            throw new EntidadNoEncontrada("El plato no existe");
        }

        restaurante.updatePlato(nombrePlato, fromDTO(platoDTO));
        
        repositorio.update(restaurante);

    }


    @Override
    public List<RestauranteDTO> searchBy(String propiedad, Object valor) throws RepositorioException, EntidadNoEncontrada {
        return toDTO(repositorio.search(new EqualsSpecification<>(propiedad, valor)));
    }
    


    // Funciones auxiliares manejo DTOS

    // ENTIDAD -> DTO 

    private List<RestauranteDTO> toDTO(List<Restaurante> restaurantes){
        return restaurantes.stream().map(r -> toDTO(r)).collect(Collectors.toList());
    }

    private RestauranteDTO toDTO(Restaurante r){
        RestauranteDTO resDTO = new RestauranteDTO();
        resDTO.setCiudad(r.getCiudad());
        resDTO.setId(r.getId());
        resDTO.setLat(r.getLat());
        resDTO.setLng(r.getLng());
        resDTO.setFechaAlta(r.getFechaAlta());
        resDTO.setNombre(r.getNombre());

        List<SitioTuristicoDTO> sitiosTuristicosDTO = new ArrayList<>();
        for(SitioTuristico s:r.getSitioTuristicos()){
            sitiosTuristicosDTO.add(toDTO(s));
        }

        resDTO.setSitioTuristicos(sitiosTuristicosDTO);
        
        List<PlatoDTO> platos = new ArrayList<>();
        for(Plato p: r.getPlatos()){
            platos.add(toDTO(p));
        }

        resDTO.setPlatos(platos);

        resDTO.setAvgValoraciones(r.getAvgValoraciones());
        resDTO.setNumValoraciones(r.getNumValoraciones());
        resDTO.setIdOpinion(r.getIdOpinion());
        resDTO.setIdGestor(r.getIdGestor());

        return resDTO;
    }

    private PlatoDTO toDTO(Plato p){
        PlatoDTO platoDTO= new PlatoDTO();
        platoDTO.setDescripcion(p.getDescripcion());
        platoDTO.setNombre(p.getNombre());
        platoDTO.setPrecio(p.getPrecio());
        return platoDTO;
    }

    private PlaceDetailsDTO toDTO(PlaceDetails placeDetails){
        PlaceDetailsDTO placeDetailsDTO = new PlaceDetailsDTO();
        placeDetailsDTO.setResumen(placeDetails.getResumen());
        placeDetailsDTO.setCategorias(placeDetails.getCategorias());
        placeDetailsDTO.setImagen(placeDetails.getImagen());
        placeDetailsDTO.setEnlaces(placeDetails.getEnlaces());
        return placeDetailsDTO;
    }

    private SitioTuristicoDTO toDTO(SitioTuristico s){
        SitioTuristicoDTO sitioTuristicoDTO = new SitioTuristicoDTO();
        sitioTuristicoDTO.setDistance(s.getDistance());
        sitioTuristicoDTO.setLat(s.getLat());
        sitioTuristicoDTO.setLng(s.getLng());
        sitioTuristicoDTO.setSummary(s.getSummary());
        sitioTuristicoDTO.setTitle(s.getTitle());
        sitioTuristicoDTO.setWikiURL(s.getWikiURL());

        PlaceDetails placeDetails = s.getPlaceDetails();
        if(placeDetails!=null){
            sitioTuristicoDTO.setPlaceDetails(toDTO(placeDetails));
        }
        return sitioTuristicoDTO;
    }

    // DTO -> ENTIDAD 
    private Plato fromDTO(PlatoDTO platoDTO){
        Plato plato = new Plato();
        plato.setNombre(platoDTO.getNombre());
        plato.setDescripcion(platoDTO.getDescripcion());
        plato.setPrecio(platoDTO.getPrecio());
        return plato;
    }

    private PlaceDetails fromDTO(PlaceDetailsDTO placeDetailsDTO){
        PlaceDetails placeDetails = new PlaceDetails();
        placeDetails.setResumen(placeDetailsDTO.getResumen());
        placeDetails.setCategorias(placeDetailsDTO.getCategorias());
        placeDetails.setEnlaces(placeDetailsDTO.getEnlaces());
        placeDetails.setImagen(placeDetailsDTO.getImagen());
        return placeDetails;
    }

    private SitioTuristico fromDTO(SitioTuristicoDTO sitioDTO){
        SitioTuristico sitioTuristico = new SitioTuristico();
        sitioTuristico.setLat(sitioDTO.getLat());
        sitioTuristico.setLng(sitioDTO.getLng());
        sitioTuristico.setDistance(sitioDTO.getDistance());
        sitioTuristico.setSummary(sitioDTO.getSummary());

        PlaceDetailsDTO placeDetailsDTO = sitioDTO.getPlaceDetails();
        if(placeDetailsDTO!=null){
            sitioTuristico.setPlaceDetails(fromDTO(placeDetailsDTO));
        }
        return sitioTuristico;
    }

    private Restaurante fromDTO(RestauranteDTO restauranteDTO){
        Restaurante restaurante = new Restaurante();

        if(restauranteDTO.getId()!=null){
            restaurante.setId(restauranteDTO.getId());
        }

        List<Plato> platos = new ArrayList<>();
        for (PlatoDTO platoDTO : restauranteDTO.getPlatos()) {
            platos.add(fromDTO(platoDTO));
        }
        restaurante.setPlatos(platos);

        List<SitioTuristico> sitioTuristicos = new ArrayList<>();
        for (SitioTuristicoDTO sitioDTO : restauranteDTO.getSitioTuristicos()) {
            sitioTuristicos.add(fromDTO(sitioDTO));
        }

        restaurante.setSitioTuristicos(sitioTuristicos);

        restaurante.setCiudad(restauranteDTO.getCiudad());
        restaurante.setLat(restauranteDTO.getLat());
        restaurante.setLng(restauranteDTO.getLng());
        restaurante.setNombre(restauranteDTO.getNombre());
        restaurante.setAvgValoraciones(restauranteDTO.getAvgValoraciones());
        restaurante.setNumValoraciones(restauranteDTO.getNumValoraciones());
        restaurante.setIdOpinion(restauranteDTO.getIdOpinion());
        restaurante.setIdGestor(restauranteDTO.getIdGestor());
        return restaurante;
    }

    @Override
    public void altaOpiniones(RestauranteDTO restauranteDTO)
            throws RepositorioException, EntidadNoEncontrada {

        IServicioOpiniones servicioOpiniones = FactoriaServicios.getServicio(IServicioOpiniones.class);
        String idOpinion = servicioOpiniones.darAltaOpinion(restauranteDTO.getNombre());

        restauranteDTO.setAvgValoraciones(0f);
        restauranteDTO.setNumValoraciones(0);
        restauranteDTO.setIdOpinion(idOpinion);

        repositorio.update(fromDTO(restauranteDTO));

    }

    @Override
    public List<Valoracion> getValoraciones(String recurso)
            throws RepositorioException, EntidadNoEncontrada {
        
        IServicioOpiniones servicioOpiniones = FactoriaServicios.getServicio(IServicioOpiniones.class);
        return servicioOpiniones.getValoraciones(recurso);

    }

    /* Rabbit MQ */
    private void configRabbitMQ(){

        ConnectionFactory factory = new ConnectionFactory();
		try {
            factory.setUri("amqps://gdfsrihu:wN9-9EhkyZnXh5PXhUS3DbcJwVTL7axi@rat.rmq2.cloudamqp.com/gdfsrihu");
        
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();
                        
            /** Declaración de la cola y enlace con el exchange **/
    
            final String exchangeName = "evento.valoracion";
            final String queueName = "arso_valoracion_creada";
            final String bindingKey = "nueva_valoracion";
    
            boolean durable = true;
            boolean exclusive = false;
            boolean autodelete = false;
            Map<String, Object> properties = null; // sin propiedades
            channel.queueDeclare(queueName, durable, exclusive, autodelete, properties);
    
            channel.queueBind(queueName, exchangeName, bindingKey);
            
            /** Configuración del consumidor **/
            
            boolean autoAck = false;
            
            String etiquetaConsumidor = "servicio-restaurantes";
            
            // Consumidor push
		
            channel.basicConsume(queueName, autoAck, etiquetaConsumidor, 
            
                new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                        byte[] body) throws IOException {
                    
                                        
                    long deliveryTag = envelope.getDeliveryTag();

                    String contenido = new String(body);
                    
                    ObjectMapper mapper = new ObjectMapper(); // Jackson
                    
                    EventoValoracionCreada evento = mapper.readValue(contenido, EventoValoracionCreada.class);
                    
                    // Procesamos el evento y actualizamos el resumen...                    
                    try {
                        Restaurante r = repositorio.getBy("idOpinion", evento.getOpinionID());
                    
                        r.setNumValoraciones(evento.getNumValoraciones());
                        r.setAvgValoraciones((float)evento.getAvgValoraciones());
                        
                        repositorio.update(r);

                    } catch (RepositorioException | EntidadNoEncontrada e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    // Confirma el procesamiento
                    channel.basicAck(deliveryTag, false);
                }
            });

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
