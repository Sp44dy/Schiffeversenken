package com.schiffeversenken.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.schiffeversenken.interf.*;

/**
 * Main Klasse für den Server. Hier wird RMI gestartet und an die Serverimpl. 
 * gebunden.
 * 
 * @author Benedict Kohls  {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 *
 */
public class Server {
	/**
	 * Hauptklasse für den Server.
	 * Registriert RMI
	 * @param args
	 */
	public static void main(String[] args)  {
		// Server starten ggf. Exception fangen
		try {
			ServerImpl impl = new ServerImpl();
			Registry regestry = LocateRegistry.createRegistry(Constant.RMI_PORT);
			regestry.bind(Constant.RMI_ID, impl);
			System.out.println("[DEBUG] Server gestartet");
		} catch (RemoteException e) {	
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		
	}
}
