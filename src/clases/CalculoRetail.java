/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/**
 *
 * @author donyo
 */
//Clase creada exclusivamente para hacer calculos de retail
public class CalculoRetail {
    private double[] letras;
    private double A, B, C, D, E;
    private double S0;
    private double Sf;
    private int calidad;
    private float precio;
    private float bono;
    private double tiempo;
    private float ventaHoraSinBonificacion;
    private float ventaHoraBonificacion; 
    private float mejorPrecio;
            
    public CalculoRetail(double[] letras){
        this.letras = letras;
        A = letras[0];
        B = letras[1];
        C = letras[2];
        D = letras[3];
        E = letras[4];
        S0 = letras[5];
        //System.out.println("A: " + A + " ,B:  " + B + " ,C " + C + " ,D: " +D + " ,E: " + E);
    }
      
    public void calculaSfinal(){
        Sf = S0 - 0.24*calidad;
        if(-0.38 > Sf){
            Sf = -0.38;
        }       
        //System.out.println("Sf : " + Sf);
    }     
    
    public void setCalidad(int calidad){
        this.calidad = calidad;
    }
    
    public void setPrecio( float precio){
        this.precio = precio;
    }
    
     public void setBono(float bono){
        this.bono = bono;
    }
    
    public void calculaTiempoVenta(){
        calculaSfinal();
        double ABC = (precio*A) + ((Sf - 0.5)/B) - C;
        //System.out.println("ABC : " +ABC );
        tiempo = ((Math.pow(ABC, 2) )* D) + E;      
        //System.out.println("tiempo: " + tiempo );
        
    }
    
    public void calculaVentaHoraSinBonificacion(){
        ventaHoraSinBonificacion = (float) (3600 / tiempo);
    }
    
    public void calculaVentaHoraBonificacion(){
        ventaHoraBonificacion =  getVentaHoraSinBonificacion()/(1-(bono/100));
    }
    
    //Calcula el precio en el que se vendan mas unidades
    public void calculaPrecioMasVendible(){
        mejorPrecio = (float) ((C - (Sf - 0.5)/B)/A) ;
    }
    
    public void calculaTodo(){
        calculaTiempoVenta();
        calculaVentaHoraSinBonificacion();
        calculaVentaHoraBonificacion();
        calculaPrecioMasVendible();
    }
    
    public float getVentaHoraSinBonificacion(){
        return ventaHoraSinBonificacion;
    }
    
    public float getVentaHoraBonificacion(){
        return ventaHoraBonificacion;
    }
    
    public double getTiempo(){ return tiempo;}   
    
    public float getPrecioMasVendible(){
        return mejorPrecio;
    }   
    
}
