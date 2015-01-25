package oeztuerk_kuljancic;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
/**
 * Diese Klasse testet die Klasse Rueckwaertssalto
 * 
 * @author Öztürk Sefa, Kuljancic Mirza
 *
 */
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
