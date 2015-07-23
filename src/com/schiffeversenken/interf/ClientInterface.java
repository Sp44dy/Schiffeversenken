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
	 * 
	 * @param nachricht
	 * @throws RemoteException
	 */
	public void chatNachricht(String nachricht) throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public void setSpielerNummer(int spielernummer) throws RemoteException;

	/**
	 * 
	 * @throws RemoteException
	 */
	public void settingships() throws RemoteException;

	/**
	 * 
	 * @throws RemoteException
	 */
	public void startGame() throws RemoteException;

	/**
	 * 
	 * @throws RemoteException
	 */
	public void waitForSpieler() throws RemoteException;

	/**
	 * 
	 * @param felder
	 * @throws RemoteException
	 */
	public void setSpielzuege(int[][][] felder) throws RemoteException;

	/**
	 * 
	 * @throws RemoteException
	 */
	public void allowSpielzug() throws RemoteException;

	/**
	 * Beendet das Spiel da ein Spieler gewonnen hat
	 * 
	 * @throws RemoteException
	 */
	public void gameEnde() throws RemoteException;
}
