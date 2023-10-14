package it.vito.desktop.scuolaCalcio.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import it.vito.desktop.scuolaCalcio.model.Allievo;
import it.vito.desktop.scuolaCalcio.service.Response;

public interface AllievoDao {
	//void saveAllievo(Allievo allievo);
	List<Allievo> getAllAllievi();
	Response saveOrUpDate(Allievo allievo);
	Response deliteAllievo(Allievo allievo);

}
