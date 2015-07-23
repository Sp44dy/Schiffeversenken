package com.schiffeversenken.interf;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Interface für den Server, damit die benoetigten Methoden auch vom Server
 * implementiert werden. Alle funktionen geben eine Exception weiter, weshalb
 * diese beim Aufruf abgefangen werden sollten.
 * 
 * @author Benedict Kohls {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 *
 */
public interface ServerInterface extends Remote {
	/**
	 * Diese Funktion sendet einen Spielzug an den Server. Also Parameter wird
	 * die Koordinate und die Spielernumemr benötigt. Als rückgabe gibt es das
	 * Spielfeld mit dem aktualierten Spielzug.
	 * 
	 * @param spielzug
	 *            Koordinate des durchzufuehrenden Spielzuges
	 * @param player
	 *            Spielernummer der den Zug durchführt
	 * @throws RemoteException
	 *             Weitergabe entsprechender Fehlermeldungen
	 */
	public void doSpielzug(Point spielzug, int player) throws RemoteException;

	/**
	 * Gibt die Spielzuege in anhängigkeit der Spielernummer zurück
	 * 
	 * @param player
	 *            Spielernummer
	 * @return Gibt ein int Arry mit dem Spielfeld zurück
	 * @throws RemoteException Weitergabe entsprechender Fehlermeldungen
	 * 
	 */
	public int[][][] getSpielzuege(int player) throws RemoteException;

	/**
	 * Registriert die Schiffe beim Server.
	 * 
	 * @param ship
	 *            ArrayList mit den Schiffspositionen
	 * @param spieler
	 *            Spielernummer
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void setShips(ArrayList<Point> ship, int spieler)
			throws RemoteException;

	/**
	 * Sendet an den Server eine Chat-Nachricht. Dieser teil den Clients
	 * die gesendete Nachricht anschließend mit.
	 * 
	 * @param nachricht
	 *            Nachricht die gesendet werden soll.
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void sendChatMessage(String nachricht) throws RemoteException;

	/**
	 * Registriert einen neuen Spieler (maximal 2)
	 * 
	 * @param inter ClientInterface des Clients
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void login(ClientInterface inter) throws RemoteException;

}
