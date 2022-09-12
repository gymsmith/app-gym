package com.todoteg.model;

import org.springframework.data.annotation.Id;

public class Empleado {
	
	@Id
	private String identificacion;
	private String nombres;
	private String Huella;
	private String Firma;
	private String Contrato;
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
	public String getHuella() {
		return Huella;
	}
	public void setHuella(String huella) {
		Huella = huella;
	}
	public String getFirma() {
		return Firma;
	}
	public void setFirma(String firma) {
		Firma = firma;
	}
	public String getContrato() {
		return Contrato;
	}
	public void setContrato(String contrato) {
		Contrato = contrato;
	}
	
	
}
