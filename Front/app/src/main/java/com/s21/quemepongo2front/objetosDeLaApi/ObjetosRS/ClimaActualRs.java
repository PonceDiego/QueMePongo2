package com.s21.quemepongo2front.objetosDeLaApi.ObjetosRS;


public class ClimaActualRs {
	
	private int ciudadId;
	private String ciudadNombre,nombreClima;
	private double temperatura, viento, humedad ;
	
	
	public ClimaActualRs() {
	}
	

	
	public int getCiudadId() {
		return ciudadId;
	}
	public void setCiudadId(int ciudadId) {
		this.ciudadId = ciudadId;
	}
	public String getCiudadNombre() {
		return ciudadNombre;
	}
	public void setCiudadNombre(String ciudadNombre) {
		this.ciudadNombre = ciudadNombre;
	}

	public double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}

	public double getViento() {
		return viento;
	}

	public void setViento(double wind) {
		this.viento = wind;
	}

	public double getHumedad() {
		return humedad;
	}

	public void setHumedad(double humedad) {
		this.humedad = humedad;
	}

	public String getNombreClima() {
		return nombreClima;
	}

	public void setNombreClima(String nombreClima) {
		this.nombreClima = nombreClima;
	}
}
