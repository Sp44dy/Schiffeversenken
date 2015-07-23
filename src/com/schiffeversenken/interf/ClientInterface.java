package com.schiffeversenken.interf;

import java.rmi.*;

/**
 * Interface für den Client, damit die benoetigten Methoden auch vom Server
 * implementiert werden. Alle funktionen geben eine Exception weiter, weshalb
 * diese beim Aufruf abgefangen werden sollten.
 * 
 * @author Benedict Kohls {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 */
public interface ClientInterface extends Remote {
	/**
	 * Empfängt die Chatnachricht des Servers
	 * 
	 * @param nachricht
	 *            Text/Nachricht des Servers
	 * @throws RemoteException
	 *             Weitergabe entsprechender Fehlermeldungen
	 * 
	 */
	public void chatNachricht(String nachricht) throws RemoteException;

	/**
	 * Setzt die Spielernummer fuer den Client
	 * 
	 * @param spielernummer Spielernummer
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 * 
	 */
	public void setSpielerNummer(int spielernummer) throws RemoteException;

	/**
	 * Sagt dem Client das er Schiffe setzen kann
	 * 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void settingships() throws RemoteException;

	/**
	 * Teilt dem Client den Spielstart mit
	 * 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void startGame() throws RemoteException;

	/**
	 * Sagt dem Client, dass er auf andere Spieler warten muss
	 * 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void waitForSpieler() throws RemoteException;

	/**
	 * Leitet die vom Server kalkulierten Spielzuege an den Client weiter
	 * 
	 * @param felder Spielfelder als 3D-Array
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void setSpielzuege(int[][][] felder) throws RemoteException;

	/**
	 * Sagt dem Client, dass er einen Spielzug machen darf
	 * 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void allowSpielzug() throws RemoteException;

	/**
	 * Beendet das Spiel da ein Spieler gewonnen hat
	 * 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void gameEnde() throws RemoteException;
}
