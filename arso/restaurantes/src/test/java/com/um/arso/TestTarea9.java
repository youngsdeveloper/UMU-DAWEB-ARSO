package com.um.arso;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.um.arso.dto.RestauranteDTO;
import com.um.arso.models.Valoracion;
import com.um.arso.repositories.EntidadNoEncontrada;
import com.um.arso.repositories.RepositorioException;
import com.um.arso.services.FactoriaServicios;
import com.um.arso.services.IServicioOpiniones;
import com.um.arso.services.IServicioRestaurantes;


public class TestTarea9 {
    static IServicioRestaurantes servicioRestaurantes;
    static IServicioOpiniones serviciOpiniones;

    
    @Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();

	final IServicioOpiniones gestorMock = context.mock(IServicioOpiniones.class);

    @Before
	public void setUp() {
		servicioRestaurantes = FactoriaServicios.getServicio(IServicioRestaurantes.class);
	}

    @Test
    public void testDarAltaOpinion() throws RepositorioException, EntidadNoEncontrada{
        RestauranteDTO r = new RestauranteDTO();
        r.setNombre("testMock");
        r.setId("644c11808270c18135c2cbba");
        context.checking(new Expectations(){
            {
                oneOf(gestorMock).darAltaOpinion("testMock");

            }
        });
       gestorMock.darAltaOpinion("testMock");
    }

    @Test
    public void testGetValoraciones(){
        List<Valoracion> valoraciones =new LinkedList<Valoracion>();
        context.checking(new Expectations(){
            {
                oneOf(gestorMock).getValoraciones("testMock");
                will(returnValue(valoraciones) );
            }
        });
        assertEquals(valoraciones,gestorMock.getValoraciones(("testMock")));
    } 
}