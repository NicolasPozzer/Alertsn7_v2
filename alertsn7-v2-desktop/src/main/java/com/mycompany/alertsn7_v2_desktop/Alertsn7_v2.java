package com.mycompany.alertsn7_v2_desktop;

import com.formdev.flatlaf.FlatDarkLaf;
import gui.HomeGUI;
import javax.swing.UIManager;

public class Alertsn7_v2 {

    public static void main(String[] args) {

    // SOLUCION DEFINITIVA FIX COMBOBOX(acordeon) + FLATLAF
    System.setProperty("sun.java2d.d3d", "false");
    System.setProperty("sun.java2d.opengl", "false");

    javax.swing.JPopupMenu.setDefaultLightWeightPopupEnabled(true);
    UIManager.put("ComboBox.isPopDown", Boolean.TRUE);

    try {
        UIManager.setLookAndFeel(new FlatDarkLaf());
    } catch (Exception ex) {
        System.err.println("Failed to initialize FlatLaf");
    }

    java.awt.EventQueue.invokeLater(() -> {
        HomeGUI princi = new HomeGUI();
        princi.setVisible(true);
        princi.setLocationRelativeTo(null);
        princi.setTitle("TradingRoom By Niko7even");
        princi.cargarDatosDeInicio();
        princi.lanzarHilos();
    });
}
}
