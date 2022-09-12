package com.todoteg.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HuellaTempEntrada {
	
	private String serial;
	private byte[] huella;
	private byte[] imgHuella;
	private String texto;
	private String statusPlantilla;
	
	private String documento;
	private String nombre;
	private String dedo;
	private String option;
	
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
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
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public String getStatusPlantilla() {
		return statusPlantilla;
	}
	public void setStatusPlantilla(String statusPlantilla) {
		this.statusPlantilla = statusPlantilla;
	}
	
	
	public String getDedo() {
		return dedo;
	}
	public void setDedo(String dedo) {
		this.dedo = dedo;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	
	
	
	
	
	
}
