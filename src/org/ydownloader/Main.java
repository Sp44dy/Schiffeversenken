package org.ydownloader;

import java.awt.EventQueue;
import java.io.File;

import org.ydownloader.gui.GUI;
import org.ydownloader.youtube.Youtube;

public class Main {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/**EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		/**
		Youtube y = new Youtube();
		 // ex: http://www.youtube.com/watch?v=Nj6PFaDmp6c
        String url = "https://www.youtube.com/watch?v=lDzsO0oC3-M";
        // ex: /Users/axet/Downloads/
        String path = "/home/benedict/Downloads/youtube";
        y.run(url, new File(path));
        **/
	}
}
