package com.todoteg.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // Para evitar que se expongan campos nulos en la respuesta del servicio
@Document(collection = "usuarios")
public class Usuario {

	@Id
	private String id;
	private String usuario;
	
	private String identificacion; // cedula
	private String nombres;
	private byte[] huella;
	private byte[] imgHuella;
	private String firma;
	
	private String clave;
	private Boolean estado;
	private List<Rol> roles; // Variamos el ejercicio y decimo que es una lista de Roles como tal
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public Boolean getEstado() {
		return estado;
	}
	public void setEstado(Boolean estado) {
		this.estado = estado;
	}
	public List<Rol> getRoles() {
		return roles;
	}
	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}
	
	
	public String getIdentificacion() {
		return identificacion;
	}
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	public String getNombres() {
		return nombres;
	}
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}
	public byte[] getHuella() {
		return huella;
	}
	public void setHuella(byte[] huella) {
		this.huella = huella;
	}
	public byte[] getImgHuella() {
		return imgHuella;
	}
	public void setImgHuella(byte[] imgHuella) {
		this.imgHuella = imgHuella;
	}
	public String getFirma() {
		return firma;
	}
	public void setFirma(String firma) {
		this.firma = firma;
	}
	
	

	
}
