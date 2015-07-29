package com.schiffeversenken.gui;

import java.awt.*;
import java.awt.event.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JTextArea;

import com.schiffeversenken.interf.ClientInterface;
import com.schiffeversenken.interf.Constant;
import com.schiffeversenken.interf.ServerInterface;

/**
 * Die Klasse ist fuer die Auswertung der Server Daten und das Zeichnen der Gui
 * verantwortlich.
 * 
 * @author Benedict Kohls {@literal <bkohls91@gmail.com>}
 * @author Patrick Labisch {@literal <paul.florian09@gmail.com>}
 *
 */
@SuppressWarnings("serial")
public class Spielfeld2d extends Frame {
	/**
	 * Start X-Position fuer das Zeichnen
	 */
	protected int startX = 60;
	/**
	 * Start Y-Position fuer das Zeichnen
	 */
	protected int startY = 90;
	/**
	 * End Y-Position fuer das Zeichnen
	 */
	protected int endY = 390;
	/**
	 * End X-Position fuer das Zeichnen
	 */
	protected int endX = 360;
	/**
	 * Letzte X-Position der Maus
	 */
	protected int lastX = 0;
	/**
	 * Letzte Y-Position der Maus
	 */
	protected int lastY = 0;
	/**
	 * Anzahl der Spieler
	 */
	protected short spieler;
	/**
	 * Linkes Spielfeld als Array
	 */
	private int spielfeld1[][];
	/**
	 * Rechtes Spielfeld as Array
	 */
	private int spielfeld2[][];
	/**
	 * Spielzuege fuer das Linke Spielfeld
	 */
	private ArrayList<Point> spielzuege1;

	/**
	 * Nummer des Spielers
	 */
	private int spielerNummer;
	/**
	 * Erlauben iob Schiffe gesetzen werden duerfen
	 */
	public boolean setzeSchiffe;
	/**
	 * Die gesetzen Schiffe
	 */
	public ArrayList<Point> gesetzeSchiffe;
	/**
	 * Anzahl der gesetzen Schiffe
	 */
	private int anzahlschiffe;
	/**
	 * Erlauben ob der Spieler einen Spielzug machen darf
	 */
	public boolean allowSpielzug;
	/**
	 * Drehen beim Schiffe setzen
	 */
	private boolean drehen;

	/**
	 * Schiffe welche gesetz werden können
	 */
	private final int ships[] = new int[] { 5, 4, 3, 2, 2, 1, 1, 1 };
	/**
	 * TextArea fuer Chat Nachrichten
	 */
	private JTextArea chatArea = new JTextArea();
	/**
	 * Absenden-Button fuer den Chat
	 */
	private JButton chatSenden = new JButton("Abesenden");
	/**
	 * TextArea fuer das schreiben von Chat Nachrichten
	 */
	private JTextArea chatSendenArea = new JTextArea();

	/**
	 * Haelt die Instanz des Servers fuer die Kommunikation
	 */
	private ServerInterface server = null;

	/**
	 * Haelt die Instanz fuer den Client fuer die Kommunikation
	 */
	public ClientInterface client = null;
	/**
	 * Aktueller Status fuer die Ausgabe an den Client
	 */
	String status = null;

