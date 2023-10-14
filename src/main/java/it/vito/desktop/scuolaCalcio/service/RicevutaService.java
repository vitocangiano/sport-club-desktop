package it.vito.desktop.scuolaCalcio.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import it.vito.desktop.scuolaCalcio.dao.RicevutaDao;
import it.vito.desktop.scuolaCalcio.dao.RicevutaDaoImpl;
import it.vito.desktop.scuolaCalcio.model.Allievo;
import it.vito.desktop.scuolaCalcio.model.Ricevuta;
import jakarta.activation.DataHandler;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

public class RicevutaService extends BaseService {
	private final RicevutaDao ricevutaDaoImpl = new RicevutaDaoImpl();

	public Response saveOrUpDate(Ricevuta ricevuta) {
		responseReset();
		StringBuilder v = valid(ricevuta);
		if (!v.isEmpty()) {
			setError(v.toString());
			response.setMessage("Salvataggio Fallito");
			return response;
		}
		response = ricevutaDaoImpl.saveOrUpDate(ricevuta);

		return response;

	}

	public List<Ricevuta> getAll() {
		List<Ricevuta> list = ricevutaDaoImpl.getAllRicevute();
		return list;
	}

	public Response deliteRicevuta(Ricevuta ricevuta) {
		response = ricevutaDaoImpl.deliteRicevuta(ricevuta);
		return response;
	}

	public List<Ricevuta> maxNumber() {
		return ricevutaDaoImpl.getMaxNumber();
	}

	// ricevute per ogni allievo
	public List<Ricevuta> getAllIdRicevuta(Integer idAllievo) {
		List<Ricevuta> list = new ArrayList<>();
		try {
			
			list = ricevutaDaoImpl.getAllIdRicevute(idAllievo);
			
		} catch (Exception e) {
			setError(e);
		}
		return list;

	}

	public Session getSession() {
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		prop.put("mail.smtp.starttls.enable", true);
		prop.put("mail.smtp.starttls.required", true);

		return Session.getInstance(prop, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("vpurse87@gmail.com", "thxpashmclqrmnsk");
			}
		});

	}

	public Response sendMailPdf(String msg, String mailTo, ByteArrayInputStream pdf) throws Exception {

		try {
			Message message = new MimeMessage(getSession());
			message.setFrom(new InternetAddress("vitocangiano@virgilio.it"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mailTo));
			message.setSubject("Ricevuta Scuola Calcio");

			

			MimeBodyPart mimeBodyPart = new MimeBodyPart();
			mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(mimeBodyPart);

			message.setContent(multipart);

			// Send the complete message parts
			message.setContent(multipart);

			if (pdf != null && pdf instanceof ByteArrayInputStream) {
				// create the second message part with the attachment from a OutputStrean
				MimeBodyPart attachment = new MimeBodyPart();
				ByteArrayDataSource ds = new ByteArrayDataSource(pdf, "application/pdf");
				attachment.setDataHandler(new DataHandler(ds));
				attachment.setFileName("Report.pdf");
				multipart.addBodyPart(attachment);
			}

			Transport.send(message);

		} catch (Exception e) {
			response.setStatus(false);
			checkOrThrow("mail non inviata" + e.getMessage());
		}

		return response;
	}

	public static ByteArrayInputStream pdf(Allievo allievo, Ricevuta ricevuta) {
		Document document = new Document(PageSize.A6.rotate());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {

			PdfWriter.getInstance(document, out);
			document.open();

			Font fontHeader = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);
			Font fontFooter = FontFactory.getFont(FontFactory.TIMES_ROMAN, 8);
			Font title = FontFactory.getFont(FontFactory.TIMES_BOLD, 12);

//			Path path = Paths.get(ClassLoader.getSystemResource("logoEsteso.png").toURI());
//			Image img = Image.getInstance(path.toAbsolutePath().toString());
//			img.scaleAbsolute(150f, 45f);
//			img.setAlignment(Element.ALIGN_CENTER);
//			document.add(img);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			String dataPag = formatter.format(ricevuta.getData());
			Paragraph titolo = new Paragraph(
					"Ricevuta N° : " + ricevuta.getNumero() + "                     Data : " + dataPag, fontHeader);
			titolo.setAlignment(Element.ALIGN_LEFT);
			document.add(titolo);
			document.add(Chunk.NEWLINE);
			Paragraph nome = new Paragraph(
					"Ricevuti da : " + allievo.getCognome().toUpperCase() + " " + allievo.getNome().toUpperCase(),
					fontHeader);
			titolo.setAlignment(Element.ALIGN_LEFT);
			document.add(nome);
//			Paragraph code = new Paragraph(
//					"Codice Fiscale : " + allievo.getCf(), fontHeader);
//			code.setAlignment(Element.ALIGN_LEFT);
//			document.add(code);
			Paragraph note = new Paragraph("Note : " + ricevuta.getNote() + "  Importo : €" + ricevuta.getImporto(),
					fontHeader);
			titolo.setAlignment(Element.ALIGN_LEFT);
			document.add(note);
			Paragraph per = new Paragraph("Per : Corso Scuola Dello Sport", fontHeader);
			titolo.setAlignment(Element.ALIGN_LEFT);
			document.add(per);
			document.add(Chunk.NEWLINE);
			Paragraph timbro = new Paragraph("A.S.D. Fasulla - via Fasulla, 123 - 80126 (NA)", fontFooter);
			timbro.setAlignment(Element.ALIGN_CENTER);
			document.add(timbro);
			Paragraph cf = new Paragraph("C.F: 000000000 - P.IVA: 000000000000", fontFooter);
			cf.setAlignment(Element.ALIGN_CENTER);
			document.add(cf);

			document.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return new ByteArrayInputStream(out.toByteArray());
	}

}
