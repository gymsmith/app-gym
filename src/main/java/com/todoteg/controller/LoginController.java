package com.todoteg.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;

import com.todoteg.security.AuthRequest;
import com.todoteg.security.AuthResponse;
import com.todoteg.security.ErrorLogin;
import com.todoteg.security.JWTUtil;

import com.todoteg.service.IUsuarioService;

import reactor.core.publisher.Mono;

//Clase S8
@RestController
public class LoginController {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private IUsuarioService service;
	
	/*
	 * @Autowired private IClienteService serviceCliente;
	 */
	
	@PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest ar){
		return service.buscarPorUsuario(ar.getUsername())
				.map((userDetails) -> {
					System.out.println(userDetails);
					// Valida si es que la clave que recibio en el body puede alguna ves llegar a ser el string encriptado que esta en la BD
					if(BCrypt.checkpw(ar.getPassword(), userDetails.getPassword())) {
						String token = jwtUtil.generateToken(userDetails); // En caso de cumplir la condicion, se aplica el proceso de generar el token
						Date expiracion = jwtUtil.getExpirationDateFromToken(token); // generar la fecha de expiracion
						
						return ResponseEntity.ok(new AuthResponse(token, expiracion)); // responde con un status 200 el token y la expiracion envuelto en la estructura que se definio para tal fin
					}else {
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorLogin("credenciales incorrectas", new Date()));
					}
				}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
	// se aplica lo mismo que en el primer enpoind de logeo, a diferencia que aqui se recibe la informacion en el header mas no en el body.
	/*
	 * @PostMapping("/v2/login") public Mono<ResponseEntity<?>>
	 * login(@RequestHeader("usuario") String DocumentoID){ return
	 * serviceCliente.ObtenerUsuarioPorNombre(DocumentoID) .map((userDetails) -> {
	 * 
	 * if(BCrypt.checkpw(DocumentoID, userDetails.getPassword())) { String token =
	 * jwtUtil.generateToken(userDetails); Date expiracion =
	 * jwtUtil.getExpirationDateFromToken(token);
	 * 
	 * return ResponseEntity.ok(new AuthResponse(token, expiracion)); }else { return
	 * ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new
	 * ErrorLogin("credenciales incorrectas", new Date())); }
	 * }).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); }
	 */
}
