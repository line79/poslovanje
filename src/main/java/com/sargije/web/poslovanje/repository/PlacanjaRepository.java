package com.sargije.web.poslovanje.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.Placanja;

@Transactional
public interface PlacanjaRepository extends CrudRepository<Placanja, Long>{

}
