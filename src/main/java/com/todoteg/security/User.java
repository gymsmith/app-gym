package com.todoteg.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

//Clase S1

// UserDetails Interfas propia de spring security -> es una plantilla con la cual adecuar la informacion y dar la data necesaria a spring pueda gestionarla
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private String id; 
	private String nombres;

	private String username;
	
	private String password;
	
	private Boolean enabled;
		
	private List<String> roles;

	public User(String id, String nombres, String username, String password, Boolean enabled, List<String> authorities) {
		super();
		this.id = id;
		this.nombres = nombres;

		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.roles = authorities;
	}

	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}




	@Override
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	/* ROL en spring se denomina Authorities.
	 * Spring para gestionar los roles asociados al usuario, 
	 * los engloba en objetos definidos por la siguiente interfaz: "GrantedAuthority" 
	 * con la estructura necesaria para que spring pueda gestionarlos y diferenciarlos
	 * ya que este no acepta un string basico
	 * */
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
	}

	@JsonIgnore 	// para que la clave no sea visible
	@Override
	public String getPassword() {
		return password;
	}
	
	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}