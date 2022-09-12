package com.todoteg.repo;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import com.todoteg.model.Cliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



public interface IClienteRepo extends IGenericRepo<Cliente, String> {
	//Mono<Cliente> findByNombres (String usuario);
	
	/*
	 * @Query("{$or: [{'nombres': ?0},{'identificacion': ?0}]}") Mono<Cliente>
	 * buscarUsuario (String usuario);
	 */
	
	// { 'nombres': /^?0/i  } -> indica que la busqueda debe iniciar por los caracteres que recibe dicha funcion sin distincion entre mayusculas y minusculas.
	@Query("{$or: [{'nombres': { $regex: /^?0/, $options: 'i'} }, {'subscripcion.plan._id': ?0}]}")
	Flux<Cliente> buscarUsuarios(String nombres, Pageable pagina);
	
	
	@Query(value = "{$or: [{'nombres': { $regex: /^?0/, $options: 'i'} }, {'subscripcion.plan._id': ?0}]}", count=true)
	Mono<Long> allByNombresAndPlan(String nombres);
}
