package com.um.arso.rest;

import java.net.URI;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import com.um.arso.dto.PlatoDTO;
import com.um.arso.dto.RestauranteDTO;
import com.um.arso.dto.SitioTuristicoDTO;
import com.um.arso.models.Restaurante;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.rest.seguridad.AvailableRoles;
import com.um.arso.rest.seguridad.Secured;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioRestaurantes;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api
@Path("restaurantes")
public class RestaurantesControladorRest {

	private IServicioRestaurantes servicio = FactoriaServicios.getServicio(IServicioRestaurantes.class);

	@Context
	private UriInfo uriInfo;

	@Context
	private SecurityContext securityContext;

	// curl -i -X GET -H "Accept: application/json" -H "Authorization: Bearer $TOKEN" http://localhost:8090/restaurantes/642bff4ee3196b7318ec9ebf
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta un restaurante", notes = "Retorna un restaurante utilizando su id", response = Restaurante.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
	@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado") })
	public Response getRestaurante(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id) throws Exception,EntidadNoEncontrada {

		RestauranteDTO r;

		r = servicio.findRestaurante(id);

	

		return Response.status(Response.Status.OK).entity(r).build();
		 

	}

	// curl -i -X GET -H "Accept: application/json" -H "Authorization: Bearer $TOKEN" http://localhost:8090/restaurantes/

	@GET
	@Path("/")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta todos los restaurantes", notes = "Retorna los restaurantes", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = "")})
	public Response getRestaurantes() throws Exception {

		List<RestauranteDTO> restaurantes = servicio.getAllRestaurantes();

		return Response.ok(restaurantes).build();


	}
	
	//curl -i -X DELETE -H "Authorization: Bearer $TOKEN" http://localhost:8090/restaurantes/641338a066163a350a21217e

	@DELETE
	@Path("/{id}")
	@Secured(AvailableRoles.GESTOR)
	public Response removeRestaurante(@PathParam("id") String id) throws Exception,EntidadNoEncontrada,RepositorioException {

		RestauranteDTO r = servicio.findRestaurante(id);
		if(!r.getIdGestor().equals(securityContext.getUserPrincipal().getName())){
			return Response.status(Status.UNAUTHORIZED).build();
		}

		servicio.removeRestaurante(id);

		return Response.status(Response.Status.NO_CONTENT).build();

	}

	// curl -i -X POST -H "Content-type: application/json"  -H "Authorization: Bearer $TOKEN" -d @test-files/restaurante.json http://localhost:8090/restaurantes/
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	public Response createRestaurante(RestauranteDTO r) throws RepositorioException{


		if(r.getNombre()==null || r.getNombre().isBlank()){
			throw new IllegalArgumentException("\"Debes poner un nombre\"");
		}

		if(r.getCiudad()==null || r.getCiudad().isBlank()){
			throw new IllegalArgumentException("Debes poner un ciudad");
		}

		if(r.getLat()==null || r.getLng()==null){
			throw new IllegalArgumentException("Debes introducir las coordenadas del restaurante");
		}

		String idUsuario = securityContext.getUserPrincipal().getName();
		r.setIdGestor(idUsuario);

		String id = servicio.create(r);


		URI nuevaURL = uriInfo.getAbsolutePathBuilder().path(id).build();

		return Response.created(nuevaURL).build();

	}
	
	// curl -i -X PUT -H "Content-type: application/json"-H "Authorization: Bearer $TOKEN"  -d @test-files/restaurante.json http://localhost:8090/restaurantes/642bff4ee3196b7318ec9ebf
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	public Response updateRestaurante(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id,RestauranteDTO r) throws RepositorioException, EntidadNoEncontrada{

		if(r==null)
			throw new IllegalArgumentException("Debes introducir un restaurante en la petición");

		if (!id.equals(r.getId()))
			throw new IllegalArgumentException("El identificador no coincide: " + id);

		RestauranteDTO r2 = servicio.findRestaurante(id);
		if(!r2.getIdGestor().equals(securityContext.getUserPrincipal().getName())){
			return Response.status(Status.UNAUTHORIZED).build();
		}
		servicio.update(r);

		return Response.noContent().build();
		
	}

