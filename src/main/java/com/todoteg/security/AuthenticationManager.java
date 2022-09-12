package com.todoteg.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

//Clase S5 -> Valida Token
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager{

	@Autowired
	private JWTUtil jwtUtil; // Utiliza la configuracion previa de generacion, valides, y obtencion del contenido del token
	
	@Override	
	public Mono<Authentication> authenticate(Authentication authentication) {
		String token = authentication.getCredentials().toString(); // Recupera el token
		
		String usuario;
		try {
			usuario = jwtUtil.getUsernameFromToken(token); // Obtengo en Usuario logeado a partir de su token
		} catch (Exception e) {
			usuario = null;
		}
		// verifica si es que el usuario es nulo y ademas el token es valido 
		if (usuario != null && jwtUtil.validateToken(token)) {
			Claims claims = jwtUtil.getAllClaimsFromToken(token); // Decodifica el token, estrayendo toda la data relacionada
			
			List<String> rolesMap = claims.get("roles", List.class); // Obtiene los roles del usuario contenidos en el token en una lista
			
			// se indica a Spring el Usuario autenticado y sus roles para que se alojen en memoria 
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				usuario,
				null,
				rolesMap.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList())
			);
			return Mono.just(auth);
		}else {
			//return Mono.empty();
			return Mono.error(new InterruptedException("Token no válido o ha expirado"));
		}
	}
}

