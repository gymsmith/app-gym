package com.todoteg.repo;

import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;

import com.todoteg.model.HuellaTemp;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IHuellaTempRepo extends IGenericRepo<HuellaTemp, String> {
	Mono<HuellaTemp> findByPcSerial (String serial);
	
	
	// Optiene Solo el campo update_time siempre y cuando pcSerial coincida
	@Query(value = "{'pcSerial' :  ?0 }", fields = "{ ?1 : 1 , '_id': 0}")
	Flux<HuellaTemp> huellaUpdateTime(String serial, String campo, Pageable pageable);
	
	@Query(value = "{'pcSerial' :  ?0 }", fields = "{ ?1 : 1 , ?2 : 1, '_id': 0}")
	Flux<HuellaTemp> huellaUpdateTime(String serial, String campo, String campo2, Pageable pageable);
	
	@Query(value = "{}", fields = "{'pcSerial': 1,'imgHuella': 1,'update_time': 1,'texto': 1,'statusPlantilla': 1,'documento': 1,'nombre': 1,'opc': 1}")
	Flux<HuellaTemp> huellaHttpush(Pageable pageable);
}
