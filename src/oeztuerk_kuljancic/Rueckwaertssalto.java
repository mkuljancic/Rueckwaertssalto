package oeztuerk_kuljancic;

import java.awt.Font;
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
 * @author Sefa Öztürk, Kuljancic Mirza
 * @version 25-12-2015
 */
public class Rueckwaertssalto {

	public static void main(String[] args) {
		Connection connection;
		Statement stmt;
		ResultSet rSet,rSet2,rSet3;
		//DatabaseMetaData db;

		//Standardattribute
		String h_ = "localhost";
		String u_ = "root";//System.getProperty("user.name");
		String p_ = "Fener1907";
		String d_= "premiere";

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
			//DIent zum ausfindig machen der PK und FKs
			java.sql.DatabaseMetaData meta = connection.getMetaData();
			//dynamische Liste für Anzahl der Tabellen
			List<String> tabellen=new ArrayList<String>();

			//Tabellennamen in ArrayList speichern
			while (rSet.next()) {
				tabellen.add(rSet.getString(1));
			} 	

			//pk-Array beinhaltet alle PK
			String[] pk=new String[tabellen.size()];
			String[] fk=new String[tabellen.size()];
			int c=0;//Counter für Array

			for(int k=0;k<tabellen.size();k++){
				rSet2=meta.getPrimaryKeys(null, null, tabellen.get(k));
				while (rSet2.next()) {
					pk[c] =""+rSet2.getString("COLUMN_NAME");
					c++;
				}
				//				rSet3=meta.getImportedKeys(null, null, tabellen.get(k));
				//				while (rSet3.next()) {
				//					fk[c] =""+rSet3.getString("COLUMN_NAME");
				//					c++;
				//				}
			}

			//Dieses Array beinhaltet später alle Attribute
			String a[]=new String[10];
			for(int h=0;h<tabellen.size();h++){
				a[h]="";
			}
			//Dient zur Fehlerbehebung
			String  [] hilf=new String[a.length];
			//Attribute in Array speichern
			for(int co=0;co<tabellen.size();co++){
				rSet=stmt.executeQuery("DESCRIBE "+tabellen.get(co));
				while(rSet.next()){
					a[co]=a[co]+rSet.getString(1)+",";
				}
			}
			//Hilfarray initialisieren
			for(int h=0;h<tabellen.size();h++){
				hilf[h]="";
			}

			//Hilfarray fehlerbeheben
			for(int k=0;k<tabellen.size();k++){
				for(int j=0;j<a[k].length()-1;j++){
					hilf[k]=hilf[k]+a[k].charAt(j);
				}
			}
			a=hilf;

			//Ausgabe in Konsole und in Textfile
			for(int x=0;x<tabellen.size();x++){
				System.out.println(tabellen.get(x)+"("+a[x]+")"+"    <PK  "+pk[x]+">");
				ausgabe.println(tabellen.get(x)+"("+a[x]+")"+"    <PK  "+pk[x]+">");
			}
			//Textfile schließen
			ausgabe.close();
			//Verbindung unterbinden
			connection.close();
			//Statement schließen
			stmt.close();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {

			System.out.println("Parameter ungültig: \n-h ... Hostname des DBMS. Standard: localhost"
					+"\n-u ... Benutzername. Standard: Benutzername des im Betriebssystem angemeldeten Benutzers"
					+"\n-p ... Passwort. Standard: keins"
					+"\n-d ... Name der Datenbank");
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

