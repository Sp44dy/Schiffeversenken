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
	 * @return Das neue Spielfeld mit dem uebermittelten Spielzug
	 * @throws RemoteException
	 *             Weitergabe entsprechender Fehlermeldungen
	 */
	public int[][][] doSpielzug(Point spielzug, int player)
			throws RemoteException;

	/**
	 * Diese Funktion gibt die Spielernummer des Spielers zurueck, welcher am
	 * Zug ist.
	 * 
	 * @return Spielernummer
	 * @throws RemoteException
	 *             Weitergabe entsprechender Fehlermeldungen
	 */
	public int getNextPlayer() throws RemoteException;

	/**
	 * Registiriert einen neuen Spieler. Sollte die maximale Spieleanzahl
	 * erreich sein gibt diese -1 zurück
	 * 
	 * @return Gibt die Spielernummer zurueck oder bei Fehler -1
	 * @throws RemoteException
	 *             Weitergabe entsprechender Fehlermeldungen
	 */
	public int registerNewPlayer() throws RemoteException;

	/**
	 * Gibt true zurueck wenn das Spiel gestartet werden kann , andern falls
	 * wird false zurueckgegenem
	 * 
	 * @return true spiel gestartet, false nicht gestartet
	 * @throws RemoteException
	 *             Weitergabe entsprechender Fehlermeldungen
	 */
	public boolean gameStarted() throws RemoteException;

	/**
	 * Gibt die Spielernummer zuerck welcher Spieler gewonnen hat. Wenn das
	 * Spiel noch nicht gewonnen ist gibt diese Funktion 0 zurueck.
	 * 
	 * @return Bei 0 hat keiner gewonnen, andernfalls die Spielernummer
	 * @throws RemoteException
	 */
	public int gewonnen() throws RemoteException;

	/**
	 * Gibt die Spielzuege in anhängigkeit der Spielernummer zurück
	 * 
	 * @param player
	 *            Spielernummer
	 * @return Gibt ein int Arry mit dem Spielfeld zurück
	 * @throws RemoteException
	 */
	int[][][] getSpielzuege(int player) throws RemoteException;

	/**
	 * Registriert die Schiffe beim Server.
	 * 
	 * @param ship
	 *            ArrayList mit den Schiffspositionen
	 * @param spieler
	 *            Spielernummer
	 * @throws RemoteException
	 */
	public void setShips(ArrayList<Point> ship, int spieler)
			throws RemoteException;

	/**
	 * Gibt zurueck ob 2 Spieler anwesend sind.
	 * 
	 * @return Gibt zurück ob 2 Spieler da sind
	 * @throws RemoteException
	 */
	public boolean gameReady() throws RemoteException;

	/**
	 * Sendet an den Server eine Chat-Nachricht
	 * 
	 * @param nachricht Nachricht die gesendet werden soll.
	 * @throws RemoteException
	 */
	public void chatMessage(String nachricht) throws RemoteException;

	/**
	 * Holt den Chatverlauf.
	 * @return Gibt den Chatverlauf zurueck.
	 * @throws RemoteException
	 */
	public ArrayList<String> getChatMessages() throws RemoteException;
}
