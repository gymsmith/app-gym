package com.todoteg.security;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

//Clase S4 -> Valides y creacion del token
@Component
public class JWTUtil implements Serializable{

	// letura de esta propiedad en el properties
	@Value("${jjwt.secret}")
	private String secret;
	
	@Value("${jjwt.expiration}")
	private String expirationTime;
	
	public Claims getAllClaimsFromToken(String token) {
		// Encargado de la decodificacion del token recibido
		return Jwts.parserBuilder()
				.setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	
	public String getUsernameFromToken(String token) {
		// A partir del token se obtiene para quien se genero este tocken
		return getAllClaimsFromToken(token).getSubject();
	}
	
	public Date getExpirationDateFromToken(String token) {
		// A partir del token se obtiene la fecha de expiracion
		return getAllClaimsFromToken(token).getExpiration();
	}
	
	private Boolean isTokenExpired(String token) {
		// Comprueba si la fecha de expiracion es anterior a la fecha actual. 
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	//Aqui se agrega al payload del token
	public String generateToken(User user) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", user.getRoles());
		claims.put("id", user.getId());
		claims.put("nombres", user.getNombres());
		return doGenerateToken(claims, user.getUsername());
	}
	
	/* Metodo encargado de generar Token
	 * este metodo recibe un conjunto de valores a modo de diccionario
	 * con la informacion que se quiera encriptar en el token 
	 * esta informacion no debe ser sencible ya que esta a disposicion del usuario
	 * 
	 * como segundo parametro recibe el usuario para el que se genero el token*/
	private String doGenerateToken(Map<String, Object> claims, String username) {
		Long expirationTimeLong = Long.parseLong(expirationTime);
		
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + expirationTimeLong * 1000); // se calcula la fecha de expiracion del token
		
		// codigo como se solia trabajar en el pasado
		/*return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, Base64.getEncoder().encodeToString(secret.getBytes()))
				.compact();
		*/
		
		SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes()); // Genera el key o firma del token a partir de la cadena autogenerada del properties
		
		// Compacta todas las configuraciones anteriores para retornar una cadena con el formato JWT 
		return Jwts.builder()
				   .setClaims(claims)    		  // payload -> info adicional no sencible que contiene el token 
				   .setSubject(username) 		  // quien genero el token
				   .setIssuedAt(createdDate) 	  // fecha de creacion del token
				   .setExpiration(expirationDate) // fecha de expiracion 
				   .signWith(key)				  // llave de cracion
				   .compact();		
	}
	
	public Boolean validateToken(String token) {
		// recibe el token y valida si esta expirado
		return !isTokenExpired(token);
	}
}
