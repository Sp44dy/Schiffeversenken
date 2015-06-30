package com.schiffeversenken.chat;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import com.schiffeversenken.interf.Constant;


public class client {

	public static void main(String[] args) {
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry("localhost",
					Constant.RMI_PORT);
			chatinterface remote = (chatinterface) registry
					.lookup(Constant.RMI_ID);
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			remote.send("Hallo 123");
			remote.send("Hallo duda");
			remote.send("Hallo duda");
			for (String s : remote.get()) {
				System.out.println(s);
			}
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

}
