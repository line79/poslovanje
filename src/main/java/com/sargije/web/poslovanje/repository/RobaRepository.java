package com.sargije.web.poslovanje.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.Roba;

@Transactional
public interface RobaRepository extends CrudRepository<Roba, Long>{	
}
