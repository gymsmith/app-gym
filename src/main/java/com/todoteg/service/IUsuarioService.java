package com.todoteg.service;

import com.todoteg.model.Usuario;
import com.todoteg.security.User;

import reactor.core.publisher.Mono;

public interface IUsuarioService extends ICRUD<Usuario, String>{
	Mono<Usuario> buscarUsuario(String usuario);
	Mono<Usuario> registrarHash(Usuario usuario);
	Mono<User> buscarPorUsuario(String usuario);
	
}
