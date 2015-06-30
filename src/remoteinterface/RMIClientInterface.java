package remoteinterface;

import java.rmi.*;

/*
 * Diese Remote-Schnittstelle enthaelt verschiedene
 * Methodendeklarationen von Methoden, die der Server
 * bei einem Client aufrufen kann.
 *
 * @author Konrad Preiser
 * @version 23.06.2015
 */
public interface RMIClientInterface extends Remote {
	
   // Der Server ruft diese Methode auf, um die eingegangenen
   // Chat-Nachrichten an die Clients zu publizieren.
   void sendeNachrichtSC (String msg) throws RemoteException;

   // Gibt den Namen des Clients zurueck
   public String getNameSC() throws RemoteException;
}
