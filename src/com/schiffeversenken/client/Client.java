package com.schiffeversenken.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 * GUI und ServerInterface importieren.
 */
import com.schiffeversenken.gui.Spielfeld2d;
import com.schiffeversenken.interf.*;

/**
 * Diese Klasse ruft die GUI für den Schifferversenken-Client auf. Die Klasse
 * ist auch für die Verbindung mit dem RMI-Server zuständig. Der Server kann funktionen
 * dieser Klasse aufrufen.
 * 
 * 
 * @author Benedict Kohls {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 * 
 *
 */
public class Client extends UnicastRemoteObject implements ClientInterface {
	/**
	 * Haelt die Instanz der GUI
	 */
	public Spielfeld2d ui = null;
	/**
	 * Konstrukter, welcher die Instanz des Spiefeldes erzeugt
	 * und das Spielfeld initialisiert, sowie die Verbindung mit 
	 * dem Server herstellt
	 * 
	 * @throws RemoteException
	 * @throws NotBoundException
	 */
	public Client() throws RemoteException, NotBoundException {
		super();
		ui = new Spielfeld2d();
		ui.client = this;
		// Anmelden!
		ui.connect();

	}

	/**
	 * Client Hauptklasse. Diese erzeugt eine neue Instanz der Client Klasse.
	 * @param args
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws NotBoundException {
		try {
			new Client();
		} catch (RemoteException e) {
			e.printStackTrace();
		} finally {

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.schiffeversenken.interf.ClientInterface#chatNachricht(java.lang.String
	 * )
	 */
	@Override
	public void chatNachricht(String nachricht) throws RemoteException {
		//an den Client weiterleiten
		ui.setChatNachricht(nachricht);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.schiffeversenken.interf.ClientInterface#setSpielerNummer(int)
	 */
	@Override
	public void setSpielerNummer(int spielernummer) throws RemoteException {
		//Den Client die Spielernumemr mitteilen
		ui.setSpielernummer(spielernummer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.schiffeversenken.interf.ClientInterface#settingships()
	 */
	@Override
	public void settingships() throws RemoteException {
		//Dem CLient mitteilen das die Schiffe gesetzt werden
		ui.startPlaceingShips();
		ui.setStatus("Schiffe setzen!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.schiffeversenken.interf.ClientInterface#startGame()
	 */
	@Override
	public void startGame() throws RemoteException {
		//Status setzen
		ui.setStatus("Spiel gestartet!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.schiffeversenken.interf.ClientInterface#waitForSpieler()
	 */
	@Override
	public void waitForSpieler() throws RemoteException {
		ui.setStatus("Warte auf Spieler!");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.schiffeversenken.interf.ClientInterface#setSpielzuege(int[][][])
	 */
	@Override
	public void setSpielzuege(int[][][] felder) throws RemoteException {
		//Spielfeld 1
		ui.setSpielzuege1(felder[0]);
		//Spielfeld 2
		ui.setSpielzuege2(felder[1]);
		//Neu malen (immer bei aenderungen)
		ui.repaint();

	}
	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ClientInterface#allowSpielzug()
	 */
	@Override
	public void allowSpielzug() {
		ui.allowSpielzug = true;
		ui.setStatus("Bitte schießen!");
	}
	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ClientInterface#gameEnde()
	 */
	@Override
	public void gameEnde() throws RemoteException {
		ui.allowSpielzug = false;
		ui.setStatus("Spiel Beendet");
	}
	
}