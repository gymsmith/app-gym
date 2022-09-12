package com.todoteg.service;

import java.util.List;

import com.todoteg.model.HttpushResponseUtil;
import com.todoteg.model.HuellaTemp;

import reactor.core.publisher.Mono;



public interface IHuellaTempService extends ICRUD<HuellaTemp, String> {
	
	Mono<Void> eliminarTodo();
	
	Mono<HuellaTemp> ObtenerHuellaTempPorSerial(String serial);
	
	Mono<List<HuellaTemp>> ObtenerUpdateTimePorSerial(String serial, String campo);
	
	Mono<HttpushResponseUtil> ObtenerHuellaHttpush();
	
	
}
