/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
//import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
//import javax.net.ssl.HttpsURLConnection;
//import static jdk.nashorn.internal.objects.Global.getJSON;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
//import static solicitudapi.JsonReader.readJsonAFromUrl;

/**
 *
 * @author donyo
 */
public class InfoMercado {

    private int producto;
    private String url_sim = "https://www.simcompanies.com/api/v2/market/";
    private float[] calidad;
    private float[] precio;
    private float[] calidadB;
    private float[] precioB;
    private int nDatos = 0;
    private String nombre;
    private int calidadMax = 8 ;
    
    public InfoMercado(int producto) throws IOException {
        this.producto = producto;
    }
    
    //Extrae la calidad y el precio de el JSON y los guarda en arreglos int
    public void Extrae() throws IOException {
        JSONArray json = readJsonAFromUrl(url_sim + producto);
        nDatos = json.length();
        
        calidad = new float[json.length()];
        precio = new float[json.length()];

        for (int i = 0; i < json.length(); i++) {
            JSONObject vendedor = json.getJSONObject(i);
            calidad[i] = vendedor.getFloat("quality");
            precio[i] = vendedor.getFloat("price");

        }
        
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static JSONArray readJsonAFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONArray json = new JSONArray(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    
    public void leeBaratos() throws IOException{
        Extrae();

        precioB = new float [calidadMax];
        calidadB = new float [calidadMax];
        
        for(int i = 0 ; i < calidadMax ; i++){
            for(int j = 0 ; j < nDatos ; j++){
                if(calidad[j] == i){
                    calidadB[i] = i;
                    precioB[i] = precio[j];
                    break;
                }else{
                    calidadB[i] = i;
                    precioB[i] = -1;
                }
            }
        }
        
        ordenaBaratos();
    }
    
    public void ordenaBaratos(){
        //float masBarato = precioB[0] ; 
        
        for(int i = precioB.length-1;  i > 0 ; i-- ){
            //System.out.println(producto + "---Precio B[i]= " + precioB[i]);
            if(precioB[i] < precioB[i-1] && precioB[i] > 0 ){           
                //System.out.println(producto + "---Precio B[i]= " + precioB[i] + "  , precio B[i-1] = " + precioB[i-1] +"Cambiando" );
                precioB[i-1] = precioB[i];
            }else if( precioB[i-1] ==  -1 && precioB[i] > 0  ){
                precioB[i-1] = precioB[i];
            }
        }
        
        /*for(int i = 1 ; i < calidadMax-1; i++){
            if(precioB[i] > precioB[i+1] &&   precioB[i+1] > -1){
                precioB[i] = precioB[i+1];
                //System.out.println(producto + "---Precio B[i]= " + precioB[i] + "  , precio B[i+1] = " + precioB[i+1] +"Cambiando" );
            }
            
        }*/
    }
    
    
        //regresa el arreglo con todos los precios 
    public float[] getPreciosB(){
        return precioB;
    } 
    
    //regresa el arreglo con todas las calidades
    public float[] getCalidadesB(){
        return calidadB;
    }   
    
    //regresa el arreglo con todos los precios 
    public float[] getPrecios(){
        return precio;
    } 
    
    //regresa el arreglo con todas las calidades
    public float[] getCalidades(){
        return calidad;
    }           
            
}
