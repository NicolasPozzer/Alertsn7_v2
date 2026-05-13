
package gui;

import javax.swing.JFrame;

public class PositionCalculator extends javax.swing.JFrame {

    
    public PositionCalculator() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtArriesgar = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtPorcentaje2 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtPorcentaje = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtCapital = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(51, 51, 51));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(242, 242, 242));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.jLabel1.text")); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 110, 190, 80));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.jLabel4.text")); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 170, 60));

        jPanel4.setBackground(new java.awt.Color(0, 153, 255));

        txtArriesgar.setFont(new java.awt.Font("Segoe UI", 0, 21)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(txtArriesgar, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.txtArriesgar.text")); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(txtArriesgar, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txtArriesgar, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 130, 60));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, 320, 60));

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtPorcentaje2.setFont(new java.awt.Font("Segoe UI", 0, 22)); // NOI18N
        txtPorcentaje2.setForeground(new java.awt.Color(230, 230, 230));
        org.openide.awt.Mnemonics.setLocalizedText(txtPorcentaje2, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.txtPorcentaje2.text")); // NOI18N
        jPanel3.add(txtPorcentaje2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 60, 50));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 70, 50));

        jLabel2.setFont(new java.awt.Font("Bookman Old Style", 0, 40)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(242, 242, 242));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.jLabel2.text")); // NOI18N
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 20, 480, 80));

        txtPorcentaje.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtPorcentaje.setText(org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.txtPorcentaje.text")); // NOI18N
        txtPorcentaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPorcentajeActionPerformed(evt);
            }
        });
        jPanel1.add(txtPorcentaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, -1, 40));

        jButton1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 260, 100, 30));

        jLabel5.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(242, 242, 242));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.jLabel5.text")); // NOI18N
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, -1, 30));

        txtCapital.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCapital.setText(org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.txtCapital.text")); // NOI18N
        txtCapital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCapitalActionPerformed(evt);
            }
        });
        jPanel1.add(txtCapital, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 110, 40));

        jLabel6.setFont(new java.awt.Font("Bookman Old Style", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(242, 242, 242));
        org.openide.awt.Mnemonics.setLocalizedText(jLabel6, org.openide.util.NbBundle.getMessage(PositionCalculator.class, "PositionCalculator.jLabel6.text")); // NOI18N
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 180, 190, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 650, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCapitalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCapitalActionPerformed
        String txt = "";
        txtCapital.setText(txt);
    }//GEN-LAST:event_txtCapitalActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String cap = txtCapital.getText();
        String porce = txtPorcentaje.getText();
        String porce2 = txtPorcentaje.getText();
        
        Double capital = Double.parseDouble(cap);
        Double porcentaje = Double.parseDouble(porce);
        Double porcentaje2 = Double.parseDouble(porce2);
        
        porcentaje = porcentaje * capital;
        porcentaje2 = porcentaje2 * 100;
        
        String porcentajeParceado = String.valueOf(porcentaje);
        String porcentajeParceado2 = String.valueOf(porcentaje2);
        
        txtArriesgar.setText(porcentajeParceado);
        txtPorcentaje2.setText(porcentajeParceado2);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtPorcentajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorcentajeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentajeActionPerformed

    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel txtArriesgar;
    private javax.swing.JTextField txtCapital;
    private javax.swing.JTextField txtPorcentaje;
    private javax.swing.JLabel txtPorcentaje2;
    // End of variables declaration//GEN-END:variables
}
