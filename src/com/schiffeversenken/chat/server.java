package com.schiffeversenken.chat;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.schiffeversenken.interf.Constant;



public class server {
	
		public static void main(String[] args) throws RemoteException, AlreadyBoundException {
			serverimpl impl = new serverimpl();
			// Instanz des Chat servers
			Registry regestry = LocateRegistry.createRegistry(Constant.RMI_PORT);
			regestry.bind(Constant.RMI_ID, impl);
			System.out.println("Started!");
		}
}
