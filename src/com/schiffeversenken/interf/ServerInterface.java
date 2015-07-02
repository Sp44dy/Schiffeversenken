package com.schiffeversenken.interf;

import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * 
 * @author Benedict Kohls <bkohls91@gmail.com>
 * @author Patrick Labisch <paul.florian09@gmail.com>
 *
 */
public interface ServerInterface extends Remote {
	/**
	 * 
	 * @param spielzug
	 * @param player
	 * @return
	 * @throws RemoteException
	 */
	public int[][][] doSpielzug(Point spielzug, int player)
			throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public int getNextPlayer() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public int registerNewPlayer() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean gameStarted() throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public int gewonnen() throws RemoteException;

	/**
	 * 
	 * @param player
	 * @return
	 * @throws RemoteException
	 */
	int[][][] getSpielzuege(int player) throws RemoteException;

	/**
	 * 
	 * @param ship
	 * @param spieler
	 * @throws RemoteException
	 */
	public void setShips(ArrayList<Point> ship, int spieler)
			throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	public boolean gameReady() throws RemoteException;
}
