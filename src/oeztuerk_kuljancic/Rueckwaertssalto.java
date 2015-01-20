package oeztuerk_kuljancic;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.DatabaseMetaData;
import com.mysql.jdbc.ResultSetMetaData;
/**
 * Erzeugt eine Konsolen-Anwendung, die den Inhalt einer beliebigen Tabelle
 * eines RDBMS als Textfile oder in der Konsole ausgibtausgibt 
 * 
 * @author Sefa Öztürk
 * @version 25-12-2015
 */
public class Rueckwaertssalto {

	public static void main(String[] args) {
		Connection connection;
		Statement stmt;
		ResultSet rSet;
		
		//Standardattribute
		String h_ = "localhost";
		String u_ = "root";//System.getProperty("user.name");
		String p_ = "Fener1907";
		String d_= "rueck";

		//Ausgabeschreiber
		PrintWriter ausgabe;
		//dient für die Fehlermeldung ""Gleiche Parameter

		//Wenn keine Fehlermeldung auftritt, wird das Programm nach den folgenden Parametern verarbeitet.

		try {
			//args: ist eine Referenzvariable, die alle Parameter in der Konsole in ein Array speichert
			//Die System.outs zeigen auf der Konsole welche Eingaben erfolgreich durchgeführt wurden
			for(int j=0;j<args.length;j++){//Länge der Parameter
				switch(args[j]){//switch-case: Wenn eins von den unteren Fällen auftaucht, dann soll diesbezüglich gehandelt werden
				case "-h":
					h_=args[j+1];//Parameter mit der Standard-Variable überschreiben
					System.out.println("Hostname: "+h_);
					break;

				case "-u":
					u_=args[j+1];//Parameter mit der Standard-Variable überschreiben
					System.out.println("Benutzername: "+u_);
					break;

				case "-p":
					p_=args[j+1];//Parameter mit der Standard-Variable überschreiben
					System.out.println("Passwort: "+p_);
					break;

				case "-d":
					d_=args[j+1];//Parameter mit der Standard-Variable überschreiben
					System.out.println("Datenbankname: "+d_);
					break;
				}

			}
			// Datenquelle erzeugen
			Class.forName("com.mysql.jdbc.Driver");

			// Verbindung herstellen
			connection = DriverManager.getConnection("jdbc:mysql://" + h_+"/"+d_, u_, p_);//Endgültigen Parameter anwenden

			// Abfrage vorbereiten und ausführen
			stmt = connection.createStatement();

			//Prüfen ob Tabellenname und die erwünschten Spalten vorhanden sind

			rSet = stmt.executeQuery("SHOW TABLES");
			//ausgabe=ausgabedatei
			ausgabe= new PrintWriter("ausgabe.txt", "UTF-8");

			List<String> tabellen=new ArrayList<String>();
			while (rSet.next()) {
				tabellen.add(rSet.getString(1));	
			} 	

			String ausgabe2="";
			for(int b=0;b<tabellen.size();b++){
				rSet=stmt.executeQuery("DESCRIBE "+tabellen.get(b));
				while(rSet.next()){
					//	System.out.print(","+rSet.getString(1));
				}
			}

			String a[]=new String[10];
			
			for(int co=0;co<tabellen.size();co++){
				rSet=stmt.executeQuery("DESCRIBE "+tabellen.get(co));
				while(rSet.next()){
					a[co]=a[co]+","+rSet.getString(1);
				}
			}


			for(int x=0;x<tabellen.size();x++){
				System.out.println(tabellen.get(x)+"("+a[x]+")");
			}

			ausgabe.close();//File-schließen


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {

			System.out.println("Parameter ungültig: \n-h ... Hostname des DBMS. Standard: localhost"
					+"\n-u ... Benutzername. Standard: Benutzername des im Betriebssystem angemeldeten Benutzers"
					+"\n-p ... Passwort. Standard: keins"
					+"\n-d ... Name der Datenbank"
					+"\n-s ... Feld, nach dem sortiert werden soll. Standard: keines"
					+"\n-r ... Sortierrichtung. Standard: ASC"
					+"\n-w ... eine Bedingung in SQL-Syntax, die um Filtern der Tabelle verwendet wird. Standard: keine"
					+"\n-t ... Trennzeichen, dass für die Ausgabe verwendet werden soll. Standard: ; "
					+"\n-f ... Kommagetrennte Liste (ohne Leerzeichen) der Felder, die im Ergebnis enthalten sein sollen."
					+"\n-o ... Name der Ausgabedatei. Standard: keine -> Ausgabe auf der Konsole"
					+"-T ... Tabellenname");

			System.out.println("--------------");
		} catch (FileNotFoundException e) {
			System.out.println("Achtung das Ausgabefile lautet ausgabe.txt");//Hilfestellung bei Fehlermeldung
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException e){
			System.out.println("Sie haben ein Parameter leer gelassen");//Hilfestellung bei Fehlermeldung
		}

	}
}

