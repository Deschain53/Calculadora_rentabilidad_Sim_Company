/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principales;

import clases.CalculoRetail;
import clases.DatosEntrantesCalcu;
import clases.ExtraeTodoEdificio;
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
import clases.ProductObjectIndependent;
import frames.DetalleProductos;
import frames.DetalleProductosEditable;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import org.json.JSONObject;

/**
 *
 * @author donyo
 */
public class RetailCalculator extends javax.swing.JFrame {

    //Proxima actualizaion: cambiar porcentaje de venta a mercado de int a float, agregar version Avanzada.
    public String[] edificios_nombres_es = {"Tienda de comestibles", "Ferretería", "Tienda de moda", "Estación de servicio",
        "Tienda de electrónica", "Concesionario"};
    public String[] edificios_nombres_EN = {"Grocery store", "Hardware store", "Fashion store", "Service station",
        "Electronic shop", "Concessionaire"};  ///Verificar las traducciones
    public String[] edificios_code = {"G", "d", "H", "A", "C", "2"};
    String[] nombreColumnas_es = {"Producto", "Precio de compra", "Precio promedio de venta", "Unidades/hora", "Ganancia/hora"};
    String[] nombreColumnas_EN = {"Product", "Purchase price", "Average sell price", "Units/hour", "Profit/hour"};
    String[] tipoCalcu_es = {"Calculadora básica", "Calculadora avanzada"};
    String[] tipoCalcu_EN = {"Basic calculator", "Advanced calculator"};

    float bonificacion_ventas = 0;
    int nivel_edificio = 0;
    float gastos_administrativos = 0;
    float pcbm = 0;  //Porcentaje de compra bajo mercado
    int calidad = 0;
    int[] codigosEdificio = null;
    int lastEdificioD = -1;   //Guarda el index del ultimo edificio que esta en la tablaD
    String[] nombresEdificio = null;
    ExtraeTodoEdificio etf;
    ProductObject[] po ;
    ExtraeTodoEdificio[] etfT = new ExtraeTodoEdificio[edificios_code.length];
    int maxCalidad = 7;
    boolean español;
    boolean calculado;
    boolean calculado_todo;
    int indexL = -1;    //El index para los Frame de información

    DefaultTableModel modeloAvanzado;
    Vector nCes_vector;
    Vector nCEN_vector;
    int fase = 1;
    NombresPEN nPEN;
    ArrayList<ProductObject> POIlist = new ArrayList<ProductObject>(); // Para guardar lo de la tabla dos
    ArrayList<String> porcentajeBM_lista = new ArrayList<String>();
    ArrayList<Integer> calidad_list = new ArrayList<Integer>();
    CalculoRetail cr;

    ArrayList<DatosEntrantesCalcu> dec = new ArrayList<DatosEntrantesCalcu>();

    public RetailCalculator() {
        initComponents();
        devuelveVentanaInicial();
        //this.setSize(875, 412);    ///(874, 485);        //(790, 485);880, 670)
        this.setResizable(false); //*************************devolver a false despues
        this.setTitle("Calculadora de rentabilidad Retail Sim companies");
        this.setLocationRelativeTo(null);
        nivel_edificio_txt.setText("");
        Pvbm_txt.setText("");
        gastos_administrativos_txt.setText("");
        bonificacion_txt.setText("");
        edificios_combo.setModel(new DefaultComboBoxModel(edificios_nombres_es));
        tipoCalcu_combo.setModel(new DefaultComboBoxModel(tipoCalcu_es));
        edificios_combo.setSelectedIndex(4);
        español = true;
        calculado = false;
        calculado_todo = false;
        odn_button.setVisible(false);
        tablaA.setVisible(false);
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
        po = new ProductObject[200];
        //Temporal, en lo que se agrega la calculadora avanzada********       
        tipoCalcu_combo.setVisible(true);
        ocultaCalculadoraAvanzada();
        calculaTodo_button.setVisible(false);
    }

