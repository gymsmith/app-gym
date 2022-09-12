package com.todoteg.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpushResponseUtil {
	private String id;
	private long timestamp;
	private String texto;
	private String statusPlantilla;
	private String nombre;
	private String documento;
	private byte[] imgHuella;
	private String tipo;
	
	
	
	public HttpushResponseUtil(String tipo) {
		this.tipo = tipo;
	}
	public HttpushResponseUtil(String id, long timestamp, String texto, String statusPlantilla, String nombre,
			String documento, byte[] imgHuella, String tipo) {
		super();
		this.id = id;
		this.timestamp = timestamp;
		this.texto = texto;
		this.statusPlantilla = statusPlantilla;
		this.nombre = nombre;
		this.documento = documento;
		this.imgHuella = imgHuella;
		this.tipo = tipo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
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
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public byte[] getImgHuella() {
		return imgHuella;
	}
	public void setImgHuella(byte[] imgHuella) {
		this.imgHuella = imgHuella;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
