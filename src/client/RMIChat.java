package client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;

import remoteinterface.ChatException;
import remoteinterface.RMIServer3;

/**
 * Das ist die Einstiegsklasse in das Client-Programm.
 * 
 * @author Konrad Preiser
 * @version 23.06.2015
 */
public class RMIChat extends JPanel implements ActionListener {

	/**
	 * Name der Anwendung, der in der Titelleiste angezeigt wird.
	 */
	private static String nameDerAnwendung = "Chat Client mit GUI";
	/**
	 * Beschriftung des Knopfes "chatTextSendenBtn".
	 */
	private String chatTextSendenBtnText = "Nachricht absenden";
	/**
	 * Beschriftung des Knopfes "verbindeMitServerBtn".
	 */
	private String verbindeMitServerBtnText = "Verbinde mit Server";
	/**
	 * Anzahl der Zeilen im Chat-Fenster.
	 */
	private int chatAllenzeilen = 15;
	/**
	 * Anzahl der Spalten im Chat-Fenster.
	 */
	private int chatAllenspalten = 80;
	/**
	 * Textfeld, in dem alle Chat-Nachrichten angezeigt werden. Wir waehlen hier
	 * JTextPane, damit wir spaeter noch z.B. die eigenen Nachrichten
	 * hervorheben koennen.
	 */
	private JTextPane chatAlleTextPane; // ### etwas komplizierter ...
	private JTextArea chatAlleTextArea; // ### vorlaeufig....
	/**
	 * JScrollPane, die dafuer sorgt, dass das Textfenster scrollbar
	 * wird.
	 */
	private JScrollPane chatAlleTextAreaScrollPane;
	/**
	 * Texteingabezeile, in die der Benutzer seine Chat-Nachricht eingeben kann.
	 */
	private JTextField chatSelbstTextField;
	/**
	 * Knopf, mit dem der Benutzer seine Chat-Nachricht absenden kann.
	 */
	private JButton chatTextSendenBtn;
	/**
	 * Knopf zum Verbinden mit dem Server.
	 */
	private JButton verbindeMitServerBtn;
	
	/**
	 * Referenz auf das Remote-Objekt des Servers RMIServer3, welches
	 * von der RMIRegistry erhalten wird.
	 */
	RMIServer3 rmiServer3;
	/**
	 * Ein Objekt der Verbindungsklasse zum Verbinden mit dem
	 * RMI-Server.
	 */
	VerbindungMitRMIServer verbindungMitRMIServer;
	/**
	 * Ein Objekt der Klasse, die das lokale Client-Remote-Interface
	 * fuer Callbacks von Server implementiert.
	 */
	RMIClientImpl rmiClientImpl;
	/**
	 * Benutzername (zuerst per Kommandozeilenparameter, dann
	 * auch per Eingabefeld im GUI).
	 */
	static String benutzerName;

