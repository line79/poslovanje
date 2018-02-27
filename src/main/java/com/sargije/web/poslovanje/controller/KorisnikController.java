package com.sargije.web.poslovanje.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.sargije.web.poslovanje.model.Korisnik;
import com.sargije.web.poslovanje.model.Placanja;
import com.sargije.web.poslovanje.model.Porudzbenica;
import com.sargije.web.poslovanje.model.Valuta;
import com.sargije.web.poslovanje.repository.KorisnikRepository;
import com.sargije.web.poslovanje.repository.PlacanjaRepository;
import com.sargije.web.poslovanje.repository.ValutaRepository;
import com.sargije.web.poslovanje.repository.VrstaRobeRepository;

@Controller
public class KorisnikController{
    
	@Autowired
	KorisnikRepository korisnikRepository;
	
	@Autowired
	ValutaRepository valutaRepository;
	
	@Autowired
	PlacanjaRepository placanjaRepository;
	
	@Autowired
	VrstaRobeRepository vrstaRobeRepository;
	
	 @GetMapping("/valuta")
	 public String greeting(Model model) {
		 model.addAttribute("forms", "valuta");
		 model.addAttribute("table", "valuta");
		 model.addAttribute("valute",valutaRepository.findAll());
		 model.addAttribute("valuta", new Valuta());
	     return "default";
	     
	 }
	 
	 @PostMapping("/valuta")
	 public String postValuta(@ModelAttribute Valuta valuta) {		 
		 Valuta val = new Valuta();
		 val.setValutaNaziv(valuta.getValutaNaziv());
		 val.setValutaOznaka(valuta.getValutaOznaka());		 
		 valutaRepository.save(val);
		 return "redirect:valuta";
	 }
	 
	 @GetMapping("/korisnik")
	 public String getKorisnik(Model model) {
		 model.addAttribute("table", "korisnici");
		 model.addAttribute("korisnici",korisnikRepository.findAll());
	     return "default";
	     
	 }
	 
	 @GetMapping("/korisnik/{id}")
	 public String getCertainKorisnik(Model model, @PathVariable(value="id") Long id) {
		 model.addAttribute("forms", "korisnik");
		 model.addAttribute("valuta", valutaRepository.findAll());
		 model.addAttribute("korisnik",korisnikRepository.findOne(id));
	     return "default";
	     
	 }
	 @GetMapping("/korisnik/novi")
	 public String getNewKorisnik(Model model) {
		 model.addAttribute("forms", "korisnik");
		 model.addAttribute("valuta", valutaRepository.findAll());
		 model.addAttribute("korisnik", new Korisnik());
	     return "default";
	 }
	 @PostMapping("/korisnik")
	 public String postKorisnik(@Valid Korisnik korisnik, BindingResult bindingResult) {
		 if (!bindingResult.hasErrors()) {
			 Korisnik kor = new Korisnik();
			 
			 if (korisnik.getKorisnikId() != null) {				 
				 kor.setKorisnikId(korisnik.getKorisnikId());			 
			 }
			 
			 kor.setKorisnikNaziv(korisnik.getKorisnikNaziv());
			 kor.setValuta(korisnik.getValuta());
			 kor.setKorisnikAdresa(korisnik.getKorisnikAdresa());
			 kor.setPocetnoDugovanje(korisnik.getPocetnoDugovanje());
			 korisnikRepository.save(kor);
			 return "redirect:korisnik";
	      }else {
	    	  return "redirect:korisnik/novi";
	      }

	 }	 
	 
	 @GetMapping("/placanja/{id}")
	 public String getUplate(Model model, @PathVariable(value="id") Long id) {

		 Korisnik korisnik = korisnikRepository.findOne(id);
		 BigDecimal ukupnoPlaceno = new BigDecimal(0);
		 BigDecimal ukupnoPoruceno = new BigDecimal(0);
		 if(korisnik.getPocetnoDugovanje() != null) ukupnoPoruceno = korisnik.getPocetnoDugovanje();
		 for(Placanja placanje: korisnik.getPlacanjas()) {
			 ukupnoPlaceno = ukupnoPlaceno.add(placanje.getIznos());
		 }
		 for(Porudzbenica porudzbenica : korisnik.getPorudzbenicas()) {
			 if(porudzbenica.getOtvorena().equals(false)) {
				 BigDecimal cena = new BigDecimal(0);
				 if(porudzbenica.getPopust() != null) {
					cena = porudzbenica.getCena().subtract(porudzbenica.getCena().multiply(porudzbenica.getPopust()).divide(new BigDecimal(100)));
				 }else {
					cena = porudzbenica.getCena();
				 }
				 ukupnoPoruceno = ukupnoPoruceno.add(cena);
			 }
		 }

		 model.addAttribute("korisnik",korisnik);
		 model.addAttribute("forms", "uplata");
		 model.addAttribute("table", "uplata");
		 model.addAttribute("ukupnoPoruceno", ukupnoPoruceno);
		 model.addAttribute("ukupnoPlaceno", ukupnoPlaceno);
		 model.addAttribute("uplata", new Placanja());
		 
	     return "default";   
	 }
	 
	 @PostMapping("/placanje")
	 public String postPlacanje(@ModelAttribute Placanja placanja) {
		 
		 //placanja.setDatumPlacanja(new Date());
		 placanja.setPotvrdjena(false);
		 placanjaRepository.save(placanja);
		 return "redirect:placanja/" + placanja.getKorisnik().getKorisnikId();
	 }
	 
	 @GetMapping("/potvrdi_placanje/{id}")
	 public String postPlacanje(Model model, @PathVariable(value="id") Long id) {
		 
		 Placanja placanje = placanjaRepository.findOne(id);
		 placanje.setPotvrdjena(true);
		 placanjaRepository.save(placanje);
		 return "redirect:/placanja/" + placanje.getKorisnik().getKorisnikId();
	 }
	 
	 @GetMapping("/izbrisi_placanje/{id}")
	 public String postIzbrisiPlacanje(Model model, @PathVariable(value="id") Long id) {
		 
		 Placanja placanje = placanjaRepository.findOne(id);
		 placanjaRepository.delete(id);
		 return "redirect:/placanja/" + placanje.getKorisnik().getKorisnikId();
	 }
}
