package com.todoteg.model;

import java.time.LocalDate;
//import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "huellas_temp")
public class HuellaTemp {
	
	@Id
	private String id;
	private Date fecha_creacion = new Date();
	@Field(name = "pc_serial")
	private String pcSerial;
	private byte[] imgHuella;
	private byte[] huella;
	private String texto;
	private LocalDate fecha_actualizacion;
	private String statusPlantilla;
	private String documento;
	private String nombre;
	private String dedo;
	private String opc;
	private Date update_time;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getFecha_creacion() {
		return fecha_creacion;
	}
	public void setFecha_creacion(Date fecha_creacion) {
		this.fecha_creacion = fecha_creacion;
	}
	public String getPc_serial() {
		return pcSerial;
	}
	public void setPc_serial(String pc_serial) {
		this.pcSerial = pc_serial;
	}
	public byte[] getImgHuella() {
		return imgHuella;
	}
	public void setImgHuella(byte[] imgHuella) {
		this.imgHuella = imgHuella;
	}
	public byte[] getHuella() {
		return huella;
	}
	public void setHuella(byte[] huella) {
		this.huella = huella;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public LocalDate getFecha_actualizacion() {
		return fecha_actualizacion;
	}
	public void setFecha_actualizacion(LocalDate fecha_actualizacion) {
		this.fecha_actualizacion = fecha_actualizacion;
	}
	public String getStatusPlantilla() {
		return statusPlantilla;
	}
	public void setStatusPlantilla(String statusPlantilla) {
		this.statusPlantilla = statusPlantilla;
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
	public String getDedo() {
		return dedo;
	}
	public void setDedo(String dedo) {
		this.dedo = dedo;
	}
	public String getOpc() {
		return opc;
	}
	public void setOpc(String opc) {
		this.opc = opc;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "HuellaTemp [id=" + id + ", fecha_creacion=" + fecha_creacion + ", pcSerial=" + pcSerial +  ", texto=" + texto
				+ ", fecha_actualizacion=" + fecha_actualizacion + ", statusPlantilla=" + statusPlantilla
				+ ", documento=" + documento + ", nombre=" + nombre + ", dedo=" + dedo + ", opc=" + opc
				+ ", update_time=" + update_time + "]";
	}

	
	
}
