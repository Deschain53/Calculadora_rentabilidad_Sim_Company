/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principales;

import clases.DatosEntrantesCalcu;
import clases.ExtraeTodoEdificio;
import clases.JSONedificiosProduccion;
import clases.JSONproductos;
import clases.ProductObject;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import clases.LeeJSON;
import clases.NombresPEN;
import frames.DetalleProductos;
import frames.DetalleProductosEditable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.json.JSONObject;

/**
 *
 * @author donyo
 */
public class Calculadora_rentabilidad extends javax.swing.JFrame {

    //Proxima actualizaion: cambiar porcentaje de venta a mercado de int a float, corregir metodo recalcula() , agregar boton "Eliminar" y hacer testeo general
    private String[] edificios_nombres_es = {"Plantación", "Planta de concreto", "Cantera",
        "Fábrica de construcción", "Contratista general", "Fábrica textil", "Mina", "Fábrica",
        "Fábrica de electrónicos", "Electrónica aeroespacial", "Fábrica de motores", "Fábrica de automotores", "Fábrica aeroespacial",
        "Plataforma petrolera", "Refinería", "Hangar", "Instalación de integración vertical", "Granja", "Depósito de embarque", "Embalse de agua", "Centro de investigación agrícola",
        "Laboratorio de física", "Laboratorio ganadero", "Laboratorio de química", "I+D Automotriz", "Diseño de moda",
        "Central eléctrica"};
    private String[] edificios_nombres_EN = {"Plantation", "Concrete plant", "Quarry",
        "Construction Factory", "General Contractor", "Textile Factory", "Mine", "Factory",
        "Electronics Factory", "Aerospace Electronics", "Engine Factory", "Automotive Factory", "Aerospace Factory",
        "Oil platform", "Refinery", "Hangar", "Vertical integration facility", "Farm", "Shipping deposit", "Water reservoir", "Plant research center",
        "Physics laboratory", "Breeding laboratory", "Chemestry laboratory", "Automotive R&D", "Fashion & design",
        "Power plant"};
    private String[] edificios_code = {"P", "o", "Q", "x", "g", "T", "M", "Y", "L", "8", "D", "1", "7", "O", "R", "0", "9", "F", "S", "W",
        "p", "h", "b", "c", "a", "f", "E"};
    private String[] nombreColumnas_es = {"Producto", "Costo", "Precio en mercado", "Unidades/hora", "Ganancia/hora Mercado", "Ganancia/hora contratos"};
    private String[] nombreColumnas_EN = {"Product", "Cost", "Market price", "Units/hour", "Profit/hour Market", "Profit/hour contracts"};
    private String[] tipoCalcu_es = {"Calculadora básica", "Calculadora avanzada"};
    private String[] tipoCalcu_EN = {"Basic calculator", "Advanced calculator"};

    private float bonificacion_produccion = 0;
    private int nivel_edificio = 0;
    private float gastos_administrativos = 0;
    private float porcentaje_bajoMercado = 0;
    private float precio_transporte = 0;
    private int calidad = 0;
    private int[] codigosEdificio = null;
    private int lastEdificioD = -1;   //Guarda el index del ultimo edificio que esta en la tablaD
    private String[] nombresEdificio = null;
    private ExtraeTodoEdificio etf;
    private ProductObject[] po;
    //;
    private ExtraeTodoEdificio[] etfT = new ExtraeTodoEdificio[edificios_code.length];
    //;
    private int maxCalidad = 7;
    private boolean español;
    private boolean calculado;
    private boolean calculado_todo;
    private int indexL = -1;    //El index para los Frame de información

    private DefaultTableModel modeloAvanzado;
    private Vector nCes_vector;
    private Vector nCEN_vector;
    private DetalleProductos dp;
    private DetalleProductosEditable dp2;
    private int fase = 1;
    private float abundancia;
    private NombresPEN nPEN;
    private JSONedificiosProduccion jep;
    private JSONproductos jp;
    private ArrayList<ProductObject> POIlist = new ArrayList<ProductObject>(); // Para guardar lo de la tabla dos
    private int investigaciones;
    private int xFrame;

    private ArrayList<DatosEntrantesCalcu> dec = new ArrayList<DatosEntrantesCalcu>();  //Lista de objeto que contiene informacion ingresada
    //Para hacer la conexion;
    private Connection cn;
    private String usuario = "sql10415622";
    private String url = "jdbc:mysql://sql10.freesqldatabase.com:3306/sql10415622";
    private String contrasena = "wCH5k18SGz";
    
    private int oldFase;

