package com.todoteg.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteInfoUtil {
	private String identificacion;
	@NotNull
	private String nombres;
	@Size(min = 10, max = 12)
	private String telefono;
	private int edad;
	private String peso;
	private String altura;
	private String sexo;
	private byte[] imgHuella;
	private String firma;
	private Subscripcion subscripcion;
	private List<Seguimiento> seguimiento;
	private Boolean activo;
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
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}
	public String getAltura() {
		return altura;
	}
	public void setAltura(String altura) {
		this.altura = altura;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
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
	public Subscripcion getSubscripcion() {
		return subscripcion;
	}
	public void setSubscripcion(Subscripcion subscripcion) {
		this.subscripcion = subscripcion;
	}
	public List<Seguimiento> getSeguimiento() {
		return seguimiento;
	}
	public void setSeguimiento(List<Seguimiento> lista) {
		this.seguimiento = lista;
	}
	public Boolean getActivo() {
		return activo;
	}
	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
	
	
}
