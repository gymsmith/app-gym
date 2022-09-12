package com.todoteg.service.impl;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.todoteg.model.Seguimiento;
import com.todoteg.pagination.PageSupport;
import com.todoteg.repo.IGenericRepo;
import com.todoteg.repo.ISeguimientoRepo;
import com.todoteg.service.ISeguimientoService;

import reactor.core.publisher.Mono;

@Service
public class SeguimientoServiceImpl extends CRUDImpl<Seguimiento, String> implements ISeguimientoService{

	@Autowired
	private ISeguimientoRepo repo;
	
	@Override
	protected IGenericRepo<Seguimiento, String> getRepo() {
		return repo;
	}

	@Override
	public Mono<PageSupport<Seguimiento>> buscarPorUsuario(String usuarioId, Pageable pagina ) {
		// se crea un mapa con el id del usuario para que coincida la busqueda con la estructura guardado en mongo
		HashMap<String,String> usuario = new HashMap<String, String>(); 
		usuario.put("_id", usuarioId);
		return repo.allByUsuario(usuario) // devuelve la cuenta del total de documentos utilizando como criterio el usuario
				.flatMap(total -> {
					return repo.findByUsuario(usuario, pagina)
							.collectList()
							.map(listaSeguimiento -> new PageSupport<>(listaSeguimiento, pagina.getPageNumber(),pagina.getPageSize(), total));
				});
		
	}

}
