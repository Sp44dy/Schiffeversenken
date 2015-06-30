package remoteinterface;

import java.rmi.*;

/*
 * Diese Klasse enthaelt eine Exception, die
 * in verschiedenen Situationen instanziert
 * wird.
 * <br />
 * Hinweis: Laut "RMI-Bibel" sollte NICHT von RemoteException
 * abgeleitet werden, da sich sonst problematische Situationen
 * ergeben koennen.
 * 
 * @author Konrad Preiser
 * @version 23.06.2015
  */
public class ChatException extends Exception {

	/**
	 * Konstruktor. 
	 * @param msg
	 */
	public ChatException(String msg) {
		super(msg);
	}

} // ChatException

