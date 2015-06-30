package client;


import java.net.*;
import java.rmi.*;
import java.rmi.server.*;
import java.util.*;

import remoteinterface.ChatException;
import remoteinterface.RMIClientInterface;
import remoteinterface.RMIServer3;

/**
 * Diese Klasse implementiert die Remote-Methoden,
 * die der Client dem Server anbietet.
 * @author Konrad Preiser
 * @version 23.06.2015
 */
public class RMIClientImpl extends UnicastRemoteObject implements
		RMIClientInterface {
	private RMIChat rmiChat;

	public RMIClientImpl(RMIChat rmiChat) throws RemoteException {
		this.rmiChat = rmiChat;
	}

	/**
	 * Implementierung der Methode getName() aus der Schnittstelle
	 * RMIClientInterface.
	 */
	public String getNameSC() {
		return rmiChat.getBenutzerName();
	}

	// Implementierung der Methode sendeNachricht() aus der Schnitt-
	// stelle RMIClientInterface. Der Server ruft sendeNachricht()
	// auf, um dem Client eine Chat-Nachricht mitzuteilen, die ein
	// anderer Chat-Teilnehmer eingegeben hat.
	public void sendeNachrichtSC(String msg) throws RemoteException {
		System.out.print(msg + "\nEingabe: ");
		// Ausgabe an das GUI leiten
		this.rmiChat.statusAusgabeImGUIThread(msg);
	}

	// Methode run() aus Schnittstelle Runnable implementieren.
	public void run_toter_Code() {
		RMIServer3 server = null;
		// Verbindung aufbauen

		// Anmelden und chatten
		try {
			// Ameldung am Chat-Server
			server.anmeldenCS(this);

			Scanner eingabe = new Scanner(System.in);
			String msg = null;
			while (true) {
				// Solange nicht "exit" eingegeben wird, bleibt
				// der Client angemeldet und kann mit anderen
				// Teilnehmern chatten
				msg = eingabe.nextLine();
				if (msg.equalsIgnoreCase("exit")) {
					break;
				}
				// server.se
			} // while
				// Die Endlosschleife wurde verlassen, weil der
				// Client sich abmelden will. Also muss die
				// Methode abmelden() aufgerufen werden.
			server.abmelden(this);
		} catch (ChatException e) {
			// Ein Fehler ist waehrend des Chats aufgetreten
			System.out.println(e.getMessage());
		} catch (RemoteException e) {
			// Wenn waehrend der Kommunikation ein Fehler auftritt
			System.out.println(e.getMessage());
		}
	}
}
