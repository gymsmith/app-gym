package com.todoteg.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteHuellaUtil {
	private Long count;
	private String documento;
	private String nombre_completo;
	private String nombre_dedo;
	private byte[] Huella;
	private byte[] imgHuella;
	
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public String getNombre_completo() {
		return nombre_completo;
	}
	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}
	public String getNombre_dedo() {
		return nombre_dedo;
	}
	public void setNombre_dedo(String nombre_dedo) {
		this.nombre_dedo = nombre_dedo;
	}
	public byte[] getHuella() {
		return Huella;
	}
	public void setHuella(byte[] huella) {
		Huella = huella;
	}
	public byte[] getImgHuella() {
		return imgHuella;
	}
	public void setImgHuella(byte[] imgHuella) {
		this.imgHuella = imgHuella;
	}
	
	
	
}
