package oeztuerk_kuljancic;

import java.io.*;
import java.sql.*;
import java.util.*;

import com.mysql.jdbc.DatabaseMetaData;

/**
 * Erzeugt eine Konsolen-Anwendung, die den Inhalt einer beliebigen Tabelle
 * eines RDBMS als Textfile oder in der Konsole ausgibtausgibt
 * 
 * @author Sefa Öztürk, Kuljancic Mirza
 * @version 07-01-2015
 */
public class Rueckwaertssalto {
	private Connection con;// Verbindung
	private Statement stmt;// Statement
	private ResultSet rSet;

	private DatabaseMetaData meta;
	private List<String> tabellen;
	private PrintWriter ausgabe;
	private String[] fk, pk, a;

	private String h_, u_, p_, d_;

	public Rueckwaertssalto() {
		h_ = "localhost";
		u_ = "root";
		p_ = "Fener1907";
		d_ = "rueck";
	}

	public void konsolenEingabe(String[] args) {
		// args: ist eine Referenzvariable, die alle Parameter in der
		// Konsole in ein Array speichert
		// Die System.outs zeigen auf der Konsole welche Eingaben
		// erfolgreich durchgeführt wurden
		for (int j = 0; j < args.length; j++) {// Länge der Parameter
			switch (args[j]) {// switch-case: Wenn eins von den unteren
			// Fällen auftaucht, dann soll diesbezüglich
			// gehandelt werden
			case "-h":
				h_ = args[j + 1];// Parameter mit der Standard-Variable
				// überschreiben
				System.out.println("Hostname: " + h_);
				break;

			case "-u":
				u_ = args[j + 1];// Parameter mit der Standard-Variable
				// überschreiben
				System.out.println("Benutzername: " + u_);
				break;

			case "-p":
				p_ = args[j + 1];// Parameter mit der Standard-Variable
				// überschreiben
				System.out.println("Passwort: " + p_);
				break;

			case "-d":
				d_ = args[j + 1];// Parameter mit der Standard-Variable
				// überschreiben
				System.out.println("Datenbankname: " + d_);
				break;
			}

		}

	}

	public void verbindung() throws SQLException, ClassNotFoundException {
		// Datenquelle erzeugen
		Class.forName("com.mysql.jdbc.Driver");

		// Verbindung herstellen
		con = DriverManager.getConnection("jdbc:mysql://" + h_ + "/" + d_, u_, p_);// Endgültigen

		// Abfrage vorbereiten und ausführen
		stmt = con.createStatement();
	}

	public void tabellenName() throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		// Tabellennamen anzeigen
		rSet = stmt.executeQuery("SHOW TABLES");
		// dynamische Liste für Anzahl der Tabellen
		tabellen = new ArrayList<String>();

		// Tabellennamen in ArrayList speichern
		while (rSet.next()) {
			tabellen.add(rSet.getString(1));
		}
	}

	public void pk_fk() throws SQLException {
		// pk-Array beinhaltet alle PK
		pk = new String[tabellen.size()];
		// fk-Array beinhaltet alle FK
		fk = new String[tabellen.size()];
		// Dient später zum ausfindigmachen der PK und FKs
		meta = (DatabaseMetaData) con.getMetaData();

		int c = 0;// Counter für Array-Index
		for (int k = 0; k < tabellen.size(); k++) {
			rSet = meta.getPrimaryKeys(null, null, tabellen.get(k));
			pk[c] = "";
			while (rSet.next()) {
				pk[c] = pk[c] + rSet.getString("COLUMN_NAME") + " ";
			}

			if (pk[c].equals("")) {
				pk[c] = "no PK";
			}

			rSet = meta.getImportedKeys(null, null, tabellen.get(k));
			fk[c] = "";
			while (rSet.next()) {
				fk[c] = fk[c] + rSet.getString("FKCOLUMN_NAME") + " ";
			}

			if (fk[c].equals("")) {
				fk[c] = "no FK";
			}
			c++;
		}
	}

	public void attribute() throws SQLException {
		// Dieses Array beinhaltet später alle Attribute
		a = new String[tabellen.size()];
		for (int h = 0; h < tabellen.size(); h++) {
			a[h] = "";
		}

		String[] hilf = new String[a.length];
		// Attribute in Array speichern
		for (int co = 0; co < tabellen.size(); co++) {
			rSet = stmt.executeQuery("DESCRIBE " + tabellen.get(co));
			while (rSet.next()) {
				a[co] = a[co] + rSet.getString(1) + ",";
			}
		}
		// Dient zur BeistrichFehlerbehebung
		// Hilfarray initialisieren
		for (int h = 0; h < tabellen.size(); h++) {
			hilf[h] = "";
		}

		// Hilfarray Beistrichfehler beheben
		for (int k = 0; k < tabellen.size(); k++) {
			for (int j = 0; j < a[k].length() - 1; j++) {
				hilf[k] = hilf[k] + a[k].charAt(j);
			}
		}
		a = hilf;
	}

	public void ausgabe() throws FileNotFoundException, UnsupportedEncodingException {
		// ausgabe=ausgabedatei
		ausgabe = new PrintWriter("ausgabe.txt", "UTF-8");

		// Ausgabe in Konsole und in Textfile
		System.out.println("Relationen Model: " + d_ + "\n");
		ausgabe.println("Relationen Model: " + d_);
		ausgabe.println("");

		for (int x = 0; x < tabellen.size(); x++) {
			System.out.println(tabellen.get(x) + "(" + a[x] + ")" + "    < PK:  " + pk[x] + " > " + " < FK:  " + fk[x]
					+ " >");
			ausgabe.println(tabellen.get(x) + "(" + a[x] + ")" + "    < PK:  " + pk[x] + ">" + " < FK:  " + fk[x]
					+ " >");
		}
	}

	public void abschluss() throws SQLException {
		// Textfile schließen
		ausgabe.close();
		// Verbindung unterbinden
		con.close();
		// Statement schließen
		stmt.close();
		// ResultSet schließen
		rSet.close();
	}

}
