package com.todoteg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoteg.model.Subscripcion;
import com.todoteg.repo.IGenericRepo;
import com.todoteg.repo.ISubscripcionRepo;
import com.todoteg.service.ISubscripcionService;

@Service
public class SubscripcionServiceImpl extends CRUDImpl<Subscripcion, String> implements ISubscripcionService{

	@Autowired
	private ISubscripcionRepo repo;
	
	@Override
	protected IGenericRepo<Subscripcion, String> getRepo() {
		return repo;
	}

}
