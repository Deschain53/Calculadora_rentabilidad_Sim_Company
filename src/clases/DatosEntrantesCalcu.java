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
public class DatosEntrantesCalcu {
    private float gastosAdministrativos;
    private float bonificacion;
    private float costoTransporte;
    private int calidad;
    private int faseEconomica;
    private float porcentaje_bajo_mercado;
    private float nivel_edificio;
    private int edificio;
    private float abundancia;
    private boolean robots;

    public float getAbundancia() {
        return abundancia;
    }

    public void setAbundancia(float abundancia) {
        this.abundancia = abundancia;
    }
         
    public DatosEntrantesCalcu(){
        
    }

    public float getGastosAdministrativos() {
        return gastosAdministrativos;
    }

    public void setGastosAdministrativos(float gastosAdministrativos) {
        this.gastosAdministrativos = gastosAdministrativos;
    }

    public float getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(float bonificacion) {
        this.bonificacion = bonificacion;
    }

    public float getCostoTransporte() {
        return costoTransporte;
    }

    public void setCostoTransporte(float costoTransporte) {
        this.costoTransporte = costoTransporte;
    }

    public int getCalidad() {
        return calidad;
    }

    public void setCalidad(int calidad) {
        this.calidad = calidad;
    }

    public int getFaseEconomica() {
        return faseEconomica;
    }

    public void setFaseEconomica(int faseEconomica) {
        this.faseEconomica = faseEconomica;
    }

    public float getPorcentaje_bajo_mercado() {
        return porcentaje_bajo_mercado;
    }

    public void setPorcentaje_bajo_mercado(float porcentaje_bajo_mercado) {
        this.porcentaje_bajo_mercado = porcentaje_bajo_mercado;
    }

    public float getNivel_edificio() {
        return nivel_edificio;
    }

    public void setNivel_edificio(float nivel_edificio) {
        this.nivel_edificio = nivel_edificio;
    }

    public int getEdificio() {
        return edificio;
    }

    public void setEdificio(int Edificio) {
        this.edificio = Edificio;
    }
    
    public void setRobots(boolean robots){
        this.robots = robots;
    }
    
    public boolean getRobots(){
        return robots;
    }
    
    
    
    
}