	/**
	 * Konstruktor fuer unsere aktuelle Klasse; hierin werden die einzelnen
	 * Methoden gerufen, die die GUI-Komponenten erzeugen.
	 */
	private RMIChat()  {
		// Kommunikationsklasse(n) instanzieren
		this.verbindungMitRMIServer = new VerbindungMitRMIServer();
		// Implementierung des Client-Interface instanzieren
		try {
			this.rmiClientImpl = new RMIClientImpl(this);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// GUI aufbauen
		BorderLayout borderLayout = new BorderLayout();
		this.setLayout(borderLayout);
		this.verbindeMitServerBtn = new JButton(this.verbindeMitServerBtnText);
		this.verbindeMitServerBtn.addActionListener(this);
		this.add(this.verbindeMitServerBtn, BorderLayout.WEST);
		
		this.chatAlleTextArea = new JTextArea(this.chatAllenzeilen,
				this.chatAllenspalten);
		this.chatAlleTextArea.setEditable(false);		
		chatAlleTextAreaScrollPane = new  JScrollPane(this.chatAlleTextArea); 
		this.add(this.chatAlleTextAreaScrollPane, BorderLayout.NORTH);
		
		this.chatSelbstTextField = new JTextField();
		this.chatSelbstTextField.addActionListener(this);		
		this.add(this.chatSelbstTextField, BorderLayout.CENTER);
		
		this.chatTextSendenBtn = new JButton(this.chatTextSendenBtnText);
		this.chatTextSendenBtn.addActionListener(this);
		this.add(this.chatTextSendenBtn, BorderLayout.EAST);
	} // Konstruktor

	/**
	 * Hauptprogramm.
	 * 
	 * @param args
	 *            Gewuenschter Benutzername.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.out.println("Aufruf: RMIChat <Nickname>");
			System.exit(1); // ### Das ist zu hart; der Name sollte auch
							// interaktiv setzbar sein.
		}
		// ### Spaeter noch cleverer!!! 
		benutzerName = args[0];

		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		System.out.println("main(): Threadname: "
				+ Thread.currentThread().getName());
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				System.out.println("main(): new Runnable() - Methode run: "
						+ Thread.currentThread().getName());
				createAndShowGUI();
			}
		});
		System.out.println("main(): nach invokeLater: "
				+ Thread.currentThread().getName());
		/*
		 * try { // Neuen Thread erzeugen Thread t = new Thread(new
		 * RMIClientImpl(args[0]));
		 * 
		 * // starten t.start();
		 * 
		 * // und warten, bis der Thread zu Ende gelaufen ist t.join();
		 * System.exit(0); } catch (Exception e) {
		 * System.out.println(e.getMessage()); }
		 */
	} // main

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event dispatch thread. Diese Methode wird in den
	 * Beispielen von Sun stets verwendet. Der Uebergang in die
	 * objektorientierte Welt erfolgt im Konstruktor. Der Konstruktor wird in
	 * den Sun-Beispielen zum Erzeugen der Oberflaechen-Elemente genutzt.
	 */
	private static void createAndShowGUI() {
		// Create and set up the window.
		JFrame frame = new JFrame(nameDerAnwendung);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Add content to the window.
		RMIChat rmiChat = new RMIChat();
		frame.add(rmiChat);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * ActionListener fuer die Tastendruecke auf den Knopf chatTextSendenBtn.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.verbindeMitServerBtn) {
				Thread thread = new Thread(new Runnable() {
					public void run() {
						// Die Remote-Referenz auf den Server abrufen ...
						statusAusgabeImGUIThread(
								"Verbindung zum Server wird aufgebaut ...");
						System.out.println("actionPerformed(): new Runnable() - Methode run: "
								+ Thread.currentThread().getName());
						rmiServer3 = verbindungMitRMIServer.rmiVerbindungAufbauenCS();
						if(rmiServer3 == null) {
							statusAusgabeImGUIThread(
									"Verbindungsaufbau zum Server misslungen!");
							return;
						}
						// Jetzt noch den Benutzer registrieren ...
						statusAusgabeImGUIThread(
								"Verbindung ist gelungen, Benutzer wird registriert ...");
						if (verbindungMitRMIServer.benutzerBeiRMIServer3Anmelden(benutzerName, 
								rmiClientImpl, rmiServer3))
							statusAusgabeImGUIThread(
									"Registrierung ist gelungen!");
						else
							statusAusgabeImGUIThread(
									"Registrierung ist misslungen! Benutzername vielleicht schon vorhanden?");
					} // run
				});
				thread.start();
		} else if (e.getSource() == this.chatTextSendenBtn
				/* || e.getSource() == this.chatSelbstTextField */) {
			// Die eingegebene Chat-Nachricht zum Server absenden
			String text = chatSelbstTextField.getText();
			System.out.println("text = " + text);
			chatSelbstTextField.setText("");
			verbindungMitRMIServer.sendeNachrichtCS(text, this.rmiClientImpl,
					this.rmiServer3);
		} else
			System.out.println("Noch keine Action implementiert!");
	} // actionPerformed
	
	/**
	 * Diese Methode sorgt dafuer, dass Statusmeldungen im GUI-Thread
	 * ausgefuehrt werden.
	 */
	public void statusAusgabeImGUIThread(String text) {
		final String finalText = text;
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					System.out.println("statusAusgabeImGUIThread(): Threadname: "
							+ Thread.currentThread().getName());
					chatAlleTextArea.setText(chatAlleTextArea.getText() + finalText + "\n");
				}
			});
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} // statusAusgabeImGUIThread

	/**
	 * get-Methode fuer den Benutzernamen.
	 */
	public String getBenutzerName() {
		return this.benutzerName;
	}
} // class RMIChat
