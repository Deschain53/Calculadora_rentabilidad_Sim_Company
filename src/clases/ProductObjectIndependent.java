/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import org.json.JSONObject;

/**
 *
 * @author donyo
 */
public class ProductObjectIndependent extends ProductObject {
    //variables para usar por tabla A
    private float salarioEdificio;
    private float salarioUnidad;
    private float costoFab;
    private float calidad;
    private float [] preciosMaterias;
    
    public ProductObjectIndependent(JSONObject jop) {
        super(jop);
    }
    
    public void setSalarioEdificio(float salarioEdif){
        this.salarioEdificio = salarioEdif;
    }
    
    public void setCalidad(int calidad){
        this.calidad = calidad;
    }
    
    public void creaArrayNewPrecios(){
        preciosMaterias = new float[getNumeroMateriasPrimas()];
    }
    
    public void setNewPrecioMateriaPrima(int i, float Newprecio){
        preciosMaterias[i] = Newprecio;
    }
    
    
    
    
    
    
    
    
}
