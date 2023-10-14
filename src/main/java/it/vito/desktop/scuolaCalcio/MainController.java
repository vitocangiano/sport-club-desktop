package it.vito.desktop.scuolaCalcio;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Date;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import it.vito.desktop.scuolaCalcio.model.Allievo;
import it.vito.desktop.scuolaCalcio.service.AllievoService;
import it.vito.desktop.scuolaCalcio.service.BaseService;
import it.vito.desktop.scuolaCalcio.service.ListLuogoNascitaItalia;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class MainController extends BaseService implements Initializable {
	AllievoService allievoService = new AllievoService();
	List<Allievo> listAlliev = allievoService.getAll();
	private static Allievo allievoAppoggio;

	@FXML
	private Label labelError;

	@FXML
	private Label labelMessage;

	@FXML
	private TableView<Allievo> tblTable;

	@FXML
	private TableColumn<Allievo, String> txtCognome;

	@FXML
	private TableColumn<Allievo, String> txtIndirizzo;

	@FXML
	private TableColumn<Allievo, String> txtNome;
	@FXML
	private TableColumn<Allievo, Date> tblDataNascita;

	@FXML
	private TableColumn<Allievo, String> tblLuogoNascita;

	@FXML
	private TableColumn<Allievo, String> tblNote;

	@FXML
	private TextField fieldCognome;

	@FXML
	private TextField fieldIndirizzo;

	@FXML
	private TextField fieldLuogo;

	@FXML
	private TextArea fieldNote;

	@FXML
	private TextField fieldNome;
	@FXML
	private TextField fieldCodiceFiscale;
	@FXML
	private DatePicker txtData;
	@FXML
	private TextField txtFind;

	private AutoCompletionBinding<List<String>> autoComplite;

	@FXML
	void onCalcolaCodiceFiscale(ActionEvent event) {
		labelError.setText("");
		labelMessage.setText("");
		// prendo gli ultimi 4 char della stringa (NAPOLI;F839)
		String luogo = fieldLuogo.getText();
		int dimensione = luogo.length();
		String result = "";
		for (int i = 0; i < 4; i++) {
			result = result + luogo.charAt(dimensione - 4);
			dimensione++;
		}
		// convert localDate to Date
		// default time zone
		ZoneId defaultZoneId = ZoneId.systemDefault();

		// creating the instance of LocalDate using the day, month, year info
		LocalDate localDate = txtData.getValue();
		Date date = null;
		if (localDate != null) {
			date = (Date) Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());

		}
		String cf = CalcolaCodiceFiscale.calcolaCf(fieldCognome.getText(), fieldNome.getText(), "M", date, result);
		fieldCodiceFiscale.setText(cf);
	}

	@FXML
	void getItem(MouseEvent event) {
		labelError.setText("");
		labelMessage.setText("");
		Allievo allievoService = tblTable.getSelectionModel().getSelectedItem();
		fieldCognome.setText(allievoService.getCognome());
		fieldNome.setText(allievoService.getNome());
		fieldNote.setText(allievoService.getNote());
		fieldIndirizzo.setText(allievoService.getIndirizzo());
		fieldLuogo.setText(allievoService.getLuogoDiNascita());
		// convert date to localDeta
		if (allievoService.getDataDiNascita() == null) {
			txtData.setValue(null);
		} else {
			LocalDate localDate = LocalDate
					.parse(new SimpleDateFormat("yyyy-MM-dd").format(allievoService.getDataDiNascita()));
			txtData.setValue(localDate);
		}
		allievoAppoggio = allievoService;

	}

	@FXML
	void onRelased(KeyEvent event) {
		tblTable.getItems().clear();
		tblTable.setItems(FXCollections.observableArrayList(find(txtFind.getText())));
	}

	public List<Allievo> find(String name) {
		List<Allievo> listFind = new ArrayList<>();
		for (Allievo a : listAlliev) {
			if (a.getCognome().toLowerCase().indexOf(name.toLowerCase()) != -1) {
				listFind.add(a);

			}

		}
		return listFind;

	}

	@FXML
	void onNewAllievo(ActionEvent event) throws IOException {
		labelError.setText("");
		labelMessage.setText("");

		fieldCognome.clear();
		fieldNome.clear();
		fieldNote.clear();
		fieldIndirizzo.clear();
		fieldLuogo.clear();
		txtData.setValue(null);

	}

	@FXML
	void onUpDate(ActionEvent event) {
		responseReset();
		labelError.setText("");
		labelMessage.setText("");
		Allievo alli = buildAlliviListFieldTxt();
		response = allievoService.saveOrUpDate(alli);
		labelError.setText(response.getError());
		labelMessage.setText(response.getMessage());
		// aggiorna tabella
		listAlliev = allievoService.getAll();
		tblRefresh();
	}

	Allievo buildAlliviListFieldTxt() {
		Allievo alli = new Allievo();
		// default time zone
		ZoneId defaultZoneId = ZoneId.systemDefault();

		// creating the instance of LocalDate using the day, month, year info
		LocalDate localDate = txtData.getValue();
		if (localDate != null) {
			Date date = (Date) Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
			alli.setDataDiNascita(date);
		}

		alli.setIdAllievo(allievoAppoggio.getIdAllievo());
		alli.setCognome(fieldCognome.getText());
		alli.setNome(fieldNome.getText());

		alli.setLuogoDiNascita(fieldLuogo.getText());
		alli.setIndirizzo(fieldIndirizzo.getText());
		alli.setNote(fieldNote.getText());
		return alli;
	}

	@FXML
	void onDelite(ActionEvent event) {
		response = allievoService.deliteAllievo(allievoAppoggio);
		labelError.setText(response.getError());
		labelMessage.setText(response.getMessage());
		// aggiorno la tabella
		listAlliev = allievoService.getAll();
		tblRefresh();
	}

	@FXML
	void onRicevuta(ActionEvent event) throws IOException {
		// apro un altra finestra
		 Parent root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
		
		
		Stage stage = new Stage();
		Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        scene.getRoot().setStyle("-fx-font-family: 'serif'");

        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();

	}

	void tblRefresh() {
		tblTable.setItems(FXCollections.observableArrayList(listAlliev));

		txtCognome.setCellValueFactory(new PropertyValueFactory<Allievo, String>("cognome"));
		txtNome.setCellValueFactory(new PropertyValueFactory<Allievo, String>("nome"));
		txtIndirizzo.setCellValueFactory(new PropertyValueFactory<Allievo, String>("indirizzo"));
		tblLuogoNascita.setCellValueFactory(new PropertyValueFactory<Allievo, String>("luogoDiNascita"));
		tblDataNascita.setCellValueFactory(new PropertyValueFactory<Allievo, Date>("dataDiNascita"));
		tblNote.setCellValueFactory(new PropertyValueFactory<Allievo,String>("note"));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ListLuogoNascitaItalia listIta = new ListLuogoNascitaItalia();
		tblRefresh();

		TextFields.bindAutoCompletion(fieldLuogo, listIta.list);

	}

	public static Allievo getAllievoAppoggio() {
		return allievoAppoggio;
	}

	public static void setAllievoAppoggio(Allievo allievoAppoggio) {
		MainController.allievoAppoggio = allievoAppoggio;
	}

}