package com.um.arso;

import java.time.LocalDate;

import com.um.arso.models.Opinion;
import com.um.arso.models.Valoracion;
import com.um.arso.repositories.FactoriaRepositorios;
import com.um.arso.repositories.Repositorio;
import com.um.arso.repositories.RepositorioException;

public class App {
  public static void main(String[] args) {
    

    Opinion op1 = new Opinion();
    op1.setRecurso("Universidad");

    Valoracion v1 = new Valoracion();
    v1.setCalificacion(5);
    v1.setEmail("kike@um.es");
    v1.setComentario("Prueba");
    v1.setFechaRegistro(LocalDate.now().toString());

    op1.addValoracion(v1);


    Valoracion v2 = new Valoracion();
    v2.setCalificacion(3);
    v2.setEmail("enrique@um.es");
    v2.setComentario("Todo mal");
    v2.setFechaRegistro(LocalDate.now().toString());

    op1.addValoracion(v2);

    System.out.println(op1);

    Repositorio<Opinion,String> repositorio =  FactoriaRepositorios.getRepositorio(Opinion.class);
    
    try {
      repositorio.add(op1);
    } catch (RepositorioException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
