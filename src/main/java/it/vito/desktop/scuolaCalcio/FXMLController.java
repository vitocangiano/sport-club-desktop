package it.vito.desktop.scuolaCalcio;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import it.vito.desktop.scuolaCalcio.model.Allievo;
import it.vito.desktop.scuolaCalcio.model.Ricevuta;
import it.vito.desktop.scuolaCalcio.service.BaseService;
import it.vito.desktop.scuolaCalcio.service.RicevutaService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class FXMLController extends BaseService implements Initializable {
	RicevutaService rs = new RicevutaService();
	List<Ricevuta> ListRicevute =rs.getAllIdRicevuta(MainController.getAllievoAppoggio().getIdAllievo());
	Ricevuta ricevutaAppoggio = new Ricevuta();
	
	@FXML
	private Label labelError;

	@FXML
	private Label labelMessage;
	@FXML
	private Label labelCognome;

	@FXML
	private Label labenNome;

	@FXML
	private CheckBox checkBonifico;

	@FXML
	private TextField fieldImporto;

	@FXML
	private TextField fieldNote;

	@FXML
	private DatePicker fieldData;

	@FXML
	private TableView<Ricevuta> tblTable;

	@FXML
	private TableColumn<Ricevuta, CheckBox> tblBonifico;

	@FXML
	private TableColumn<Ricevuta, java.util.Date> tblData;

	@FXML
	private TableColumn<Ricevuta, String> tblNote;

	@FXML
	private TableColumn<Ricevuta, Float> tblImporto;

	@FXML
	private TableColumn<Ricevuta, Integer> tblNumeroRicevuta;

	@FXML
	void onSave(ActionEvent event) {
		StringBuilder messError = new StringBuilder();
		responseReset();
		labelError.setText("");
		labelMessage.setText("");
		Ricevuta ricevuta = buildRicevutaField();
		response = rs.saveOrUpDate(ricevuta);
		
		//invio mail
		ByteArrayInputStream pdf= rs.pdf(MainController.getAllievoAppoggio(), ricevuta);
		if(MainController.getAllievoAppoggio().getEmail()!=null) {
		try {
			rs.sendMailPdf("Sono un bravo programmatore", MainController.getAllievoAppoggio().getEmail(), pdf);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			messError.append(e.getMessage());
			e.printStackTrace();
		}
		}
		//messaggi do output
		labelError.setText(messError.append(response.getError()).toString());
		labelMessage.setText(response.getMessage());
		// aggiorna tabella
		ListRicevute = rs.getAllIdRicevuta(MainController.getAllievoAppoggio().getIdAllievo());
		tblRefresh();
	}

	public Ricevuta buildRicevutaField() {
		Ricevuta ricevuta = new Ricevuta();
		ricevuta.setImporto(Float.valueOf(fieldImporto.getText()));
		// default time zone
		ZoneId defaultZoneId = ZoneId.systemDefault();

		// creating the instance of LocalDate using the day, month, year info
		LocalDate localDate = fieldData.getValue();
		if (localDate != null) {
			Date date = (Date) Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			ricevuta.setData(date);
		}
		if (checkBonifico.isSelected()) {
			ricevuta.setBonifico((byte) 1);
		} else {
			ricevuta.setBonifico((byte) 0);
		}
		ricevuta.setNote(fieldNote.getText());
		ricevuta.setIdAllievo(MainController.getAllievoAppoggio().getIdAllievo());
		ricevuta.setIdRicevuta(ricevutaAppoggio.getIdRicevuta());
		List l = rs.maxNumber();
		// se ricevuta.getid e 0 fai salva (max numero ricevuta +1) else aggiorna
		// (stesso numero di ricevura)
		if (ricevuta.getIdRicevuta() == 0) {
			ricevuta.setNumero(Integer.valueOf(l.get(0).toString()) + 1);
		} else {
			ricevuta.setNumero(ricevutaAppoggio.getNumero());
		}
		return ricevuta;
	}

	@FXML
	void getItem(MouseEvent event) {
		Ricevuta ricevuta = tblTable.getSelectionModel().getSelectedItem();
		fieldImporto.setText(String.valueOf(ricevuta.getImporto()));
		fieldNote.setText(ricevuta.getNote());
		if (ricevuta.getCheckBonifico().isSelected()) {
			checkBonifico.setSelected(true);
		} else {
			checkBonifico.setSelected(false);
		}
		if (ricevuta.getData() == null) {
			fieldData.setValue(null);
		} else {
			LocalDate localDate = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(ricevuta.getData()));
			fieldData.setValue(localDate);
		}
		ricevutaAppoggio = ricevuta;

	}

	void tblRefresh() {
		// aggiungo i check box a ogni riga della tbl
		List<Ricevuta> list = new ArrayList<>();
		for (Ricevuta r : ListRicevute) {
			CheckBox c = new CheckBox();
			if (r.getBonifico() == 1) {
				c.setSelected(true);
				r.setCheckBonifico(c);
			} else {

				c.setSelected(false);
				r.setCheckBonifico(c);
			}
			list.add(r);
		}
		tblTable.setItems(FXCollections.observableArrayList(list));

		tblNumeroRicevuta.setCellValueFactory(new PropertyValueFactory<Ricevuta, Integer>("numero"));
		tblImporto.setCellValueFactory(new PropertyValueFactory<Ricevuta, Float>("importo"));
		tblData.setCellValueFactory(new PropertyValueFactory<Ricevuta, Date>("data"));
		tblBonifico.setCellValueFactory(new PropertyValueFactory<Ricevuta, CheckBox>("checkBonifico"));
		tblNote.setCellValueFactory(new PropertyValueFactory<Ricevuta, String>("note"));

	}

	// non elimino le ricevute le segno come errato in note
	@FXML
	void onErrato(ActionEvent event) {
		responseReset();
		if (ricevutaAppoggio.getIdRicevuta() != 0) {
			Ricevuta ricevuta = ricevutaAppoggio;
			ricevuta.setNote("ERRATO");
			ricevuta.setImporto(0);
			response = rs.saveOrUpDate(ricevuta);
			labelError.setText(response.getError());
			labelMessage.setText(response.getMessage());
			// aggiorna tabella
			ListRicevute = rs.getAllIdRicevuta(MainController.getAllievoAppoggio().getIdAllievo());
			tblRefresh();

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		labenNome.setText(MainController.getAllievoAppoggio().getNome().toUpperCase());
		labelCognome.setText(MainController.getAllievoAppoggio().getCognome().toUpperCase());
		tblRefresh();
	}

}
