import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
public class SchiffeVersenkenGui {
	private JPanel[] mainPanel;
	
	private JButton[][] button;
	private JPanel[] layout;
	
	/*
	 * 
	 */
	private JFrame frame;
	/*
	 * 
	 */
	private JPanel[] spielfeld1;
	/*
	 * 
	 */
	private JLabel[] spielfeld1text;
	/*
	 * 
	 */
	
	private JButton[][] spielfeld1Button;
	/*
	 * 
	 */
	private JPanel[] spielfeld1Anordnung;
	/*
	 * 
	 */
	private JPanel[] spielfeld2;
	/*
	 * 
	 */
	private JButton[][] spielfeld2Button;
	/*
	 * 
	 */
	private JPanel[] spielfeld2Anordnung;
	/*
	 * 
	 */
	private JPanel initSpielfeld1() {
		JPanel ret = new JPanel();
		spielfeld1Anordnung = new JPanel[11];
		spielfeld1text = new JLabel[20];
		spielfeld1Button = new JButton[10][10];
		for(int i=0;i<11;i++) {
			spielfeld1Anordnung[i] = new JPanel();
		}
		for(int i=0;i<20;i++) {
			spielfeld1text[i] = new JLabel();
		}
		for(int i=0;i<10;i++) {
			spielfeld1Anordnung[i].setLayout(
				new BoxLayout(
						spielfeld1Anordnung[i],BoxLayout.PAGE_AXIS));
			spielfeld1Anordnung[i].add(Box.createRigidArea(new Dimension(0,10)));
		}
		spielfeld1Anordnung[0].add(new JLabel("<html><br/></html>"));
		for(int i=11;i<20;i++) {
			spielfeld1text[i].setText("<html><br/>" + String.valueOf(i-10) + "</html>");
			spielfeld1Anordnung[0].add(spielfeld1text[i]);
		}
		for(int i=1;i<10;i++) {
			for(int j=0;j<10;j++) {
				if(j==0) {
					spielfeld1text[i].setText("    " + String.valueOf(i));
					spielfeld1Anordnung[i].add(spielfeld1text[i]);
				}
				spielfeld1Button[i][j] = new JButton("X");
				//spielfeld1Button[i][j].setPreferredSize(new Dimension(60, 60));
				spielfeld1Anordnung[i].add(spielfeld1Button[i][j]);
			}
		}
		
		
		ret.setLayout(new BoxLayout(ret,BoxLayout.X_AXIS));
		for(int i=0;i<10;i++) {
			ret.add(spielfeld1Anordnung[i],i);
		}
		return ret;
	}
	
	private JPanel initSpielfeld2() {
		JPanel ret = new JPanel();
		spielfeld2Anordnung = new JPanel[10];
		spielfeld2Button = new JButton[10][10];
		for(int i=0;i<10;i++) {
			spielfeld2Anordnung[i] = new JPanel();
		}
		for(int i=0;i<10;i++) {
			spielfeld2Anordnung[i].setLayout(
				new BoxLayout(
						spielfeld2Anordnung[i],BoxLayout.PAGE_AXIS));
			spielfeld2Anordnung[i].add(Box.createRigidArea(new Dimension(0,10)));
		}
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				spielfeld2Button[i][j] = new JButton("X");
				spielfeld2Anordnung[i].add(spielfeld2Button[i][j]);
			}
		}
		ret.setLayout(new BoxLayout(ret,BoxLayout.X_AXIS));
		for(int i=0;i<10;i++) {
			ret.add(spielfeld2Anordnung[i],i);
		}
		return ret;
	}
	/*
	 * 
	 */
	public SchiffeVersenkenGui() {
		button = new JButton[10][10];
		this.initWindow();
	}
	/*
	 * 
	 */
	private void initWindow() {
		this.frame = new JFrame("Test");
		
		//this.frame.setLayout(new BoxLayout(this.frame.getContentPane(),BoxLayout.X_AXIS));
		this.frame.setLayout(new FlowLayout());
		//this.frame.setSize(1000,500);
		
		//this.frame.add(initSpielfeld1());
		
		JPanel spielfeld = new JPanel();
		spielfeld.setLayout(new BorderLayout(10,0));
		spielfeld.add(new JSeparator(JSeparator.HORIZONTAL),BorderLayout.PAGE_START);
		spielfeld.add(initSpielfeld1(),BorderLayout.LINE_START);
		spielfeld.add(new JSeparator(JSeparator.VERTICAL),BorderLayout.CENTER);
		spielfeld.add(initSpielfeld2(),BorderLayout.LINE_END);
		this.frame.add(spielfeld);
		
		this.frame.pack();
		this.frame.setVisible(true); 
	}
}
