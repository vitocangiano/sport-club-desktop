package it.vito.desktop.scuolaCalcio.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;


/**
 * The persistent class for the allievo database table.
 * 
 */
@Entity
@NamedQuery(name="Allievo.findAll", query="SELECT a FROM Allievo a")
public class Allievo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_allievo")
	private int idAllievo;

	private byte active;
	@Email
	private String email;

	private String cognome;
	
	private String indirizzo;
	@Size(max = 250, message = "max 250 caratteri")
	private String note;

	@Temporal(TemporalType.DATE)
	@Column(name="data_di_nascita")
	private Date dataDiNascita;

	@Temporal(TemporalType.DATE)
	@Column(name="data_iscrizione")
	private Date dataIscrizione;

	@Column(name="luogo_di_nascita")
	private String luogoDiNascita;

	private String nome;

	public Allievo() {
		
	}

	public int getIdAllievo() {
		return this.idAllievo;
	}

	public void setIdAllievo(int idAllievo) {
		this.idAllievo = idAllievo;
	}

	public byte getActive() {
		return this.active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	public String getCognome() {
		return this.cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public Date getDataDiNascita() {
		return this.dataDiNascita;
	}

	public void setDataDiNascita(Date dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}

	public Date getDataIscrizione() {
		return this.dataIscrizione;
	}

	public void setDataIscrizione(Date dataIscrizione) {
		this.dataIscrizione = dataIscrizione;
	}

	public String getLuogoDiNascita() {
		return this.luogoDiNascita;
	}

	public void setLuogoDiNascita(String luogoDiNascita) {
		this.luogoDiNascita = luogoDiNascita;
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

	
	

}