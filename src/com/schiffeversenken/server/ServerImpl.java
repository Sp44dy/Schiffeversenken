package com.schiffeversenken.server;

import com.schiffeversenken.interf.*;

import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Diese Klasse gibt das Spielfeld für die Clients aus, sowie handhabt die
 * eingaben des Clients und entscheidet eigenständig wer gewonnen hat.
 * 
 * @author Benedict Kohls  {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 *
 */
@SuppressWarnings("serial")
public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	/**
	 * Spielzuege von Spieler 1
	 */
	private ArrayList<Point> spieler1;
	/**
	 * Spielzuege von Spieler 2
	 */
	private ArrayList<Point> spieler2;
	/**
	 * Schiffe von Spieler 1
	 */
	private ArrayList<Point> ships1;
	/**
	 * Schiffe von Spieler 2
	 */
	private ArrayList<Point> ships2;
	/**
	 * Spielfeld von Spieler 1
	 */
	private int[][] spielfeld1;
	/**
	 * Spielfeld von Spieler 2
	 */
	private int[][] spielfeld2;
	/**
	 * Aktuelle Runde
	 */
	private short runde;
	/**
	 * Spieleranzahl
	 */
	private int spieler;

	/**
	 * Setzt die Schiffe die vom Client an den Server uebertragen
	 * wurden. 
	 * @param ship Liste mit den Positionen der Schiffe
	 * @param spieler SPielernummer
	 * @throws RemoteException Weiterleitung von Fehlermeldung
	 */
	@Override
	public void setShips(ArrayList<Point> ship, int spieler)
			throws RemoteException {
		// Dem richtigen Spieler die Schiffe setzen
		if (spieler == 1) {
			ships1 = ship;
		} else {
			ships2 = ship;
		}
		//Spielfeld neu berechnen
		calcSpielfeld(spieler);
	}

	/**
	 * Konstruktor für die Klasse ServerImpl. 
	 * Es werden alle wichtigen variabeln initialsiert.
	 * 
	 * @throws RemoteException
	 */
	protected ServerImpl() throws RemoteException {
		super();
		spieler1 = new ArrayList<Point>();
		spieler2 = new ArrayList<Point>();
		runde = 0;
	}

	/**
	 * Berechnet das Spielfeld neu.
	 * 
	 * @param spieler
	 */
	private void calcSpielfeld(int spieler) {

		// Arrays neu initalisieren (leeren)
		spielfeld1 = new int[11][11];
		spielfeld2 = new int[11][11];

		//Spieler 1
		for (Point p : spieler1) {
			if (ships2.contains(p)) {
				//Treffer
				spielfeld1[p.x][p.y] = 2;
			} else {
				//Kein Treffer
				spielfeld1[p.x][p.y] = 1;
			}
		}
		
		//Spieler 2
		for (Point p : spieler2) {
			if (ships1.contains(p)) {
				//Treffer
				spielfeld2[p.x][p.y] = 2;
			} else {
				//Kein Treffer
				spielfeld2[p.x][p.y] = 1;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ServerInterface#getNextPlayer()
	 */
	@Override
	public int getNextPlayer() throws RemoteException {
		// Runden die durch 2 teilbar sind (rest=0) sind für Spieler 2.
		// Andere sind für Spieler 1.
		if (runde % 2 == 0) {
			return 2;
		} else {
			return 1;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ServerInterface#doSpielzug(java.awt.Point, int)
	 */
	@Override
	public int[][][] doSpielzug(Point spielzug, int player)
			throws RemoteException {
		//Spielzug dem richtigen Spieler zuordnen
		if (player == 1) {
			this.spieler1.add(spielzug);
		} else {
			this.spieler2.add(spielzug);
		}
		//nächste Runde einleiten
		runde++;
		//Spielfeld neu berechnen
		calcSpielfeld(player);
		//Neues Spielfeld zurueck geben
		return getSpielzuege(player);
	}

	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ServerInterface#getSpielzuege(int)
	 */
	@Override
	public int[][][] getSpielzuege(int player) throws RemoteException {
		// Array initaliseren
		int[][][] p = new int[2][][];
		// SPielfeld für Spieler 1
		if (player == 1) {
			p[0] = spielfeld1;
			p[1] = spielfeld2;
		} else {
		// Das Spielfeld für Spieler 2 ist gedreht!!!
			p[0] = spielfeld2;
			p[1] = spielfeld1;
		}
		//Spielfeld zurückgeben
		return p;
	}

	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ServerInterface#gameStarted()
	 */
	@Override
	public boolean gameStarted() throws RemoteException {
		//Wenn beide Spieler die Schiffe gesetzt haben gehts los!
		if (ships1 != null && ships2 != null)
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ServerInterface#registerNewPlayer()
	 */
	@Override
	public int registerNewPlayer() throws RemoteException {
		// Mehr als 2 Spieler sind unzulässig
		if (spieler == 2) {
			return -1;
		}
		// Spieleranzahl erhöhen
		spieler++;
		// Spielernummer zurueckgeben
		return spieler;
	}

	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ServerInterface#gewonnen()
	 */
	@Override
	public int gewonnen() throws RemoteException {
		
		int cntSpieler = 0;
		int cnt = 0;
		// Kontrolle ob Spieler 1 alle Schiffe von Spieler 2 getroffen hat
		for (Point p : ships2) {
			cnt++;
			if (spieler1.contains(p)) {
				cntSpieler++;
			}
		}
		// Wenn die alle Schiffe getroffen wurden hat Spieler 1 gewonnen
		if (cnt == cntSpieler) {
			return 1;
		}
		cnt = 0;
		cntSpieler = 0;
		// Das selbe für Spieler 2 mit den Schiffen von Spieler 1
		for (Point p : ships1) {
			cnt++;
			if (spieler2.contains(p)) {
				cntSpieler++;
			}
		}
		// Wenn die alle Schiffe getroffen wurden hat Spieler 1 gewonnen
		if (cnt == cntSpieler) {
			return 2;
		} 
		// Wenn keiner gewonnen hat 0 zurueck geben.
		return 0;

	}

	/*
	 * (non-Javadoc)
	 * @see com.schiffeversenken.interf.ServerInterface#gameReady()
	 */
	@Override
	public boolean gameReady() throws RemoteException {
		// Wenn 2 Spieler da sind is das Spiel bereit
		if (spieler == 2)
			return true;
		else
			return false;
	}
}
