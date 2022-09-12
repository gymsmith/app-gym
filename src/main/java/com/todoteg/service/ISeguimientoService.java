package com.todoteg.service;

import org.springframework.data.domain.Pageable;

import com.todoteg.model.Seguimiento;
import com.todoteg.pagination.PageSupport;

import reactor.core.publisher.Mono;

public interface ISeguimientoService extends ICRUD<Seguimiento, String> {
	Mono<PageSupport<Seguimiento>> buscarPorUsuario (String usuarioId, Pageable paginacion);
}

