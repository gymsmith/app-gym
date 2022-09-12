package com.todoteg.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todoteg.model.Plan;
import com.todoteg.repo.IGenericRepo;
import com.todoteg.repo.IPlanRepo;
import com.todoteg.service.IPlanService;

@Service
public class PlanServiceImpl extends CRUDImpl<Plan, String> implements IPlanService{

	@Autowired
	private IPlanRepo repo;
	
	@Override
	protected IGenericRepo<Plan, String> getRepo() {
		return repo;
	}

}
