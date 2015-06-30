package com.schiffeversenken.chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;


public class serverimpl extends UnicastRemoteObject implements chatinterface {
	protected serverimpl() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<String> text = new ArrayList<String>();
	@Override
	public void send(String in)  throws RemoteException{
		text.add(in);
		
	}

	@Override
	public ArrayList<String> get()  throws RemoteException{
		return text;
	}
}
