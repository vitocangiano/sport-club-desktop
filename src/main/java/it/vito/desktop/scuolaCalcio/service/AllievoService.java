package it.vito.desktop.scuolaCalcio.service;

import java.util.List;

import it.vito.desktop.scuolaCalcio.dao.AllievoDao;
import it.vito.desktop.scuolaCalcio.dao.AllievoDaoImpl;
import it.vito.desktop.scuolaCalcio.model.Allievo;

public class AllievoService extends BaseService {
	private final AllievoDao allievoDaoImpl = new AllievoDaoImpl();

	public Response saveOrUpDate(Allievo allievo) {
		responseReset();
		StringBuilder v = valid(allievo);
		if (!v.isEmpty()) {
			setError(v.toString());
			response.setMessage("Salvataggio Fallito");
			return response;
		}
		response = allievoDaoImpl.saveOrUpDate(allievo);
		
		return response;

	}

	public List<Allievo> getAll() {
		List<Allievo> list = allievoDaoImpl.getAllAllievi();
		return list;
	}
	public Response deliteAllievo(Allievo allievo) {
		response = allievoDaoImpl.deliteAllievo(allievo);
		return response;
	}

}
