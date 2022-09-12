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

import com.todoteg.model.Plan;
import com.todoteg.service.IPlanService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/plan")
public class PlanController {
	
	@Autowired
	private IPlanService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Plan>>> Listar(){
		return Mono.just(ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar()));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Plan>> ListarPorId(@PathVariable("id") String id){
		return service.listarPorId(id)
				.map(p -> ResponseEntity
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(p))
				.defaultIfEmpty(ResponseEntity.notFound().build()); //build: Cree la entidad de respuesta sin cuerpo.
	}
	
	@PostMapping 
	public Mono<ResponseEntity<Plan>> registrar(@Valid @RequestBody Plan p, final ServerHttpRequest req){
		return service.registrar(p)
				.map(newPlan -> ResponseEntity
						.created(URI.create(req.getURI().toString().concat("/").concat(newPlan.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(newPlan));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Plan>> modificar(@Valid @RequestBody Plan p, @PathVariable String id){
		
		Mono<Plan> PlanBD = service.listarPorId(id);
		Mono<Plan> PlanBody = Mono.just(p);
		
		return PlanBD.zipWith(PlanBody, (pBD, pBody)-> {
			pBD.setTitulo(pBody.getTitulo());
			pBD.setSubtitulo(pBody.getSubtitulo());
			pBD.setPrecio(pBody.getPrecio());
			pBD.setDescripcion(pBody.getDescripcion());
			
			return pBD;
		})
		.flatMap(service::modificar) // Mono<Plan>
		.map(Plan -> ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Plan))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
		return service.listarPorId(id) // Mono<Plan>
				.flatMap(p -> {
					return service.eliminar(p.getId())
							.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)); // <Mono<ResponseEntity<Void>>
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}

}

