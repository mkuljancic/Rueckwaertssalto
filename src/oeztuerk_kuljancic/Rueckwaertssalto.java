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
 * @author Sefa �zt�rk
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
		//dient f�r die Fehlermeldung ""Gleiche Parameter

		//Wenn keine Fehlermeldung auftritt, wird das Programm nach den folgenden Parametern verarbeitet.

		try {
			//args: ist eine Referenzvariable, die alle Parameter in der Konsole in ein Array speichert
			//Die System.outs zeigen auf der Konsole welche Eingaben erfolgreich durchgef�hrt wurden
			for(int j=0;j<args.length;j++){//L�nge der Parameter
				switch(args[j]){//switch-case: Wenn eins von den unteren F�llen auftaucht, dann soll diesbez�glich gehandelt werden
				case "-h":
					h_=args[j+1];//Parameter mit der Standard-Variable �berschreiben
					System.out.println("Hostname: "+h_);
					break;

				case "-u":
					u_=args[j+1];//Parameter mit der Standard-Variable �berschreiben
					System.out.println("Benutzername: "+u_);
					break;

				case "-p":
					p_=args[j+1];//Parameter mit der Standard-Variable �berschreiben
					System.out.println("Passwort: "+p_);
					break;

				case "-d":
					d_=args[j+1];//Parameter mit der Standard-Variable �berschreiben
					System.out.println("Datenbankname: "+d_);
					break;
				}

			}
			// Datenquelle erzeugen
			Class.forName("com.mysql.jdbc.Driver");

			// Verbindung herstellen
			connection = DriverManager.getConnection("jdbc:mysql://" + h_+"/"+d_, u_, p_);//Endg�ltigen Parameter anwenden

			// Abfrage vorbereiten und ausf�hren
			stmt = connection.createStatement();

			//Pr�fen ob Tabellenname und die erw�nschten Spalten vorhanden sind

			rSet = stmt.executeQuery("SHOW TABLES");
			//ausgabe=ausgabedatei
			ausgabe= new PrintWriter("ausgabe.txt", "UTF-8");

			java.sql.DatabaseMetaData meta = connection.getMetaData();

			List<String> tabellen=new ArrayList<String>();


			while (rSet.next()) {
				tabellen.add(rSet.getString(1));
			} 	

			String[] pk=new String[tabellen.size()];
			String[] fk=new String[tabellen.size()];
			int c=0;
			for(int k=0;k<tabellen.size();k++){
				rSet2=meta.getPrimaryKeys(null, null, tabellen.get(k));
				while (rSet2.next()) {
					pk[c] = ""+rSet2.getString("COLUMN_NAME");
					c++;
				}
			}
//			



			String a[]=new String[10];
			for(int h=0;h<tabellen.size();h++){
				a[h]="";
			}

			String  [] hilf=new String[a.length];

			for(int co=0;co<tabellen.size();co++){
				rSet=stmt.executeQuery("DESCRIBE "+tabellen.get(co));
				while(rSet.next()){
					a[co]=a[co]+rSet.getString(1)+",";
				}
			}

			for(int h=0;h<tabellen.size();h++){
				hilf[h]="";
			}


			for(int k=0;k<tabellen.size();k++){
				for(int j=0;j<a[k].length()-1;j++){
					hilf[k]=hilf[k]+a[k].charAt(j);
				}
			}
			a=hilf;

			for(int x=0;x<tabellen.size();x++){
				System.out.println(tabellen.get(x)+"("+a[x]+")"+"    <PK  "+pk[x]+">");
				ausgabe.println(tabellen.get(x)+"("+a[x]+")"+"    <PK  "+pk[x]+">");
			}
			
			ausgabe.close();//File-schlie�en


		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {

			System.out.println("Parameter ung�ltig: \n-h ... Hostname des DBMS. Standard: localhost"
					+"\n-u ... Benutzername. Standard: Benutzername des im Betriebssystem angemeldeten Benutzers"
					+"\n-p ... Passwort. Standard: keins"
					+"\n-d ... Name der Datenbank"
					+"\n-s ... Feld, nach dem sortiert werden soll. Standard: keines"
					+"\n-r ... Sortierrichtung. Standard: ASC"
					+"\n-w ... eine Bedingung in SQL-Syntax, die um Filtern der Tabelle verwendet wird. Standard: keine"
					+"\n-t ... Trennzeichen, dass f�r die Ausgabe verwendet werden soll. Standard: ; "
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

