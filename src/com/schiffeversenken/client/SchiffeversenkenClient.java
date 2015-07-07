package com.schiffeversenken.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


import java.util.ArrayList;

/**
 * GUI und ServerInterface importieren.
 */
import com.schiffeversenken.gui.Spielfeld2d;
import com.schiffeversenken.interf.*;

/**
 * Diese Klasse ruft die GUI für den Schifferversenken-Client auf. Die Klassen
 * ist auch für die Verbindung mit dem RMI-Server zuständig.
 * 
 * 
 * @author Benedict Kohls  {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 * 
 *
 */
public class SchiffeversenkenClient {
	/**
	 * 
	 * @param args
	 * @throws RemoteException
	 * @throws NotBoundException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) {
		Spielfeld2d sp = null;
		Registry registry = null;
		ServerInterface remote = null;
		int p[][][] = null;
		
		try {
			registry = LocateRegistry.getRegistry("141.72.113.122",
					Constant.RMI_PORT);
			remote = (ServerInterface) registry.lookup(Constant.RMI_ID);
			sp = new Spielfeld2d(remote);
			// Spieler nummer vom Server holen
			int spielernr = remote.registerNewPlayer();
			System.out.println("Spieler" + spielernr);
			if (spielernr == -1) {
				System.out.println("No way dude");
				System.exit(404);
			}
			
			
			sp.setSpielerNummer(spielernr);
			Thread t  = new Thread() {
	            private ServerInterface rem = null;
	            private Spielfeld2d sp = null;
	            Thread init(ServerInterface remo,Spielfeld2d spiel){

	                this.rem = remo;
	                this.sp = spiel;
	                return this;
	            }

	            public void run(){
	                while(true) {
	            	try {
						ArrayList<String> nachrichten = rem.getChatMessages();
					
							if(nachrichten.size() != sp.chatNachrichten.size()) {
								sp.setChatNachrichten(nachrichten);
							}
					
						
						Thread.sleep(500);
					} catch (RemoteException | InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            		
	                }
	                
	            }
	        }.init(remote,sp);
			t.start();
			
			// Auf 2 Spieler warten
			System.out.println("Warte auf andere Spieler....");
			while (true) {
				if (remote.gameReady()) {
					break;
				}
				// ne runde schlafen
				Thread.sleep(500);
			}
			// Beginnen die Schiffe zu setzen
			System.out.println("Alle da..");
			System.out.println("Schiffe setzen");
			// Gui erlauben Schiffe setzen...
			sp.startPlaceingShips();
			while (!remote.gameStarted()) {
				if (sp.setzeSchiffe == false) {
					remote.setShips(sp.gesetzeSchiffe, sp.getSpielerNummer());
				}
				Thread.sleep(500);
			}
			System.out.println("Starte Spielzüge");
			
			while (remote.gewonnen() == 0) {
				if (remote.getNextPlayer() == sp.getSpielerNummer()) {
					sp.allowSpielzug = true;
					
					if (sp.isSpielzugdone() == true) {
						p = remote.doSpielzug(sp.getSpielzug(),
								sp.getSpielerNummer());
						sp.allowSpielzug = false;
						sp.setSpielzugdone(false);
						sp.setSpielzuege1(p[0]);
						sp.setSpielzuege2(p[1]);
						sp.repaint();
					} else {
						p = remote.getSpielzuege(sp.getSpielerNummer());
						sp.setSpielzuege1(p[0]);
						sp.setSpielzuege2(p[1]);
						sp.repaint();
					}
				}
				Thread.sleep(250);
			}
			
			
			System.out.println("The winner is Player " + remote.gewonnen() + "!!!!!!");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			
		}

		/**
		
		 *  } catch (RemoteException e) { // TODO Auto-generated catch
		 * block e.printStackTrace(); }
		 **/

	}
}