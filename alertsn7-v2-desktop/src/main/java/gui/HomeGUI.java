package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import model.Ticket;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.springframework.web.client.RestClientException;
import service.AlertaService;
import service.EnvironmentService;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HomeGUI extends javax.swing.JFrame {

    private static String filePath;
    private AlertaService alertaService;

    EnvironmentService envServ = new EnvironmentService();
    AlertaService alertaServ = new AlertaService(envServ);

    // ✅ t5 como atributo de clase para poder interrumpirlo
    private Thread t5;
    private volatile boolean runningg = true;
    private volatile boolean running  = true;
    private final ExecutorService alertExecutor = Executors.newCachedThreadPool();

    public HomeGUI() {
        initComponents();

        // ✅ Hookear el cierre de ventana aquí para garantizar apagado limpio
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                detenerTodo();
                dispose();
            }
        });
    }

    public void lanzarHilos() {
        t5 = new Thread(() -> {
            while (runningg) {
                try {
                    SwingUtilities.invokeAndWait(() -> {
                        System.out.println("Corriendo hilo5 Chequeando Alertas...");
                        cargarTablaAlertas();
                    });
                    Thread.sleep(30000);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // ✅ sale del while limpiamente
                    break;
                } catch (InvocationTargetException | RestClientException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Hilo t5 detenido.");
        }, "hilo-chequeo-alertas"); // ✅ nombre para debug

        t5.setDaemon(true); // ✅ si la JVM cierra, este hilo no la retiene
        t5.start();
    }

    /**
     * Único método de apagado. Detiene t5 y el executor de alertas limpiamente.
     */
    public void detenerTodo() {
        runningg = false;

        // ✅ Interrumpir t5 por si está en sleep(30000)
        if (t5 != null && t5.isAlive()) {
            t5.interrupt();
        }

        // ✅ No acepta nuevas alertas y espera que las activas terminen (máx 5s)
        alertExecutor.shutdown();
        try {
            if (!alertExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                alertExecutor.shutdownNow(); // fuerza si no terminaron
                System.out.println("AlertExecutor forzado a cerrar.");
            }
        } catch (InterruptedException e) {
            alertExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("Todos los hilos detenidos correctamente.");
    }
    
    //CARGAMOS TODOS LOS DATOS DEL USUARIO EN ESTE METODO
    public void cargarDatosDeInicio() {
        CargarDisenioGUI();
        txtNameUser.setText("nico");

        playSound("alert.wav");
    }

    public void CargarDisenioGUI() {
        AlertsPane.setVisible(true);
        txtPrecio.setForeground(Color.WHITE);
        apiSucess.setVisible(false);
        apiFail.setVisible(true);
        calculatorButton.setVisible(false);
        txtPrecio.setVisible(false);
        txtPrecio.setVisible(true);
        loadRefresh.setVisible(false);
        
        Properties props = new Properties();
        try (InputStream input = getClass().getResourceAsStream("/config.properties")) {
            if (input == null) {
                System.out.println("config.properties no encontrado, usando URL por defecto");
                iptxt.setText("");
            }
            props.load(input);
            iptxt.setText(props.getProperty("backend.url", "null"));
        } catch (IOException e) {
            e.printStackTrace();
            iptxt.setText("");
        }
    }


    /*===============================================*/
 /*==========CARGAR TICKET Y TABLA ALERTAS==============*/
 /*===============================================*/
    public void saveAlerta() {

    }

    public void cargarTablaAlertas() {
        loadRefresh.setVisible(true);

        DefaultTableModel modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String[] titulos = {"TICKET", "PrecioEjecucion", "side", "Encendido??"};
        modeloTabla.setColumnIdentifiers(titulos);

        // Chequeo de estado del servidor
        try {
            Boolean respuesta = alertaServ.checkStatusServer();
            apiSucess.setVisible(respuesta);
            apiFail.setVisible(!respuesta);
        } catch (Exception e) {
            System.out.println("Error en la respuesta del servidor");
        }

        // Carga de alertas
        try {
            List<Ticket> listaTickets = alertaServ.listaAlertas();

            if (listaTickets != null) {
                for (Ticket tic : listaTickets) {
                    Object[] objeto = {
                        tic.getNombre(),
                        tic.getPrecioEstablecido(),
                        tic.getDireccion(),
                        tic.getEncendido()
                    };
                    modeloTabla.addRow(objeto);

                    // 🔔 Si hay alerta: lanzar en hilo separado, NO bloquea el chequeo
                    if (tic.getEmitirAlerta() == 1) {
                        lanzarAlertaEnHiloSeparado(tic);
                    }
                }
            }
            TablaAlertas.setModel(modeloTabla);

        } catch (Exception e) {
            System.out.println("Error al cargar Datos de Tabla");
        } finally {
            loadRefresh.setVisible(false);
        }
    }

    /**
     * Lanza la notificación en un hilo independiente. El popup NO bloquea el
     * ciclo de chequeo de t5.
     */
    private void lanzarAlertaEnHiloSeparado(Ticket tic) {
        // Capturar datos antes de entrar al hilo (thread-safety)
        final String nombre = tic.getNombre();
        final String precio = String.valueOf(tic.getPrecioEstablecido());
        final String direccion = tic.getDireccion();

        alertExecutor.submit(() -> {
            // Sonido: no requiere EDT
            playSound("alert.wav");

            // Armar mensaje según dirección
            boolean esEncima = "Encima".equals(direccion);
            String mensaje = esEncima
                    ? "🔔 ALERTA PARA | " + nombre.toUpperCase() + " | EL PRECIO SUPERO LOS: 🠕 " + precio
                    : "🔔 ALERTA PARA | " + nombre.toUpperCase() + " | EL PRECIO CAYO DE LOS: 🠗 " + precio;
            String titulo = "🔔 ALERTA " + nombre.toUpperCase();

            // Mostrar popup en EDT desde este hilo separado
            // invokeAndWait bloquea ESTE hilo, no el hilo t5
            try {
                SwingUtilities.invokeAndWait(() -> showAlert(mensaje, titulo, "alert-icon.jpg"));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    public static void playSound(String fileName) {
        try {
            InputStream soundStream = ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
            if (soundStream != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(soundStream));
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } else {
                System.err.println("No se pudo cargar el archivo de sonido: " + fileName);
            }
        } catch (Exception e) {
            System.err.println("Error al reproducir el sonido: " + e.getMessage());
        }
    }

    public static void showAlert(String descrip, String titulo, String imageName) {
        JOptionPane optionPane = new JOptionPane(descrip);
        optionPane.setMessageType(JOptionPane.INFORMATION_MESSAGE);

        // Cargar la imagen desde el directorio de recursos
        ImageIcon icon = createImageIcon("/" + imageName);

        if (icon != null) {
            // Establecer la imagen como ícono del JOptionPane
            optionPane.setIcon(icon);
        } else {
            // Si la imagen no se puede cargar, mostrar un mensaje de advertencia
            System.err.println("No se pudo cargar la imagen: " + imageName);
        }

        JDialog dialog = optionPane.createDialog(titulo);
        dialog.setVisible(true);
    }

// Método para cargar una imagen desde el directorio de recursos y crear un ImageIcon
    protected static ImageIcon createImageIcon(String path) {
        try {
            InputStream inputStream = AlertUtil.class.getResourceAsStream(path);
            if (inputStream != null) {
                BufferedImage img = ImageIO.read(inputStream);
                return new ImageIcon(img);
            } else {
                System.out.println("No se encontró la imagen en la ruta especificada: " + path);
                return null;
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la imagen: " + e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        NavigationPane = new javax.swing.JPanel();
        txtNameUser = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        calculatorButton = new javax.swing.JButton();
        jPaneWidgetTV = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        apiSucess = new javax.swing.JLabel();
        apiFail = new javax.swing.JLabel();
        AlertsPane = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        DireccionBox = new javax.swing.JComboBox<>();
        CargarAlertaButton = new javax.swing.JButton();
        txtPrecio = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        TicketBox = new javax.swing.JComboBox<>();
        btnLimpiarCompletadas = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TablaAlertas = new javax.swing.JTable();
        loadRefresh = new javax.swing.JLabel();
        btnReload1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        iptxt = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        NavigationPane.setBackground(new java.awt.Color(51, 51, 51));
        NavigationPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtNameUser.setFont(new java.awt.Font("sansserif", 0, 18)); // NOI18N
        txtNameUser.setText("name");
        NavigationPane.add(txtNameUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(870, 80, -1, -1));

        jLabel8.setBackground(new java.awt.Color(51, 51, 51));
        jLabel8.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(251, 251, 251));
        jLabel8.setText("AlertsN7 v2");
        NavigationPane.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, -1, -1));

        calculatorButton.setBackground(new java.awt.Color(58, 58, 58));
        calculatorButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/calculadora.png"))); // NOI18N
        calculatorButton.setBorder(null);
        calculatorButton.setFocusable(false);
        calculatorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculatorButtonActionPerformed(evt);
            }
        });
        NavigationPane.add(calculatorButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 20, 60, 70));

        jPaneWidgetTV.setBackground(new java.awt.Color(51, 51, 51));
        jPaneWidgetTV.setForeground(new java.awt.Color(51, 51, 51));
        jPaneWidgetTV.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        NavigationPane.add(jPaneWidgetTV, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 290, 130));

        jPanel1.add(NavigationPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 120));

        jLabel4.setBackground(new java.awt.Color(51, 51, 51));
        jLabel4.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(251, 251, 251));
        jLabel4.setText("By Niko7even");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(987, 888, 90, 30));

        apiSucess.setForeground(new java.awt.Color(242, 242, 242));
        apiSucess.setIcon(new javax.swing.ImageIcon(getClass().getResource("/api-sucess.png"))); // NOI18N
        jPanel1.add(apiSucess, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 880, 40, 40));

        apiFail.setForeground(new java.awt.Color(242, 242, 242));
        apiFail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/api-fail.png"))); // NOI18N
        jPanel1.add(apiFail, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 890, -1, 20));

        AlertsPane.setBackground(new java.awt.Color(51, 51, 51));
        AlertsPane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AlertsPaneMouseClicked(evt);
            }
        });
        AlertsPane.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel6.setBackground(new java.awt.Color(51, 51, 51));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DireccionBox.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        DireccionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Encima", "Debajo" }));
        DireccionBox.setFocusable(false);
        jPanel6.add(DireccionBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 40, -1, -1));

        CargarAlertaButton.setText("Cargar Alerta!");
        CargarAlertaButton.setFocusable(false);
        CargarAlertaButton.setRequestFocusEnabled(false);
        CargarAlertaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargarAlertaButtonActionPerformed(evt);
            }
        });
        jPanel6.add(CargarAlertaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, -1, -1));

        txtPrecio.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        txtPrecio.setForeground(new java.awt.Color(213, 213, 213));
        txtPrecio.setText("Ej: 40.15");
        txtPrecio.setAutoscrolls(false);
        txtPrecio.setRequestFocusEnabled(false);
        txtPrecio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPrecioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtPrecioFocusLost(evt);
            }
        });
        txtPrecio.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtPrecioMouseClicked(evt);
            }
        });
        txtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioActionPerformed(evt);
            }
        });
        jPanel6.add(txtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 40, 160, -1));

        jLabel6.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel6.setText("price:");
        jPanel6.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 40, -1, -1));

        TicketBox.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        TicketBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "btc", "eth", "pol", "link", "trx", "xrp", "arb", "ltc", "bnb", "ton", "ada", "avax", "sol", "atom", "etc", "xmr", "doge" }));
        TicketBox.setFocusable(false);
        jPanel6.add(TicketBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, -1, -1));

        btnLimpiarCompletadas.setText("Limpiar Completadas...");
        btnLimpiarCompletadas.setFocusable(false);
        btnLimpiarCompletadas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarCompletadasActionPerformed(evt);
            }
        });
        jPanel6.add(btnLimpiarCompletadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 140, -1, 20));

        btnClear.setText("Limpiar");
        btnClear.setFocusable(false);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });
        jPanel6.add(btnClear, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 40, -1, -1));

        AlertsPane.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 150, 570, 170));

        jPanel5.setBackground(new java.awt.Color(51, 51, 51));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TablaAlertas.setBackground(new java.awt.Color(51, 51, 51));
        TablaAlertas.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        TablaAlertas.setFont(new java.awt.Font("sansserif", 0, 16)); // NOI18N
        TablaAlertas.setForeground(new java.awt.Color(190, 190, 190));
        TablaAlertas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"NULL", "NULL"},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        TablaAlertas.setToolTipText("");
        TablaAlertas.setGridColor(new java.awt.Color(0, 0, 0));
        TablaAlertas.setIntercellSpacing(new java.awt.Dimension(3, 3));
        TablaAlertas.setRequestFocusEnabled(false);
        TablaAlertas.setRowHeight(32);
        TablaAlertas.setRowSelectionAllowed(false);
        jScrollPane4.setViewportView(TablaAlertas);

        jPanel5.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 910, 350));

        AlertsPane.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 930, 350));

        loadRefresh.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        loadRefresh.setForeground(new java.awt.Color(242, 242, 242));
        loadRefresh.setText("Refreshing...");
        AlertsPane.add(loadRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 40, 180, 30));

        btnReload1.setText("Reload");
        btnReload1.setFocusable(false);
        btnReload1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReload1ActionPerformed(evt);
            }
        });
        AlertsPane.add(btnReload1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 10, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(242, 242, 242));
        jLabel3.setText("🔔Alerts");
        AlertsPane.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 40, 200, 90));

        jPanel1.add(AlertsPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 1038, 707));

        iptxt.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        iptxt.setForeground(new java.awt.Color(242, 242, 242));
        iptxt.setText("null");
        jPanel1.add(iptxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 890, 710, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(242, 242, 242));
        jLabel5.setText("ApiCheck:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 890, 70, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(242, 242, 242));
        jLabel7.setText("IP BackendSet:");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 890, 110, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1090, 920));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculatorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculatorButtonActionPerformed
        PositionCalculator calculator = new PositionCalculator();
        calculator.setVisible(true);
        calculator.setLocationRelativeTo(null);
        calculator.setTitle("TradingRoom By Niko7even");
    }//GEN-LAST:event_calculatorButtonActionPerformed

    private void btnLimpiarCompletadasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarCompletadasActionPerformed
        EnvironmentService envServv = new EnvironmentService();
        AlertaService alertaServvv = new AlertaService(envServv);

        try {
            alertaServvv.deleteTicket();
            showAlert("Alertas Completadas Eliminadas!!",
                    "Alertas Eliminadas", "clean.png");
            cargarTablaAlertas();
        } catch (NumberFormatException e) {
            System.out.println("Formato invalido Introducido!" + e);
        }
    }//GEN-LAST:event_btnLimpiarCompletadasActionPerformed

    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioActionPerformed
        SwingUtilities.invokeLater(() -> {
            txtPrecio.requestFocusInWindow();
            txtPrecio.selectAll(); // opcional: selecciona el texto
        });
    }//GEN-LAST:event_txtPrecioActionPerformed

    private void txtPrecioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtPrecioMouseClicked
        SwingUtilities.invokeLater(() -> {
            txtPrecio.requestFocusInWindow();
            txtPrecio.selectAll(); // opcional: selecciona el texto
        });
        String text = "";
        txtPrecio.setText(text);
    }//GEN-LAST:event_txtPrecioMouseClicked

    private void CargarAlertaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargarAlertaButtonActionPerformed
        EnvironmentService envServv = new EnvironmentService();
        AlertaService alertaServv = new AlertaService(envServv);

        String direccion = "";
        Boolean encendido = true;
        int alerta = 0;
        String color = "table-primary";
        String precioString = txtPrecio.getText();
        if (!txtPrecio.getText().isEmpty()) {
            try {
                Double precioParseado = Double.parseDouble(precioString);

                Ticket tic = new Ticket();
                tic.setId(500L);
                tic.setNombre(TicketBox.getSelectedItem().toString());
                tic.setPrecioEstablecido(precioParseado);
                tic.setDireccion(DireccionBox.getSelectedItem().toString());
                tic.setEncendido(encendido);
                tic.setColor(color);
                tic.setEmitirAlerta(0);
                alertaServv.saveTicket(tic);

                // ALERTA DE CREACION DE ALERTA DE PRECIO CON EXITO
                //showAlert("Alerta Agregada con Exito!",
                //        "Alerta Creada", "sucess.png");

                cargarTablaAlertas();
            } catch (NumberFormatException e) {
                showAlert("Formato invalido Introducido!",
                        "ERROR!!", "error.png");
                System.out.println("Formato invalido Introducido!" + e);
            }
        }
    }//GEN-LAST:event_CargarAlertaButtonActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        SwingUtilities.invokeLater(() -> {
            txtPrecio.select(0, 0);              // quitar selección
            AlertsPane.requestFocusInWindow();  // quitar foco del JTextField
            txtPrecio.setText("");
        });
    }//GEN-LAST:event_btnClearActionPerformed

    private void txtPrecioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecioFocusLost

    }//GEN-LAST:event_txtPrecioFocusLost

    private void txtPrecioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPrecioFocusGained

    }//GEN-LAST:event_txtPrecioFocusGained

    private void AlertsPaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AlertsPaneMouseClicked

    }//GEN-LAST:event_AlertsPaneMouseClicked

    private void btnReload1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReload1ActionPerformed
        cargarTablaAlertas();

        SwingUtilities.invokeLater(() -> {
            txtPrecio.select(0, 0);              //quitar selección
            AlertsPane.requestFocusInWindow();  // quitar foco del JTextField
            txtPrecio.setText("");
        });
    }//GEN-LAST:event_btnReload1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AlertsPane;
    private javax.swing.JButton CargarAlertaButton;
    private javax.swing.JComboBox<String> DireccionBox;
    private javax.swing.JPanel NavigationPane;
    private javax.swing.JTable TablaAlertas;
    private javax.swing.JComboBox<String> TicketBox;
    private javax.swing.JLabel apiFail;
    private javax.swing.JLabel apiSucess;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnLimpiarCompletadas;
    private javax.swing.JButton btnReload1;
    private javax.swing.JButton calculatorButton;
    private javax.swing.JLabel iptxt;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPaneWidgetTV;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JLabel loadRefresh;
    private javax.swing.JLabel txtNameUser;
    private javax.swing.JTextField txtPrecio;
    // End of variables declaration//GEN-END:variables

    public HomeGUI(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    private static class AlertUtil {

        public AlertUtil() {
        }
    }

}
