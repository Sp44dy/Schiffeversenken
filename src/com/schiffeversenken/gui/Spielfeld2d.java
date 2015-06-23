package com.schiffeversenken.gui;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Spielfeld2d extends Frame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Für die Berechnung des Spielfeldes
	 */
	protected int startX = 60;
	protected int startY = 90;
	protected int endY = 390;
	protected int endX = 360;
	protected int lastX = 0;
	protected int lastY = 0;
	/**
	 * 
	 */
	protected short spieler;
	/**
	 * Spielfeld als Array
	 */
	private int spielfeld1[][];
	private int spielfeld2[][];
	/**
	 * 
	 */
	private ArrayList<Point> spielzuege1;
	private ArrayList<Point> spielzuege2;
	private int spielerNummer;
	public boolean setzeSchiffe;
	public ArrayList<Point> gesetzeSchiffe;
	private int anzahlschiffe;
	public boolean allowSpielzug;
	private Point spielzug;
	private boolean drehen;
	private boolean spielzugdone;
	private int ships[] = new int[] { 5, 4, 3, 2, 2, 1, 1, 1 };
	private boolean settingships = false;

	public Spielfeld2d() {

		super("Schiffeversenken");
		// super.paintComponents(this.getGraphics());
		spielfeld1 = new int[11][11];
		spielfeld2 = new int[11][11];
		spielzuege1 = new ArrayList<Point>();
		setSpielzuege2(new ArrayList<Point>());
		// fakeZuege();
		// Set the size for this frame.
		setSize(800, 425);
		allowSpielzug = false;
		setSpielzugdone(false);
		gesetzeSchiffe = new ArrayList<Point>();
		// We need to turn on the visibility of our frame
		// by setting the Visible parameter to true.
		setVisible(true);
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				paintSelection(e.getX(), e.getY());
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == 'd' || e.getKeyChar() == 'D'/** D-Taste **/
				) {
					drehen = (drehen == false) ? true : false;
					repaint();
				}
			}
		});
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
		// Now, we want to be sure we properly dispose of resources
		// this frame is using when the window is closed. We use
		// an anonymous inner class adapter for this.
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});

		paintSpielfeld();
		startPlaceingShips();
	}

	public void startPlaceingShips() {
		setzeSchiffe = true;
		anzahlschiffe = 8;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void setShips(int x, int y) {
		if (x > 450 && x < 750 && y > 90 && y < 390) {
			int posX = Math.floorDiv(x - 450, 30);
			int posY = Math.floorDiv(y - 90, 30);
			int anzahl = ships[anzahlschiffe - 1];
			System.out.println(anzahl);
			// kontrolle ob nicht bereits in Liste
			if (drehen) {
				if (posX + anzahl <= 10) {
					for (int l = 0; l < anzahl; l++) {
						if (gesetzeSchiffe.contains(new Point(posX + l, posY)))
							return;
					}
				} else {
					return;
				}
			} else {
				if (posY + anzahl <= 10) {
					for (int l = 0; l < anzahl; l++) {
						if (gesetzeSchiffe.contains(new Point(posX, posY + l)))
							return;
					}
				} else {
					return;
				}
			}
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
			anzahlschiffe--;
			if (anzahlschiffe == 0) {
				setzeSchiffe = false;
			}
			this.repaint();

		}
	}

	/**
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
				// System.out.println("added");
				this.repaint();
				// super.paintComponents(this.getGraphics());
			}
			// System.out.println(posX + "=" + posY);
			// Spielfeld 2 hover
		}
	}

	public void setSpielzuege1(int p[][]) {
		this.spielfeld1 = p;
	}

	public void setSpielzuege2(int p[][]) {
		this.spielfeld2 = p;
	}

	public void spielzugeInArray() {
		/**
		 * spielfeld1 = new int[11][11];
		 * 
		 * spielfeld2 = new int[11][11]; for (Point p : spielzuege1) {
		 * spielfeld1[p.x][p.y] = 1; } for (Point p : spielzuege2) {
		 * spielfeld2[p.x][p.y] = 1; }
		 **/
	}

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

	public void paintSpielfeld() {

	}

	public ArrayList<Point> getSpielzuege2() {
		return spielzuege2;
	}

	public void setSpielzuege2(ArrayList<Point> spielzuege2) {
		this.spielzuege2 = spielzuege2;
	}

	public int getSpielerNummer() {
		return spielerNummer;
	}

	public void setSpielerNummer(int spielerNummer) {
		this.spielerNummer = spielerNummer;
	}

	public Point getSpielzug() {
		return spielzug;
	}

	public void setSpielzug(Point spielzug) {
		this.spielzug = spielzug;
	}

	public boolean isSpielzugdone() {
		return spielzugdone;
	}

	public void setSpielzugdone(boolean spielzugdone) {
		this.spielzugdone = spielzugdone;
	}

	public boolean isSettingships() {
		return settingships;
	}

	public void setSettingships(boolean settingships) {
		this.settingships = settingships;
	}
}
