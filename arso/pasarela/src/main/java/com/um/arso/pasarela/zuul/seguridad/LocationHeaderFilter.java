package com.um.arso.pasarela.zuul.seguridad;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class LocationHeaderFilter extends ZuulFilter {
	

	@Override
	public boolean shouldFilter() {
        return true; // Especifica cuándo aplicar el filtro
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request =  context.getRequest();
		/*
		final String location = request.getHeader(HttpHeaders.LOCATION);

		/*
		 * Mapeamos la URL de 8080 a 8090.
		 
		if(location!=null){
			System.out.println("Hay location...");
			// Hay location, cambiar URL
			String newLocation = location.replace("http://localhost:8080/api/", "http://localhost:8090/");
			response.setHeader(HttpHeaders.LOCATION, newLocation);
		}else{
			System.out.println("No hay location");
		} */
		

		return null;
	}

	@Override
	public String filterType() {
        return "post";
	}

	@Override
	public int filterOrder() {
        return 1; // Especifica el orden de ejecución del filtro
	}
	
	
}