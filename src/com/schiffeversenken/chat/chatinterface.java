package com.schiffeversenken.chat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface chatinterface  extends Remote{
	public void send(String in)  throws RemoteException;
	public ArrayList<String> get()  throws RemoteException;
}
