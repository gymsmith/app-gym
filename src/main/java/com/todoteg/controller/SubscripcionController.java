package com.todoteg.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.todoteg.model.Subscripcion;
import com.todoteg.service.ISubscripcionService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/subscripcion")
public class SubscripcionController {
	
	@Autowired
	private ISubscripcionService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Subscripcion>>> Listar(){
		return Mono.just(ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar()));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Subscripcion>> ListarPorId(@PathVariable("id") String id){
		return service.listarPorId(id)
				.map(Subs -> ResponseEntity
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(Subs))
				.defaultIfEmpty(ResponseEntity.notFound().build()); //build: Cree la entidad de respuesta sin cuerpo.
	}
	
	@PostMapping 
	public Mono<ResponseEntity<Subscripcion>> registrar(@Valid @RequestBody Subscripcion Subs, final ServerHttpRequest req){
		return service.registrar(Subs)
				.map(newSubs -> ResponseEntity
						.created(URI.create(req.getURI().toString().concat("/").concat(newSubs.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(newSubs));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Subscripcion>> modificar(@Valid @RequestBody Subscripcion Subs, @PathVariable String id){
		
		Mono<Subscripcion> SubsBD = service.listarPorId(id);
		Mono<Subscripcion> SubsBody = Mono.just(Subs);
		
		return SubsBD.zipWith(SubsBody, (sBD, sBody)-> {
			sBD.setPlan(sBody.getPlan());
			sBD.setFechaInicial(sBody.getFechaInicial());
			sBD.setFechaFinal(sBody.getFechaFinal());
			sBD.setEstado(sBody.getEstado());
			
			return sBD;
		})
		.flatMap(service::modificar) // Mono<Subscripcion>
		.map(Subscripcion -> ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Subscripcion))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
		return service.listarPorId(id) // Mono<Subscripcion>
				.flatMap(Subs -> {
					return service.eliminar(Subs.getId())
							.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)); // <Mono<ResponseEntity<Void>>
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}

}

