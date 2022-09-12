package com.todoteg.controller;

import java.net.URI;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.todoteg.model.Usuario;
import com.todoteg.service.IHuellaTempService;
import com.todoteg.service.IUsuarioService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("registrar")
public class UsuarioController {
	
	@Autowired
	private IUsuarioService service;
	
	@Autowired
	private IHuellaTempService HuellaTempService;
	
	
	@GetMapping("/{username}")
	public Mono<ResponseEntity<Usuario>> obtenerUsuario(@PathVariable("username") String username){
		return service.buscarUsuario(username)
				.map(usuario -> ResponseEntity
										.ok()
										.contentType(MediaType.APPLICATION_JSON)
										.body(usuario)
										)
				.defaultIfEmpty(ResponseEntity.notFound().build());
				
	}
	
	@PostMapping
	public Mono<ResponseEntity<Usuario>> registarUsuario(@RequestBody Usuario u, final ServerHttpRequest req){
		return service.registrarHash(u)
				.map(NewUsuario -> ResponseEntity
						.created(URI.create(req.getURI().toString().concat("/").concat(NewUsuario.getId())))
						.contentType(MediaType.APPLICATION_JSON)
						.body(NewUsuario));
	}
	
	@PutMapping("/{username}")
	public Mono<ResponseEntity<Usuario>> modificarUsuario(@PathVariable("username") String username, @RequestParam(name="serial", defaultValue = "") String serial, @RequestBody Usuario u){
		Mono<Usuario> usuarioBD = service.buscarUsuario(username);
		Mono<Usuario> usuarioBody = Mono.just(u);
		return HuellaTempService.ObtenerHuellaTempPorSerial(serial)
				.flatMap(hTemporal -> {
					return usuarioBD.zipWith(usuarioBody, (uBD, uBDY)-> {
						uBD.setIdentificacion(uBDY.getIdentificacion() != null? uBDY.getIdentificacion(): uBD.getIdentificacion());
						uBD.setNombres(uBDY.getNombres() != null? uBDY.getNombres(): uBD.getNombres());
						uBD.setImgHuella(hTemporal.getImgHuella());
						uBD.setHuella(hTemporal.getHuella());
						uBD.setFirma(uBDY.getFirma() != null? uBDY.getFirma(): uBD.getFirma());
						System.out.println(uBDY.getClave());
						uBD.setUsuario(uBDY.getUsuario() != null? uBDY.getUsuario(): uBD.getUsuario());
						uBD.setEstado(uBDY.getEstado() != null? uBDY.getEstado(): uBD.getEstado());
						uBD.setClave(uBDY.getClave() != null? uBDY.getClave(): uBD.getClave());
						
						return uBD;
					})
					.flatMap(service::registrarHash)
					.map(UsuarioModificado -> ResponseEntity
									.ok()
									.contentType(MediaType.APPLICATION_JSON)
									.body(UsuarioModificado))
					.defaultIfEmpty(ResponseEntity.notFound().build());
				})
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> eliminarUsuario(@PathVariable("id") String id){
		return service.listarPorId(id)
				.flatMap(UEliminar -> {
					return service.eliminar(UEliminar.getId())
							.thenReturn(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)); // <Mono<ResponseEntity<Void>>
				})
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
