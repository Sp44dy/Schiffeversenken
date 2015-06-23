package com.schiffeversenken.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.schiffeversenken.gui.Spielfeld2d;
import com.schiffeversenken.interf.*;

public class SchiffeversenkenClient {
	public static int p[][][];
	public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
		Registry registry  = LocateRegistry.getRegistry("localhost", Constant.RMI_PORT);
		TestRemote remote = (TestRemote)registry.lookup(Constant.RMI_ID);
		
		Spielfeld2d sp = new Spielfeld2d() ;
		//spieler "anmelden"
		sp.setSpielerNummer(remote.registerNewPlayer());
		/** in threads auslagern da sonst die gui laggt!!!! **/
		/** auf2 Spieler warten **/
		System.out.println("warte auf Spieler");
		Thread th = new Thread(new Runnable() {
		         public void run() {
		            while(true) {
		            		try {
								if(remote.gameReady()) {
									break;
								}
							} catch (RemoteException e1) {
								e1.printStackTrace();
							} 
		            	try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} 
		            }
		         }
		     });
		th.start();
		th.join();
		
		System.out.println("Schiffe setzen");
		sp.startPlaceingShips();
		th = new Thread(new Runnable() {
	         public void run() {
	            while(true) {
	            	if(sp.setzeSchiffe==false) {
	            		try {
							remote.setShips(sp.gesetzeSchiffe,sp.getSpielerNummer());
						} catch (RemoteException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            		break;
	            	}
	            	
	            	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						
						e.printStackTrace();
					} 
	            }
	         }
	     });
		th.start();
		th.join();
		
		System.out.println("Starte Spielzüge");		
		th = new Thread(new Runnable() {
	         public void run() {
	            while(true) {
	            	try {
	            		if(remote.getNextPlayer() == sp.getSpielerNummer()) {
	            			sp.allowSpielzug=true;
	            			if(sp.isSpielzugdone()==true) {
	            				p = remote.doSpielzug(sp.getSpielzug(),sp.getSpielerNummer() );
	            				sp.allowSpielzug = false;
	            				sp.setSpielzugdone(false);
	            				sp.setSpielzuege1(p[0]);
	            				sp.setSpielzuege2(p[1]);
	            				sp.repaint();
	            			} else {
	            				p=remote.getSpielzuege(sp.getSpielerNummer());
		            			sp.setSpielzuege1(p[0]);
	            				sp.setSpielzuege2(p[1]);
		            			sp.repaint();
	            			}
	            		} 
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}
	            	try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} 
	            }
	         }
	     });
		th.start();
		th.join();
	}
}