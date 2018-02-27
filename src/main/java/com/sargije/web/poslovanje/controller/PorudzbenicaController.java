package com.sargije.web.poslovanje.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sargije.web.poslovanje.model.Korisnik;
import com.sargije.web.poslovanje.model.Porudzbenica;
import com.sargije.web.poslovanje.model.PorudzbenicaStavke;
import com.sargije.web.poslovanje.model.PorudzbenicaStavkeId;
import com.sargije.web.poslovanje.model.RobaCenovnik;
import com.sargije.web.poslovanje.repository.KorisnikRepository;
import com.sargije.web.poslovanje.repository.MagacinRepository;
import com.sargije.web.poslovanje.repository.PorudzbenicaRepository;
import com.sargije.web.poslovanje.repository.PorudzbenicaStavkeRepository;
import com.sargije.web.poslovanje.repository.RobaCenovnikRepository;
import com.sargije.web.poslovanje.repository.RobaRepository;
import com.sargije.web.poslovanje.views.PdfView;

@Controller
public class PorudzbenicaController {
	
	@Autowired
	KorisnikRepository korisnikRepository;
	
	@Autowired
	PorudzbenicaRepository porudzbenicaRepository;
	
	@Autowired
	PorudzbenicaStavkeRepository porudzbenicaStavkeRepository;

	@Autowired
	RobaRepository robaRepository;
	
	@Autowired
	MagacinRepository magacinRepository;
	
	@Autowired
	RobaCenovnikRepository robaCenovnikRepository;

/*	@GetMapping("/porudzbenica/{korisnikId}")
	public String getCertainPorudzbenica(Model model, @PathVariable(value="korisnikId") Long korisnikId) {
		 Porudzbenica poruzbenica =  new Porudzbenica();
		 poruzbenica.setKorisnik(korisnikRepository.findOne(korisnikId));
		 poruzbenica.setOtvorena(true);
		 poruzbenica.setPlacena(false);
		 poruzbenica.setDatumPorudzbenice(new Date());
		 porudzbenicaRepository.save(poruzbenica);
		 return "redirect:/porudzbenice/" + korisnikId;  
	 }
*/
	@PostMapping("/porudzbenica")
	public String getCertainPorudzbenica(@ModelAttribute Porudzbenica por) {
		 Porudzbenica poruzbenica =  new Porudzbenica();
		 poruzbenica.setKorisnik(korisnikRepository.findOne(por.getKorisnik().getKorisnikId()));
		 poruzbenica.setOtvorena(true);
		 poruzbenica.setPlacena(false);
		 poruzbenica.setPopust(por.getPopust());
		 poruzbenica.setDatumPorudzbenice(por.getDatumPorudzbenice());
		 porudzbenicaRepository.save(poruzbenica);
		 return "redirect:/porudzbenice/" + por.getKorisnik().getKorisnikId();  
	 }
	
	 @GetMapping("/porudzbenice/{korisnikId}")
	 public String getPorudzbenica(Model model, @PathVariable(value="korisnikId") Long korisnikId) {
		 model.addAttribute("table", "porudzbenice");
		 model.addAttribute("forms", "porudzbenice");
		 
		 Korisnik korisnik = korisnikRepository.findOne(korisnikId);
		 model.addAttribute("korisnik", korisnik);
		 model.addAttribute("porudzbenica", new Porudzbenica());
		 model.addAttribute("porudzbenice", porudzbenicaRepository.findByKorisnik(korisnik));
	     return "default";
	 }
/*
	 @PostMapping("/zatvori_porudzbenicu")
	 public String postZatvoriPorudzbenicu(@ModelAttribute Porudzbenica por) {
		 Porudzbenica poruzbenica =  porudzbenicaRepository.findOne(por.getPorudzbenicaId());
		// Korisnik korisnik = poruzbenica.getKorisnik();
		 BigDecimal cena = new BigDecimal(0);
		 for(PorudzbenicaStavke stavka : poruzbenica.getPorudzbenicaStavkes()) {
			 RobaCenovnik robaCenovnik =robaCenovnikRepository.findByRobaAndValuta(stavka.getRoba(), poruzbenica.getKorisnik().getValuta());
			 cena = cena.add(robaCenovnik.getCena().multiply(new BigDecimal(stavka.getKolicina())));
		 }
		 poruzbenica.setOtvorena(false);
		 poruzbenica.setCena(cena);
		 porudzbenicaRepository.save(poruzbenica);
		 return "redirect:/porudzbenice/" + poruzbenica.getKorisnik().getKorisnikId();
	 }
*/	 
	 @GetMapping("/zatvori_porudzbenicu/{id}")
	 public String postZatvoriPorudzbenicu(Model model, @PathVariable(value="id") Long id) {
		 Porudzbenica poruzbenica =  porudzbenicaRepository.findOne(id);
		// Korisnik korisnik = poruzbenica.getKorisnik();
		 BigDecimal cena = new BigDecimal(0);
		 for(PorudzbenicaStavke stavka : poruzbenica.getPorudzbenicaStavkes()) {
			 RobaCenovnik robaCenovnik =stavka.getRobaCenovnik();//robaCenovnikRepository//.findByRobaAndValuta(stavka.getRoba(), poruzbenica.getKorisnik().getValuta());
			 cena = cena.add(robaCenovnik.getCena().multiply(new BigDecimal(stavka.getKolicina())));
		 }
		/* if(!poruzbenica.getPopust().equals(0) || poruzbenica.getPopust() != null) {
			 cena = cena.subtract(cena.multiply(poruzbenica.getPopust()).divide(new BigDecimal(100)));
		 }*/
		 poruzbenica.setOtvorena(false);
		 poruzbenica.setCena(cena);
		 poruzbenica.setDatumZatvaranja(new Date());
		 porudzbenicaRepository.save(poruzbenica);
		 return "redirect:/porudzbenice/" + poruzbenica.getKorisnik().getKorisnikId();
	 }
	 
