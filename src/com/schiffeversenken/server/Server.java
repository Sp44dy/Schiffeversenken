package com.schiffeversenken.server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.schiffeversenken.interf.*;
public class Server {
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		ServerImpl impl = new ServerImpl();
		Registry regestry = LocateRegistry.createRegistry(Constant.RMI_PORT);
		regestry.bind(Constant.RMI_ID, impl);
		System.out.println("Started!");
	}
}
