package it.vito.desktop.scuolaCalcio;

import java.util.Date;

public class CodiceFiscale {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	String ris=	CalcolaCodiceFiscale.calcolaCf("cangiano", "vitale", "M", new Date(), "F839");
	System.out.print(ris);

	}

}
