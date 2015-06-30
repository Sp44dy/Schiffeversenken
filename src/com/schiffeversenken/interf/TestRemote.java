package com.schiffeversenken.interf;
import java.awt.Point;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
public interface TestRemote extends Remote {
	/*
	 * 
	 */
	public int[][][] doSpielzug(Point spielzug, int player) throws RemoteException;
	/*
	 * 
	 */
	public int getNextPlayer( ) throws RemoteException;
	/*
	 * 
	 */
	public int registerNewPlayer() throws RemoteException;
	/*
	 * 
	 */
	public boolean gameStarted() throws RemoteException;
	/*
	 * 
	 */
	public int gewonnen() throws RemoteException;
	/*
	 * 
	 */
	public int[][][] getSpielzuege(int player) throws RemoteException;
	/*
	 * 
	 */
	public void setShips(ArrayList<Point> ship,int spieler) throws RemoteException;
	/*
	 * 
	 */
	public boolean gameReady() throws RemoteException;
}
