package server;

import java.rmi.*;
import java.rmi.server.*;
import java.net.*;
import java.util.*;

import remoteinterface.ChatException;
import remoteinterface.RMIClientInterface;
import remoteinterface.RMIServer3;

/*
 * Diese Klasse enthaelt den Server.
 *
 * @author Konrad Preiser
 * @version 23.06.2015
 */
public class RMIServerImpl3 extends UnicastRemoteObject
   implements RMIServer3 {
	
   private static final String HOST = "localhost";
   private static final String SERVICE_NAME = "RMI-Server3";

   // Von alle angemeldeten Clients wird die
   // Referenz in diesem Vector<T>-Objekt gespeichert
   private Vector<RMIClientInterface> clients = null;

   public RMIServerImpl3() throws RemoteException
   {
      String bindURL = null;
      try
      {
         bindURL = "rmi://" + HOST + "/" + SERVICE_NAME;
         Naming.rebind (bindURL, this);

         clients = new Vector<RMIClientInterface>();
         System.out.println (
            "RMI-Server gebunden unter Namen: "+ SERVICE_NAME);
         System.out.println ("RMI-Server ist bereit ...");
      }
      catch (MalformedURLException e)
      {
         System.out.println ("Ungueltige URL: " + bindURL);
         System.out.println (e.getMessage());
         System.exit (1);
      }
   }

   // Die Methoden des Servers sind alle synchronisiert, weil diese
   // von mehreren Clients gleichzeitig aufgerufen werden koennen.

   // Methode zum Anmelden
   public synchronized void anmeldenCS (RMIClientInterface client)
      throws RemoteException, ChatException
   {
      String msg = null;
      // Pruefen, ob der Nickname schon vergeben ist
      if (angemeldet (client.getNameSC()))
      {
         msg = client.getNameSC() + " schon vergeben.";
         throw new ChatException (msg);
      }

      // Neuen Client dem Vector hinzufuegen
      clients.add (client);
      // Willkommensnachricht senden
      msg = "Willkommen auf RMIChat. " +
            "Zum Abmelden \"Exit\" eingeben.";
      client.sendeNachrichtSC (msg);

      // Alle angemeldeten Clients ueber 
      // neuen Chat-Teilnehmer informieren
      for (RMIClientInterface c : clients)
      {
         msg = "\n" + client.getNameSC() + " hat sich angemeldet.";
         c.sendeNachrichtSC (msg);
      }
      printStatus();
   }

   // Methode zum Senden einer Chat-Nachricht an alle Teilnehmer
   public synchronized void sendeNachrichtCS (
      RMIClientInterface client, String nachricht)
      throws RemoteException, ChatException
   {
      String msg = null;
      // Pruefen, ob der Client angemeldet ist
      if (!angemeldet (client.getNameSC()))
      {
         msg = "Client " + client.getNameSC() +
               " nicht angemeldet.";
         throw new ChatException (msg);
      }

      msg = client.getNameSC()+" schreibt: " + nachricht;

      // An alle angemeldeten Chat-Teilnehmer
      // die Nachricht des Senders publizieren
      for (RMIClientInterface c : clients)
      {
         System.out.println("An " + c.getNameSC() +":\n" + msg);
         c.sendeNachrichtSC ("\n" + msg);
      }
   }

   // Methoden zum Abmelden vom Chat-Server
   public synchronized void abmelden (RMIClientInterface client)
      throws RemoteException, ChatException
   {
      String msg = null;
      // Ist der Chat-Teilnehmer ueberhaupt angemeldet?
      if (!angemeldet (client.getNameSC()))
      {
         msg = "Client " + client.getNameSC() +
               " nicht angemeldet.";
         throw new ChatException (msg);
      }

      // Referenz auf den Chat-Client entfernen
      clients.remove (client);

      // Alle noch verbleibenden Chat-Teilnehmer informieren
      for (RMIClientInterface c : clients)
      {
         msg = "\n" + client.getNameSC() +
               " hat sich abgemeldet.";
         c.sendeNachrichtSC (msg);
      }
      printStatus();
   }

   // Ausgabe, welche Clients momentan angemeldet sind
   private void printStatus() throws RemoteException
   {
      Calendar cal = GregorianCalendar.getInstance();
      String msg = cal.get (Calendar.HOUR) + ":" +
                   cal.get (Calendar.MINUTE) + ":" +
                   cal.get (Calendar.SECOND) + " Uhr: ";

      msg += clients.size() + " User aktuell online: ";

      for (RMIClientInterface c : clients)
      {
         msg += c.getNameSC() + " ";
      }
      System.out.println (msg);
   }

   // Ueberpruefung, ob der uebergebene Nickname schon vergeben ist
   private boolean angemeldet (String name) throws RemoteException
   {
      for (RMIClientInterface c : clients)
      {
         if (name.equalsIgnoreCase (c.getNameSC()))
         {
            return true;
         }
      }
      return false;
   }

   public static void main (String[] args)
   {
      try
      {
         new RMIServerImpl3();
      }
      catch (RemoteException e)
      {
         System.out.println (e.getMessage());
         System.exit (1);
      }
   }
}
