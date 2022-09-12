package com.todoteg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

//Clase S6 -> Extrae el token del request
@Component
public class SecurityContextRepository implements ServerSecurityContextRepository{

	@Autowired
	private AuthenticationManager authenticationManager; // Instancia encargada de la valides del token 

	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		ServerHttpRequest request = swe.getRequest(); 								 // Recupera el request
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);// Extrae el token recibido en el request
		
		//Bearer eyasda.sdasasdasd
		if (authHeader != null) { // Valida si se recibio el token en la peticion 
			if (authHeader.startsWith("Bearer ") || authHeader.startsWith("bearer ")) { // verifica si se antepuso el prefijo Bearer al token
				String authToken = authHeader.substring(7); // en caso de cumplirse la condicion extrar un subcadena desde la posicion 7 del string original, hasta el final osea el token original sin la palabra bearer
				Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken); // se registra el logeo
				return this.authenticationManager.authenticate(auth).map((authentication) -> {
					return new SecurityContextImpl(authentication);
				});
			}else {
				return Mono.error(new InterruptedException("No estas autorizado"));			
			}
		}
		return Mono.empty();
	}
}
