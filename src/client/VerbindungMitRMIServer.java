package client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import remoteinterface.ChatException;
import remoteinterface.RMIServer3;

/**
 * Diese Klasse enthaelt Methoden zum Aufbauen der
 * Verbindung zum Server.
 * @author Konrad Preiser
 * @version 23.06.2015
 */
public class VerbindungMitRMIServer {

	private static final String HOST = "localhost";
	private static final String BIND_NAME = "RMI-Server3";

	/**
	 * Verbindung mit dem RMIServer3 aufbauen, also eine Remote-Referenz
	 * von der Registry abrufen.
	 * @return
	 */
	public RMIServer3 rmiVerbindungAufbauenCS() {
		RMIServer3 rmiServer3 = null;
		try {
			String bindURL = "rmi://" + HOST + "/" + BIND_NAME;
			rmiServer3 = (RMIServer3) Naming.lookup(bindURL);
		} catch (NotBoundException e) {
			// Wenn der Server nicht registriert ist ...
			System.out.println("Server ist nicht gebunden:\n" + e.getMessage());
		} catch (MalformedURLException e) {
			// Wenn die URL falsch angegeben wurde ...
			System.out.println("URL ungueltig:\n" + e.getMessage());
		} catch (RemoteException e) {
			// Wenn waehrend der Kommunikation ein Fehler auftritt
			System.out.println(e.getMessage());
		}
		return rmiServer3;
	} // rmiVerbindungAufbauenCS

	/**
	 * Den Benutzer bei dem RMI-Server anmelden
	 */
	public boolean benutzerBeiRMIServer3Anmelden(String benutzerName, 
			RMIClientImpl rmiClientImpl, RMIServer3 rmiServer3) {
		try {
			rmiServer3.anmeldenCS(rmiClientImpl);
		} catch(ChatException e) {
			System.err.println("ChatException: " + e.getStackTrace());
			return false;
		} catch(RemoteException e) {
			System.err.println("RemoteException: " + e.getStackTrace());
			return false;
		}
		return true;
	} // benutzerBeiRMIServer3Anmelden
	
	
	/**
	 * Textnachricht an den Server verschicken.
	 */
	public void sendeNachrichtCS(final String text, 
			final RMIClientImpl rmiClientImpl, final RMIServer3 rmiServer3) {
		if(rmiServer3 == null)
			return;
		Thread thread = new Thread( new Runnable() {
			public void run() {
				System.out.println("--sendeNachrichtCS - run (Start) --");
				try {
					rmiServer3.sendeNachrichtCS(rmiClientImpl, text);
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (ChatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("--sendeNachrichtCS - run (Ende) --");
			}
		});
		thread.start();
	}	

} // class VerbindungMitRMIServer

