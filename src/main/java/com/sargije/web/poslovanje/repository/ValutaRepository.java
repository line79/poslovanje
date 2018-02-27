package com.sargije.web.poslovanje.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.Valuta;

@Transactional
public interface ValutaRepository extends CrudRepository<Valuta, Long>{	
}
