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
	//-----------Verbindungsbedingungen-----------\\
	private Connection con;		//Connection-Variable
	private Statement stmt;		//Statement
	private ResultSet rSet;		//ResultSet
	//-----------Verbindungsbedingungen-----------\\

	//--------Interface--------\\
	private DatabaseMetaData meta;		//PK- und FK-Quelle
	//--------Interface--------\\
	
	//-------Hilfsarrays-------\\
	private List<String> tabellen;		//Tabellennamen
	private String[] fk, pk, a;			//FK,PK und Attribute
	private String h_, u_, p_, d_;		//Connection-Parameter
	//-------Hilfsarrays-------\\
	
	//--------Ausgabe--------\\
	private PrintWriter ausgabe;		//RM in .TXT File 
	//--------Ausgabe--------\\

	/**
	 * Standard-Konstruktor: 
	 * initialisiert Standard Connection-Parameter
	 */
	public Rueckwaertssalto() {
		h_ = "localhost";
		u_ = "root";
		p_ = "Fener1907";
		d_ = "rueck";
	}

	/**
	 * Diese Methode lässt dem Benutzer in der Konsole 1-4 Connection Parameter eingeben,
	 * der Zweck besteht darin, dass eine erfolgreiche Verbindung zwischen der DB und Java
	 * hergestellt werden kann. Es werden Hostname,Benutzername,Passwort und Datenbankname
	 * abgefragt.
	 * 
	 * @param args
	 */
	public void konsolenEingabe(String[] args) {
		// args: ist eine Referenzvariable, die alle Parameter in der Konsole in ein Array speichert
		for (int j = 0; j < args.length; j++) {
			switch (args[j]) {
			case "-h":
				h_ = args[j + 1];
				break;

			case "-u":
				u_ = args[j + 1];
				break;

			case "-p":
				p_ = args[j + 1];
				break;

			case "-d":
				d_ = args[j + 1];
				break;
			}

		}

	}
	
	/**
	 * Diese Mehode stellt die Verbindung zwischen der DB und Java her.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	public void verbindung() throws SQLException, ClassNotFoundException {
		// Datenquelle erzeugen
		Class.forName("com.mysql.jdbc.Driver");

		// Verbindung herstellen
		con = DriverManager.getConnection("jdbc:mysql://" + h_ + "/" + d_, u_, p_);// Endgültigen

		// Abfrage vorbereiten und ausführen
		stmt = con.createStatement();
	}
	
	/**
	 * Hier wird sozusagen in einen dynamischen Array die Tabellennamen 
	 * der Datenbank gespeichert.
	 * 
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
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

	/**
	 * Diese Methode speichert die Primary- und Foreign Keys in
	 * String-Arrays
	 * 
	 * @throws SQLException
	 */
	public void pk_fk() throws SQLException {
		// pk-Array beinhaltet alle PK
		pk = new String[tabellen.size()];
		// fk-Array beinhaltet alle FK
		fk = new String[tabellen.size()];
		// Dient später zum ausfindigmachen der PK und FKs
		meta = (DatabaseMetaData) con.getMetaData();

		int c = 0;// Counter für Array-Index
		for (int k = 0; k < tabellen.size(); k++) {
			rSet = meta.getPrimaryKeys(null, null, tabellen.get(k));	//liefert die PKs
			pk[c] = "";		//Dem PK-Array ein Anfangswert zuweisen

			while (rSet.next()) {
				pk[c] = pk[c] + rSet.getString("COLUMN_NAME") + " ";	//PKs hinzufügen
			}

			if (pk[c].equals("")) {
				pk[c] = "no PK";		//Falls keine PKs vorhanden sind, wird "no PK" zurückgegeben
			}

			rSet = meta.getImportedKeys(null, null, tabellen.get(k));	//liefert die FKs
			fk[c] = "";		//Dem FK-Array ein Anfangswert zuweisen
			
			while (rSet.next()) {
				fk[c] = fk[c] + rSet.getString("FKCOLUMN_NAME") + " ";		//FKs hinzufügen
			}

			if (fk[c].equals("")) {
				fk[c] = "no FK";		//Falls keine FKs vorhanden sind, wird "no PK" zurückgegeben
			}
			c++;	//counter erhöhen und zum nächstes Index springen
		}
	}
	
	/**
	 * In diser Methode werden die Attribute für je Tabelle wiederum
	 * in einem String-Array gespeichert
	 * 
	 * @throws SQLException
	 */
	public void attribute() throws SQLException {
		a = new String[tabellen.size()];	//Attribut-Array generieren
		
		for (int h = 0; h < tabellen.size(); h++) {
			a[h] = "";		//Anfangswert zuweisen
		}


		// Attribute in Array speichern
		for (int co = 0; co < tabellen.size(); co++) {
			rSet = stmt.executeQuery("DESCRIBE " + tabellen.get(co));
			while (rSet.next()) {
				a[co] = a[co] + rSet.getString(1) + ",";
			}
		}
		
		//Das Attributs-Array beinhaltet Beistrichfehler,
		//deswegen wird ein Hilfarray initialisiert.
		String[] hilf = new String[a.length];	
		for (int h = 0; h < tabellen.size(); h++) {
			hilf[h] = "";		//Anfangswert zuweisen
		}

		// Hilfarray Beistrichfehler beheben
		for (int k = 0; k < tabellen.size(); k++) {
			for (int j = 0; j < a[k].length() - 1; j++) {
				hilf[k] = hilf[k] + a[k].charAt(j);		//Attribute ohne Beistrichfehler hinzufügen
			}
		}
		a = hilf;	//Stammarray mit dem neuen ersetzen
	}

	/**
	 * Wenn diese Methode ausgeführt wird, wird das RM auf
	 * der Konsole angezeigt und in einem .TXT File gespeichert.
	 * 
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void ausgabe() throws FileNotFoundException, UnsupportedEncodingException {
		//ausgabe
		ausgabe = new PrintWriter("ausgabe.txt", "UTF-8");
		//Ausgabe in Konsole und in Textfile
		System.out.println("Relationen Model: " + d_ + "\n");
		ausgabe.println("Relationen Model: " + d_);
		ausgabe.println("");
		
		//Ausgabe des Arrays
		for (int x = 0; x < tabellen.size(); x++) {
			System.out.println(tabellen.get(x) + "(" + a[x] + ")" + "    < PK:  " + pk[x] + " > " + " < FK:  " + fk[x]
					+ " >");
			ausgabe.println(tabellen.get(x) + "(" + a[x] + ")" + "    < PK:  " + pk[x] + ">" + " < FK:  " + fk[x]
					+ " >");
		}
	}
	/**
	 * 
	 * Programm fertigstellen
	 * 
	 * @throws SQLException
	 */
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
