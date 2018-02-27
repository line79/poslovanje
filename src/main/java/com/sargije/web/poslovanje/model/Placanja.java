package com.sargije.web.poslovanje.model;
// Generated May 26, 2017 2:47:21 PM by Hibernate Tools 5.2.3.Final

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Placanja generated by hbm2java
 */
@Entity
@Table(name = "placanja",  schema = "PUBLIC", catalog = "POSLOVANJE")
public class Placanja implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3146484824474521952L;
	private Long placanjaId;
	private Date datumPlacanja;
	private BigDecimal iznos;
	private Korisnik korisnik;
	private Boolean potvrdjena;

	public Placanja() {
	}

	public Placanja(Date datumPlacanja, BigDecimal iznos, Korisnik korisnik, Boolean potvrdjena) {
		this.datumPlacanja = datumPlacanja;
		this.iznos = iznos;
		this.korisnik = korisnik;
		this.potvrdjena = potvrdjena;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "PLACANJA_ID", unique = true, nullable = false)
	public Long getPlacanjaId() {
		return this.placanjaId;
	}

	public void setPlacanjaId(Long placanjaId) {
		this.placanjaId = placanjaId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DATUM_PLACANJA", length = 10)
	public Date getDatumPlacanja() {
		return this.datumPlacanja;
	}

	public void setDatumPlacanja(Date datumPlacanja) {
		this.datumPlacanja = datumPlacanja;
	}

	@Column(name = "IZNOS", precision = 15, scale = 2)
	public BigDecimal getIznos() {
		return this.iznos;
	}

	public void setIznos(BigDecimal iznos) {
		this.iznos = iznos;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "KORISNIK_ID")
	public Korisnik getKorisnik() {
		return this.korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
	
	@Column(name = "POTVRDJENA")
	public Boolean getPotvrdjena() {
		return this.potvrdjena;
	}

	public void setPotvrdjena(Boolean potvrdjena) {
		this.potvrdjena = potvrdjena;
	}
}
