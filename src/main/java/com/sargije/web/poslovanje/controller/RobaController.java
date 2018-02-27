package com.sargije.web.poslovanje.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sargije.web.poslovanje.model.Magacin;
import com.sargije.web.poslovanje.model.Roba;
import com.sargije.web.poslovanje.model.RobaCenovnik;
import com.sargije.web.poslovanje.model.VrstaRobe;
import com.sargije.web.poslovanje.repository.*;

@Controller
public class RobaController {

	@Autowired
	ValutaRepository valutaRepository;
	
	@Autowired
	VrstaRobeRepository vrstaRobeRepository;
	
	@Autowired
	RobaRepository robaRepository;
	
	@Autowired
	RobaCenovnikRepository robaCenovnikRepository;
	
	@Autowired
	MagacinRepository magacinRepository;

	 
	 @GetMapping("/vrsta_robe")
	 public String getVrstaRobe(Model model) {
		 model.addAttribute("forms", "vrsta_robe");
		 model.addAttribute("table", "vrsta_robe");
		 model.addAttribute("vrsteArtikla", vrstaRobeRepository.findAll());
		 model.addAttribute("vrstaRobe", new VrstaRobe());
	     return "default";
	     
	 }
	 
	 @PostMapping("/vrsta_robe")
	 public String postVrstaRobe(@Valid VrstaRobe vrstaRobe, BindingResult bindingResult) {
		 if (!bindingResult.hasErrors()) {
			 VrstaRobe vrRobe = new VrstaRobe();
			 vrRobe.setVrstaRobeNaziv(vrstaRobe.getVrstaRobeNaziv());
			 vrstaRobeRepository.save(vrRobe);
		 }
		 return "redirect:vrsta_robe";
	 }
	 
	 @GetMapping("/roba")
	 public String getRoba(Model model) {
		 model.addAttribute("table", "roba");
		 model.addAttribute("forms", "roba");
		 model.addAttribute("vrstaRobe", vrstaRobeRepository.findAll());
		 model.addAttribute("robe", robaRepository.findAll());
		 model.addAttribute("roba", new Roba());
	     return "default";
	     
	 }
	 /*
	 @GetMapping("/roba/nova")
	 public String getNewRoba(Model model) {
		 model.addAttribute("forms", "roba");
		 model.addAttribute("vrstaRobe", vrstaRobeRepository.findAll());
		 model.addAttribute("roba", new Roba());
	     return "default";
	     
	 }	*/ 
	 @GetMapping("/roba/{id}")
	 public String getCertainRoba(Model model, @PathVariable(value="id") Long id) {
		 model.addAttribute("forms", "roba");
		 model.addAttribute("vrstaRobe", vrstaRobeRepository.findAll());
		 model.addAttribute("roba", robaRepository.findOne(id));
	     return "default";
	     
	 }	
	 @PostMapping("/roba")
	 public String postRoba(@Valid Roba roba, BindingResult bindingResult) {
		 if (!bindingResult.hasErrors()) {
			 Roba ro = new Roba();
		 
			 if(roba.getRobaId() != null) {
				 ro.setRobaId(roba.getRobaId());
			 }
			 ro.setRobaNaziv(roba.getRobaNaziv());
			 ro.setVrstaRobe(roba.getVrstaRobe());
	
			 robaRepository.save(ro);
		 }
		 return "redirect:roba";
	 }
	 
	 @GetMapping("/cenovnik")
	 public String getCenovnik(Model model) {
		 model.addAttribute("forms", "cenovnik");
		 model.addAttribute("table", "cenovnik");
		 model.addAttribute("valuta", valutaRepository.findAll());
		 model.addAttribute("roba", robaRepository.findAll());
		 model.addAttribute("robaCenovnik", new RobaCenovnik());
		 model.addAttribute("cene", robaCenovnikRepository.findByAktivna(true));
		 
//		 Iterable<RobaCenovnik> testr = robaCenovnikRepository.findAll();
	     return "default";
	     
	 }
	 
	 @PostMapping("/cenovnik")
	 public String postCenovnik(@Valid RobaCenovnik robaCenovnik, BindingResult bindingResult) {
		 if (!bindingResult.hasErrors()) {
		 
			 RobaCenovnik postojiCena = robaCenovnikRepository.findByRobaAndValutaAndAktivna(robaCenovnik.getRoba(), robaCenovnik.getValuta(), true);
			 if(postojiCena != null) {			 
				// ro.setCenovnikId(postojiCena.getCenovnikId());
				 postojiCena.setAktivna(false);
				 robaCenovnikRepository.save(postojiCena);
			 }
			 
			 RobaCenovnik novaRobaCenovnik = new RobaCenovnik();
			 novaRobaCenovnik.setAktivna(true);
			 novaRobaCenovnik.setValuta(robaCenovnik.getValuta());
			 novaRobaCenovnik.setRoba(robaCenovnik.getRoba());
			 novaRobaCenovnik.setCena(robaCenovnik.getCena());
			 robaCenovnikRepository.save(novaRobaCenovnik);
			 
		 }
		 return "redirect:cenovnik";
	 }
	 
	 @GetMapping("/magacin")
	 public String getMagacin(Model model) {
		 model.addAttribute("forms", "magacin");
		 model.addAttribute("roba", robaRepository.findAll());
		 model.addAttribute("magacin", new Magacin());
	     return "default";     
	 }
	 
	 @PostMapping("/magacin")
	 public String postMagacin(@ModelAttribute Magacin magacin) {
		 Magacin mag = new Magacin();
		 
		 Magacin postojiMagacin = magacinRepository.findByRoba(magacin.getRoba());
		 if (postojiMagacin == null) {			 
			 mag.setRoba(magacin.getRoba());
			 mag.setKolicina(magacin.getKolicina());
		 }else {
			 mag.setKolicina(postojiMagacin.getKolicina() + magacin.getKolicina());
			 mag.setRoba(postojiMagacin.getRoba());
			 mag.setMagacinId(postojiMagacin.getMagacinId());
		 }
		 magacinRepository.save(mag);
		 return "redirect:magacin";
	 }
}
