package com.todoteg.repo;

import java.util.HashMap;

import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;

import com.todoteg.model.Seguimiento;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ISeguimientoRepo extends IGenericRepo<Seguimiento, String> {
	Flux<Seguimiento> findByUsuario (HashMap<String, String> usuarioId, Pageable paginacion);
	
	@Query(value = "{ usuario: ?0 }", count=true)
	Mono<Long> allByUsuario(HashMap<String, String> usuarioId);
}
