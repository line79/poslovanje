package com.sargije.web.poslovanje.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.Porudzbenica;
import com.sargije.web.poslovanje.model.PorudzbenicaStavke;
import com.sargije.web.poslovanje.model.PorudzbenicaStavkeId;

@Transactional
public interface PorudzbenicaStavkeRepository extends CrudRepository<PorudzbenicaStavke, PorudzbenicaStavkeId>{
	Long countByPorudzbenica(Porudzbenica porudzbenica);
	
}