    public Calculadora_rentabilidad() {
        initComponents();
        xFrame = 795;
        devuelveVentanaInicial();
        //this.setSize(875, 412);    ///(874, 485);        //(790, 485);880, 670)
        this.setResizable(true); //*************************devolver a false despues
        this.setTitle("Calculadora de rentabilidad Sim companies");
        this.setLocationRelativeTo(null);
        nivel_edificio_txt.setText("");
        Pvbm_txt.setText("");
        gastos_administrativos_txt.setText("");
        costo_transporte_txt.setText("");
        bonificacion_txt.setText("");
        edificios_combo.setModel(new DefaultComboBoxModel(edificios_nombres_es));
        tipoCalcu_combo.setModel(new DefaultComboBoxModel(tipoCalcu_es));
        edificios_combo.setSelectedIndex(8);
        español = true;
        calculado = false;
        calculado_todo = false;
        abundancia = 100;
        abundancia_txt.setText("");
        abundancia_lb.setVisible(false);
        abundanciaP_lb.setVisible(false);
        abundancia_txt.setVisible(false);
        abundancia_txt.setSize(73, 24);
        Pvbm_txt.setSize(73, 24);
        dp = new DetalleProductos(español);
        dp2 = new DetalleProductosEditable(español);
        modeloAvanzado = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return true;
            }
        };

        nCes_vector = new Vector();
        nCEN_vector = new Vector();
        for (int i = 0; i < nombreColumnas_es.length; i++) {
            nCes_vector.add(nombreColumnas_es[i]);
            nCEN_vector.add(nombreColumnas_EN[i]);
        }
        nPEN = new NombresPEN();
        jep = new JSONedificiosProduccion();
        jp = new JSONproductos();
        investigaciones = 20;
        ocultaBotonesCalculadoraAvanzada();
        cn = null;
        po = new ProductObject[200];

        promedio.setVisible(false);

    }

    private void devuelveVentanaInicial() {
        Calculadora_rentabilidad.super.setSize(xFrame, 385);//875, 412);//875, 412);
    }

    private void ocultaBotonesCalculadoraAvanzada() {
        odn_button.setVisible(false);
        agrega_button.setVisible(false);
        recalcula_button.setVisible(false);
        extrae_button.setVisible(false);
        elimina_button.setVisible(false);
        eliminaTodos.setVisible(false);
        tablaA.setVisible(false);

    }

    private void muestraBotonesCalculadoraAvanzada() {
        odn_button.setVisible(true);
        agrega_button.setVisible(true);
        recalcula_button.setVisible(true);
        extrae_button.setVisible(true);
        elimina_button.setVisible(true);
        eliminaTodos.setVisible(true);
        tablaA.setVisible(true);

    }

    private void rellenaDatosDefecto() {
        if (gastos_administrativos_txt.getText() == null || gastos_administrativos_txt.getText().equals("")) {
            gastos_administrativos_txt.setText("50");
        }

        if (bonificacion_txt.getText() == null || gastos_administrativos_txt.getText().equals("")) {
            bonificacion_txt.setText("0");
        }

        if (costo_transporte_txt.getText() == null || gastos_administrativos_txt.getText().equals("")) {
            costo_transporte_txt.setText("0.35");
        }

        if (abundancia_txt.getText() == null || gastos_administrativos_txt.getText().equals("")) {
            abundancia_txt.setText("100");
        }

        if (nivel_edificio_txt.getText() == null || gastos_administrativos_txt.getText().equals("")) {
            nivel_edificio_txt.setText("1");
        }

        if (Pvbm_txt.getText() == null || gastos_administrativos_txt.getText().equals("")) {
            Pvbm_txt.setText("3");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        fase_grupo = new javax.swing.ButtonGroup();
        precio_option = new javax.swing.ButtonGroup();
        nivel_edificio_txt = new javax.swing.JTextField();
        nivel_edificio_lb = new javax.swing.JLabel();
        administrativos_lb = new javax.swing.JLabel();
        gastos_administrativos_txt = new javax.swing.JTextField();
        bonificacion_lb = new javax.swing.JLabel();
        bonificacion_txt = new javax.swing.JTextField();
        pvbm_lb = new javax.swing.JLabel();
        Pvbm_txt = new javax.swing.JTextField();
        calidad_box = new javax.swing.JComboBox<>();
        produciendo_q_lb = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaD = new javax.swing.JTable();
        edificio_lb = new javax.swing.JLabel();
        edificios_combo = new javax.swing.JComboBox<>();
        costo_transporte_label = new javax.swing.JLabel();
        costo_transporte_txt = new javax.swing.JTextField();
        calcula_button = new javax.swing.JButton();
        informacion = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        cambia_idioma = new javax.swing.JButton();
        idioma_lb = new javax.swing.JLabel();
        tipoCalcu_combo = new javax.swing.JComboBox<>();
        odn_button = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaA = new javax.swing.JTable();
        recalcula_button = new javax.swing.JButton();
        R_button = new javax.swing.JRadioButton();
        N_button = new javax.swing.JRadioButton();
        B_button = new javax.swing.JRadioButton();
        fe_lb = new javax.swing.JLabel();
        abundancia_lb = new javax.swing.JLabel();
        abundancia_txt = new javax.swing.JTextField();
        abundanciaP_lb = new javax.swing.JLabel();
        agrega_button = new javax.swing.JButton();
        elimina_button = new javax.swing.JButton();
        eliminaTodos = new javax.swing.JButton();
        extrae_button = new javax.swing.JButton();
        recalculaTablaB_button = new javax.swing.JButton();
        tabajandoCon_lb = new javax.swing.JLabel();
        actual = new javax.swing.JRadioButton();
        promedio = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        nivel_edificio_txt.setText("jTextField1");
        nivel_edificio_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nivel_edificio_txtActionPerformed(evt);
            }
        });

        nivel_edificio_lb.setText("Nivel de edificio:");

        administrativos_lb.setText("Gastos administrativos:");

        gastos_administrativos_txt.setText("jTextField2");

        bonificacion_lb.setText("Bonificación de producción:");

        bonificacion_txt.setText("jTextField3");

        pvbm_lb.setText("Porcentaje de venta bajo mercado:");

        Pvbm_txt.setText("jTextField4");
        Pvbm_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Pvbm_txtActionPerformed(evt);
            }
        });

        calidad_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7" }));
        calidad_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calidad_boxActionPerformed(evt);
            }
        });

        produciendo_q_lb.setText("Produciendo calidad:");

        tablaD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Costo", "Precio en mercado", "Unidades/hora", "Ganancia/hora  Mercado", "Ganancia/hora Contratos"
            }
        ));
        tablaD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDMouseClicked(evt);
            }
        });
        tablaD.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                tablaDPropertyChange(evt);
            }
        });
        jScrollPane2.setViewportView(tablaD);
        if (tablaD.getColumnModel().getColumnCount() > 0) {
            tablaD.getColumnModel().getColumn(0).setResizable(false);
            tablaD.getColumnModel().getColumn(1).setResizable(false);
            tablaD.getColumnModel().getColumn(2).setResizable(false);
            tablaD.getColumnModel().getColumn(3).setResizable(false);
            tablaD.getColumnModel().getColumn(4).setResizable(false);
            tablaD.getColumnModel().getColumn(5).setResizable(false);
        }

        edificio_lb.setText("Edificio:");

        edificios_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        edificios_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edificios_comboActionPerformed(evt);
            }
        });

        costo_transporte_label.setText("Costo transporte:");

        costo_transporte_txt.setText("jTextField1");

        calcula_button.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        calcula_button.setText("Obtener datos");
        calcula_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcula_buttonActionPerformed(evt);
            }
        });

        informacion.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 12)); // NOI18N
        informacion.setText("i");
        informacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                informacionActionPerformed(evt);
            }
        });

        jLabel7.setText("%");

        jLabel8.setText("%");

        jLabel9.setText("%");

        jLabel10.setText("$");

        cambia_idioma.setText("Español");
        cambia_idioma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambia_idiomaActionPerformed(evt);
            }
        });

        idioma_lb.setText("Idioma:");

        tipoCalcu_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tipoCalcu_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoCalcu_comboActionPerformed(evt);
            }
        });

        odn_button.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        odn_button.setText("Agregar todos");
        odn_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                odn_buttonActionPerformed(evt);
            }
        });

        tablaA.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Costo", "Precio en mercado", "Unidades/hora", "Ganancia/hora Mercado", "Ganancia/hora contratos"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaA.getTableHeader().setReorderingAllowed(false);
        tablaA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaAMouseClicked(evt);
            }
        });
        tablaA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tablaAKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tablaA);
        if (tablaA.getColumnModel().getColumnCount() > 0) {
            tablaA.getColumnModel().getColumn(0).setResizable(false);
            tablaA.getColumnModel().getColumn(1).setResizable(false);
            tablaA.getColumnModel().getColumn(2).setResizable(false);
            tablaA.getColumnModel().getColumn(3).setResizable(false);
            tablaA.getColumnModel().getColumn(4).setResizable(false);
            tablaA.getColumnModel().getColumn(5).setResizable(false);
        }

        recalcula_button.setFont(new java.awt.Font("Dialog", 1, 15)); // NOI18N
        recalcula_button.setText("Recalcula");
        recalcula_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recalcula_buttonActionPerformed(evt);
            }
        });

        fase_grupo.add(R_button);
        R_button.setText("Recesión");
        R_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                R_buttonActionPerformed(evt);
            }
        });

        fase_grupo.add(N_button);
        N_button.setSelected(true);
        N_button.setText("Normal");
        N_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                N_buttonActionPerformed(evt);
            }
        });

        fase_grupo.add(B_button);
        B_button.setText("Boom");
        B_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_buttonActionPerformed(evt);
            }
        });

        fe_lb.setText("Fase económica:");

        abundancia_lb.setText("Abundancia:");

        abundancia_txt.setText("jTextField1");
        abundancia_txt.setRequestFocusEnabled(false);
        abundancia_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abundancia_txtActionPerformed(evt);
            }
        });

        abundanciaP_lb.setText("%");

        agrega_button.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        agrega_button.setText("Agregar producto");
        agrega_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agrega_buttonActionPerformed(evt);
            }
        });

        elimina_button.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        elimina_button.setText("Eliminar producto");
        elimina_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elimina_buttonActionPerformed(evt);
            }
        });

        eliminaTodos.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        eliminaTodos.setText("Elimina todos");
        eliminaTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminaTodosActionPerformed(evt);
            }
        });

        extrae_button.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        extrae_button.setText("Extrae precio nuevo");
        extrae_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                extrae_buttonActionPerformed(evt);
            }
        });

        recalculaTablaB_button.setText("Recalcula");
        recalculaTablaB_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recalculaTablaB_buttonActionPerformed(evt);
            }
        });

        tabajandoCon_lb.setText("Trabajar con:");

        precio_option.add(actual);
        actual.setSelected(true);
        actual.setText("Precios actuales");

        precio_option.add(promedio);
        promedio.setText("Precios promedio");

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(produciendo_q_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(calidad_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(calcula_button)
                                        .addGap(18, 18, 18)
                                        .addComponent(recalculaTablaB_button))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pvbm_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(Pvbm_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel7)
                                        .addGap(18, 18, 18)
                                        .addComponent(abundancia_lb)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(abundancia_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(abundanciaP_lb)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(fe_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(R_button, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(N_button)
                                        .addGap(18, 18, 18)
                                        .addComponent(B_button))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tabajandoCon_lb)
                                        .addGap(34, 34, 34)
                                        .addComponent(actual)
                                        .addGap(18, 18, 18)
                                        .addComponent(promedio))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(nivel_edificio_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(nivel_edificio_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(edificio_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(edificios_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(54, 54, 54)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(administrativos_lb)
                                    .addGap(18, 18, 18)
                                    .addComponent(gastos_administrativos_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(bonificacion_lb)
                                    .addGap(18, 18, 18)
                                    .addComponent(bonificacion_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabel9)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(costo_transporte_label, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(costo_transporte_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(idioma_lb)
                                    .addGap(18, 18, 18)
                                    .addComponent(cambia_idioma)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(informacion, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(tipoCalcu_combo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(agrega_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(odn_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminaTodos)
                        .addGap(18, 18, 18)
                        .addComponent(recalcula_button, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(extrae_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(elimina_button))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(12, 12, 12))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(R_button)
                                    .addComponent(fe_lb)
                                    .addComponent(N_button)
                                    .addComponent(B_button))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(actual)
                                    .addComponent(tabajandoCon_lb)
                                    .addComponent(promedio))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(nivel_edificio_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nivel_edificio_lb)
                                    .addComponent(edificios_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(edificio_lb)))
                            .addComponent(jSeparator1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pvbm_lb)
                            .addComponent(Pvbm_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(abundancia_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(abundancia_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(abundanciaP_lb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(calcula_button)
                            .addComponent(recalculaTablaB_button)
                            .addComponent(calidad_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(produciendo_q_lb)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gastos_administrativos_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(administrativos_lb)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bonificacion_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bonificacion_lb)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(costo_transporte_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(costo_transporte_label)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cambia_idioma)
                            .addComponent(idioma_lb)
                            .addComponent(informacion))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tipoCalcu_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agrega_button)
                    .addComponent(odn_button)
                    .addComponent(eliminaTodos)
                    .addComponent(recalcula_button, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(extrae_button)
                    .addComponent(elimina_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void nivel_edificio_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nivel_edificio_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nivel_edificio_txtActionPerformed

    private void calidad_boxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calidad_boxActionPerformed
        try {
            if (calculado) {
                cleanTabla();
                leeDatosLocales();
                calidad = calidad_box.getSelectedIndex();
                bonificacion_produccion = Float.parseFloat(bonificacion_txt.getText());
                nivel_edificio = Integer.valueOf(nivel_edificio_txt.getText());
                gastos_administrativos = Float.parseFloat(gastos_administrativos_txt.getText());
                porcentaje_bajoMercado = Integer.valueOf(Pvbm_txt.getText());
                precio_transporte = Float.parseFloat(costo_transporte_txt.getText());
                newCalcula();

            }
        } catch (NumberFormatException e) {
            if (español) {
                JOptionPane.showMessageDialog(this, "Revise que haya rellenado todos los campos correctamente",
                        "Alerta", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Check that all fields are filled correctly ",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_calidad_boxActionPerformed

    private void calcula_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcula_buttonActionPerformed
        if (calculado == true) {
            int dialogButton = 0;
            if (español) {
                dialogButton = JOptionPane.showConfirmDialog(null, "Volveras a tomar los datos desde internet, Estas seguro? ", "Advertencia", JOptionPane.YES_NO_OPTION);
            } else {
                dialogButton = JOptionPane.showConfirmDialog(null, "You will get the data from internet again, Are you sure?", "Warning", JOptionPane.YES_NO_OPTION);
            }
            if (dialogButton == JOptionPane.YES_OPTION) {

                try {
                    calculaTodo();
                } catch (SQLException ex) {
                    Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        } else {
            try {
                calculaTodo();
            } catch (SQLException ex) {
                Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);
            }
            calculado = true;
        }
    }//GEN-LAST:event_calcula_buttonActionPerformed

    public void extraeInfoEdificio(int index) throws IOException {
        etf = new ExtraeTodoEdificio(edificios_code[index]);
        etf.setNumeroEdificio(index);
        etf.setUrlEconomia(fase);
        etf.setJSONedificio(jep.getEdificioIndex(index));
        etf.extraeProduce();
        etfT[index] = etf; //Para guardar en memoria los datos del edificio extraido  ///
        etfT[index].setProductObject(po);
        if (index < investigaciones) {
            etfT[index].extraeProduce();
        } else {
            etfT[index].extraeInfoInvestigacion(); //Extrae lo concerniente a investigacion
        }
        po = etfT[index].getProducObjectArray();

    }

    private void informacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_informacionActionPerformed
        if (español) {
            JOptionPane.showMessageDialog(this, "                          "
                    + "Version 1.4.0 \n"
                    + "Creado por: Jorge Adrián Lucas Sánchez \n"
                    + "Nick: Lucas Engines \n"
                    + "Programa creado para uso personal del autor,\n"
                    + " el mismo no se hace responsable de otros \n"
                    + "usos o fines que se le pueda dar.", "Acerca de", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "                          "
                    + "Version 1.4.0 \n"
                    + "Created by: Jorge Adrián Lucas Sánchez \n"
                    + "Nick: Lucas Engines \n"
                    + "Program created for personal use, \n"
                    + "he himself is not responsible for others \n"
                    + "uses or purposes that may be given.", "About", JOptionPane.PLAIN_MESSAGE);

        }
    }//GEN-LAST:event_informacionActionPerformed

    private void cambia_idiomaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cambia_idiomaActionPerformed
        try {
            DefaultTableModel mD = (DefaultTableModel) tablaD.getModel();
            DefaultTableModel mA = (DefaultTableModel) tablaA.getModel(); //DefaultTableModel
            int index = edificios_combo.getSelectedIndex();
            int indexCalcu = tipoCalcu_combo.getSelectedIndex();

            if (español) {
                español = false;
                super.setTitle("Sim companies profit calculator");
                odn_button.setText("Get data from Basic calculator");
                recalcula_button.setText("Recalculate");
                cambia_idioma.setText("English");
                edificio_lb.setText("Building:");
                nivel_edificio_lb.setText("Building level:");
                pvbm_lb.setText("Percentage of sale under market:");
                produciendo_q_lb.setText("Producing quality:");
                administrativos_lb.setText("Administration overhead:");
                bonificacion_lb.setText("Production speed bonus:");
                costo_transporte_label.setText("Transport cost:");
                calcula_button.setText("Get data");
                idioma_lb.setText("Languaje:");
                R_button.setText("Recession");
                agrega_button.setText("Add product");
                elimina_button.setText("Delete product");
                fe_lb.setText("Economic phase:");
                eliminaTodos.setText("Delete All");
                extrae_button.setText("Get new price");
                recalculaTablaB_button.setText("Recalculate");
                tabajandoCon_lb.setText("Using:");
                actual.setText("Current price");
                promedio.setText("Average price");
                edificios_combo.setModel(new DefaultComboBoxModel(edificios_nombres_EN));
                tipoCalcu_combo.setModel(new DefaultComboBoxModel(tipoCalcu_EN));
                mD.setColumnIdentifiers(nombreColumnas_EN);
                tablaD.setModel(mD);
                mA.setColumnIdentifiers(nombreColumnas_EN);
                tablaA.setModel(mA);
                if (calculado) {
                    newCalcula();
                }
                if (lastEdificioD >= 0) {
                    recalcula();
                }
                edificios_combo.setSelectedIndex(index);
                tipoCalcu_combo.setSelectedIndex(indexCalcu);

            } else {
                español = true;
                super.setTitle("Calculadora de rentabilidad Sim companies");
                cambia_idioma.setText("Español");
                edificio_lb.setText("Edificio:");
                nivel_edificio_lb.setText("Nivel de edificio:");
                pvbm_lb.setText("Porcentaje de venta bajo mercado:");
                produciendo_q_lb.setText("Produciendo calidad:");
                administrativos_lb.setText("Gastos administrativos:");
                bonificacion_lb.setText("Bonificación de producción:");
                costo_transporte_label.setText("Costo transporte:");
                calcula_button.setText("Obtener datos");
                idioma_lb.setText("Idioma:");
                odn_button.setText("Obtener datos de calculadora normal");
                recalcula_button.setText("Recalcula");
                R_button.setText("Recesión");
                agrega_button.setText("Agregar producto");
                elimina_button.setText("Eliminar producto");
                fe_lb.setText("Fase económica:");
                eliminaTodos.setText("Elimina todos");
                extrae_button.setText("Extrae precio nuevo");
                recalculaTablaB_button.setText("Recalcula");
                tabajandoCon_lb.setText("Trabajar con:");
                actual.setText("Precios actuales");
                promedio.setText("Precios promedio");
                edificios_combo.setModel(new DefaultComboBoxModel(edificios_nombres_es));
                tipoCalcu_combo.setModel(new DefaultComboBoxModel(tipoCalcu_es));
                mD.setColumnIdentifiers(nombreColumnas_es);
                tablaD.setModel(mD);
                mA.setColumnIdentifiers(nombreColumnas_es);
                tablaA.setModel(mA);
                if (calculado) {
                    newCalcula();
                }
                if (lastEdificioD >= 0) {
                    recalcula();
                }
                edificios_combo.setSelectedIndex(index);
                tipoCalcu_combo.setSelectedIndex(indexCalcu);

            }
        } catch (NullPointerException e) {
            System.out.println("Error intentando acceder a null" + e);
        }


    }//GEN-LAST:event_cambia_idiomaActionPerformed

    private void edificios_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_edificios_comboActionPerformed
        try {
            int indexSelect = edificios_combo.getSelectedIndex();
            if (indexSelect == 2 || indexSelect == 6 || indexSelect == 13) { //codigos de la cantera, mina y plataforma petrolera
                abundancia_lb.setVisible(true);
                abundanciaP_lb.setVisible(true);
                abundancia_txt.setVisible(true);
                //Calculadora_rentabilidad.super.setSize(875, 414);
            } else {
                abundancia_lb.setVisible(false);
                abundanciaP_lb.setVisible(false);
                abundancia_txt.setVisible(false);
                /*if (tipoCalcu_combo.getSelectedIndex() == 0) {
                    devuelveVentanaInicial();
                }*/
            }

            if (calculado) {
                cleanTabla();
                etf = etfT[edificios_combo.getSelectedIndex()];
                lastEdificioD = etf.getNumeroEdificio();
                newCalcula();
            } else if (etfT[edificios_combo.getSelectedIndex()] != null) {
                cleanTabla();
                etf = etfT[edificios_combo.getSelectedIndex()];
                lastEdificioD = etf.getNumeroEdificio();
                newCalcula();
            }
        } catch (NullPointerException e) {
            System.out.println(e);
        }


    }//GEN-LAST:event_edificios_comboActionPerformed

    private void calculaTodo() throws SQLException {
        //System.out.println("Calculando todos los datos del edificio");
        long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
        TInicio = System.currentTimeMillis();

        try {
            abundancia = 100;  //Experimental, si ha algun campo para abundancia se debera de calcular primerp****
            abundancia_txt.setText("100");
            leeDatosLocales();
            cleanTabla();

            try {
                seleccionaFase();
                oldFase = fase;
                for (int i = 0; i < edificios_code.length; i++) {
                    extraeInfoEdificio(i);
                }
                if (actual.isSelected()) {
                    for (int i = 0; i < po.length; i++) {
                        if (nPEN.existe(i)) {
                            po[i] = new ProductObject(jp.getproducto(i));
                            po[i].actualizaPrecios();
                        }
                    }
                } else {
                    try {
                        estableceConexion(url, usuario, contrasena);
                    } catch (SQLException ex) {
                        Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    for (int i = 0; i < po.length; i++) {
                        if (nPEN.existe(i)) {
                            po[i] = new ProductObject(jp.getproducto(i));
                            po[i].setPrecioPromedio(obtenPreciosPromedioPara(i));
                            po[i].setIsPromedio(true);
                        }
                    }

                }

                lastEdificioD = edificios_combo.getSelectedIndex();
                etf = etfT[edificios_combo.getSelectedIndex()];

                newCalcula();

                calculado = true;
                calculado_todo = true;

            } catch (IOException ex) {
                Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);

                if (español) {
                    JOptionPane.showMessageDialog(this, "Al parecer no hay internet",
                            "Alerta", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "It seems there is not internet",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                }

            }

        } catch (NumberFormatException e) {
            if (español) {
                JOptionPane.showMessageDialog(this, "Revise que haya rellenado todos los campos correctamente",
                        "Alerta", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Check if all fields are filled correctly ",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } finally {
            TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
            tiempo = TFin - TInicio;
            System.out.println("Calculo terminado, tiempo:" + tiempo);
        }

    }

    private void tipoCalcu_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoCalcu_comboActionPerformed
        if (tipoCalcu_combo.getSelectedIndex() == 0 && calculado) {
            cambiaBasico();
        } else if (tipoCalcu_combo.getSelectedIndex() == 1 && calculado) {
            cambiaAvanzado();
        } else if (tipoCalcu_combo.getSelectedIndex() == 1 && !calculado) {
            if (español) {
                JOptionPane.showMessageDialog(this, "Primero debe obtener los datos de internet",
                        "Alerta", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "You have to get the data from internee first",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
            tipoCalcu_combo.setSelectedIndex(0);
        }
    }//GEN-LAST:event_tipoCalcu_comboActionPerformed

    private void seleccionaFase() {
        if (N_button.isSelected()) {
            fase = 1;

        } else if (R_button.isSelected()) {
            fase = 0;
        } else if (B_button.isSelected()) {
            fase = 2;
        }

        jp.setFase(fase);
    }

    private void cambiaFase(int fase) {
        for (int i = 0; i < po.length; i++) {
            if (nPEN.existe(i)) {
                po[i].setJSONproducto(jp.getproducto(i));
            }
        }
    }

    private void actualizaTablaA() {
        DefaultTableModel tb = (DefaultTableModel) tablaD.getModel();
        DefaultTableModel tbA = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                boolean editable = true;
                if (column == 0) {
                    editable = false;
                }
                return editable;
            }
        };
        if (español) {
            tbA.setDataVector(tb.getDataVector(), nCes_vector);
        } else {
            tbA.setDataVector(tb.getDataVector(), nCEN_vector);
        }
        tablaA.setModel(tbA);
        tablaA.setVisible(true);
    }


    private void odn_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_odn_buttonActionPerformed

        if (etfT[edificios_combo.getSelectedIndex()] != null) {
            for (int i = 0; i < etfT[edificios_combo.getSelectedIndex()].getNumeroManeja(); i++) {
                agregaUnProducto(i);
            }
        }
    }//GEN-LAST:event_odn_buttonActionPerformed

    private void recalcula_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recalcula_buttonActionPerformed
        recalcula();
    }//GEN-LAST:event_recalcula_buttonActionPerformed

    public void recibeDatoNewPrecio(float newPrecio) {
        int fila = tablaA.getSelectedRow();
        TableModel tm = tablaA.getModel();
        tm.setValueAt(newPrecio, fila, 1);
        tablaA.setModel(tm);
    }


    private void tablaDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDMouseClicked
        if (evt.getClickCount() == 2) {

            dp.setIdioma(español);
            if (etfT[edificios_combo.getSelectedIndex()] != null) {
                indexL = edificios_combo.getSelectedIndex();
            }
            if (indexL > 0) {
                DefaultTableModel mD = (DefaultTableModel) tablaD.getModel();
                int fila = tablaD.getSelectedRow();
                Object nombre = mD.getValueAt(fila, 0);
                dp.setNombreProducto((String) nombre);

                int[] codigosP = etfT[indexL].getProduceCodigos();
                dp.setAllPO(po);
                dp.setCalidadaAProducir(calidad);
                dp.setPO(po[codigosP[tablaD.getSelectedRow()]]);
                dp.setBonificacion(Float.parseFloat(bonificacion_txt.getText()));
                dp.setGastosAdmin(Float.parseFloat(gastos_administrativos_txt.getText()));
                dp.setSalario(etfT[indexL].getSalariosHora());
                dp.calculaInfo();
                if (indexL == 2 || indexL == 6 || indexL == 13) { //codigos de la cantera, mina y plataforma petrolera      
                    dp.setAbundancia(abundancia);
                } else {
                    dp.setAbundancia(100);
                }
                dp.traduce();
                dp.muestraTodo();
                dp.setVisible(true);
            }
        }
    }//GEN-LAST:event_tablaDMouseClicked

    private void tablaDPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tablaDPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDPropertyChange

    private void N_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_N_buttonActionPerformed
        seleccionaFase();
    }//GEN-LAST:event_N_buttonActionPerformed

    private void agrega_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agrega_buttonActionPerformed

        int rowSelected;
        try {
            rowSelected = tablaD.getSelectedRow();
            agregaUnProducto(rowSelected);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
            if (español) {
                JOptionPane.showMessageDialog(this, "Primero debe seleccionar una fila de la calculadora básica",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "You have to select a row in the basic calculator first",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


    }//GEN-LAST:event_agrega_buttonActionPerformed

    private void elimina_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_elimina_buttonActionPerformed
        eliminaUnProductoSelect();
    }//GEN-LAST:event_elimina_buttonActionPerformed

    private void eliminaUnProductoSelect() {
        try {
            int indexTablaASelec = tablaA.getSelectedRow();
            eliminaProducto(indexTablaASelec);
        } catch (ArrayIndexOutOfBoundsException e) {
            if (español) {
                JOptionPane.showMessageDialog(this, "Primero debe seleccionar una fila de la calculadora básica",
                        "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "You have to select a row in the basic calculator first",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void eliminaTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminaTodosActionPerformed

        if (español) {
            int dialogButton = JOptionPane.showConfirmDialog(null, "Borrarás todo en la calculadora avanzada. ¿Estás seguro?", "Advertencia", JOptionPane.YES_NO_OPTION);
            if (dialogButton == JOptionPane.YES_OPTION) {
                eliminaTodo();
            }
        } else {
            int dialogButton = JOptionPane.showConfirmDialog(null, "You will delete all from advanced calculator. Are you sure?", "WARNING", JOptionPane.YES_NO_OPTION);
            if (dialogButton == JOptionPane.YES_OPTION) {
                eliminaTodo();
            }
        }
    }//GEN-LAST:event_eliminaTodosActionPerformed

    private void eliminaTodo() {
        cleanTabla2();
        POIlist = new ArrayList<ProductObject>();
        dec = new ArrayList<DatosEntrantesCalcu>();
    }

    private void tablaAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAMouseClicked
        if (evt.getClickCount() == 2) {
            dp2.setIdioma(español);
            int fila = tablaA.getSelectedRow();
            ProductObject productoActual = POIlist.get(fila);
            DefaultTableModel mA = (DefaultTableModel) tablaA.getModel();
            int indexEdificio = dec.get(fila).getEdificio();

            Object nombre = mA.getValueAt(fila, 0);
            dp2.setNombreProducto((String) nombre);
            dp2.setAllPO(po);
            dp2.setCalidadaAProducir(dec.get(fila).getCalidad());
            dp2.setPO(productoActual);
            dp2.setAbundancia(dec.get(fila).getAbundancia());
            dp2.setBonificacion(dec.get(fila).getBonificacion());
            dp2.setGastosAdmin(dec.get(fila).getGastosAdministrativos());
            dp2.setSalarioEdificio(etfT[dec.get(fila).getEdificio()].getSalariosHora());
            dp2.calculaInfo();
            dp2.traduce();
            dp2.muestraTodo();
            dp2.setVisible(true);
        }
    }//GEN-LAST:event_tablaAMouseClicked

    private void extrae_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extrae_buttonActionPerformed
        try {
            recibeDatoNewPrecio(dp2.getNewPrecio());
        } catch (ArrayIndexOutOfBoundsException e) {
            if (español) {
                JOptionPane.showMessageDialog(this, "Primero debe de seleccionar una fila de la calculadora avanzada",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "You have select a row from the advanced calculator first",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_extrae_buttonActionPerformed

    private void recalculaTablaB_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recalculaTablaB_buttonActionPerformed
        if (calculado || calculado_todo) {
            try {
                if (oldFase != fase) {
                    cambiaFase(fase);
                    oldFase = fase;
                }
            } catch (Exception e) {
                System.out.println(e);
            }

            leeDatosLocales();
            cleanTabla();
            newCalcula();

        } else {
            if (español) {
                JOptionPane.showMessageDialog(this, "Primero debe de calcular los datos para algun edificio",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "You have to calculate the data for some building first",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }//GEN-LAST:event_recalculaTablaB_buttonActionPerformed

    private void tablaAKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tablaAKeyPressed
        if (evt.getKeyCode() == evt.VK_DELETE) {
            eliminaUnProductoSelect();
        }
    }//GEN-LAST:event_tablaAKeyPressed

    private void R_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_R_buttonActionPerformed
        seleccionaFase();
    }//GEN-LAST:event_R_buttonActionPerformed

    private void abundancia_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abundancia_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_abundancia_txtActionPerformed

    private void Pvbm_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Pvbm_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Pvbm_txtActionPerformed

    private void B_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_buttonActionPerformed
        seleccionaFase();
    }//GEN-LAST:event_B_buttonActionPerformed

    private void eliminaProducto(int indexTablaASelec) {
        DefaultTableModel mA = (DefaultTableModel) tablaA.getModel();
        mA.removeRow(indexTablaASelec);
        POIlist.remove(indexTablaASelec);
        dec.remove(indexTablaASelec);
        tablaA.setModel(mA);
    }

    private void agregaUnProducto(int rS) {
        try {
            DefaultTableModel mD = (DefaultTableModel) tablaD.getModel();
            int rowSelected = rS;
            int indiceLocal;
            if (etfT[edificios_combo.getSelectedIndex()] != null) {
                indiceLocal = edificios_combo.getSelectedIndex();  //***********************************
            } else {
                indiceLocal = lastEdificioD;
            }

            int idProducto = etfT[indiceLocal].getCodigoProduceNumero(rowSelected);
            ProductObject poi = po[idProducto];
            System.out.println(etfT[indiceLocal].getSalariosHora());
            DefaultTableModel mA = (DefaultTableModel) tablaA.getModel();
            Object[] fila = new Object[6];
            fila[0] = mD.getValueAt(rowSelected, 0);
            fila[1] = mD.getValueAt(rowSelected, 1);
            fila[2] = mD.getValueAt(rowSelected, 2);
            fila[3] = mD.getValueAt(rowSelected, 3);
            fila[4] = mD.getValueAt(rowSelected, 4);
            fila[5] = mD.getValueAt(rowSelected, 5);
            mA.addRow(fila);

            POIlist.add(poi);
            agrega_a_lista_DEC();

        } catch (NumberFormatException e) {
            System.out.println("El campo esta mal " + e);
        }
    }

    private void agrega_a_lista_DEC() {
        DatosEntrantesCalcu decAux = new DatosEntrantesCalcu();
        decAux.setGastosAdministrativos(gastos_administrativos);
        decAux.setBonificacion(bonificacion_produccion);
        decAux.setCostoTransporte(precio_transporte);
        decAux.setFaseEconomica(fase);
        decAux.setCalidad(calidad);
        decAux.setPorcentaje_bajo_mercado(porcentaje_bajoMercado);
        decAux.setNivel_edificio(nivel_edificio);
        decAux.setEdificio(lastEdificioD);  //Checar esta parte
        decAux.setAbundancia(abundancia);
        dec.add(decAux);

    }

    private void recalcula() {
        try {
            TableModel tm = tablaA.getModel();
            leeDatosLocales();
            DefaultTableModel modelo = new DefaultTableModel();
            Object[] fila = new Object[6];

            if (español) {
                modelo.setColumnIdentifiers(nombreColumnas_es);
            } else {
                modelo.setColumnIdentifiers(nombreColumnas_EN);
            }

            for (int i = 0; i < tm.getRowCount(); i++) {//tablaA.getRowCount()
                ProductObject materiaActual = POIlist.get(i);   //po[codigosEdificio2[i]];
                float ganancia_horaM;
                float ganancia_horaC;
                Object ph = tm.getValueAt(i, 3);
                Object cmQ = tm.getValueAt(i, 1);
                Object pm = tm.getValueAt(i, 2);

                float ProduccionHora = Float.parseFloat(ph.toString());
                float costoMprimaQ = Float.parseFloat(cmQ.toString());
                float precioMercado = Float.parseFloat(pm.toString());
                float porcentajeBM = dec.get(i).getPorcentaje_bajo_mercado();
                float precioT = dec.get(i).getCostoTransporte();

                ganancia_horaC = (float) ((((precioMercado * (1 - (porcentajeBM / 100)) - costoMprimaQ)) - materiaActual.getTransporte() * precioT * 0.5) * ProduccionHora);
                ganancia_horaM = (float) ((((precioMercado * 0.97 - costoMprimaQ)) - materiaActual.getTransporte() * precioT) * ProduccionHora);

                fila[0] = tm.getValueAt(i, 0);
                fila[1] = costoMprimaQ;
                fila[2] = precioMercado;
                fila[3] = ProduccionHora;
                if (dec.get(i).getPorcentaje_bajo_mercado() == 0) {
                    fila[4] = "0";
                } else {
                    fila[4] = ganancia_horaM;
                }
                fila[5] = ganancia_horaC;
                modelo.addRow(fila);
            }

            tablaA.setModel(modelo);

        } catch (NullPointerException e) {
            System.out.println("Error intentando recalcular" + e);
        } catch (NumberFormatException e) {
            System.out.println(e);
            if (español) {
                JOptionPane.showMessageDialog(this, "Revise que los numeros esten escritos de forma correcta",
                        "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Check your numbers are correctly writted",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }

        }

    }

    private void cambiaAvanzado() {
        Calculadora_rentabilidad.super.setSize(xFrame, 640);//877, 720);//(874, 572);  
        muestraBotonesCalculadoraAvanzada();

    }

    private void cambiaBasico() {
        devuelveVentanaInicial();
        ocultaBotonesCalculadoraAvanzada();
        newCalcula();

    }

    private void leeDatosLocales() {
        bonificacion_produccion = Float.parseFloat(bonificacion_txt.getText());
        nivel_edificio = Integer.valueOf(nivel_edificio_txt.getText());
        gastos_administrativos = Float.parseFloat(gastos_administrativos_txt.getText());
        porcentaje_bajoMercado = Float.parseFloat(Pvbm_txt.getText());  //Modificado para que pudiese aceptar porcentajes en float
        precio_transporte = Float.parseFloat(costo_transporte_txt.getText());
        calidad = calidad_box.getSelectedIndex();
        int indexSelect = edificios_combo.getSelectedIndex();
        if (indexSelect == 2 || indexSelect == 6 || indexSelect == 13) { //codigos de la cantera, mina y plataforma petrolera             
            abundancia = Float.parseFloat(abundancia_txt.getText());
        }
    }

    private void cleanTabla() {
        DefaultTableModel tb = (DefaultTableModel) tablaD.getModel();
        int a = tablaD.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount() - 1);
        }
    }

    private void cleanTabla2() {
        DefaultTableModel tb = (DefaultTableModel) tablaA.getModel();
        int a = tablaA.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount() - 1);
        }
    }

    private void calculaUno() {
        System.out.println("cargando............");
        /*long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
        TInicio = System.currentTimeMillis();*/

        try {
            //if (!calculado) {
            //    calculaTodo();
            // } else {
            //nPEN.getPerdidos();///Para pruebas
            leeDatosLocales();
            cleanTabla();
            try {
                seleccionaFase();
                int index = edificios_combo.getSelectedIndex();
                extraeInfoEdificio(index);
                etf = etfT[index];
                for (int i = 0; i < po.length; i++) {
                    if (po[i] != null) {
                        po[i].actualizaPrecios();
                    }
                }
                lastEdificioD = index;
                newCalcula();

            } catch (IOException ex) {
                Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);

                if (español) {
                    JOptionPane.showMessageDialog(this, "Al parecer no hay internet",
                            "Alerta", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "It seems there is not internet",
                            "Warning", JOptionPane.ERROR_MESSAGE);
                }

            }
            // }
        } catch (NumberFormatException e) {
            if (español) {
                JOptionPane.showMessageDialog(this, "Revise que haya rellenado todos los campos correctamente",
                        "Alerta", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Check if all fields are filled correctly ",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } finally {
            /*TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
                tiempo = TFin - TInicio;
                System.out.println("Calculo terminado, tiempo:" +  tiempo );*/
        }
    }

    private void newCalcula() {
        leeDatosLocales();
        seleccionaFase();
        codigosEdificio = etf.getProduceCodigos();
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        if (español) {
            modelo.setColumnIdentifiers(nombreColumnas_es);
        } else {
            modelo.setColumnIdentifiers(nombreColumnas_EN);
        }

        Object[] fila = new Object[6];

        for (int i = 0; i < etf.getNumeroManeja(); i++) {
            ProductObject materiaActual = po[codigosEdificio[i]];
            float costoMprimasQ = 0;
            boolean bandera = false;

            if (lastEdificioD < investigaciones) {// Para cuando el edificio no corresponda al de investigacion
                for (int j = 0; j < materiaActual.getNumeroMateriasPrimas(); j++) {
                    ProductObject materia_prima = po[materiaActual.getIDmateria(j)];
                    if (calidad == 0) {
                        costoMprimasQ = materia_prima.getPrecio(calidad) * materiaActual.getAmount(j) + costoMprimasQ;
                        if (materia_prima.getPrecio(calidad) == -1) {
                            bandera = true;
                        }
                    } else if (calidad > 0) {
                        costoMprimasQ = materia_prima.getPrecio(calidad - 1) * materiaActual.getAmount(j) + costoMprimasQ;
                        if (materia_prima.getPrecio(calidad - 1) == -1) {
                            bandera = true;
                        }
                    }

                }

            } else {
                costoMprimasQ = 0;
            }

            float salariosHora = etf.getSalariosHora();
            float ProduccionHora = (bonificacion_produccion / 100 + 1) * materiaActual.getProduccionHora();
            int indexSelect = etf.getNumeroEdificio();

            if (indexSelect == 2 || indexSelect == 6 || indexSelect == 13) { //codigos de la cantera, mina y plataforma petrolera             
                abundancia = Float.parseFloat(abundancia_txt.getText());      //aguas con esto          
                ProduccionHora = ProduccionHora * (abundancia / 100);
            }

            costoMprimasQ = ((salariosHora / ProduccionHora) + costoMprimasQ);
            float costo_administrativo = (salariosHora / ProduccionHora) * (gastos_administrativos / 100);
            costoMprimasQ = costo_administrativo + costoMprimasQ;
            float gananciaHoraContrato = (float) 0.00;
            float gananciaHoraMercado = (float) 0.00;

            if (calidad > -1) {
                gananciaHoraContrato = (float) ((((materiaActual.getPrecio(calidad) * (1 - (porcentaje_bajoMercado / 100)) - costoMprimasQ)) - materiaActual.getTransporte() * precio_transporte * 0.5) * ProduccionHora);
                gananciaHoraMercado = (float) ((((materiaActual.getPrecio(calidad) * 0.97 - costoMprimasQ)) - materiaActual.getTransporte() * precio_transporte) * ProduccionHora);
            }

            if (español) {
                fila[0] = materiaActual.getNombre();
            } else {
                fila[0] = etf.getEdificioInfo().getJSONArray("doesProduce").getJSONObject(i).getString("name");
            }
            //Para filtrar y que no aparezcan resultados negativos
            if (!bandera) {
                fila[1] = costoMprimasQ;
            } else {
                fila[1] = 0;
            }
            fila[2] = materiaActual.getPrecio(calidad);
            fila[3] = ProduccionHora * nivel_edificio;
            if (materiaActual.getPrecio(calidad) > 0) {
                fila[4] = gananciaHoraMercado * nivel_edificio;
                fila[5] = gananciaHoraContrato * nivel_edificio;
            } else {
                fila[2] = 0;
                fila[4] = 0;
                fila[5] = 0;
            }
            modelo.addRow(fila);
        }

        tablaD.setModel(modelo);

    }

    private void estableceConexion(String url, String usuario, String contrasena) throws SQLException {
        cn = DriverManager.getConnection(url, usuario, contrasena);
    }

    private float[] obtenPreciosPromedioPara(int id) throws SQLException {
        float[] preciosAux = new float[6];

        PreparedStatement pst = null;
        pst = cn.prepareStatement("SELECT precio_promedio_Q0,precio_promedio_Q1,precio_promedio_Q2,precio_promedio_Q3,precio_promedio_Q4,precio_promedio_Q5 FROM aproductos WHERE ID = " + id + " ;");
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            for (int i = 1; i <= 6; i++) {
                preciosAux[i - 1] = rs.getFloat(i);
            }
        }
        pst.close();

        return preciosAux;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //para darle al jfile chooser y el programa el aspecto de windows 
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Calculadora_rentabilidad.class.getName()).log(Level.SEVERE, null, ex);
                }
                new Calculadora_rentabilidad().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton B_button;
    private javax.swing.JRadioButton N_button;
    private javax.swing.JTextField Pvbm_txt;
    private javax.swing.JRadioButton R_button;
    private javax.swing.JLabel abundanciaP_lb;
    private javax.swing.JLabel abundancia_lb;
    private javax.swing.JTextField abundancia_txt;
    private javax.swing.JRadioButton actual;
    private javax.swing.JLabel administrativos_lb;
    private javax.swing.JButton agrega_button;
    private javax.swing.JLabel bonificacion_lb;
    private javax.swing.JTextField bonificacion_txt;
    private javax.swing.JButton calcula_button;
    private javax.swing.JComboBox<String> calidad_box;
    private javax.swing.JButton cambia_idioma;
    private javax.swing.JLabel costo_transporte_label;
    private javax.swing.JTextField costo_transporte_txt;
    private javax.swing.JLabel edificio_lb;
    private javax.swing.JComboBox<String> edificios_combo;
    private javax.swing.JButton eliminaTodos;
    private javax.swing.JButton elimina_button;
    private javax.swing.JButton extrae_button;
    private javax.swing.ButtonGroup fase_grupo;
    private javax.swing.JLabel fe_lb;
    private javax.swing.JTextField gastos_administrativos_txt;
    private javax.swing.JLabel idioma_lb;
    private javax.swing.JButton informacion;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JLabel nivel_edificio_lb;
    private javax.swing.JTextField nivel_edificio_txt;
    private javax.swing.JButton odn_button;
    private javax.swing.ButtonGroup precio_option;
    private javax.swing.JLabel produciendo_q_lb;
    private javax.swing.JRadioButton promedio;
    private javax.swing.JLabel pvbm_lb;
    private javax.swing.JButton recalculaTablaB_button;
    private javax.swing.JButton recalcula_button;
    private javax.swing.JLabel tabajandoCon_lb;
    private javax.swing.JTable tablaA;
    private javax.swing.JTable tablaD;
    private javax.swing.JComboBox<String> tipoCalcu_combo;
    // End of variables declaration//GEN-END:variables
}