	// curl -i -X GET -H "Accept: application/json" -H "Authorization: Bearer $TOKEN" http://localhost:8090/restaurantes/642bff4ee3196b7318ec9ebf/sitios-turisticos/
	@GET
	@Path("/{id}/sitios-turisticos")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "Consulta los sitios turisticos cerca de un restaurante", notes = "Retorna sitios turisticos", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = HttpServletResponse.SC_OK, message = ""),
	@ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Restaurante no encontrado") })
	@Secured(AvailableRoles.GESTOR)
	public Response getSitiosTuristicosCercanos(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id) throws Exception {

		RestauranteDTO r2 = servicio.findRestaurante(id);
		if(!r2.getIdGestor().equals(securityContext.getUserPrincipal().getName())){
			return Response.status(Status.UNAUTHORIZED).build();
		}

		List<SitioTuristicoDTO> sitioTuristicos = servicio.getSitiosTuristicosCercanos(id);


		return Response.ok(sitioTuristicos).build();
		
	}

	// curl -i -X PUT -H "Content-type: application/json" -H "Authorization: Bearer $TOKEN" -d @test-files/sitios-turisticos.json http://localhost:8090/restaurantes/642bff4ee3196b7318ec9ebf/sitios-turisticos

	@PUT
	@Path("/{id}/sitios-turisticos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	public Response setSitiosTuristicos(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id, List<SitioTuristicoDTO> sitiosTuristicos) throws RepositorioException, EntidadNoEncontrada{

		RestauranteDTO r2 = servicio.findRestaurante(id);
		if(!r2.getIdGestor().equals(securityContext.getUserPrincipal().getName())){
			return Response.status(Status.UNAUTHORIZED).build();
		}

		servicio.setSitiosTuristicos(id, sitiosTuristicos);

		return Response.noContent().build();

	}

	// curl -i -X POST -H "Content-type: application/json" -H "Authorization: Bearer $TOKEN" -d @test-files/plato.json http://localhost:8090/restaurantes/642bff4ee3196b7318ec9ebf/platos

	@POST
	@Path("/{id}/platos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	public Response crearPlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id, PlatoDTO plato) throws RepositorioException, EntidadNoEncontrada{

		RestauranteDTO r2 = servicio.findRestaurante(id);
		if(!r2.getIdGestor().equals(securityContext.getUserPrincipal().getName())){
			return Response.status(Status.UNAUTHORIZED).build();
		}

		servicio.addPlato(id, plato);

		return Response.noContent().build();

	}

	// curl -i -X DELETE -H "Authorization: Bearer $TOKEN" http://localhost:8090/restaurantes/642bff4ee3196b7318ec9ebf/platos/Spaguetti%20Carbonara

	@DELETE
	@Path("/{id}/platos/{nombre}")
	@Secured(AvailableRoles.GESTOR)
	public Response eliminarPlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id, @ApiParam(value = "Nombre del plato", required = true) @PathParam("nombre") String nombre) throws RepositorioException, EntidadNoEncontrada{

		RestauranteDTO r2 = servicio.findRestaurante(id);
		if(!r2.getIdGestor().equals(securityContext.getUserPrincipal().getName())){
			return Response.status(Status.UNAUTHORIZED).build();
		}

		servicio.removePlato(id, nombre);

		return Response.noContent().build();

	}

	// curl -i -X PUT -H "Content-type: application/json" -H "Authorization: Bearer $TOKEN" -d @test-files/plato.json http://localhost:8090/restaurantes/642bff4ee3196b7318ec9ebf/platos/Spaguetti%20Carbonara
	@PUT
	@Path("/{id}/platos/{nombre}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Secured(AvailableRoles.GESTOR)
	public Response actualizarPlato(@ApiParam(value = "id del restaurante", required = true) @PathParam("id") String id, @ApiParam(value = "Nombre del plato", required = true) @PathParam("nombre") String nombre, PlatoDTO plato) throws RepositorioException, EntidadNoEncontrada{

		RestauranteDTO r2 = servicio.findRestaurante(id);
		if(!r2.getIdGestor().equals(securityContext.getUserPrincipal().getName())){
			return Response.status(Status.UNAUTHORIZED).build();
		}
		
		servicio.updatePlato(id, nombre, plato);

		return Response.noContent().build();

	}



}