	/**
	 * Konstruktor fuer das Spielfeld. Dieser initalisiert alle Arrays und
	 * ArrayList-Variabeln, sowie erstellt alle nötigen Listener welche fuer die
	 * Gui benötigt werden.
	 *
	 * @throws NotBoundException  Weitergabe entsprechender Fehlermeldungen 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 */
	public Spielfeld2d() throws RemoteException, NotBoundException {
		// Konstruktor des Frames
		super("Schiffeversenken");
		// Spielfeld Array initalisieren
		spielfeld1 = new int[11][11];
		spielfeld2 = new int[11][11];
		// ArrayListen initalisieren
		spielzuege1 = new ArrayList<Point>();

		// Fenser auf 800x425 setzen
		setSize(1150, 425);

		allowSpielzug = false;
		gesetzeSchiffe = new ArrayList<Point>();

		// Gui zeigen
		setVisible(true);
		// Hover fuer das Spielfeld
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				paintSelection(e.getX(), e.getY());
			}
		});
		// Listener fuer die Drehung beim Schiffe setzen
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D'/** D-Taste **/
				) {
					// Wert fuer die drehen Variable aendern
					drehen = (drehen == false) ? true : false;
					// gui neumalen
					repaint();
				}
			}
		});
		// Maus Klick fuer das Schiffe setzen
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (allowSpielzug == true) {
					getMouseKlickAction(e.getX(), e.getY());
				} else if (setzeSchiffe == true) {
					setShips(e.getX(), e.getY());
				}
			}
		});
		// Listener fuer das Fenster schliessen
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
		//layout killen
		this.setLayout(null);
		
		//chat komponenten hinzufuegen
		this.add(chatSendenArea);
		this.add(chatSenden);
		this.add(chatArea);

		//Chat fenster fest setzen
		chatArea.setBounds(815, 90, 300, 250);
		chatArea.setBorder(BorderFactory.createLineBorder(Color.black));

		//Chat schreiben fest setzen
		chatSendenArea.setBounds(815, 360, 175, 30);
		chatSendenArea.setBorder(BorderFactory.createLineBorder(Color.black));

		// Button fest setzen
		chatSenden.setBounds(1000, 360, 120, 30);
		
		//Event Listener
		chatSenden.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ChatNachricht();
			}
		});
		if (status == null) {
			this.status = "WARTEN....";
		}

	}
	/**
	 * Verbindet den Client mit den Servver. Zusaetzlich wird der
	 * dem Cient eine Nummer zugewiesen.
	 * Sollte die Maximale Anzahl clients schon verbunden sein wird das
	 * Programm beendet.
	 * 
	 * @throws RemoteException  Weitergabe entsprechender Fehlermeldungen
	 * @throws NotBoundException  Weitergabe entsprechender Fehlermeldungen
	 */
	public void connect() throws RemoteException, NotBoundException {
		//RMI starten & Binden
		Registry registry = null;
		registry = LocateRegistry.getRegistry("127.0.0.1", Constant.RMI_PORT);
		server = (ServerInterface) registry.lookup(Constant.RMI_ID);
		server.login(client);
		System.out.println("[DEBUG] Spielernummer: " + spielerNummer);
		//Mehr als 2 Spieler sind nicht zulaessig
		if(spielerNummer==-1) {
			System.out.println("[DEBUG] ungueltige Spielernummer!");
			System.out.println("[DEBUG] Gute Nacht :(");
			System.exit(0);
		}

	}
	/**
	 * Wird vom Listener Aufgerufen und sendet eine Nachricht
	 * an den Server.
	 */
	private void ChatNachricht() {
		//Kein leerer Text
		if (this.chatSendenArea.getText() != null) {
			try {
				server.sendChatMessage("Spieler " + this.spielerNummer + ":"
						+ this.chatSendenArea.getText());
				this.chatSendenArea.setText("");
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Diese Funktion erlaubt es dem Client schiffe zu setzen
	 */
	public void startPlaceingShips() {
		System.out.println("[DEBUG] Schiffe setzen [START]");
		setzeSchiffe = true;
		//Die schiffe festlegen
		anzahlschiffe = ships.length;
	}

	/**
	 * Beendet das Schiffe setzen
	 */
	private void stopPlaceingShips() {
		System.out.println("[DEBUG] Schiffe setzen [ENDE]");
		this.setzeSchiffe = false;
	}

	/**
	 * Diese Funktion setzt Schiffe auf dem Spielfeld.
	 * 
	 * @param x
	 *            X-Mausposition
	 * @param y
	 *            Y-Mausposition
	 * @throws RemoteException
	 */
	private void setShips(int x, int y) {
		// Mausi innerhalb des 1. Spielfeldes?
		if (x > 450 && x < 750 && y > 90 && y < 390) {
			// Position auf dem Spielfeld ermitteln
			int posX = Math.floorDiv(x - 450, 30);
			int posY = Math.floorDiv(y - 90, 30);
			// Wie "lang" ist das Schiff
			int anzahl = ships[anzahlschiffe - 1];
			// kontrolle ob der Punkt wo das Schiff gesetzt wird
			// nicht bereits in Liste ist. Ansonsten funktion beeneden.
			// Einmal fuer die drehung (vertikal) und einmal ohne (horizontal)
			if (drehen) {
				if (posX + anzahl <= 10) {
					for (int l = 0; l < anzahl; l++) {
						// kontrolle ob nicht bereits in Liste
						if (gesetzeSchiffe.contains(new Point(posX + l, posY)))
							return;
					}
				} else {
					return;
				}
			} else {
				if (posY + anzahl <= 10) {
					for (int l = 0; l < anzahl; l++) {
						// kontrolle ob nicht bereits in Liste
						if (gesetzeSchiffe.contains(new Point(posX, posY + l)))
							return;
					}
				} else {
					return;
				}
			}

			// Wenn das Schiff gesetzt werden kann setze es.
			if (drehen) {
				if (posX + anzahl <= 10) {
					for (int l = 0; l < anzahl; l++) {
						gesetzeSchiffe.add(new Point(posX + l, posY));
					}
				}
			} else {
				if (posY + anzahl <= 10) {
					for (int l = 0; l < anzahl; l++) {
						gesetzeSchiffe.add(new Point(posX, posY + l));
					}
				}
			}
			// Gesetzte Schiffe reduzieren.
			anzahlschiffe--;
			// Wenn alle Schiffe gesetzt wurden dann "beende" das Schiffe
			// setzen.
			if (anzahlschiffe == 0) {
				stopPlaceingShips();
				try {
					//Dem Server die Schiffe mitteilen
					server.setShips(this.gesetzeSchiffe, this.spielerNummer);
					this.setStatus("Bitte warten...");
				} catch (RemoteException e) {
				
					e.printStackTrace();
				}
			}
			// Dannach neu malen damit der User die aenderungen gleich sieht!
			this.repaint();

		}
	}

	/**
	 * Listener fuer das erfassen des Mausklick 
	 * fuer das "schiessen". Sendet ggf. an den Server
	 * den "neuen" Spielzug.
	 * 
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 */

	public void getMouseKlickAction(int x, int y) {
		// Maus innerhalb des 1. Spielfeldes?
		if (x > 60 && x < 360 && y > 90 && y < 390) {
			//Feldposition ermitteln
			int posX = Math.floorDiv(x, 30) - 2;
			int posY = Math.floorDiv(y, 30) - 3;
			// Wenn dort nicht bereits geschossen wurde?
			if (!spielzuege1.contains(new Point(posX, posY))) {
				allowSpielzug = false;
				try {
					//Dem server mitteilen was sache ist
					server.doSpielzug(new Point(posX, posY), spielerNummer);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				//fuer den vergleich
				spielzuege1.add(new Point(posX, posY));
				this.repaint();
			}
		}
	}

	/**
	 * Setzt die Spielzuege fuer das Spielfeld 1
	 * @param p Spielfeld als 2D-Array
	 */
	public void setSpielzuege1(int p[][]) {
		this.spielfeld1 = p;
	}

	/**
	 * Setzt die Spielzuege fuer das Spielfeld 2
	 * @param p Spielfeld als 2D-Array
	 */
	public void setSpielzuege2(int p[][]) {
		this.spielfeld2 = p;
	}

	/**
	 * Diese Listender Funktion ist fuer das malen des
	 * Hover-Effektes fuer das Spielfeld zustaendig
	 * @param x
	 * @param y
	 */
	private void paintSelection(int x, int y) {
		// Sind wir in Spielfeld 1?
		if (x > 60 && x < 360 && y > 90 && y < 390) {
			//Positionen bestimmen
			int posX = Math.floorDiv(x, 30);
			int posY = Math.floorDiv(y, 30);
			// Hat sich die Mausposition innerhalb des Spielfeldes
			// geaendert?
			if (lastX != posX || posY != lastY) {
				// Wenn ja dann neu malen
				lastX = posX;
				lastY = posY;
				repaint();
			}
		// Sind wir in Spielfeld 2?
		} else if (x > 450 && x < 750 && y > 90 && y < 390) {
			//Das gleiche wie bei Spielfeld 1
			int posX = Math.floorDiv(x, 30);
			int posY = Math.floorDiv(y, 30);
			if (lastX != posX || posY != lastY) {
				lastX = posX;
				lastY = posY;
				repaint();
			}
		// auserhalb von Spielfeld
		// "verhindern das der Hover auf dem Feld bleibt"
		} else {
			//Position bestimmen
			int posX = Math.floorDiv(x, 30);
			int posY = Math.floorDiv(y, 30);
			
			if (lastX != posX || posY != lastY) {
				repaint();
			}
			if (lastX != 0 || lastY != 0) {
				lastX = 0;
				lastY = 0;
			}
		}
		//
	}

	/**
	 * Zeichnet das Spielfeld ein
	 * 
	 * @param g Graphics Object des UI
	 */
	public void paint(Graphics g) {

		// Horizontale Linie zwischen Spielfeld 1 & 2
		g.setColor(Color.black);
		g.drawLine(396, 20, 396, 415);
		g.drawLine(398, 20, 398, 415);
		g.drawLine(400, 20, 400, 415);
		g.drawLine(402, 20, 402, 415);
		g.drawLine(404, 20, 404, 415);

		// Horizontale Linie zwischen Spielfeld 2 & Chat
		g.setColor(Color.black);
		g.drawLine(780, 20, 780, 415);
		g.drawLine(782, 20, 782, 415);
		g.drawLine(784, 20, 784, 415);
		g.drawLine(786, 20, 786, 415);
		g.drawLine(788, 20, 788, 415);

		// Text fuer den Status
		g.setFont(new Font("Serif", Font.BOLD, 18));
		g.drawString("Dein Spielfeld", 115, 30);
		g.setColor(Color.red);
		g.drawString(this.status, 100, 55);
	

		// Chat Font
		g.setFont(new Font("Serif", Font.BOLD, 18));
		g.setColor(Color.red);
		g.drawString("Chat!", 900, 55);
		g.setFont(new Font("Serif", Font.BOLD, 12));
		g.setColor(Color.red);
		g.drawString("Du bist Spieler " + spielerNummer, 900, 80);

		g.setColor(Color.black);
		g.drawString("Dein Herausforderer", 500, 30);

		// Spielfeld 1 Beschriftung zeichnen
		for (int i = 30; i < 330; i = i + 30) {
			
			g.setFont(new Font("Serif", Font.BOLD, 14));
			if (i == 300) {
				g.drawString(Integer.toString(i / 30), startX + i - 25,
						startY - 5);
			} else {
				g.drawString(Integer.toString(i / 30), startX + i - 20,
						startY - 5);
			}
			g.drawString(String.valueOf((char) (i / 30 + 64)), startX - 15,
					startY + i - 10);
		}
		// Linien zeichnen
		for (int i = 0; i < 330; i = i + 30) {
			g.drawLine(startX + i, startY, startX + i, endY);
			g.drawLine(startX, startY + i, endX, startY + i);
		}
		// Hover effekte und treffer fuer Spiefeld 1
		for (int i = 0; i < 300; i = i + 30) {
			for (int j = 0; j < 300; j = j + 30) {
				// Koordinaten
				int x = i / 30;
				int y = j / 30;
				// Wenn keine Schiffe gesetzen werden
				if (!setzeSchiffe) {
					//Noch kein schuss auf dem Feld dann blauen Hover
					if (spielfeld1[x][y] == 0) {
						if (startX + i + 1 == 30 * lastX + 1
								&& startY + j + 1 == 30 * lastY + 1) {
							g.setColor(Color.blue);
							g.fillRect(30 * lastX + 1, 30 * lastY + 1, 29, 29);
							g.setColor(Color.white);
							g.drawLine(30 * lastX + 1 + 10,
									30 * lastY + 1 + 10, 30 * lastX + 1 + 20,
									lastY * 30 + 1 + 20);
							g.drawLine(30 * lastX + 1 + 20,
									30 * lastY + 1 + 10, 30 * lastX + 1 + 10,
									lastY * 30 + 1 + 20);
						}
					// geschossen aber kein Treffer
					} else if (spielfeld1[x][y] == 1) {
						// Wenn hover dann gray
						if (startX + i + 1 == 30 * lastX + 1
								&& startY + j + 1 == 30 * lastY + 1) {
							g.setColor(Color.gray);
							g.fillRect(30 * lastX + 1, 30 * lastY + 1, 29, 29);
						} else {
							//Kein Hover dann DarkGrey
							g.setColor(Color.darkGray);
							g.fillRect(startX + i + 1, startY + j + 1, 29, 29);
						}
					//Wenn treffer
					} else if (spielfeld1[x][y] == 2) {
						// wenn maus ueber feld dann Orange-Hover  
						if (startX + i + 1 == 30 * lastX + 1
								&& startY + j + 1 == 30 * lastY + 1) {
							g.setColor(Color.orange);
							g.fillRect(30 * lastX + 1, 30 * lastY + 1, 29, 29);
							g.setColor(Color.white);
							g.drawLine(30 * lastX + 1 + 10,
									30 * lastY + 1 + 10, 30 * lastX + 1 + 20,
									lastY * 30 + 1 + 20);
							g.drawLine(30 * lastX + 1 + 20,
									30 * lastY + 1 + 10, 30 * lastX + 1 + 10,
									lastY * 30 + 1 + 20);
						} else {
							// wenn maus ueber feld dann Roten-Hover  
							g.setColor(Color.red);
							g.fillRect(startX + i + 1, startY + j + 1, 29, 29);
							g.setColor(Color.white);
							g.drawLine(startX + i + 1 + 10,
									startY + j + 1 + 10, startX + i + 1 + 20,
									startY + j + 1 + 20);
							g.drawLine(startX + i + 1 + 20,
									startY + j + 1 + 10, startX + i + 1 + 10,
									startY + j + 1 + 20);
						}
					}
				}
			}
		}
		// Spielfeld2
		int lstartX = 450;
		int lendX = 750;
		g.setColor(Color.black);
		// Beschriftung des Feld 2
		for (int i = 30; i < 330; i = i + 30) {
			g.setFont(new Font("Serif", Font.BOLD, 14));
			if (i == 300) {
				g.drawString(Integer.toString(i / 30), lstartX + i - 25,
						startY - 5);
			} else {
				g.drawString(Integer.toString(i / 30), lstartX + i - 20,
						startY - 5);
			}
			g.drawString(String.valueOf((char) (i / 30 + 64)), lstartX - 15,
					startY + i - 10);
		}
		//Linen des 2. Spiefeldes
		for (int i = 0; i < 330; i = i + 30) {
			g.drawLine(lstartX + i, startY, lstartX + i, endY);
			g.drawLine(lstartX, startY + i, lendX, startY + i);
		}

		// Hover effekte
		for (int i = 0; i < 300; i = i + 30) {
			for (int j = 0; j < 300; j = j + 30) {
				int x = i / 30;
				int y = j / 30;
				//Fuers schiffe setzen
				if (gesetzeSchiffe.contains(new Point(x, y)) && setzeSchiffe) {
					//gesetzte schiffe schwart malen
					g.setColor(Color.black);
					g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
				//schiffe duerfen gesetzen werden
				} else if (setzeSchiffe) {
					// Hover effekt
					if (lstartX + i + 1 == 30 * lastX + 1
							&& startY + j + 1 == 30 * lastY + 1) {
						if (gesetzeSchiffe.contains(new Point(x, y))) {
							g.setColor(Color.red);
							g.fillRect(30 * lastX + 1, 30 * lastY + 1, 29, 29);
						} else {
							g.setColor(Color.blue);
							g.fillRect(30 * lastX + 1, 30 * lastY + 1, 29, 29);
						}
						int anzahl = ships[anzahlschiffe - 1];
						// Drehung in X-Richtug
						if (drehen) {
							// Wie lang ist das Schiff?
							for (int k = 0; k < anzahl; k++) {
								//Innerhalb des Spielfeldes Malen
								if (lstartX + i + 1 + k * 30 < lendX) {
									if (!gesetzeSchiffe.contains(new Point(x
											+ k, y)))
										g.setColor(Color.blue);
									else
										g.setColor(Color.red);
									g.fillRect(lstartX + i + 1 + k * 30, startY
											+ j + 1, 29, 29);
								}
							}
						// Drehung in Y-Richtug
						} else {
							// Wie lang ist das Schiff?
							for (int k = 0; k < anzahl; k++) {
								//Innerhalb des Spielfeldes Malen
								if (startY + k * 30 + j + 1 < endY) {
									if (!gesetzeSchiffe.contains(new Point(x, y
											+ k)))
										g.setColor(Color.blue);
									else
										g.setColor(Color.red);
									g.fillRect(lstartX + i + 1, startY + k * 30
											+ j + 1, 29, 29);
								}
							}
						}
					}
					// Wenn keine schiffe gesetzt werden
				} else {
					// Kein schuss auf das Feld = DarkGrey
					if (spielfeld2[x][y] == 0
							&& gesetzeSchiffe.contains(new Point(x, y))) {
						g.setColor(Color.darkGray);
						g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
					// Schuss = Gray
					} else if (spielfeld2[x][y] == 1) {
						g.setColor(Color.gray);
						g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
					//Treffer mit Roten-Kreuz
					} else if (spielfeld2[x][y] == 2) {
						g.setColor(Color.red);
						g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
						g.setColor(Color.black);
						g.drawLine(lstartX + i + 1 + 10, startY + j + 1 + 10,
								lstartX + i + 1 + 20, startY + j + 1 + 20);
						g.drawLine(lstartX + i + 1 + 20, startY + j + 1 + 10,
								lstartX + i + 1 + 10, startY + j + 1 + 20);
					}
				}
			}
		}
		// Chat-GUI am "Ende" zeichnen
		super.paint(g);
	}


	/**
	 * Erlaubt das Setzen von Schiffen
	 * 
	 */
	public void setzeSchiffe() {
		this.setzeSchiffe = true;
	}

	/**
	 * Fuegt eine Chat Nachricht hinzu
	 * 
	 * @param msg Chat-Nachricht
	 */
	public void setChatNachricht(String msg) {
		chatArea.append(msg);
	}

	/**
	 * Setzt einen Status in die GUI
	 * 
	 * @param status Status
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Setzt die Spielernummer
	 * 
	 * @param nummer Spielernummer
	 */
	public void setSpielernummer(int nummer) {
		this.spielerNummer = nummer;
	}
}