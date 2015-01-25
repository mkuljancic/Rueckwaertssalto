package oeztuerk_kuljancic;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

public class Test {
	public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException,UnsupportedEncodingException {
		Rueckwaertssalto n = new Rueckwaertssalto();
		n.konsolenEingabe(args);
		n.verbindung();
		n.tabellenName();
		n.pk_fk();
		n.attribute();
		n.ausgabe();
		n.abschluss();
	}
}
