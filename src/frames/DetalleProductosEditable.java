/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frames;

import clases.NombresPEN;
import clases.ProductObject;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author donyo
 */
public class DetalleProductosEditable extends javax.swing.JFrame {
    private boolean espanol;
    private ProductObject  materia_prima;
    private ProductObject [] allP;
    private String[] nombreColumnas_es = {"Producto", "Cantidad", "Costo unitario", "Costo total"};
    private String[] nombreColumnas_EN = {"Product", "Amount", "Unit cost", "Total cost"};
    private int calidad_a_producir;
    private float costo_totalMP;
    private float gastosAdminUnidad; // valor de gastos administrativos por unidad
    private float gastosAdmin;
    private float  bonificacion;
    private float salarioEdificio;
    private float salarioUnidad;
    private float costoFab;
    private float abundancia;
    //private float newPrecio;
    NombresPEN nPEN;
    
    private float porcentajeMP; //porcntaje materias primas
    private float porcentajeSalarios; 
    private float porcentajeAdmin;
    private boolean robots;
        
    
    //private String nombreProducto;
    public DetalleProductosEditable(boolean espanol) {
        this.espanol = espanol;
        initComponents();
         this.setSize(412, 345);    ///(874, 485);        //(790, 485);  this.setSize(412, 300); 
        this.setResizable(false);
        //this.setTitle("Detalle de productos");
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);    
        descuento_txt.setText("0");
        nPEN = new NombresPEN();
    }

    /*private DetalleProductos() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    public void traduce(){
        if(espanol){
            super.setTitle("Detalle de productos");
            ctmp.setText("Costo total de materias primas:   $");
            paf_lb.setText("Producto a fabricar:");
            gastosAdmin_lb.setText("Gastos admistrativos:   $");
            salarios_lb.setText("Salarios:   $");
            costoFab_lb.setText("Costo total de fabricación:   $");
            recalcular_button.setText("Recalcular");
            descuento_button.setText("Aplicar descuento:");
        }else{
            super.setTitle("Cost price in detail");
            ctmp.setText("Total cost of items:   $");
            paf_lb.setText("Making:");
            gastosAdmin_lb.setText("Administration overhead:   $");
            salarios_lb.setText("Wages:   $");
            costoFab_lb.setText("Total manufacturing cost:   $");
            recalcular_button.setText("Recalculate");
            descuento_button.setText("Apply discount:");
        }
    }
    
    public void setIdioma(boolean idioma){
        this.espanol = idioma;
    }
    
    public void setNombreProducto(String producto){
        producto_lb.setText(producto);
    }
    
    public void setPO(ProductObject po){
        this.materia_prima = po;
    }
    
    public void setAllPO(ProductObject[] AllPs){
        this.allP = AllPs;
    }
    
    public void setCalidadaAProducir(int c){
        this.calidad_a_producir = c;
    }
    
    
    public void calculaInfo(){
        //metelesIngles();   //De prueba
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return true; 
            }
        };
        if(espanol){
            modelo.setColumnIdentifiers(nombreColumnas_es);
        }else{
            modelo.setColumnIdentifiers(nombreColumnas_EN);
        }
        
       
        costo_totalMP = 0; 
        Object[] fila = new Object[4];   
        for(int i= 0 ; i <  materia_prima.getNumeroMateriasPrimas(); i++ ){           
            int codMP = materia_prima.getIDmateria(i);
            if(espanol){
                fila[0] = allP[codMP].getNombre();
            }else{
                fila[0] = allP[codMP].getNombre(); 
                allP[codMP].setName(nPEN.getNamePEN(codMP));
                fila[0] = allP[codMP].getName();     // nPEN.getNamePEN(codMP);
            }
            
            float cantidad = materia_prima.getAmount(i);
            fila[1] = cantidad;
            float precio;
            if(calidad_a_producir == 0){
                precio = allP[codMP].getPrecio(0);
            }else{
                precio= allP[codMP].getPrecio(calidad_a_producir -1);
            }
            fila[2] = precio;
            fila[3] = cantidad* precio;
            modelo.addRow(fila);
            costo_totalMP = precio*cantidad + costo_totalMP;
        } 
       
        tablaP.setModel(modelo);
    }
    
    //Muestra todo en el jFrame xD
    public void muestraTodo(){
        muestraCostoTotalMP();
        muestraGastosAdmin();
        muestraSalarios();
        muestraCostoFab();
        muestraCalidad();
        muestraPorcentaje();
    }
    
    public void muestraCostoTotalMP(){
        costoFinla_txt.setText(Float.toString(costo_totalMP));
    }
     
    public float costoTotalMP(){
        return costo_totalMP;
    }
    
    public void setGastosAdmin(float gastosAdmin){
        this.gastosAdmin = gastosAdmin;
    }
    
    public void setBonificacion(float bonificacion){
        this.bonificacion = bonificacion;
    }
    
    public void setAbundancia(float abundancia){
        this.abundancia = abundancia;
    }     
    
    private void calculaSalarioUnidad(){
        float produccionHoraBonificada = (bonificacion / 100 + 1) * materia_prima.getProduccionHora()*(abundancia/100);
        float salarioEdificioR= 0 ;
        
        if(robots){
            salarioEdificioR = (float) (salarioEdificio*0.97);  
        }else{
            salarioEdificioR = salarioEdificio;
        }
        
        
        salarioUnidad =  salarioEdificioR /produccionHoraBonificada;   
    }
    
    public float getSalarioUnidad(){
        calculaSalarioUnidad();      
        return salarioUnidad;
    }
    
    private void calculaValorGastosAdmin(){
        gastosAdminUnidad = getSalarioUnidad()* (gastosAdmin/100);
    }
    private float getValorGastosAdmin(){
        calculaValorGastosAdmin();
        return gastosAdminUnidad;
    }
    
    public void muestraGastosAdmin(){
        gastosAdmin_txt.setText( Float.toString(getValorGastosAdmin() ) );
    }
    
    public void muestraSalarios(){
        salarios_txt.setText( Float.toString( getSalarioUnidad() ) );
    }
    
    public void calculaCostoFab(){
        costoFab = getValorGastosAdmin() + getSalarioUnidad() + getValorTotalMateriasPrimas();     
    }    
    
    public void muestraCalidad(){
        Q_lb.setText("Q"+ calidad_a_producir);
    } 
    
    public float getValorTotalMateriasPrimas(){
        return costo_totalMP;
    }
    
    public void muestraCostoFab(){
        calculaCostoFab();
        costoFab_txt.setText( Float.toString( costoFab ) );
    }
    
    public float getNewPrecio(){
        return costoFab;
    }

    public float getSalarioEdificio() {
        return salarioEdificio;
    }

    public void setSalarioEdificio(float salarioEdificio) {
        this.salarioEdificio = salarioEdificio;
    }
   
    public void setRobots(boolean robots){
        this.robots = robots;
    }
    
    public void muestraPorcentaje(){
        calculaPorcentajes();
        porSalarios_lb.setText(getPorcentajeSalarios() + "%");
        porMP_lb.setText(getPorcentajeMP() + "%");
        porAdmin_lb.setText(getPorcentajeAdmin()+"%");
        /*System.out.println(  materia_prima.getNombre() + " \t " + getPorcentajeMP() + " \t " + getPorcentajeSalarios() + " \t " + 
            getPorcentajeAdmin() );*/
    }
    
    //Para calcular porcentajes una vez que se haya hecho los anteriores procesos
    public void calculaPorcentajes(){
        porcentajeMP = (costo_totalMP/costoFab)*100;
        porcentajeSalarios = (salarioUnidad/costoFab)*100; 
        porcentajeAdmin = (gastosAdminUnidad/costoFab)*100;
    }
    
    public float getPorcentajeMP(){
        return porcentajeMP;
    }
    
    public float getPorcentajeSalarios(){
        return porcentajeSalarios;
    }
    
    public float getPorcentajeAdmin(){
        return porcentajeAdmin;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        paf_lb = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaP = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        producto_lb = new javax.swing.JLabel();
        ctmp = new javax.swing.JLabel();
        costoFinla_txt = new javax.swing.JTextField();
        salarios_lb = new javax.swing.JLabel();
        gastosAdmin_lb = new javax.swing.JLabel();
        salarios_txt = new javax.swing.JTextField();
        gastosAdmin_txt = new javax.swing.JTextField();
        costoFab_lb = new javax.swing.JLabel();
        costoFab_txt = new javax.swing.JTextField();
        recalcular_button = new javax.swing.JButton();
        Q_lb = new javax.swing.JLabel();
        descuento_txt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        descuento_button = new javax.swing.JButton();
        porMP_lb = new javax.swing.JLabel();
        porSalarios_lb = new javax.swing.JLabel();
        porAdmin_lb = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        paf_lb.setText("Producto a fabricar:");

        tablaP.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Producto", "Cantidad", "Costo unitario", "Costo total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tablaP);
        if (tablaP.getColumnModel().getColumnCount() > 0) {
            tablaP.getColumnModel().getColumn(0).setPreferredWidth(150);
        }

        producto_lb.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        producto_lb.setText("Electrónicos de alto grado");

        ctmp.setText("Costo total de materias primas:   $");

        costoFinla_txt.setEditable(false);
        costoFinla_txt.setText("jTextField1");
        costoFinla_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                costoFinla_txtActionPerformed(evt);
            }
        });

        salarios_lb.setText("Salarios:   $");

        gastosAdmin_lb.setText("Gastos admistrativos:   $");

        salarios_txt.setEditable(false);
        salarios_txt.setText("jTextField1");

        gastosAdmin_txt.setEditable(false);
        gastosAdmin_txt.setText("jTextField1");

        costoFab_lb.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        costoFab_lb.setText("Costo total de fabricación:   $");

        costoFab_txt.setEditable(false);
        costoFab_txt.setText("jTextField1");

        recalcular_button.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        recalcular_button.setText("Recalcular");
        recalcular_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recalcular_buttonActionPerformed(evt);
            }
        });

        Q_lb.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        Q_lb.setText("Q0");

        descuento_txt.setText("jTextField1");
        descuento_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descuento_txtActionPerformed(evt);
            }
        });

        jLabel3.setText("%");

        descuento_button.setText("Aplicar descuento:");
        descuento_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                descuento_buttonActionPerformed(evt);
            }
        });

        porMP_lb.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        porMP_lb.setText("jLabel1");

        porSalarios_lb.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        porSalarios_lb.setText("jLabel3");

        porAdmin_lb.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        porAdmin_lb.setText("jLabel4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(salarios_lb)
                            .addComponent(gastosAdmin_lb)
                            .addComponent(costoFab_lb)
                            .addComponent(ctmp))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(costoFinla_txt)
                            .addComponent(salarios_txt)
                            .addComponent(gastosAdmin_txt)
                            .addComponent(costoFab_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(porMP_lb, javax.swing.GroupLayout.DEFAULT_SIZE, 72, Short.MAX_VALUE)
                            .addComponent(porSalarios_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(porAdmin_lb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(paf_lb)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(producto_lb, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Q_lb))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(descuento_button, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(descuento_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(recalcular_button, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(paf_lb)
                    .addComponent(producto_lb)
                    .addComponent(Q_lb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(recalcular_button)
                    .addComponent(descuento_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(descuento_button))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(costoFinla_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ctmp)
                            .addComponent(porMP_lb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salarios_lb)
                            .addComponent(salarios_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(porSalarios_lb))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(gastosAdmin_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(gastosAdmin_lb)
                            .addComponent(porAdmin_lb))
                        .addGap(31, 31, 31))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(costoFab_txt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(costoFab_lb)))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    
    private void costoFinla_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_costoFinla_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_costoFinla_txtActionPerformed

    private void recalcular_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recalcular_buttonActionPerformed
        recalcula();
        muestraTodo();         
        
    }//GEN-LAST:event_recalcular_buttonActionPerformed

    private void descuento_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descuento_buttonActionPerformed
       try{
        DefaultTableModel tb = (DefaultTableModel) tablaP.getModel();   
        DefaultTableModel tbAux = tb;
        float porcentaje = Float.parseFloat(descuento_txt.getText());
        
        for (int i = 0; i < tb.getRowCount(); i++) {
            float precio;
            int codMP = materia_prima.getIDmateria(i);            
            if(calidad_a_producir == 0){
                precio = allP[codMP].getPrecio(0);
            }else{
                precio= allP[codMP].getPrecio(calidad_a_producir -1);
            }
            float valorNew = precio*(1-porcentaje/100);
           tbAux.setValueAt(valorNew, i, 2);          
        }        
        
        tablaP.setModel(tbAux);
        recalcula();
        muestraTodo(); 
       }catch(NumberFormatException e){
            if (espanol) {
                JOptionPane.showMessageDialog(this, "Revise que el porcentaje sea un numero",
                        "Alerta", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Check that the porcentaje is really a number",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
       }
         
       
    }//GEN-LAST:event_descuento_buttonActionPerformed

    private void descuento_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_descuento_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_descuento_txtActionPerformed

    public void recalcula(){
                    try {
        DefaultTableModel mAux = new DefaultTableModel();            
                    
        DefaultTableModel modelo = (DefaultTableModel) tablaP.getModel();
        if(espanol){
            modelo.setColumnIdentifiers(nombreColumnas_es);
            mAux.setColumnIdentifiers(nombreColumnas_es);
        }else{
            modelo.setColumnIdentifiers(nombreColumnas_EN);
            mAux.setColumnIdentifiers(nombreColumnas_EN);
        }

        costo_totalMP = 0; 
        Object[] fila = new Object[4];   
        
        for(int i= 0 ; i <  materia_prima.getNumeroMateriasPrimas(); i++ ){               
            
            int codMP = materia_prima.getIDmateria(i);
            if(espanol){
                fila[0] = allP[codMP].getNombre();
            }else{
                fila[0] = allP[codMP].getNombre(); 
                allP[codMP].setName(nPEN.getNamePEN(codMP));
                fila[0] = allP[codMP].getName();     // nPEN.getNamePEN(codMP);
            }
            
            float cantidad = materia_prima.getAmount(i);
            fila[1] = cantidad;           
            Object pO = modelo.getValueAt(i, 2);    
            float precio = Float.parseFloat(pO.toString());

            fila[2] = precio;
            fila[3] = cantidad* precio;
            mAux.addRow(fila);
            costo_totalMP = precio*cantidad + costo_totalMP;
        } 
       
        tablaP.setModel(mAux);
        
        
            
            
        } catch (NullPointerException e) {
            System.out.println("Error intentando recalcular");
        }catch(NumberFormatException e){
            if (espanol) {
                JOptionPane.showMessageDialog(this, "Revise que los numeros esten escritos de forma correcta",
                        "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Check your numbers are correctly writted",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        }
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
            java.util.logging.Logger.getLogger(DetalleProductosEditable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DetalleProductosEditable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DetalleProductosEditable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DetalleProductosEditable.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               new DetalleProductosEditable(true).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Q_lb;
    private javax.swing.JLabel costoFab_lb;
    private javax.swing.JTextField costoFab_txt;
    private javax.swing.JTextField costoFinla_txt;
    private javax.swing.JLabel ctmp;
    private javax.swing.JButton descuento_button;
    private javax.swing.JTextField descuento_txt;
    private javax.swing.JLabel gastosAdmin_lb;
    private javax.swing.JTextField gastosAdmin_txt;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel paf_lb;
    private javax.swing.JLabel porAdmin_lb;
    private javax.swing.JLabel porMP_lb;
    private javax.swing.JLabel porSalarios_lb;
    private javax.swing.JLabel producto_lb;
    private javax.swing.JButton recalcular_button;
    private javax.swing.JLabel salarios_lb;
    private javax.swing.JTextField salarios_txt;
    private javax.swing.JTable tablaP;
    // End of variables declaration//GEN-END:variables
}
