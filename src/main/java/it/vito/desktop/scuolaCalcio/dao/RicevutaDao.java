package it.vito.desktop.scuolaCalcio.dao;

import java.util.List;

import it.vito.desktop.scuolaCalcio.model.Ricevuta;
import it.vito.desktop.scuolaCalcio.service.Response;



public interface RicevutaDao {
	List<Ricevuta> getAllRicevute();
	List<Ricevuta> getAllIdRicevute(Integer idAllievo) throws Exception;
	Response saveOrUpDate(Ricevuta ricevuta);
	Response deliteRicevuta(Ricevuta ricevuta);
	List<Ricevuta> getMaxNumber();

}
