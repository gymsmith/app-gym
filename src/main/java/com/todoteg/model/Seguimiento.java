package com.todoteg.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Seguimiento {
	
	@Id
	private String id;
	private Cliente usuario;
	private String peso;
	private String medidaCuello;
	private String medidaPecho;
	private String medidaAbdomen;
	private String medidaBiceps;
	private String medidaMuslo;
	private String medidaPantorrilla;
	private String medidaHombro;
	private String medidaCintura;
	private String medidaCadera;
	private LocalDate fecha;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Cliente getUsuario() {
		return usuario;
	}
	public void setUsuario(Cliente usuario) {
		this.usuario = usuario;
	}
	public String getPeso() {
		return peso;
	}
	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getMedidaCuello() {
		return medidaCuello;
	}
	public void setMedidaCuello(String medidaCuello) {
		this.medidaCuello = medidaCuello;
	}
	public String getMedidaPecho() {
		return medidaPecho;
	}
	public void setMedidaPecho(String medidaPecho) {
		this.medidaPecho = medidaPecho;
	}
	public String getMedidaAbdomen() {
		return medidaAbdomen;
	}
	public void setMedidaAbdomen(String medidaAbdomen) {
		this.medidaAbdomen = medidaAbdomen;
	}
	public String getMedidaBiceps() {
		return medidaBiceps;
	}
	public void setMedidaBiceps(String medidaBiceps) {
		this.medidaBiceps = medidaBiceps;
	}
	public String getMedidaMuslo() {
		return medidaMuslo;
	}
	public void setMedidaMuslo(String medidaMuslo) {
		this.medidaMuslo = medidaMuslo;
	}
	public String getMedidaPantorrilla() {
		return medidaPantorrilla;
	}
	public void setMedidaPantorrilla(String medidaPantorrilla) {
		this.medidaPantorrilla = medidaPantorrilla;
	}
	public String getMedidaHombro() {
		return medidaHombro;
	}
	public void setMedidaHombro(String medidaHombro) {
		this.medidaHombro = medidaHombro;
	}
	public String getMedidaCintura() {
		return medidaCintura;
	}
	public void setMedidaCintura(String medidaCintura) {
		this.medidaCintura = medidaCintura;
	}
	public String getMedidaCadera() {
		return medidaCadera;
	}
	public void setMedidaCadera(String medidaCadera) {
		this.medidaCadera = medidaCadera;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	
	
	
}
