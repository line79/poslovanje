package com.sargije.web.poslovanje.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.Magacin;
import com.sargije.web.poslovanje.model.Roba;

@Transactional
public interface MagacinRepository extends CrudRepository<Magacin, Long>{
	Magacin findByRoba(Roba roba);
}
