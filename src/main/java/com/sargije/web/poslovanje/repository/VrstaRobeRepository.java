package com.sargije.web.poslovanje.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.VrstaRobe;

@Transactional
public interface VrstaRobeRepository extends CrudRepository<VrstaRobe, Long>{	
}
