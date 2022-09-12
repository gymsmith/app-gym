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

import com.todoteg.model.Cliente;
import com.todoteg.model.ClienteInfoUtil;
import com.todoteg.pagination.PageSupport;
import com.todoteg.service.IClienteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/usuarios") // se establece la direccion url particular
public class ClienteController {

	@Autowired
	private IClienteService service;
	
	//ResponseEntity: Clase que me permite extender funcionalidades sobre la peticion http, controlar el status code.
	@GetMapping
	public Mono<ResponseEntity<Flux<Cliente>>> listar(){
		
		Flux<Cliente> usuarios = service.listar();
		
		return Mono.just(ResponseEntity
				.ok() // indica el status code de respuesta
				.contentType(MediaType.APPLICATION_JSON)
				.body(usuarios)); // se envia informacion en el body de la respuesta
	}
	
	@GetMapping("/pageable")
	public Mono<ResponseEntity<PageSupport<ClienteInfoUtil>>> listarPageable(
			@RequestParam(name = "search", defaultValue = "") String search,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "7") int size
			){
		
		Sort ordenamiento = Sort.by(Sort.Direction.ASC, "nombres");
		Pageable pageRequest = PageRequest.of(page, size, ordenamiento);
		
		return service.ListarPagina(search, pageRequest)
				.map(pag -> ResponseEntity.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(pag))
				.defaultIfEmpty(ResponseEntity.noContent().build());
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<ClienteInfoUtil>> listarPorId(@PathVariable("id") String id){
		return service.buscarPorId(id) // Mono<Usuario>
				.map(usuarioPorID -> ResponseEntity
						.ok()
						.contentType(MediaType.APPLICATION_JSON)
						.body(usuarioPorID)) // se Transforma a Mono<ResponseEntity<Usuario>>
				.defaultIfEmpty(ResponseEntity.notFound().build()); //build: Cree la entidad de respuesta sin cuerpo.
	}
	
	/*
	 * @GetMapping("/nombre/") public Mono<ResponseEntity<Cliente>>
	 * listarPorUsuario(@RequestHeader("nombre") String nombre){ return
	 * service.ObtenerUsuarioPorNombre(nombre) // Mono<Usuario>
	 * .map(usuarioPorNombre -> ResponseEntity .ok()
	 * .contentType(MediaType.APPLICATION_JSON) .body(usuarioPorNombre)) // se
	 * Transforma a Mono<ResponseEntity<Usuario>>
	 * .defaultIfEmpty(ResponseEntity.notFound().build()); //build: Cree la entidad
	 * de respuesta sin cuerpo. }
	 */
	
	// @Valid Para hacer cumplir las validaciones propuestas en el modelo
	
	@PostMapping
	public Mono<ResponseEntity<Cliente>> registrar(@Valid  @RequestParam("token") String token, @RequestBody Cliente u, final ServerHttpRequest req){
		return service.registrar(token, u)
				.map(newUsuario -> ResponseEntity
						.created(URI.create(req.getURI().toString().concat("/").concat(newUsuario.getIdentificacion()))) // se obtiene url dinamica del recurso
						.contentType(MediaType.APPLICATION_JSON)
						.body(newUsuario));
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Cliente>> modificar(@Valid @PathVariable("id") String id, @RequestBody Cliente u){
		
		Mono<Cliente> UsuarioBD = service.listarPorId(id);
		Mono<Cliente> UsuarioBody = Mono.just(u);
		
		return UsuarioBD.zipWith(UsuarioBody, (uBD, uBody) -> {
			uBD.setIdentificacion(uBody.getIdentificacion() != null ? uBody.getIdentificacion(): uBD.getIdentificacion());
			uBD.setNombres(uBody.getNombres() != null ? uBody.getNombres(): uBD.getNombres());
			uBD.setTelefono(uBody.getTelefono() != null ? uBody.getTelefono(): uBD.getTelefono());
			uBD.setPeso(uBody.getPeso() != null ? uBody.getPeso(): uBD.getPeso());
			uBD.setAltura(uBody.getAltura() != null ? uBody.getAltura(): uBD.getAltura());
			uBD.setSexo(uBody.getSexo() != null ? uBody.getSexo(): uBD.getSexo());
			uBD.setSubscripcion(uBody.getSubscripcion() != null ? uBody.getSubscripcion(): uBD.getSubscripcion());
			uBD.setActivo(uBody.getActivo() != null ? uBody.getActivo(): uBD.getActivo());
			
			return uBD;
		})
		.flatMap(service::modificar) // usuarioModificado -> service.modificar(usuarioModificado) devuelve Mono<Usuario>
		.map(uModificado -> ResponseEntity
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(uModificado))
		.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminar(@PathVariable String id){
		return service.listarPorId(id) // Mono<Usuario>
				.flatMap(usuarioAEliminar -> {
					return service.eliminar(usuarioAEliminar.getIdentificacion())// devuelve Mono<Void>
							.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)); // <Mono<ResponseEntity<Void>>
				})
				.defaultIfEmpty(new ResponseEntity<Void>(HttpStatus.NOT_FOUND));
	}
}

