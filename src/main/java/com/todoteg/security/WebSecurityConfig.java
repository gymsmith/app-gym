package com.todoteg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import reactor.core.publisher.Mono;

//Clase S7 -> Indica que rutas se quieren proteger
@EnableWebFluxSecurity			// habilita la seguridad de web flux
@EnableReactiveMethodSecurity   // y seguridad reactiva
public class WebSecurityConfig {

	// Liberia interna en spring security para hacer hash de texto
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	// Instancias de las configuraciones previas
	@Autowired
	private AuthenticationManager authenticationManager; // Instancia encargada de la valides del token 
	
	@Autowired
	private SecurityContextRepository securityContextRepository; // Instancia extrae el token, y crea el contexto en spring o guadrda en memoria la sesion
	
	@Bean
	public SecurityWebFilterChain securitygWebFilterChain(ServerHttpSecurity http) {
		// sirve como un interceptador, por cada peticion este metodo interviene y con base a sus configuraciones (Validaciones) permite o denega el acceso 
		return http // sobrescribe la peticion http para manejar una serie etapas
				.exceptionHandling() // primera etapa es el manejo de excepciones
				.authenticationEntryPoint((swe, e) -> {					
					return Mono.fromRunnable(() -> {
						swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); // Configura qué hacer cuando la aplicación solicita autenticación, no se envio el token 		
					});
				}).accessDeniedHandler((swe, e) -> {					
					return Mono.fromRunnable(() -> {						
						swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN); //Configura qué hacer cuando un usuario autenticado no tiene la autoridad requerida
					});
				})
				.and() // Concatena la configuracion previa con la nueva configuracion
				.csrf().disable()				// Desabilita los token csrf ya que la vista no es responsabilidad de spring en este caso
				.formLogin().disable()			// Desabilita Login de la seguridad basica de spring
				.httpBasic().disable()
				.authenticationManager(authenticationManager) // configura el mecanismo de autenticacion establecido por nosotros
				.securityContextRepository(securityContextRepository)
				.authorizeExchange() 						// URLS que se van a permitir o prohibir 
				.pathMatchers(HttpMethod.OPTIONS).permitAll()				
				//SWAGGER PARA SPRING SECURITY				Si no se liveran las direcciones de swagger, este no va a cargar
				.pathMatchers("/swagger-resources/**").permitAll()
				.pathMatchers("/swagger-ui.html").permitAll()
				.pathMatchers("/webjars/**").permitAll()
				//SWAGGER PARA SPRING SECURITY
				.pathMatchers("/login").permitAll()
				.pathMatchers("/v2/login").permitAll()
				.pathMatchers("/registrar/**").permitAll()
				//.pathMatchers("/v2/**").hasAnyAuthority("ADMIN") // debe tener el rol admin
				/*.pathMatchers("/v2/**")
					.access((mono, context) -> mono
	                        .map(auth -> auth.getAuthorities()
	                        		.stream()
	                                .filter(e -> e.getAuthority().equals("ADMIN"))
	                                .count() > 0)
	                        .map(AuthorizationDecision::new)
	                )*/
				.pathMatchers("/plan/**").permitAll()
				.pathMatchers("/huella_temp/**").permitAll()
				.pathMatchers("/usuarios/{id}").permitAll()
				.pathMatchers("/usuarios/**").authenticated()
				.pathMatchers("/seguimiento/{id}").permitAll()
				.pathMatchers("/seguimiento/**").authenticated()
				.anyExchange().authenticated() // cualquier otra ruta que no haya sido expuesta en esta configuracion se pide autenticacion
				.and().build();
	}
}
