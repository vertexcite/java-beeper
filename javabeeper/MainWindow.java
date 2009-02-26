/*
 * Created on 22/10/2003
 */
package javabeeper;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainWindow extends JFrame {
  
  public static void main(String args[]) {
  	MainWindow theWindow = new MainWindow();
  }
  
  protected long snoozeDuration = 20 * 1000 * 60; // Twenty minutes.
  JButton snoozeButton = new JButton( "Snooze" );
  JPanel panel1 = new JPanel();
  JTextField snoozeTextField = new JTextField("20");
  private boolean showing = true;

    
  public MainWindow() {
  	
  	getContentPane().add( panel1, BorderLayout.NORTH );
  	panel1.setLayout( new GridLayout( 1, 2 ) );
  	panel1.add( snoozeButton );
  	panel1.add( snoozeTextField );
  	
  	snoozeButton.addActionListener( 
  	  new ActionListener() {

		public void actionPerformed(ActionEvent arg0) {
			snoozeClicked();
			
		}
  	  }
  	 );

  	 snoozeTextField.addActionListener(
  	   new ActionListener() {

		public void actionPerformed(ActionEvent arg0) {
	      snoozeDuration =(long)( Double.parseDouble( snoozeTextField.getText() ) * 1000 * 60 );
	      snoozeClicked();
		}
  	   	
  	   }
  	 );
  	 
    setDefaultCloseOperation( EXIT_ON_CLOSE );
    Dimension max = getMaximumSize();
    setBounds( 0, 0, max.width, max.height );
    show();
    showing = true;
    irritate();
  }

  private void irritate() {
  	while( showing ) {
  		final int oneMinute = 60 * 1000;
  		try {
			Thread.sleep( oneMinute );
		} catch (InterruptedException e) {
			// Do Nothing
		}
  		if(showing){
  			show();
  		}
  	}
  }

  protected void snoozeClicked() {
  	hide();
  	showing = false;
  	boolean snoozedByUser = true;
  	waitToShow();
  }

  private void waitToShow() {
  	new Thread() {
  		
  		public void run() {
  			try {
  				Thread.sleep( snoozeDuration );
  				
  				
  				// Beep
  				Toolkit.getDefaultToolkit().beep();     			
  				
  				show();
  				showing = true;
  				
  				irritate();
  				
  				
  			} catch (InterruptedException e) {
  				// Do nothing, don't care if interrupted.
  			}
  		}
  	}.start();
  }
}