    public void ocultaCalculadoraAvanzada() {
        odn_button.setVisible(false);
        agrega_button.setVisible(false);
        recalcula_button.setVisible(false);
        extrae_button.setVisible(false);
        elimina_button.setVisible(false);
        eliminaTodos.setVisible(false);
        tablaA.setVisible(false);
    }

    public void muestraCalculadoraAvanzada() {
        odn_button.setVisible(true);
        agrega_button.setVisible(true);
        recalcula_button.setVisible(true);
        extrae_button.setVisible(true);
        elimina_button.setVisible(true);
        eliminaTodos.setVisible(true);
        tablaA.setVisible(true);
    }

    public void devuelveVentanaInicial() {
        RetailCalculator.super.setSize(875, 412);//875, 412);
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
        tipo_precio = new javax.swing.ButtonGroup();
        nivel_edificio_txt = new javax.swing.JTextField();
        nivel_edificio_lb = new javax.swing.JLabel();
        administrativos_lb = new javax.swing.JLabel();
        gastos_administrativos_txt = new javax.swing.JTextField();
        bonificacion_lb = new javax.swing.JLabel();
        bonificacion_txt = new javax.swing.JTextField();
        pcbm_lb = new javax.swing.JLabel();
        Pvbm_txt = new javax.swing.JTextField();
        calidad_box = new javax.swing.JComboBox<>();
        produciendo_q_lb = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaD = new javax.swing.JTable();
        edificio_lb = new javax.swing.JLabel();
        edificios_combo = new javax.swing.JComboBox<>();
        calcula_button = new javax.swing.JButton();
        informacion = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cambia_idioma = new javax.swing.JButton();
        idioma_lb = new javax.swing.JLabel();
        calculaTodo_button = new javax.swing.JButton();
        tipoCalcu_combo = new javax.swing.JComboBox<>();
        odn_button = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaA = new javax.swing.JTable();
        recalcula_button = new javax.swing.JButton();
        R_button = new javax.swing.JRadioButton();
        N_button = new javax.swing.JRadioButton();
        B_button = new javax.swing.JRadioButton();
        fe_lb = new javax.swing.JLabel();
        agrega_button = new javax.swing.JButton();
        elimina_button = new javax.swing.JButton();
        eliminaTodos = new javax.swing.JButton();
        extrae_button = new javax.swing.JButton();
        recalculaTablaB_button = new javax.swing.JButton();
        precio_promedio_jrb = new javax.swing.JRadioButton();
        mejor_precio_jrb = new javax.swing.JRadioButton();

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

        bonificacion_lb.setText("Bonificación de venta:");

        bonificacion_txt.setText("jTextField3");

        pcbm_lb.setText("Porcentaje de compra bajo mercado:");

        Pvbm_txt.setText("jTextField4");

        calidad_box.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0", "1", "2", "3", "4", "5", "6", "7" }));
        calidad_box.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calidad_boxActionPerformed(evt);
            }
        });

        produciendo_q_lb.setText("Vendiendo calidad:");

        tablaD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto", "Precio de compra", "Precio promedio de venta", "Unidades/hora", "Ganancia/hora"
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
        }

        edificio_lb.setText("Edificio:");

        edificios_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        edificios_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                edificios_comboActionPerformed(evt);
            }
        });

        calcula_button.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        calcula_button.setText("Calcula");
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

        cambia_idioma.setText("Español");
        cambia_idioma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cambia_idiomaActionPerformed(evt);
            }
        });

        idioma_lb.setText("Idioma:");

        calculaTodo_button.setText("Calcula para todos los edificios");
        calculaTodo_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calculaTodo_buttonActionPerformed(evt);
            }
        });

        tipoCalcu_combo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tipoCalcu_combo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tipoCalcu_comboActionPerformed(evt);
            }
        });

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
                "Producto", "Precio de compra", "Precio de venta", "Unidades/hora", "Ganancia/hora"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
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
        }

        recalcula_button.setText("Recalcula");
        recalcula_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recalcula_buttonActionPerformed(evt);
            }
        });

        fase_grupo.add(R_button);
        R_button.setText("Recesión");

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

        fe_lb.setText("Fase económica:");

        agrega_button.setText("Agregar producto");
        agrega_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agrega_buttonActionPerformed(evt);
            }
        });

        elimina_button.setText("Eliminar producto");
        elimina_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                elimina_buttonActionPerformed(evt);
            }
        });

        eliminaTodos.setText("Elimina todos");
        eliminaTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminaTodosActionPerformed(evt);
            }
        });

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

        tipo_precio.add(precio_promedio_jrb);
        precio_promedio_jrb.setSelected(true);
        precio_promedio_jrb.setText("Precio promedio");
        precio_promedio_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precio_promedio_jrbActionPerformed(evt);
            }
        });

        tipo_precio.add(mejor_precio_jrb);
        mejor_precio_jrb.setText("Mejor precio");
        mejor_precio_jrb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mejor_precio_jrbActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(pcbm_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(Pvbm_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(produciendo_q_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(calidad_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(calcula_button, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel7))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(recalculaTablaB_button))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(nivel_edificio_lb)
                                .addGap(18, 18, 18)
                                .addComponent(nivel_edificio_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(edificio_lb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edificios_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(B_button)
                            .addComponent(N_button)
                            .addComponent(fe_lb)
                            .addComponent(R_button, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(administrativos_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(gastos_administrativos_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(bonificacion_lb)
                                        .addGap(18, 18, 18)
                                        .addComponent(bonificacion_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(precio_promedio_jrb)
                                .addGap(18, 18, 18)
                                .addComponent(mejor_precio_jrb)
                                .addGap(6, 6, 6))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(odn_button)
                                .addGap(18, 18, 18)
                                .addComponent(agrega_button)
                                .addGap(18, 18, 18)
                                .addComponent(recalcula_button)
                                .addGap(18, 18, 18)
                                .addComponent(extrae_button))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(calculaTodo_button)
                                .addGap(83, 83, 83)
                                .addComponent(tipoCalcu_combo, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(idioma_lb)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cambia_idioma)
                                .addGap(57, 57, 57)
                                .addComponent(informacion, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(elimina_button)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(eliminaTodos))))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(32, 32, 32))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(nivel_edificio_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(nivel_edificio_lb)
                                .addComponent(edificio_lb))
                            .addComponent(edificios_combo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pcbm_lb)
                            .addComponent(Pvbm_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(calidad_box, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(produciendo_q_lb)
                            .addComponent(calcula_button)
                            .addComponent(recalculaTablaB_button)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(fe_lb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(R_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(N_button)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(B_button))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(administrativos_lb)
                            .addComponent(gastos_administrativos_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bonificacion_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bonificacion_lb)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(precio_promedio_jrb)
                            .addComponent(mejor_precio_jrb))))
                .addGap(21, 21, 21)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cambia_idioma)
                    .addComponent(idioma_lb)
                    .addComponent(informacion)
                    .addComponent(calculaTodo_button)
                    .addComponent(tipoCalcu_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(odn_button)
                    .addComponent(recalcula_button)
                    .addComponent(agrega_button)
                    .addComponent(elimina_button)
                    .addComponent(eliminaTodos)
                    .addComponent(extrae_button))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                bonificacion_ventas = Float.parseFloat(bonificacion_txt.getText());
                nivel_edificio = Integer.valueOf(nivel_edificio_txt.getText());
                gastos_administrativos = Float.parseFloat(gastos_administrativos_txt.getText());
                pcbm = Integer.valueOf(Pvbm_txt.getText());
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
        System.out.println("cargando............");
        long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
        TInicio = System.currentTimeMillis();
        
        cleanTabla();       
        int index = edificios_combo.getSelectedIndex();
        calcula_edificio(index);
        newCalcula();
        
        TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
        tiempo = TFin - TInicio;
        System.out.println("Calculo terminado, tiempo:" + tiempo);
    }//GEN-LAST:event_calcula_buttonActionPerformed

    private void calcula_edificio(int index) {
        try {
            seleccionaFase();
            leeDatosLocales();
            try {
                //seleccionaFase();

                etf = new ExtraeTodoEdificio(edificios_code[index]);
                etf.setNumeroEdificio(index);
                etf.setUrlEconomia(fase);
                etf.extraeVende();
                //etf.setNumeroEdificio(index);
                etfT[edificios_combo.getSelectedIndex()] = etf; //Para guardar en memoria los datos del edificio extraido
                etfT[index].setProductObject(po);
                etfT[index].extraeVende();
                po = etfT[index].getProducObjectArray();

                for (int i = 0; i < etfT[index].getNumeroVende(); i++) {  // i <etf.codigos.length  o i < etf.NumeroManejaT
                    if (po[etfT[index].getCodigoVendeNumero(i)] != null) {  //po[codigo actual]
                        po[etfT[index].getCodigoVendeNumero(i)].actualizaPrecios();  //Leyendo los precios mas recientes del mercado
                    }
                }
                lastEdificioD = index;
                calculado = true;
            } catch (IOException ex) {
                Logger.getLogger(RetailCalculator.class.getName()).log(Level.SEVERE, null, ex);

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
                JOptionPane.showMessageDialog(this, "Check that all fields are filled correctly ",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void leeDatosLocales() {
        bonificacion_ventas = Float.parseFloat(bonificacion_txt.getText());
        nivel_edificio = Integer.valueOf(nivel_edificio_txt.getText());
        gastos_administrativos = Float.parseFloat(gastos_administrativos_txt.getText());
        pcbm = Float.parseFloat(Pvbm_txt.getText());
        calidad = calidad_box.getSelectedIndex();
    }

    private void informacionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_informacionActionPerformed
        if (español) {
            JOptionPane.showMessageDialog(this, "                          "
                    + "Version 1.0.2 \n"
                    + "Creado por: Jorge Adrián Lucas Sánchez \n"
                    + "Nick: Lucas Engines \n"
                    + "Programa creado para uso personal del autor,\n"
                    + " el mismo no se hace responsable de otros \n"
                    + "usos o fines que se le pueda dar.", "Acerca de", JOptionPane.PLAIN_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "                          "
                    + "Version 1.0.2 \n"
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
                super.setTitle("Sim companies Retail profit calculator");
                odn_button.setText("Get data from Basic calculator");
                recalcula_button.setText("Recalculate");
                cambia_idioma.setText("English");
                nivel_edificio_lb.setText("Building level:");
                pcbm_lb.setText("Percentage of buying under market:");
                produciendo_q_lb.setText("Selling quality:");
                administrativos_lb.setText("Administration overhead:");
                bonificacion_lb.setText("Selling speed bonus:");
                calcula_button.setText("Calculate");
                idioma_lb.setText("Languaje:");
                R_button.setText("Recession");
                edificio_lb.setText("Building:");
                calculaTodo_button.setText("Add all product building");
                agrega_button.setText("Add product");
                elimina_button.setText("Delete product");
                fe_lb.setText("Economic phase");
                eliminaTodos.setText("Delete All");
                extrae_button.setText("Get new price");
                recalculaTablaB_button.setText("Recalculate");
                precio_promedio_jrb.setText("Average price");
                mejor_precio_jrb.setText("Best price");

                if (precio_promedio_jrb.isSelected()) {
                    nombreColumnas_EN[2] = "Average sell price";
                } else {
                    nombreColumnas_EN[2] = "Best price to sell";
                }

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
                super.setTitle("Calculadora de rentabilidad Retail Sim companies");
                cambia_idioma.setText("Español");
                nivel_edificio_lb.setText("Nivel de edificio:");
                pcbm_lb.setText("Porcentaje de venta bajo mercado:");
                produciendo_q_lb.setText("Vendiendo calidad:");
                administrativos_lb.setText("Gastos administrativos:");
                bonificacion_lb.setText("Bonificación de ventas:");
                calcula_button.setText("Calcula");
                idioma_lb.setText("Idioma:");
                odn_button.setText("Obtener datos de calculadora normal");
                recalcula_button.setText("Recalcula");
                R_button.setText("Recesión");
                edificio_lb.setText("Edificio:");
                calculaTodo_button.setText("Agregar todos los productos del edificio");
                agrega_button.setText("Agregar producto");
                elimina_button.setText("Eliminar producto");
                fe_lb.setText("Fase económica");
                eliminaTodos.setText("Elimina todos");
                extrae_button.setText("Extrae precio nuevo");
                recalculaTablaB_button.setText("Recalcula");
                precio_promedio_jrb.setText("Precio promedio");
                mejor_precio_jrb.setText("Mejor precio");

                if (precio_promedio_jrb.isSelected()) {
                    nombreColumnas_es[2] = "Precio promedio de venta";
                } else {
                    nombreColumnas_es[2] = "Mejor precio de venta";
                }

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
                RetailCalculator.super.setSize(875, 414);
            } else {
                if (tipoCalcu_combo.getSelectedIndex() == 0) {
                    devuelveVentanaInicial();
                }
            }

            if (calculado_todo) {
                cleanTabla();
                etf = etfT[edificios_combo.getSelectedIndex()];
                newCalcula();
                lastEdificioD = etf.getNumeroEdificio();
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

    private void calculaTodo_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calculaTodo_buttonActionPerformed

        System.out.println("Calculando todos los datos del edificio");
        long TInicio, TFin, tiempo; //Variables para determinar el tiempo de ejecución
        TInicio = System.currentTimeMillis();

        for (int i = 0; i < edificios_combo.getItemCount(); i++) {
            calcula_edificio(i);       
        }
        etf = etfT[edificios_combo.getSelectedIndex()];
        newCalcula();
        
        TFin = System.currentTimeMillis(); //Tomamos la hora en que finalizó el algoritmo y la almacenamos en la variable T
        tiempo = TFin - TInicio;
        System.out.println("Calculo terminado, tiempo:" +  tiempo );


        
    }//GEN-LAST:event_calculaTodo_buttonActionPerformed

    private void tipoCalcu_comboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tipoCalcu_comboActionPerformed
        if (tipoCalcu_combo.getSelectedIndex() == 0 && calculado) {
            cambiaBasico();
        } else if (tipoCalcu_combo.getSelectedIndex() == 1 && calculado) {
            cambiaAvanzado();
        } else if (tipoCalcu_combo.getSelectedIndex() == 1 && !calculado) {
            if (español) {
                JOptionPane.showMessageDialog(this, "Primero debe calcular los datos para algun edificio",
                        "Alerta", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "You have to calculate the data for any building first",
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
            for (int i = 0; i < etfT[edificios_combo.getSelectedIndex()].getNumeroVende(); i++) {
                agregaUnProducto(i);
            }
        }
        //actualizaTablaA();
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

    }//GEN-LAST:event_tablaDMouseClicked

    private void tablaDPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_tablaDPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDPropertyChange

    private void N_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_N_buttonActionPerformed
        // TODO add your handling code here:
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
        porcentajeBM_lista = new ArrayList<String>();
        calidad_list = new ArrayList<Integer>();
    }

    private void tablaAMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaAMouseClicked

    }//GEN-LAST:event_tablaAMouseClicked

    private void extrae_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_extrae_buttonActionPerformed
        try {
            //recibeDatoNewPrecio(dp2.getNewPrecio());
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
            leeDatosLocales();
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

    private void mejor_precio_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mejor_precio_jrbActionPerformed
        if (calculado || calculado_todo) {
            leeDatosLocales();
            newCalcula();
        }
    }//GEN-LAST:event_mejor_precio_jrbActionPerformed

    private void precio_promedio_jrbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precio_promedio_jrbActionPerformed
        if (calculado || calculado_todo) {
            leeDatosLocales();
            newCalcula();
        }
    }//GEN-LAST:event_precio_promedio_jrbActionPerformed

    private void eliminaProducto(int indexTablaASelec) {
        DefaultTableModel mA = (DefaultTableModel) tablaA.getModel();
        mA.removeRow(indexTablaASelec);
        POIlist.remove(indexTablaASelec);
        porcentajeBM_lista.remove(indexTablaASelec);  //experimental
        calidad_list.remove(indexTablaASelec);  //experimental
        //System.out.println("Eliminado " + indexTablaASelec + " ;POIl numero: " + POIlist.size()+ mA.getRowCount());   
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

            int idProducto = etfT[indiceLocal].getCodigoVendeNumero(rowSelected);
            ProductObject poi = po[idProducto];
            System.out.println(etfT[indiceLocal].getSalariosHora());
            DefaultTableModel mA = (DefaultTableModel) tablaA.getModel();
            Object[] fila = new Object[6];
            fila[0] = mD.getValueAt(rowSelected, 0);
            fila[1] = mD.getValueAt(rowSelected, 1);
            fila[2] = mD.getValueAt(rowSelected, 2);
            fila[3] = mD.getValueAt(rowSelected, 3);
            fila[4] = mD.getValueAt(rowSelected, 4);
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
        decAux.setBonificacion(bonificacion_ventas);
        //decAux.setCostoTransporte(precio_transporte);
        decAux.setFaseEconomica(fase);
        decAux.setCalidad(calidad);
        decAux.setPorcentaje_bajo_mercado(pcbm);
        decAux.setNivel_edificio(nivel_edificio);
        if (etfT[edificios_combo.getSelectedIndex()] != null) {
            decAux.setEdificio(edificios_combo.getSelectedIndex());  //Checar esta parte
        } else {
            decAux.setEdificio(lastEdificioD);
        }
        decAux.setAbundancia(100);
        dec.add(decAux);
    }

    public void recalcula() {
        try {
            TableModel tm = tablaA.getModel();
            leeDatosLocales();
            DefaultTableModel modelo = new DefaultTableModel();
            Object[] fila = new Object[5];

            if (español) {
                modelo.setColumnIdentifiers(nombreColumnas_es);
            } else {
                modelo.setColumnIdentifiers(nombreColumnas_EN);
            }

            for (int i = 0; i < tm.getRowCount(); i++) {//tablaA.getRowCount()

                ProductObject materiaActual = POIlist.get(i);
                //System.out.println("Para : " + materiaActual.getNombre() + " ------------------------");

                cr = new CalculoRetail(materiaActual.getLetras());
                cr.setBono(dec.get(i).getBonificacion());
                cr.setCalidad(dec.get(i).getCalidad());
                float salariosHora = etfT[dec.get(i).getEdificio()].getSalariosHora();
                float costo_administrativoH = salariosHora * (gastos_administrativos / 100);

                Object pVo = tm.getValueAt(i, 2);
                Object pCo = tm.getValueAt(i, 1);
                float precioVenta = Float.parseFloat(pVo.toString());
                float precioCompra = Float.parseFloat(pCo.toString());
                //System.out.println("Para " + materiaActual.getNombre() + " --Precio venta: " + precioVenta + "  --Precio compra: "
                 //       + precioCompra);

                if (precioCompra < 0) {
                    precioCompra = 0;
                }
                cr.setPrecio(precioVenta);
                cr.calculaTodo();

                float ventaHora = cr.getVentaHoraBonificacion();
                float ingresos_Salarios = precioVenta - ((salariosHora + costo_administrativoH) / ventaHora);

                if (español) {
                    fila[0] = materiaActual.getNombre();
                } else {
                    fila[0] = nPEN.getNamePEN(materiaActual.getId()); //etf.getEdificioInfo().getJSONArray("doesProduce").getJSONObject(i).getString("name");
                }

                fila[1] = precioCompra;

                fila[2] = precioVenta;
                fila[3] = ventaHora * (dec.get(i).getNivel_edificio());

                if (precioCompra == 0) {
                    fila[4] = 0;
                } else {
                    fila[4] = ((precioVenta - precioCompra) * ventaHora - salariosHora - costo_administrativoH) * (dec.get(i).getNivel_edificio());
                }
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
        odn_button.setVisible(true);
        RetailCalculator.super.setSize(875, 635);//877, 720);//(874, 572);  
        //tablaA.setVisible(true);
        muestraCalculadoraAvanzada();

    }

    private void cambiaBasico() {
        odn_button.setVisible(false);
        devuelveVentanaInicial();
        newCalcula();
    }

    public void cleanTabla() {
        DefaultTableModel tb = (DefaultTableModel) tablaD.getModel();
        int a = tablaD.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount() - 1);
        }
    }

    public void cleanTabla2() {
        DefaultTableModel tb = (DefaultTableModel) tablaA.getModel();
        int a = tablaA.getRowCount() - 1;
        for (int i = a; i >= 0; i--) {
            tb.removeRow(tb.getRowCount() - 1);
        }
    }

    private void newCalcula() {
        leeDatosLocales();
        codigosEdificio = etf.getProduceCodigos();
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };

        if (español) {
            if (precio_promedio_jrb.isSelected()) {
                nombreColumnas_es[2] = "Precio promedio de venta";
            } else {
                nombreColumnas_es[2] = "Mejor precio de venta";
            }
            modelo.setColumnIdentifiers(nombreColumnas_es);
        } else {
            if (precio_promedio_jrb.isSelected()) {
                nombreColumnas_EN[2] = "Average sell price";
            } else {
                nombreColumnas_EN[2] = "Best price to sell";
            }
            modelo.setColumnIdentifiers(nombreColumnas_EN);
        }

        Object[] fila = new Object[5];
        for (int i = 0; i < etf.getNumeroVende(); i++) {
            ProductObject materiaActual = po[codigosEdificio[i]];
            cr = new CalculoRetail(materiaActual.getLetras());
            cr.setBono(bonificacion_ventas);
            cr.setCalidad(calidad);
            float salariosHora = etf.getSalariosHora();
            float costo_administrativoH = salariosHora * (gastos_administrativos / 100);
            float precioVenta = 0;
            float precioCompra = materiaActual.getPrecio(calidad) * (1 - (pcbm / 100)); //El precio de acuerdo al porcentaje bajo el que se compraria   
            if (precio_promedio_jrb.isSelected()) {
                precioVenta = (float) etf.getPrecioPromedio(materiaActual.getId());
            } else if (mejor_precio_jrb.isSelected()) {
                precioVenta = obtenMejorPrecio(materiaActual.getId(), calidad, etf.getSalariosHora(), gastos_administrativos, bonificacion_ventas, precioCompra);
            }

            if (precioCompra < 0) {
                precioCompra = 0;
            }
            cr.setPrecio(precioVenta);
            cr.calculaTodo();

            float ventaHora = cr.getVentaHoraBonificacion();
            float ingresos_Salarios = precioVenta - ((salariosHora + costo_administrativoH) / ventaHora);

            if (español) {
                fila[0] = materiaActual.getNombre();
            } else {
                fila[0] = nPEN.getNamePEN(materiaActual.getId()); //etf.getEdificioInfo().getJSONArray("doesProduce").getJSONObject(i).getString("name");
            }

            fila[1] = precioCompra;
            fila[2] = precioVenta;
            fila[3] = ventaHora * nivel_edificio;
            fila[4] = ((precioVenta - precioCompra) * ventaHora - salariosHora - costo_administrativoH) * nivel_edificio;
            if (precioCompra == 0) {
                fila[4] = 0;
            }
            modelo.addRow(fila);
        }

        tablaD.setModel(modelo);

    }

    public float obtenMejorPrecio(int iDmateria, int Q, float salary, float gAdmin, float bonoV, float precioC) {
        float paso = 0;
        cr = new CalculoRetail(po[iDmateria].getLetras());
        cr.setCalidad(Q);
        cr.setBono(bonoV);
        cr.calculaPrecioMasVendible();
        float precioI = cr.getPrecioMasVendible();
        float precioA = precioI;
        float precioB = precioI + paso;
        float ventaA = 0;
        float ventaB = 0;
        float imsuA = -1; //= produccionHora*cr.getPrecioMasVendible(); 
        float imsuB = 0;/// = gananciaI;
        float costo_administrativoH = salary * (gAdmin / 100);
        float gastos = salary + costo_administrativoH;
        int i = 0;

        if (precioI >= 1000) {
            paso = precioI / 100;
        } else if (precioI >= 100 && precioI < 1000) {
            paso = precioI / 75;
        } else if (precioI >= 20 && precioI < 100) {
            paso = precioI / 10;
        } else if (precioI >= 10 && precioI < 20) {
            paso = 1;
        } else if (precioI >= 5 && precioI < 10) {
            paso = (float) 0.5;
        } else {
            paso = (float) 0.1;
        }

        cr.setPrecio(precioB);
        cr.calculaTodo();
        ventaB = cr.getVentaHoraBonificacion();
        imsuB = ((precioB - precioC) * ventaB - gastos);
        do {
            imsuA = imsuB;
            precioA = precioB;
            precioB = precioB + paso;
            cr.setPrecio(precioB);
            cr.calculaTodo();
            ventaB = cr.getVentaHoraBonificacion();
            imsuB = ((precioB - precioC) * ventaB - gastos);
            i++;
            //System.out.println("Ganancia A: " + imsuA + "    Ganancia B = " + imsuB);
        } while (imsuA < imsuB || i == 1000);

        // System.out.println("precioA: " + precioA);
        precioB = precioA;
        cr.setPrecio(precioB);
        cr.calculaTodo();
        ventaB = cr.getVentaHoraBonificacion();
        imsuB = ((precioB - precioC) * ventaB - gastos);
        //paso = 1;

        if (precioA > 50) {
            paso = 1;
        } else if (precioA >= 20 && precioA < 50) {
            paso = (float) 0.5;
        } else {
            paso = (float) 0.1;
        }

        do {
            imsuA = imsuB;
            precioA = precioB;
            precioB = precioB + paso;
            cr.setPrecio(precioB);
            cr.calculaTodo();
            ventaB = cr.getVentaHoraBonificacion();
            imsuB = ((precioB - precioC) * ventaB - gastos);
            i++;
            //System.out.println("Ganancia A: " + imsuA + "    Ganancia B = " + imsuB);
        } while (imsuA < imsuB || i == 1000);

        return precioA;
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
            java.util.logging.Logger.getLogger(RetailCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RetailCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RetailCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RetailCalculator.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //para darle al jfile chooser y el programa el aspecto de windows 
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(RetailCalculator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(RetailCalculator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(RetailCalculator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(RetailCalculator.class.getName()).log(Level.SEVERE, null, ex);
                }
                new RetailCalculator().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton B_button;
    private javax.swing.JRadioButton N_button;
    private javax.swing.JTextField Pvbm_txt;
    private javax.swing.JRadioButton R_button;
    private javax.swing.JLabel administrativos_lb;
    private javax.swing.JButton agrega_button;
    private javax.swing.JLabel bonificacion_lb;
    private javax.swing.JTextField bonificacion_txt;
    private javax.swing.JButton calculaTodo_button;
    private javax.swing.JButton calcula_button;
    private javax.swing.JComboBox<String> calidad_box;
    private javax.swing.JButton cambia_idioma;
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
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JRadioButton mejor_precio_jrb;
    private javax.swing.JLabel nivel_edificio_lb;
    private javax.swing.JTextField nivel_edificio_txt;
    private javax.swing.JButton odn_button;
    private javax.swing.JLabel pcbm_lb;
    private javax.swing.JRadioButton precio_promedio_jrb;
    private javax.swing.JLabel produciendo_q_lb;
    private javax.swing.JButton recalculaTablaB_button;
    private javax.swing.JButton recalcula_button;
    private javax.swing.JTable tablaA;
    private javax.swing.JTable tablaD;
    private javax.swing.JComboBox<String> tipoCalcu_combo;
    private javax.swing.ButtonGroup tipo_precio;
    // End of variables declaration//GEN-END:variables
}
