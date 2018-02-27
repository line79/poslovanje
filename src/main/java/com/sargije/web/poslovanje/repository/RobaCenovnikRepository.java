package com.sargije.web.poslovanje.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.sargije.web.poslovanje.model.Roba;
import com.sargije.web.poslovanje.model.RobaCenovnik;
import com.sargije.web.poslovanje.model.Valuta;

@Transactional
public interface RobaCenovnikRepository extends CrudRepository<RobaCenovnik, Long>{

	RobaCenovnik findByRoba(Roba roba);

	RobaCenovnik findByRobaAndValuta(Roba roba, Valuta valuta);	
	
	List<RobaCenovnik> findByAktivna(Boolean aktivna);

	RobaCenovnik findByRobaAndValutaAndAktivna(Roba roba, Valuta valuta, Boolean b);

	List<RobaCenovnik>  findByValutaAndAktivna(Valuta valuta, Boolean b);
}