	 @GetMapping("/porudzbenica_stavke/{id}")
	 public String getPorudzbenicaStavke(Model model, @PathVariable(value="id") Long id) {
		 
		 model.addAttribute("table", "porudzbenica_stavke");
		 model.addAttribute("porudzbenica_stavke", new PorudzbenicaStavke());
		 Porudzbenica porudzbenica = porudzbenicaRepository.findOne(id);
		 model.addAttribute("porudzbenica", porudzbenica);
		 model.addAttribute("porudzbenicaId", id);
		 //model.addAttribute("roba", robaRepository.findAll());
		 model.addAttribute("roba", robaCenovnikRepository.findByValutaAndAktivna(porudzbenica.getKorisnik().getValuta(), true));
		 if(porudzbenica.getOtvorena() == true) {
			 model.addAttribute("forms", "porudzbenica_stavke");
		 }
		 model.addAttribute("stavka", porudzbenicaStavkeRepository.countByPorudzbenica(porudzbenica));
	     return "default";
	     
	 }
	 @PostMapping("/porudzbenica_stavke")
	 public String postPorudzbenicaStavka(@ModelAttribute PorudzbenicaStavke porudzbenicaStavke, Model model) {
		 
		Long porudzbenicaStavkaId = porudzbenicaStavkeRepository.countByPorudzbenica(porudzbenicaStavke.getPorudzbenica());
		
	//	Magacin postojiMagacin = magacinRepository.findByRoba(porudzbenicaStavke.getRoba());
		
		if(porudzbenicaStavke.getKolicina() != null) {
		//	if(postojiMagacin.getKolicina() - porudzbenicaStavke.getKolicina() >= 0) {
		//		System.out.println(postojiMagacin.getKolicina());
				model.addAttribute("successful", false);
				
				//skini sa stanja magacina
		//		postojiMagacin.setKolicina(postojiMagacin.getKolicina() - porudzbenicaStavke.getKolicina());
		//		magacinRepository.save(postojiMagacin);
				
				//kreiraj porudžbenicu
				PorudzbenicaStavke por = new PorudzbenicaStavke();
				PorudzbenicaStavkeId porId = new PorudzbenicaStavkeId();
				porId.setPorudzbenicaId(porudzbenicaStavke.getPorudzbenica().getPorudzbenicaId().intValue());
				
				
				porId.setPorudzbenicaStavkaId(porudzbenicaStavkaId.intValue() + 1);
				
				
				por.setId(porId);
				
				//por.setRoba(porudzbenicaStavke.getRoba());
				por.setRobaCenovnik(porudzbenicaStavke.getRobaCenovnik());
				por.setPorudzbenica(porudzbenicaStavke.getPorudzbenica());
				por.setKolicina(porudzbenicaStavke.getKolicina());
				
				porudzbenicaStavkeRepository.save(por);
				model.addAttribute("successful", true);
				
		//	}
		}
		return "redirect:/porudzbenica_stavke/" + porudzbenicaStavke.getPorudzbenica().getPorudzbenicaId();
	 }	
		
	@GetMapping(value="/stampaj_porudzbenicu/{id}")
    public ModelAndView getPdf(@PathVariable(value="id") Long id, HttpServletRequest request, HttpServletResponse response) throws SQLException{
        Map<String, Object> model = new HashMap<String, Object>();
        
        model.put("porudzbenica", porudzbenicaRepository.findOne(id));
        response.setContentType( "application/pdf" );
        response.setHeader( "Content-disposition", "inline");//"attachment; filename=myfile.pdf" );         
        return new ModelAndView(new PdfView(), model);
    }
	
	 @GetMapping("/brisi_poruzbenicu/{id}")
	 public String getBrisiPorudzbenicu(Model model, @PathVariable(value="id") Long id) {
		 

		 Porudzbenica porudzbenica = porudzbenicaRepository.findOne(id);
		 if(porudzbenica.getOtvorena().equals(true)) {
			 porudzbenicaRepository.delete(id);
		 }
		 return "redirect:/porudzbenice/" + porudzbenica.getKorisnik().getKorisnikId();
	     
	 }
	 
	 @GetMapping("/brisi_stavku/{porudzbenicaId}/{porudzbenicaStavkaId}")
	 public String getBrisiStavkuPorudzbenica(Model model, @PathVariable(value="porudzbenicaId") Long porudzbenicaId,
			 @PathVariable(value="porudzbenicaStavkaId") Long porudzbenicaStavkaId) {
		 model.addAttribute("table", "porudzbenice");
		 Porudzbenica porudzbenica = porudzbenicaRepository.findOne(porudzbenicaId);
		 if(porudzbenica.getOtvorena().equals(true)) {
			 PorudzbenicaStavke porudzbenicaStavke = porudzbenicaStavkeRepository.findOne(new PorudzbenicaStavkeId(porudzbenicaId.intValue(), porudzbenicaStavkaId.intValue()));
			 porudzbenicaStavkeRepository.delete(porudzbenicaStavke);
		 }
		
		 return "redirect:/porudzbenica_stavke/" + porudzbenicaId;
	 }
}
