package javabeeper;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 *
 * @author Randall Britten
 */
public class AlertWindow extends javax.swing.JFrame implements SnoozeObserver {

    private SnoozeController snoozeController;

    /**
     * Creates new form AlertWindow
     */
    public AlertWindow() {
        initComponents();
        setSpaceAsActionForButtons();
        getRootPane().setDefaultButton(snooze);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        snooze = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        snoozeTimeHours = new javax.swing.JSpinner();
        snoozeTimeMinutes = new javax.swing.JSpinner();
        snoozeTimeSeconds = new javax.swing.JSpinner();
        soundEnabled = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        arbitraryMessage = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Restart timer");

        snooze.setText("Snooze");
        snooze.setFocusCycleRoot(true);
        snooze.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                snoozeActionPerformed(evt);
            }
        });

        jLabel1.setText("Snooze time (hh:mm:ss)");

        snoozeTimeHours.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), null, Long.valueOf(1L)));

        snoozeTimeMinutes.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(20L), Long.valueOf(0L), null, Long.valueOf(1L)));

        snoozeTimeSeconds.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), null, Long.valueOf(1L)));

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(snoozeTimeHours, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(snoozeTimeMinutes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(snoozeTimeSeconds, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 58, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(snoozeTimeHours, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(snoozeTimeMinutes, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(snoozeTimeSeconds, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        soundEnabled.setSelected(true);
        soundEnabled.setText("Enable audio. (Note: if disabled, then resetting snooze will enable audio again)");
        soundEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundEnabledActionPerformed(evt);
            }
        });

        jLabel2.setText("Arbitrary text message:");

        arbitraryMessage.setText("You can type anything you want here, like a note to yourself for later.");
        arbitraryMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                arbitraryMessageActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(soundEnabled, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jLabel2)
                    .add(snooze)
                    .add(arbitraryMessage))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(soundEnabled)
                .add(35, 35, 35)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(arbitraryMessage, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 75, Short.MAX_VALUE)
                .add(snooze)
                .add(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void soundEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundEnabledActionPerformed
        snoozeController.setSoundEnabled(soundEnabled.isSelected());
    }//GEN-LAST:event_soundEnabledActionPerformed

    private void snoozeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_snoozeActionPerformed
        snoozeAction();
    }//GEN-LAST:event_snoozeActionPerformed

    private void arbitraryMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_arbitraryMessageActionPerformed
        snoozeAction();
    }//GEN-LAST:event_arbitraryMessageActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField arbitraryMessage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton snooze;
    private javax.swing.JSpinner snoozeTimeHours;
    private javax.swing.JSpinner snoozeTimeMinutes;
    private javax.swing.JSpinner snoozeTimeSeconds;
    private javax.swing.JCheckBox soundEnabled;
    // End of variables declaration//GEN-END:variables

    void setController(SnoozeController controller) {
        snoozeController = controller;
    }
    
    void beepAndShow() {
        if(snoozeController.isSoundEnabled()) {
                Toolkit.getDefaultToolkit().beep();
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);

                
                int state = getExtendedState();
                state &= ~JFrame.ICONIFIED;
                state |= javax.swing.JFrame.MAXIMIZED_BOTH;
                setExtendedState(state);
                setAlwaysOnTop(true);
                toFront();
                snooze.requestFocus();
                snooze.requestFocusInWindow();
                setAlwaysOnTop(false);



                if(snoozeController.useFullScreenActive()) {
                    GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(AlertWindow.this);
                }
            }
        });                

    }

    @Override
    public void setSoundEnabled(boolean soundEnabled) {
            this.soundEnabled.setSelected(soundEnabled);

    }

    @Override
    public void setSnoozeDurationAndDoSnooze(double snoozeDurationMinutes) {
        setSnoozeDuration(snoozeDurationMinutes);
        toBack();
    }
    
    public void snoozeAction() {
        long hours = (long) snoozeTimeHours.getValue();
        long minutes = (long) snoozeTimeMinutes.getValue();
        long seconds = (long) snoozeTimeSeconds.getValue();

        snoozeController.restartSnoozing(new Utilities.HoursMinutesSeconds(hours, minutes, seconds));
    }

    // @todo: not sure if this is doing anything useful at the moment.
    // @todo: If this is doing anything, it seems to be application wide, move elsewhere.
    private static void setSpaceAsActionForButtons() {
        InputMap im = (InputMap) UIManager.getDefaults().get("Button.focusInputMap");
        Object pressedAction = im.get(KeyStroke.getKeyStroke("pressed SPACE"));
        Object releasedAction = im.get(KeyStroke.getKeyStroke("released SPACE"));

        im.put(KeyStroke.getKeyStroke("pressed ENTER"), pressedAction);
        im.put(KeyStroke.getKeyStroke("released ENTER"), releasedAction);
    }

    @Override
    public void setSnoozeDuration(double snoozeDurationMinutes) {
        snoozeTimeHours.setValue(Utilities.minutesToHoursMinutesSeconds(snoozeDurationMinutes).hours);
        snoozeTimeMinutes.setValue(Utilities.minutesToHoursMinutesSeconds(snoozeDurationMinutes).minutes);
        snoozeTimeSeconds.setValue(Utilities.minutesToHoursMinutesSeconds(snoozeDurationMinutes).seconds);
    }

    @Override
    public void itIsTimeToShowAlert() {
        beepAndShow();
    }
    
}
