package com.todoteg.service;

import org.springframework.data.domain.Pageable;

import com.todoteg.model.Cliente;
import com.todoteg.model.ClienteInfoUtil;
import com.todoteg.pagination.PageSupport;

import reactor.core.publisher.Mono;


public interface IClienteService extends ICRUD<Cliente, String> {
	//Mono<User> ObtenerUsuarioPorNombre(String usuario);
	Mono<ClienteInfoUtil> buscarPorId(String id);
	Mono<Cliente> registrar(String serial, Cliente cliente);
	Mono<PageSupport<ClienteInfoUtil>> ListarPagina(String busquedaPorNombre, Pageable pagina);
}
