package com.schiffeversenken.server;

import com.schiffeversenken.interf.*;

import java.awt.Point;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * 
 * @author Benedict Kohls  {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 *
 */
public class ServerImpl extends UnicastRemoteObject implements ServerInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 30062015L;
	/*
	 * 
	 */
	private ArrayList<Point> spieler1;
	/**
	 * 
	 */
	private ArrayList<Point> spieler2;
	/**
	 * 
	 */
	private ArrayList<Point> ships1;
	/**
	 * 
	 */
	private ArrayList<Point> ships2;
	/**
	 * 
	 */
	private int[][] spielfeld1;
	/**
	 * 
	 */
	private int[][] spielfeld2;
	/**
	 * 
	 */
	private short runde;
	/**
	 * 
	 */
	private int spieler;

	/**
	 * 
	 * @param ship
	 * @param spieler
	 * @throws RemoteException
	 */
	@Override
	public void setShips(ArrayList<Point> ship, int spieler)
			throws RemoteException {
		if (spieler == 1) {
			ships1 = ship;
		} else {
			ships2 = ship;
		}
		calcSpielfeld(spieler);
	}

	/**
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
	 * 
	 * @param spieler
	 * @param gameID
	 */
	private void calcSpielfeld(int spieler) {

		spielfeld1 = new int[11][11];
		spielfeld2 = new int[11][11];

		for (Point p : spieler1) {
			if (ships2.contains(p)) {
				spielfeld1[p.x][p.y] = 2;
			} else {
				spielfeld1[p.x][p.y] = 1;
			}
		}
		for (Point p : spieler2) {
			if (ships1.contains(p)) {
				spielfeld2[p.x][p.y] = 2;
			} else {
				spielfeld2[p.x][p.y] = 1;
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public int getNextPlayer() throws RemoteException {
		if (runde % 2 == 0) {
			return 2;
		} else {
			return 1;
		}
	}

	@Override
	public int[][][] doSpielzug(Point spielzug, int player)
			throws RemoteException {
		if (player == 1) {
			this.spieler1.add(spielzug);
		} else {
			this.spieler2.add(spielzug);
		}
		runde++;
		calcSpielfeld(player);
		return getSpielzuege(player);
	}

	/**
	 * 
	 * 
	 */
	@Override
	public int[][][] getSpielzuege(int player) throws RemoteException {
		int[][][] p = new int[2][][];
		if (player == 1) {
			p[0] = spielfeld1;
			p[1] = spielfeld2;
		} else {
			p[0] = spielfeld2;
			p[1] = spielfeld1;
		}
		return p;
	}

	/**
	 * 
	 */
	@Override
	public boolean gameStarted() throws RemoteException {
		if (ships1 != null && ships2 != null)
			return true;
		else
			return false;
	}

	/**
	 * 
	 */
	@Override
	public int registerNewPlayer() throws RemoteException {
		if (spieler == 2)
			return -1;
		spieler++;
		return spieler;
	}

	/**
	 * 
	 */
	@Override
	public int gewonnen() throws RemoteException {

		int cntSpieler = 0;
		int cnt = 0;
		for (Point p : ships2) {
			cnt++;
			if (spieler1.contains(p)) {
				cntSpieler++;
			}
		}
		if (cnt == cntSpieler) {
			return 1;
		}
		cnt = 0;
		cntSpieler = 0;
		for (Point p : ships1) {
			cnt++;
			if (spieler2.contains(p)) {
				cntSpieler++;
			}
		}
		if (cnt == cntSpieler) {
			return 2;
		} else {
			return 0;
		}

	}

	/**
	 * 
	 */
	@Override
	public boolean gameReady() throws RemoteException {
		if (spieler == 2)
			return true;
		else
			return false;
	}

	/**
	 * 
	 */
	public int startNewGame() {
		return 0;
	}

}
