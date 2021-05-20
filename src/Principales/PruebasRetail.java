/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Principales;
import clases.CalculoRetail;
import clases.ExtraeTodoEdificio;
import clases.JSONedificiosProduccion;
import clases.JSONproductos;
import clases.NombresPEN;
import clases.ProductObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
/**
 *
 * @author donyo
 */
public class PruebasRetail {
    public static void main(String args[]) throws IOException {
        
        JSONproductos jp = new JSONproductos();
        JSONObject op;
        
        jp.setFase(1);
        op = new JSONObject(jp.getproducto(20));
        System.out.println("Para fase normal: "+ jp.getproducto(20));
        
        
        jp.setFase(0);
        op = jp.getproducto(20);
        System.out.println("Para fase recesion: "+ op);
        System.out.println(op.getInt("db_letter"));
        
       JSONedificiosProduccion jep = new JSONedificiosProduccion();
       System.out.println("En la posicion [0] se tiene : \n" + jep.getEdificioIndex(0)  );
       op =  jep.getEdificioIndex(1);
       System.out.println("En la posicion [1] se tiene : \n" + op  );
       
       
      //NombresPEN  nPEN = new NombresPEN(); 
      
      //ProductObject po;
       
        /*for(int i = 0; i < nPEN.numeroProductos(); i++) {
                if (nPEN.existe(i)) {
                    po  = new ProductObject(i);
                    po.setUrlEconomia(2);
                    po.extraeJSONproducto();
                    System.out.println("producto["+i+"] = " +po.getJsonProduct() );
            }
        }*/
        

        
        
        
        
        //Para pruebas retail individual
        /*ExtraeTodoEdificio etf = new ExtraeTodoEdificio("C");  //De la tienda de electrónica
        etf.setUrlEconomia(0);
        etf.extraeVende();
        
        int numeroDeProductosAVenta = etf.getNumeroManeja();
        System.out.println("El edificio de electronica produce " +numeroDeProductosAVenta + " productos" );
        
        CalculoRetail cr;
        
        for(int i = 0 ; i < numeroDeProductosAVenta; i++){
            int numeroProductoI = etf.getCodigoProductoNumero(i);
            cr = new CalculoRetail(etf.getLetrasProducto(numeroProductoI));
            cr.setCalidad(3);
            cr.setBono(13);        
            cr.calculaPrecioMasVendible();
            System.out.println("--------------------------- "+ i +" -------------------------------");
            float mejorPrecio = cr.getPrecioMasVendible();
            System.out.println("Para " + etf.getNombreProducto(numeroProductoI) + " ---El mejor precio para vender calidad 3 es: " + cr.getPrecioMasVendible());
            cr.setPrecio(mejorPrecio);
            cr.calculaTodo();
                     
            System.out.println("Tiempo de venta por unidad: " + cr.getTiempo());
            System.out.println("Unidades por hora SIN bonificacion: " + cr.getVentaHoraSinBonificacion());
            System.out.println("Unidades por hora con bonificacion: " + cr.getVentaHoraBonificacion());
            System.out.println("El precio promedio es: " + etf.getPrecioPromedio(numeroProductoI));
        }*/
        
        
        
        
        
        
        //double[] letras0;      
        //letras0  =  etf.getLetrasProducto(0);
        
       
        
        
        ////Pruba 2
        /*ProductObject po = new ProductObject(98);
        try {
            po.extraeJSONproducto();
        } catch (IOException ex) {
            Logger.getLogger(PruebasRetail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        po.setUrlEconomia(0);       
        //String ecuacion =  "(Math.pow(price*0.689517 + (-601.634928 + (saturation - 0.5)/0.011352), 2.000000)*0.068570 + 2269.531029)*amount";  //po.getRetailModeling();
        String ecuacion = po.getRetailModeling();
        System.out.println(ecuacion);
        
        po.extraeLetras();
        double [] letras = null;
        letras  = po.getLetras();       
        
        CalculoRetail cr = new CalculoRetail(letras);       
        cr.setCalidad(3);
        cr.setBono(13);
        cr.setPrecio((float) 970.66);
        cr.calculaTodo();
        
        System.out.println("Tiempo de venta por unidad: " + cr.getTiempo());
        System.out.println("Unidades por hora SIN bonificacion: " + cr.getVentaHoraSinBonificacion());
        System.out.println("Unidades por hora con bonificacion: " + cr.getVentaHoraBonificacion());
        
        System.out.println("El mejor precio para vender calidad 3 es: " + cr.getPrecioMasVendible());*/
        
        
        
        
        
        //////////Prueba 1*******************
        /*System.out.println("El valor de A es: " + letras[0]);   
        System.out.println("El valor de B es: " + letras[1]);  
        System.out.println("El valor de C es: " + letras[2]);  
        System.out.println("El valor de D es: " + letras[3]);  
        System.out.println("El valor de E es: " + letras[4]); 
        System.out.println("El valor de S0 es: " + letras[5]);  */
        
        
        
        /*int index1Multi = ecuacion.indexOf("*");     
        int index1Suma = ecuacion.indexOf("+", index1Multi );     
        String As = ecuacion.substring(index1Multi + 1,index1Suma);
        As = As.trim();        
        System.out.println("El valor de A es: " + As);
        
        int index3Resta = ecuacion.indexOf("-", index1Suma ); 
        int index2Suma = ecuacion.indexOf("+", index3Resta ); 
        
        String Cs = ecuacion.substring(index3Resta +1,index2Suma);
        Cs = Cs.trim();
        System.out.println("El valor de C es: " + Cs);
        
        int index1Division = ecuacion.indexOf("/", index2Suma ); 
        int index6Parentesis = ecuacion.indexOf(")", index1Division );
        String Bs = ecuacion.substring( index1Division + 1 , index6Parentesis );
        Bs = Bs.trim();
        System.out.println("El valor de B es: " + Bs);
        
        int index2Multi = ecuacion.indexOf("*", index6Parentesis );
        int index3Suma = ecuacion.indexOf("+", index2Multi );
        String Ds = ecuacion.substring( index2Multi + 1,index3Suma);
        Ds = Ds.trim();      
        System.out.println("El valor de D es: " + Ds);
        
        int index8Parentesis = ecuacion.indexOf(")", index3Suma );
        String Es = ecuacion.substring( index3Suma + 1,index8Parentesis);
        Es = Es.trim();
        System.out.println("El valor de E es: " + Es);       
        
        System.out.println("Convirtiendolos a float");
 
        float A =  Float.parseFloat(As);
        float B =  Float.parseFloat(Bs);
        float C =  Float.parseFloat(Cs);
        float D =  Float.parseFloat(Ds);
        float E =  Float.parseFloat(Es);*/

        
        
    }
}


