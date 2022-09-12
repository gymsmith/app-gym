package com.todoteg.repo;


import com.todoteg.model.Usuario;

import reactor.core.publisher.Mono;


public interface IUsuarioRepo extends IGenericRepo<Usuario, String>{

	//Necesario para la seccion de seguridad
	
	//SELECT * FROM USUARIO U WHERE U.USUARIO = ? 
	//{usuario : ?}
	//DerivedQueries
	Mono<Usuario> findOneByUsuario(String usuario);	

}
