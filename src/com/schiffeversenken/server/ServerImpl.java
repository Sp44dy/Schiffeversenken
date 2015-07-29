package com.schiffeversenken.server;

import com.schiffeversenken.interf.*;


import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.util.ArrayList;
import java.awt.Point;

/**
 * Diese Klasse berechnet das Spielfeld fuer die Clients, sowie handhabt die
 * eingaben des Clients und entscheidet eigenstaendig wer gewonnen hat. Zusaetzlich
 * steht ein Chat zur verfuegung.
 * 
 * @author Benedict Kohls {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 *
 */

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
	 * Haelt die vorhandenen Chat-Nachrichten
	 */
	private ArrayList<String> chat;
	/**
	 * ClientInterfaces zur Kommunikation mit dem Client
	 */
	private ClientInterface[] clients;

	/**
	 * Konstruktor fuer die Klasse ServerImpl. Es werden alle wichtigen variabeln
	 * initialsiert.
	 * 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	protected ServerImpl() throws RemoteException {
		super();
		//Felder der Spieler initialisieren
		spieler1 = new ArrayList<Point>();
		spieler2 = new ArrayList<Point>();
		// Chat initialisieren
		chat = new ArrayList<String>();
		//Clients Array fuer die Kommonikation reservieren
		clients = new ClientInterface[2]; // 2 Clients erlauben
		runde = 0;
		spieler = 0;
	}

	/**
	 * Setzt die Schiffe die vom Client an den Server uebertragen wurden.
	 * 
	 * @param ship
	 *            Liste mit den Positionen der Schiffe
	 * @param spieler
	 *            SPielernummer
	 * @throws RemoteException  Weiterleitung von Fehlermeldung
	 *            
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
		// Spielfeld neu berechnen
		calcSpielfeld(spieler);
		// Spiel starten!
		if (gameStarted()) {
			// Den richtigen Client sagen das er starten darf
			setNextRound();
		}
	}

	/**
	 * Fuegt eine Chat-Nachricht hinzu
	 * @param nachricht Nachricht
	 * @throws RemoteException  Weiterleitung der Fehlermeldung
	 */
	public void chatMessage(String nachricht) throws RemoteException {
		//Chatnachricht hinzufuegen
		this.chat.add(nachricht);
		
	}
	/**
	 * Teilt den Clients mit ob diese am Zug sind oder nicht.
	 */
	private void setNextRound() {
		try {
			//Spieler 1 ist dran
			if (this.getNextPlayer() == 1) {
				this.clients[0].allowSpielzug();
				this.clients[1].waitForSpieler();
				
			} else {
				//Spieler 2 ist am Zug
				this.clients[1].allowSpielzug();
				this.clients[0].waitForSpieler();
			}
			//Clients die Spielzuege uebermitteln 
			this.clients[0].setSpielzuege(this.getSpielzuege(1));
			this.clients[1].setSpielzuege(this.getSpielzuege(2));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
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

		// Spieler 1
		for (Point p : spieler1) {
			if (ships2.contains(p)) {
				// Treffer
				spielfeld1[p.x][p.y] = 2;
			} else {
				// Kein Treffer
				spielfeld1[p.x][p.y] = 1;
			}
		}

		// Spieler 2
		for (Point p : spieler2) {
			if (ships1.contains(p)) {
				// Treffer
				spielfeld2[p.x][p.y] = 2;
			} else {
				// Kein Treffer
				spielfeld2[p.x][p.y] = 1;
			}
		}
		
		
	}

	/**
	 * Gibt zurueck welcher Spieler am Zug ist.
	 * @return Spielernummer
	 */
	protected int getNextPlayer() {
		// Runden die durch 2 teilbar sind (rest=0) sind fuer Spieler 2.
		// Andere sind fuer Spieler 1.
		if (runde % 2 == 0) {
			return 2;
		} else {
			return 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.schiffeversenken.interf.ServerInterface#doSpielzug(java.awt.Point,
	 * int)
	 */
	@Override
	public void doSpielzug(Point spielzug, int player) throws RemoteException {
		// Spielzug dem richtigen Spieler zuordnen
		if (player == 1) {
			this.spieler1.add(spielzug);
		} else {
			this.spieler2.add(spielzug);
		}
		
		// naechste Runde einleiten
		runde++;
		
		// Spielfeld neu berechnen
		calcSpielfeld(player);
		
		// Den Clients mitteilen was sache ist
		setNextRound();
		//pruefen ob jemand gewonnen hat
				try {
					int gSpieler = this.gewonnen();
					// 0 bedeutet keiner hat gewonnen
					if(gSpieler != 0 ) {
						//Den Clients dies auch mitteilen!
						sendChatMessage("Spiel: Es hat Spieler " + gSpieler + " gewonnen!!");
						this.clients[0].gameEnde();
						this.clients[1].gameEnde();
					}
				} catch (RemoteException e) {
					e.printStackTrace();
				}
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.schiffeversenken.interf.ServerInterface#getSpielzuege(int)
	 */

	public int[][][] getSpielzuege(int player) throws RemoteException {
		// Array initaliseren
		int[][][] p = new int[2][][];
		// Spielfeld fuer Spieler 1
		if (player == 1) {
			p[0] = spielfeld1;
			p[1] = spielfeld2;
		} else {
			// Das Spielfeld fuer Spieler 2 ist gedreht!!!
			p[0] = spielfeld2;
			p[1] = spielfeld1;
		}
		// Spielfeld zurueckgeben
		return p;
	}

	/**
	 * Gibt an ob alle Spieler die Schiffe gesetzt haben
	 * @return Ja oder Nein
	 */
	private boolean gameStarted() {
		// Wenn beide Spieler die Schiffe gesetzt haben gehts los!
		if (ships1 != null && ships2 != null)
			return true;
		else
			return false;
	}

	/**
	 * Gibt die Spielernummer zurueck, welcher gewonnen hat
	 * 
	 * @return Niemand (0) oder Spielernummer
	 */
	private int gewonnen()  {
		int cntSpieler = 0;
		int cnt = 0;
		// Kontrolle ob Spieler 1 alle Schiffe von Spieler 2 getroffen hat
		for (Point p : ships2) {
			cnt++;
			if (spieler1.contains(p)) {
				cntSpieler++;
			}
		}
		// Wenn alle Schiffe getroffen wurden hat Spieler 1 gewonnen
		if (cnt == cntSpieler) {
			return 1;
		}
		cnt = 0;
		cntSpieler = 0;
		// Das selbe fuer Spieler 2 mit den Schiffen von Spieler 1
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
	 * 
	 * @see
	 * com.schiffeversenken.interf.ServerInterface#sendChatMessage(java.lang
	 * .String)
	 */
	@Override
	public void sendChatMessage(String nachricht) throws RemoteException {
		// Die Chat-Nachricht beiden Clients mitteilen
		nachricht = nachricht + "\n";
		if (clients[0] != null)
			this.clients[0].chatNachricht(nachricht);
		if (clients[1] != null)
			this.clients[1].chatNachricht(nachricht);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.schiffeversenken.interf.ServerInterface#login(com.schiffeversenken
	 * .interf.ClientInterface)
	 */
	@Override
	public synchronized void login(ClientInterface inter)
			throws RemoteException {
		//mehr als 2 Spieler nicht!
		if (spieler == 2) {
			inter.setSpielerNummer(-1);
			return;
		}
		//Den CLient beim Server registrieren
		this.clients[spieler] = inter;
		// Spieleranzahl erhöhen
		this.spieler++;
		//Spielernumemr setzen
		inter.setSpielerNummer(spieler);
		//Auf andere Spieler warten
		inter.waitForSpieler();
		//Wenn 2 SPieler dann den Clients mitteilen
		// das die Schiffe gesetzt werden können
		if (this.spieler == 2) {
			// Schiffe setzen
			this.clients[0].settingships();
			this.clients[1].settingships();
		}

		System.out.println("[DEBUG] Client " + this.spieler
				+ " hat sich angemeldet");
	}
}
