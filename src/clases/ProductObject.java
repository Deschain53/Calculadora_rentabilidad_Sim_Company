/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import clases.LeeJSON;
import clases.InfoMercado;
/**
 *
 * @author donyo
 */

//Objeto que contendra el ObjecJson con la informacion del producto y el arreglo con la información de las calidades
public class ProductObject {
    private LeeJSON rj = new LeeJSON(); 
    private JSONObject jsonObjectProducto;  //Informacion desde la biblioteca de este producto
    private float[] precios;
    private InfoMercado im;
    private String name;  //nombre del producto en ingles
        //variables para usar por tabla A
    private float salarioEdificio;
    private float salarioUnidad;
    private float costoFab;
    private float bonificacion;
    private float gastosadministrativos;
    private int calidad;
    private int numEdificio;
    private float newProduccionHora;
    private float abundancia;
    private float [] preciosMaterias;
    //***********************Variables para la venta minorista   
    private int idProduct;
    private String url_productos = "https://www.simcompanies.com/api/v3/es/encyclopedia/resources/0/";
    private double [] letras;
    
    
    public ProductObject(int numeroProducto){
       this.idProduct = numeroProducto;
        //constructor por defecto
    }
    
    public ProductObject(JSONObject jop){
        this.jsonObjectProducto = jop;
    }   
    
    public JSONObject getJsonProduct(){
        return jsonObjectProducto;
    }
    
    //Regresa el precio en mercado para cierta calidad
    public float getPrecio(int calidad){
        return precios[calidad];
    }
    
    public void setPrecios(float[] precios){
        this.precios = precios;
    }
    
    //Regresa el id del producto
    public int getId(){
        return jsonObjectProducto.getInt("db_letter");
    }
    
    //Regresa el nombre del producto en español
    public String getNombre(){
        return jsonObjectProducto.getString("name");
    }
    
    public void setJSONproducto(JSONObject json){
        jsonObjectProducto = json;
    }
    
    //Clase que actualiza los precios de mercado de este producto en particular
    public void actualizaPrecios() throws IOException{
        im = new InfoMercado(getId());
        im.leeBaratos();
        precios = im.getPreciosB();       
    }
    
    public  float getProduccionHora(){
        return jsonObjectProducto.getFloat("producedAnHour");
    }
    
    public float getTransporte(){
        return jsonObjectProducto.getFloat("transportNeeded");
    }
    
    public float[] getPrecios(){
        return precios;
    }
    
    public float getAmount(int i){      
        return jsonObjectProducto.getJSONArray("producedFrom").getJSONObject(i).getFloat("amount");
    }
    
    public int getNumeroMateriasPrimas(){
        return jsonObjectProducto.getJSONArray("producedFrom").length();    
    }
    
    public int getIDmateria(int i){      
        return jsonObjectProducto.getJSONArray("producedFrom").getJSONObject(i).getJSONObject("resource").getInt("db_letter");
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    //Metodos para obtener informacion sobre venta minorista//****************************   
    public void setUrlEconomia(int fase){   //En caso que se quiera extraer de alguna fase especifica
        url_productos = "https://www.simcompanies.com/api/v3/es/encyclopedia/resources/"+ fase +"/";
    }
    
    public void extraeJSONproducto() throws IOException{
        jsonObjectProducto =rj.readJsonFromUrl(url_productos + idProduct);
    }
    
    public String getRetailModeling(){
        return jsonObjectProducto.getString("retailModeling");
    }
    
    //De prueba, obtiene el precio promedia de la calidad
    public double getPrecioPromedio(){
        //JSONArray ja =   jsonObjectProducto.getJSONArray("retailData");
        int tamaño =  jsonObjectProducto.getJSONArray("retailData").length();//ja.length(); 
       // System.out.println("El tamaño es: " + tamaño);
        return jsonObjectProducto.getJSONArray("retailData").getJSONObject(tamaño-1).getDouble("averagePrice");
    }
    
    //Metodo para filtrar la informacion del String y obtener los valores A,B,C,D y S0
    public void extraeLetras(){
        letras  = new double[7];
        String ecuacion = getRetailModeling();
        
        //System.out.println(ecuacion);
        
        int index1Multi = ecuacion.indexOf("*");     
        int index1Suma = ecuacion.indexOf("+", index1Multi );     
        String As = ecuacion.substring(index1Multi + 1,index1Suma);
        As = As.trim();        
        //System.out.println("El valor de A es: " + As);
        
        int index3Resta = ecuacion.indexOf("-", index1Suma ); 
        int index2Suma = ecuacion.indexOf("+", index3Resta ); 
        
        String Cs = ecuacion.substring(index3Resta +1,index2Suma);
        Cs = Cs.trim();
        //System.out.println("El valor de C es: " + Cs);
        
        int index1Division = ecuacion.indexOf("/", index2Suma ); 
        int index6Parentesis = ecuacion.indexOf(")", index1Division );
        String Bs = ecuacion.substring( index1Division + 1 , index6Parentesis );
        Bs = Bs.trim();
        //System.out.println("El valor de B es: " + Bs);
        
        int index2Multi = ecuacion.indexOf("*", index6Parentesis );
        int index3Suma = ecuacion.indexOf("+", index2Multi );
        String Ds = ecuacion.substring( index2Multi + 1,index3Suma);
        Ds = Ds.trim();      
        //System.out.println("El valor de D es: " + Ds);
        
        int index8Parentesis = ecuacion.indexOf(")", index3Suma );
        String Es = ecuacion.substring( index3Suma + 1,index8Parentesis);
        Es = Es.trim();
        
        letras[0] =  Double.parseDouble(As);
        letras[1] =  Double.parseDouble(Bs);
        letras[2] =  Double.parseDouble(Cs);
        letras[3] =  Double.parseDouble(Ds);
        letras[4]=   Double.parseDouble(Es);   
        letras[5] =  jsonObjectProducto.getDouble("marketSaturation");
    }
    
    //Metodos para devolver la A, B, C , y S0
    public double[] getLetras(){
        extraeLetras();
        return letras;
    }  
    
    //Metodos para comunicacion con tabla A//************************************************
    public void setSalarioEdificio(float salarioEdif){
        this.salarioEdificio = salarioEdif;
    } 
    
    public float getSalarioEdificio(){
        return this.salarioEdificio;
    }
    
    public void setCalidad(int calidad){
        this.calidad = calidad;
    }
    
    public int getCalidad(){
        return calidad;
    }
    
    public void creaArrayNewPrecios(){
        preciosMaterias = new float[getNumeroMateriasPrimas()];
    }
    
    public void setNewPrecioMateriaPrima(int i, float Newprecio){
        preciosMaterias[i] = Newprecio;
    }
    
    public void setNewProduccionHora(float produccionH){
        this.newProduccionHora = produccionH;
    }
    
    public float getNewProduccionHora(){
        return newProduccionHora;
    }
    
    public void setNumeroEdificio(int numEdif){
        this.numEdificio = numEdif;
    }
    
    public int getNumeroEdificio(){
        return numEdificio;
    }
    
    public void setBonificacion( float bonificacion){
        this.bonificacion = bonificacion;
    }
    
    public float getBonificacion(){
        return bonificacion;
    }
    
    public void setGastosAdmin( float gastosAdmin){
       this.gastosadministrativos = gastosAdmin;
    }
    
    public float getGastosAdmin(){
       return gastosadministrativos; 
    }
    
    public void setAbundancia(float a){
        this.abundancia = a ;
    }
    
    public float getAbundancia(){ return abundancia;}
    
}
