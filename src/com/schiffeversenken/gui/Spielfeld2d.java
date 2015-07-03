package com.schiffeversenken.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
/**
 * Die Klasse ist für die Auswertung der Server Daten und 
 * das Zeichnen der Gui verantwortlich.
 * 
 * @author Benedict Kohls  {@literal <bkohls91@gmail.com>}
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
	 * Spielzüge für das Linke Spielfeld
	 */
	private ArrayList<Point> spielzuege1;
	/**
	 * Spielzüge für das Rechte Spielfeld
	 */
	private ArrayList<Point> spielzuege2;
	/**
	 * Nummer des Spielers
	 */
	private int spielerNummer;
	/**
	 * Erlauben iob Schiffe gesetzen werden dürfen
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
	 * Der aktuelle Spielzug
	 */
	private Point spielzug;
	/**
	 * Drehen beim Schiffe setzen
	 */
	private boolean drehen;
	/**
	 * Ob der Spielzug fertig ist
	 */
	private boolean spielzugdone;
	/**
	 * Schiffe welche gesetz werden können
	 */
	private final int ships[] = new int[] { 5, 4, 3, 2, 2, 1, 1, 1 };
	/**
	 * Ob Schiffe gesetzt werden
	 */
	private boolean settingships = false;
	/**
	 * Konstruktor für das Spielfeld.
	 * Dieser initalisiert alle Arrays und
	 * ArrayList-Variabeln, sowie erstellt alle nötigen
	 * Listener die für die Gui benötigt werden.
	 * @param none
	 * @return none
	 */
	public Spielfeld2d() {
		// Konstruktor des Frames
		super("Schiffeversenken");
		// Spielfeld Array initalisieren
		spielfeld1 = new int[11][11];
		spielfeld2 = new int[11][11];
		// ArrayListen initalisieren
		spielzuege1 = new ArrayList<Point>();
		spielzuege2= new ArrayList<Point>();
		// Fenser auf 800x425 setzen
		setSize(800, 425);
		
		allowSpielzug = false;
		spielzugdone = false;
		gesetzeSchiffe = new ArrayList<Point>();
		
		// Gui zeigen
		setVisible(true);
		// Hover für das Spielfeld
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				paintSelection(e.getX(), e.getY());
			}
		});
		// Listener für die Drehung beim Schiffe setzen
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D'/** D-Taste **/
				) {
					// Wert für die drehen Variable ändern
					drehen = (drehen == false) ? true : false;
					//gui neumalen
					repaint();
				}
			}
		});
		// Maus Klick für das Schiffe setzen
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
		// Listener für das Fenster schließen
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
		
	}
	/**
	 * Diese Funktion erlaubt es dem Client schiffe
	 * zu setzen
	 */
	public void startPlaceingShips() {
		setzeSchiffe = true;
		anzahlschiffe = ships.length;
	}

	/**
	 * Diese Funktion setzt Schiffe auf dem Spielfeld.
	 * 
	 * @param x X-Mausposition
	 * @param y Y-Mausposition
	 */
	private void setShips(int x, int y) {
		// Innerhalb des 1. Spielfeldes
		if (x > 450 && x < 750 && y > 90 && y < 390) {
			//Position auf dem Spielfeld ermitteln
			int posX = Math.floorDiv(x - 450, 30);
			int posY = Math.floorDiv(y - 90, 30);
			// Wie "lang" ist das Schiff
			int anzahl = ships[anzahlschiffe - 1];
			// kontrolle ob der Punkt wo das Schiff gesetzt wird
			// nicht bereits in Liste ist. Ansonsten funktion beeneden.
			// Einmal für die drehung (vertikal) und einmal ohne (horizontal)
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
			// Wenn alle Schiffe gesetzt wurden dann "beende" das Schiffe setzen.
			if (anzahlschiffe == 0) {
				setzeSchiffe = false;
			}
			// Dannach neu malen damit der User die änderungen gleich sieht!
			this.repaint();

		}
	}

	/**
	 * 
	 * 
	 * @param x
	 * @param y
	 */
	public void getMouseKlickAction(int x, int y) {
		if (x > 60 && x < 360 && y > 90 && y < 390) {
			int posX = Math.floorDiv(x, 30) - 2;
			int posY = Math.floorDiv(y, 30) - 3;
			if (!spielzuege1.contains(new Point(posX, posY))) {
				allowSpielzug = false;
				setSpielzug(new Point(posX, posY));
				setSpielzugdone(true);
				spielzuege1.add(new Point(posX, posY));
				this.repaint();
			}
		}
	}
	/**
	 * 
	 * @param p
	 */
	public void setSpielzuege1(int p[][]) {
		this.spielfeld1 = p;
	}
	/**
	 * 
	 * @param p
	 */
	public void setSpielzuege2(int p[][]) {
		this.spielfeld2 = p;
	}
	/**
	 * 
	 */
	public void spielzugeInArray() {
		/**
		 * spielfeld1 = new int[11][11];
		 * 
		 * spielfeld2 = new int[11][11]; for (Point p : spielzuege1) {
		 * spielfeld1[p.x][p.y] = 1; } for (Point p : spielzuege2) {
		 * spielfeld2[p.x][p.y] = 1; }
		 **/
	}
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void paintSelection(int x, int y) {
		if (x > 60 && x < 360 && y > 90 && y < 390) {
			int posX = Math.floorDiv(x, 30);
			int posY = Math.floorDiv(y, 30);
			if (lastX != posX || posY != lastY) {
				lastX = posX;
				lastY = posY;
				repaint();
			}
			// Spielfeld 2 hover
		} else if (x > 450 && x < 750 && y > 90 && y < 390) {
			int posX = Math.floorDiv(x, 30);
			int posY = Math.floorDiv(y, 30);
			if (lastX != posX || posY != lastY) {
				lastX = posX;
				lastY = posY;
				repaint();
			}
			// auserhalb von Spielfeld
		} else {
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
	/*
	 * (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		spielzugeInArray();
		// Beschriftungen etc
		g.setColor(Color.black);
		g.drawLine(396, 20, 396, 415);
		g.drawLine(398, 20, 398, 415);
		g.drawLine(400, 20, 400, 415);
		g.drawLine(402, 20, 402, 415);
		g.drawLine(404, 20, 404, 415);
		g.setFont(new Font("Serif", Font.BOLD, 18));
		g.drawString("Dein Spielfeld", 115, 30);
		if (setzeSchiffe == true) {
			g.setColor(Color.red);
			g.drawString("Setze deine Schiffe!", 100, 55);
		} else {
			if (allowSpielzug == true) {
				g.setColor(Color.red);
				g.drawString("Du bist dran!", 100, 55);
			} else {
				g.setColor(Color.red);
				g.drawString("Dein Herausforderer ist am Zug!", 90, 55);
			}
		}
		g.setColor(Color.black);
		g.drawString("Dein Herausforderer", 500, 30);

		// Spielfeld 1
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
		/*
		 * Lienien
		 */
		for (int i = 0; i < 330; i = i + 30) {
			g.drawLine(startX + i, startY, startX + i, endY);
			g.drawLine(startX, startY + i, endX, startY + i);
		}
		/**
		 * 
		 */
		for (int i = 0; i < 300; i = i + 30) {
			for (int j = 0; j < 300; j = j + 30) {
				/**
				 * Koordinaten
				 */
				int x = i / 30;
				int y = j / 30;
				/**
				 * Leeres Feld
				 */
				if (!setzeSchiffe) {
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
					} else if (spielfeld1[x][y] == 1) {
						if (startX + i + 1 == 30 * lastX + 1
								&& startY + j + 1 == 30 * lastY + 1) {
							g.setColor(Color.gray);
							g.fillRect(30 * lastX + 1, 30 * lastY + 1, 29, 29);
						} else {
							g.setColor(Color.darkGray);
							g.fillRect(startX + i + 1, startY + j + 1, 29, 29);
						}

					} else if (spielfeld1[x][y] == 2) {
						if (startX + i + 1 == 30 * lastX + 1
								&& startY + j + 1 == 30 * lastY + 1) {
							g.setColor(Color.orange);
							g.fillRect(30 * lastX + 1, 30 * lastY + 1, 29, 29);
							g.setColor(Color.white);
							g.drawLine(30 * lastX + 1 + 10,	30 * lastY + 1 + 10, 30 * lastX + 1 + 20,lastY * 30 + 1 + 20);
							g.drawLine(30 * lastX + 1 + 20,	30 * lastY + 1 + 10, 30 * lastX + 1 + 10,lastY * 30 + 1 + 20);
						} else {
							g.setColor(Color.red);
							g.fillRect(startX + i + 1, startY + j + 1, 29, 29);
							g.setColor(Color.white);
							g.drawLine(startX + i + 1 + 10, startY + j + 1 + 10, startX	+ i + 1 + 20, startY + j + 1 + 20);
							g.drawLine(startX + i + 1 + 20, startY + j + 1 + 10, startX + i + 1 + 10, startY + j + 1 + 20);
						}
					}
				}
			}
		}
		// Spielfeld2
		int lstartX = 450;
		int lendX = 750;
		g.setColor(Color.black);
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
		for (int i = 0; i < 330; i = i + 30) {
			g.drawLine(lstartX + i, startY, lstartX + i, endY);
			g.drawLine(lstartX, startY + i, lendX, startY + i);
		}
		for (int i = 0; i < 300; i = i + 30) {
			for (int j = 0; j < 300; j = j + 30) {
				int x = i / 30;
				int y = j / 30;
				/**
				 * fürs schiffe setzen!!!
				 * 
				 **/
				if (gesetzeSchiffe.contains(new Point(x, y)) && setzeSchiffe) {
					g.setColor(Color.black);
					g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
				} else if (setzeSchiffe) {
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
						// in x
						if (drehen) {
							for (int k = 0; k < anzahl; k++) {
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
							// in y
						} else {
							for (int k = 0; k < anzahl; k++) {
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
					continue;
				} else {
					if (spielfeld2[x][y] == 0
							&& gesetzeSchiffe.contains(new Point(x, y))) {
						g.setColor(Color.darkGray);
						g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
					} else if (spielfeld2[x][y] == 1) {
						g.setColor(Color.gray);
						g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
					} else if (spielfeld2[x][y] == 2) {
						g.setColor(Color.red);
						g.fillRect(lstartX + i + 1, startY + j + 1, 29, 29);
						g.setColor(Color.black);
						g.drawLine(lstartX + i + 1 + 10, startY + j + 1 + 10, lstartX	+ i + 1 + 20, startY + j + 1 + 20);
						g.drawLine(lstartX + i + 1 + 20, startY + j + 1 + 10, lstartX + i + 1 + 10, startY + j + 1 + 20);
					}
				}
			}
		}

	}

	
	/**
	 * 
	 * @return ArrayList<Point>
	 */
	public ArrayList<Point> getSpielzuege2() {
		return spielzuege2;
	}
	/**
	 * 
	 * @param spielzuege2
	 */
	public void setSpielzuege2(ArrayList<Point> spielzuege2) {
		this.spielzuege2 = spielzuege2;
	}
	/**
	 * 
	 * @return
	 */
	public int getSpielerNummer() {
		return spielerNummer;
	}
	/**
	 * 
	 * @param spielerNummer
	 */
	public void setSpielerNummer(int spielerNummer) {
		this.spielerNummer = spielerNummer;
	}
	/**
	 * 
	 * @return
	 */
	public Point getSpielzug() {
		return spielzug;
	}
	/**
	 * 
	 * @param spielzug
	 */
	public void setSpielzug(Point spielzug) {
		this.spielzug = spielzug;
	}
	/**
	 * 
	 * @return boolean
	 */
	public boolean isSpielzugdone() {
		return spielzugdone;
	}
	/**
	 * 
	 * @param spielzugdone
	 */
	public void setSpielzugdone(boolean spielzugdone) {
		this.spielzugdone = spielzugdone;
	}
	/**
	 * 
	 * @return boolean 
	 */
	public boolean isSettingships() {
		return settingships;
	}
	/**
	 * 
	 * @param settingships
	 * @return void
	 */
	public void setSettingships(boolean settingships) {
		this.settingships = settingships;
	}
}
