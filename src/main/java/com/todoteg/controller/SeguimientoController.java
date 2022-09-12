package com.todoteg.controller;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todoteg.model.Seguimiento;
import com.todoteg.pagination.PageSupport;
import com.todoteg.service.ISeguimientoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/seguimiento")
public class SeguimientoController {
	
	@Autowired
	private ISeguimientoService service;
	
	@GetMapping
	public Mono<ResponseEntity<Flux<Seguimiento>>> Listar(){
		return Mono.just(ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.listar()));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Seguimiento>> ListarPorId(@PathVariable("id") String id){
		return service.listarPorId(id)
				.map(su -> ResponseEntity
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(su))
				.defaultIfEmpty(ResponseEntity.notFound().build()); //build: Cree la entidad de respuesta sin cuerpo.
	}
	
	@GetMapping("/usuario/{id}")
	public Mono<ResponseEntity<PageSupport<Seguimiento>>> ListarPorUsuario(
			@PathVariable("id") String id,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "5") int size){
		
		Sort ordenamiento = Sort.by(Sort.Direction.DESC, "fecha");
		Pageable pageRequest = PageRequest.of(page, size, ordenamiento);
		Mono<PageSupport<Seguimiento>> segui =  service.buscarPorUsuario(id, pageRequest);
		
		return segui
				.map(datos -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(datos))
				.defaultIfEmpty(ResponseEntity.noContent().build());
	}
	
	@PostMapping 
	public Mono<ResponseEntity<Seguimiento>> registrar(@Valid @RequestBody Seguimiento s, final ServerHttpRequest req){
		return service.registrar(s)
				.map(newSegui -> ResponseEntity
						.created(URI.create(req.getURI().toString().concat("/").concat(newSegui.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(newSegui));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Seguimiento>> modificar(@Valid @RequestBody Seguimiento p, @PathVariable String id){
		
		Mono<Seguimiento> SeguiBD = service.listarPorId(id);
		Mono<Seguimiento> SeguiBody = Mono.just(p);
		
		return SeguiBD.zipWith(SeguiBody, (sBD, sBody)-> {
			sBD.setPeso(sBody.getPeso());
			sBD.setPeso(sBody.getPeso());
			sBD.setMedidaCuello(sBody.getMedidaCuello());
			sBD.setMedidaPecho(sBody.getMedidaPecho());
			sBD.setMedidaAbdomen(sBody.getMedidaAbdomen());
			sBD.setMedidaBiceps(sBody.getMedidaBiceps());
			sBD.setMedidaMuslo(sBody.getMedidaMuslo());
			sBD.setMedidaPantorrilla(sBody.getMedidaPantorrilla());
			sBD.setMedidaHombro(sBody.getMedidaHombro());
			sBD.setMedidaCintura(sBody.getMedidaCintura());
			sBD.setMedidaCadera(sBody.getMedidaCadera());
			
			return sBD;
		})
		.flatMap(service::modificar) // Mono<Seguimiento>
		.map(Seguimiento -> ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(Seguimiento))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
		return service.listarPorId(id) // Mono<Seguimiento>
				.flatMap(s -> {
					return service.eliminar(s.getId())
							.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)); // <Mono<ResponseEntity<Void>>
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}

}

