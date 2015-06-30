package remoteinterface;

import java.rmi.*;

/*
 * Diese Schnittstelle enthaelt die Deklarationen
 * von mehreren Methoden, die ein Client bei dem
 * Server aufrufen kann.
 *
 * @author Konrad Preiser
 * @version 23.06.2015
 */
public interface RMIServer3 extends Remote {

	// Ein Client kann sich hiermit am Chat-Server
   // anmelden. Ist sein Nickname bereits vergeben,
   // so wird eine ChatException geworfen.
   public void anmeldenCS (RMIClientInterface client)
      throws RemoteException, ChatException;

   // Ein angemeldeter Client ruft diese Methode
   // auf, um eine Nachricht an alle Chat-Teilnehmer
   // zu senden. Der Server verteilt die Nachrichten
   // dann nach dem Publisher-Subscriber-Prinzip
   public void sendeNachrichtCS (
      RMIClientInterface client, String msg)
      throws RemoteException, ChatException;

   // Angemeldete Clients melden sich mit Aufruf
   // dieser Methode vom Chat-Server ab.
   public void abmelden (RMIClientInterface client)
      throws RemoteException, ChatException;

} // RMIServer3
