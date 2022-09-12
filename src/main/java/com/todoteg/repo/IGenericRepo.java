package com.todoteg.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

//con heredar de mongoRepository voy a tener a disposicion metodos encargado de hacer el CRUD
//Todos devuelve operadores reactivos ya que extendemos del driver reactivo

@NoRepositoryBean // se evita la creacion de una instancia, ya que spring desconoce como crear una instancia de un operador generico
public interface IGenericRepo<T,ID> extends ReactiveMongoRepository<T,ID>{

}
