package com.sargije.web.poslovanje.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.Korisnik;
import com.sargije.web.poslovanje.model.Porudzbenica;

@Transactional
public interface PorudzbenicaRepository extends CrudRepository<Porudzbenica, Long>{
	List<Porudzbenica> findByKorisnik(Korisnik korisnik);
}
