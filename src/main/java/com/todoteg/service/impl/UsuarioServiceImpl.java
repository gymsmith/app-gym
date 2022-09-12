package com.todoteg.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.todoteg.model.Usuario;
import com.todoteg.repo.IGenericRepo;
import com.todoteg.repo.IRolRepo;
import com.todoteg.repo.IUsuarioRepo;
import com.todoteg.security.User;
import com.todoteg.service.IUsuarioService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class UsuarioServiceImpl extends CRUDImpl<Usuario, String> implements IUsuarioService {
	private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
	@Autowired
	private IUsuarioRepo repo;

	@Autowired
	private IRolRepo rolRepo;
	
	@Autowired
	private BCryptPasswordEncoder bcrypt;
	
	@Override
	protected IGenericRepo<Usuario, String> getRepo() {
		return repo;
	}

	@Override
	public Mono<Usuario> registrarHash(Usuario usuario) {
		Matcher matcher = this.BCRYPT_PATTERN.matcher(usuario.getClave()); // compara la entrada dada con el patron establecido
		
		// Verdadero si toda la secuencia coincide con el patron de dicho comparador
		if(!matcher.matches()) {			
			usuario.setClave(bcrypt.encode(usuario.getClave()));
		}
		return repo.save(usuario);		
	}

	@Override
	public Mono<User> buscarPorUsuario(String usuario) {
		Mono<Usuario> monoUsuario = repo.findOneByUsuario(usuario); // trae todos los datos del usuario por su Atributo usuario
		
		List<String> roles = new ArrayList<String>(); // se intancia una lista vacia para listar los roles del usuario luego
		
		return monoUsuario.flatMap(u -> {
			return Flux.fromIterable(u.getRoles())  // se crea un flux con cada id-rol del usuario como item 
					.flatMap(rol -> {				
						return rolRepo.findById(rol.getId()) // en este punto se consulta el id de cada rol para obtener sus datos completos
								.map(r -> {
									roles.add(r.getNombre());// se añade el nombre del  rol a la lista de roles antes instanciada
									return r;
								});
					}).collectList().flatMap(list -> { // se compacta en un Mono<List>
						u.setRoles(list); // Se Remplaza el bloque de roles en el objecto usuario por la nueva lista con los datos completos
						return Mono.just(u);
					});
		})	
		.flatMap(u -> {			
			// se cumple con el requerimiento del metodo; y se crea una instancia de user requerido por spring security
			return Mono.just(new User(u.getId(), u.getNombres(), u.getUsuario(), u.getClave(), u.getEstado(), roles)); 
		});
	}

	@Override
	public Mono<Usuario> buscarUsuario(String usuario) {
		return repo.findOneByUsuario(usuario);
	}


}
