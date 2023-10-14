package it.vito.desktop.scuolaCalcio.model;

import java.io.Serializable;
import javax.persistence.*;

import javafx.scene.control.CheckBox;

import java.util.Date;


/**
 * The persistent class for the ricevuta database table.
 * 
 */
@Entity
@Table(name="ricevuta")
@NamedQuery(name="Ricevuta.findAll", query="SELECT r FROM Ricevuta r")
public class Ricevuta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_ricevuta", unique=true, nullable=false)
	private int idRicevuta;
	
	private int numero;

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	private byte bonifico;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date data;

	@Column(name="id_allievo", nullable=false)
	private int idAllievo;

	@Column(nullable=false)
	private float importo;
	@Transient
	private CheckBox checkBonifico;

	@Column(length=150)
	private String note;

	public Ricevuta() {

	}

	public int getIdRicevuta() {
		return this.idRicevuta;
	}

	public void setIdRicevuta(int idRicevuta) {
		this.idRicevuta = idRicevuta;
	}

	public byte getBonifico() {
		return this.bonifico;
	}

	public void setBonifico(byte bonifico) {
		this.bonifico = bonifico;
	}

	public Date getData() {
		return this.data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getIdAllievo() {
		return this.idAllievo;
	}

	public void setIdAllievo(int idAllievo) {
		this.idAllievo = idAllievo;
	}

	public float getImporto() {
		return this.importo;
	}

	public void setImporto(float importo) {
		this.importo = importo;
	}

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public CheckBox getCheckBonifico() {
		return checkBonifico;
	}

	public void setCheckBonifico(CheckBox checkBonifico) {
		this.checkBonifico = checkBonifico;
	}

}