/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javabeeper;

/**
 *
 * @author rbri053
 */
public class MonitorWindow extends javax.swing.JFrame implements SnoozeObserver {

    private SnoozeController snoozeController;
    
    /**
     * Creates new form MonitorWindow
     */
    public MonitorWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        resetSnoozeDelayTime = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        timeRemaining = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        soundEnabled = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Beeper");

        jLabel1.setText("Reset countdown to:");

        resetSnoozeDelayTime.setText("20");
        resetSnoozeDelayTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetSnoozeDelayTimeActionPerformed(evt);
            }
        });

        jLabel2.setText("Current countdown:");

        timeRemaining.setEditable(false);
        timeRemaining.setFont(new java.awt.Font("Lucida Grande", 1, 72)); // NOI18N
        timeRemaining.setText(" ");
        timeRemaining.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timeRemainingActionPerformed(evt);
            }
        });

        jLabel3.setText("Version: 0.17");

        soundEnabled.setSelected(true);
        soundEnabled.setText("Enable audio. (Note: if disabled, then resetting snooze will enable audio again)");
        soundEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soundEnabledActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jLabel3))
                    .add(layout.createSequentialGroup()
                        .add(130, 130, 130)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(timeRemaining, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 390, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(layout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(soundEnabled)
                            .add(layout.createSequentialGroup()
                                .add(jLabel1)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                .add(resetSnoozeDelayTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(timeRemaining, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 62, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(resetSnoozeDelayTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(soundEnabled)
                .add(34, 34, 34)
                .add(jLabel3)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void resetSnoozeDelayTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetSnoozeDelayTimeActionPerformed

        snoozeController.restartSnoozing(Double.parseDouble(resetSnoozeDelayTime.getText()));
    }//GEN-LAST:event_resetSnoozeDelayTimeActionPerformed

    private void timeRemainingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timeRemainingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_timeRemainingActionPerformed

    private void soundEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundEnabledActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_soundEnabledActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField resetSnoozeDelayTime;
    private javax.swing.JCheckBox soundEnabled;
    private javax.swing.JTextField timeRemaining;
    // End of variables declaration//GEN-END:variables

    void setController(SnoozeController controller) {
        snoozeController = controller;
    }
    
    @Override
    public void setSoundEnabled(boolean soundEnabled) {
        this.soundEnabled.setSelected(soundEnabled);		
    }

    @Override
    public void setSnoozeDuration(double snoozeDurationMinutes) {
        resetSnoozeDelayTime.setText(Double.toString(snoozeDurationMinutes));
    }

    public void setTimeRemainingDisplay(double paramMinutesRemaining) {
        String timeStringHHMMSS = Utilities.minutesAsTimeStringHHMMSS(paramMinutesRemaining);
        timeRemaining.setText(timeStringHHMMSS);
        setTitle(timeStringHHMMSS + "  Java Beeper" );
    }
    

}
