/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import clases.LeeJSON;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import clases.ProductObject;

//Extrae la información del mercado de cada producto de un edificio
public class ExtraeTodoEdificio {

    private String edificio_code;
    private JSONObject edificio_info;
    private JSONObject[] maneja_info; //Array de jason objects con la informacion de los productos que un edificio puede producir
    private String url_edificios = "https://www.simcompanies.com/api/v2/encyclopedia/buildings/";
    private String url_productos = "https://www.simcompanies.com/api/v3/es/encyclopedia/resources/0/";
    private LeeJSON rj = new LeeJSON();
    private int numero_maneja;       //Numero de productos que un edificio puede producir
    private int numero_productosT;
    private int productos_en_sim = 200;
    private ProductObject[] po;     //Arreglo de productos, este es el importante
    private int[] maneja_codigos;
    private int numEdificio;
    private JSONproductos jsonP;

    //***********Para la parte de las ventas
    public ExtraeTodoEdificio(String edificio) {
        this.edificio_code = edificio;
        po = new ProductObject[productos_en_sim];
        jsonP = new JSONproductos();
    }

    public void setUrlEconomia(int fase) {
        url_productos = "https://www.simcompanies.com/api/v3/es/encyclopedia/resources/" + fase + "/";
        jsonP.setFase(fase);
    }

    //Clase que extraeProduce el Json con la informacion del edificio de la enciclopedia para su posterior procesamiento
    public void extrae_JsonsEdificio() throws IOException {
        edificio_info = rj.readJsonFromUrl(url_edificios + edificio_code);
    }

    //Clase que rellena el array con la informacion de las materias primas que produce desde la enciclopedia
    public void extrae_materiasProduce() throws IOException {
        numero_maneja = edificio_info.getJSONArray("doesProduce").length();
        maneja_info = new JSONObject[numero_maneja];

        for (int i = 0; i < numero_maneja; i++) {
            maneja_info[i] = jsonP.getproducto(getCodigoProduceNumero(i));
        }

    }

    //Rellena en cada posicion del ProductObject con la informacion desde la enciclopedia del recurso
    private void rellena_ProductObject() throws IOException {
        maneja_codigos = new int[numero_maneja];
        numero_productosT = numero_maneja;

        for (int i = 0; i < numero_maneja; i++) {

            int id = maneja_info[i].getInt("db_letter");
            po[id] = new ProductObject(maneja_info[i]);
            maneja_codigos[i] = id;
            for (int j = 0; j < maneja_info[i].getJSONArray("producedFrom").length(); j++) {
                int id_materia_prima = maneja_info[i].getJSONArray("producedFrom").getJSONObject(j).getJSONObject("resource").getInt("db_letter");
                po[id_materia_prima]
                        //= new ProductObject(rj.readJsonFromUrl(url_productos + id_materia_prima));
                        = new ProductObject(jsonP.getproducto(id_materia_prima));
                numero_productosT++;
            }
        }
    }

    //Rellena el array con la informacion con los codigos que un edificio produce
    public void rellenaManejaArrayLocal() {
        maneja_codigos = new int[edificio_info.getJSONArray("doesProduce").length()];
        for (int i = 0; i < numero_maneja; i++) {
            maneja_codigos[i] = edificio_info.getJSONArray("doesProduce").getJSONObject(i).getInt("db_letter");
        }
    }

    public void extraeProduce() throws IOException {
        extrae_materiasProduce();
        rellena_ProductObject();

    }

    //Para extraer solo lo concerniente a investigacion
    public void extraeInfoInvestigacion() throws IOException {
        extrae_materiasProduce();
        numero_productosT = numero_maneja;

        for (int i = 0; i < numero_maneja; i++) {
            int id = maneja_info[i].getInt("db_letter");
            po[id] = new ProductObject(maneja_info[i]);
            maneja_codigos[i] = id;
        }
    }

    public ProductObject[] getProducObjectArray() {
        return po;
    }

    //Numero de productos que produce el edificio
    public int getNumeroManeja() {
        return edificio_info.getJSONArray("doesProduce").length();
    }

    //Numero de productos que maneja el edificio = numero_produce + materias_primas
    public int getNumeroProductosT() {
        return numero_productosT;
    }

    public int[] getProduceCodigos() {
        return maneja_codigos;
    }

    public float getSalariosHora() {
        return edificio_info.getFloat("wages");
    }

    public JSONObject getEdificioInfo() {
        return edificio_info;
    }

    public void setProductObject(ProductObject[] po) {
        this.po = po;
    }

    public void setNumeroEdificio(int ne) {
        numEdificio = ne;
    }

    public int getNumeroEdificio() {
        return numEdificio;
    }

    public int getCodigoProduceNumero(int i) {
        return edificio_info.getJSONArray("doesProduce").getJSONObject(i).getInt("db_letter");//maneja_codigos[i];
    }

    public void setJSONedificio(JSONObject jsonEdificio) {
        this.edificio_info = jsonEdificio;
    }

    //Para la seccion de ventas ************************************************
    public int getCodigoVendeNumero(int i) {
        return edificio_info.getJSONArray("doesSell").getJSONObject(i).getInt("db_letter");//maneja_codigos[i];
    }

    //Numero de productos que vende el edicidio
    public int getNumeroVende() {
        return edificio_info.getJSONArray("doesSell").length();//numero_maneja; -------------------
    }

    //Extrae el array de los productos que vende
    private void extrae_materiasVende() throws IOException {
        numero_maneja = edificio_info.getJSONArray("doesSell").length();
        maneja_info = new JSONObject[numero_maneja];
        for (int i = 0; i < numero_maneja; i++) {
            maneja_info[i] = rj.readJsonFromUrl(url_productos + edificio_info.getJSONArray("doesSell").getJSONObject(i).getInt("db_letter"));
        }
    }

    private void rellena_ProductObjectVende() throws IOException {
        maneja_codigos = new int[numero_maneja];
        numero_productosT = numero_maneja;

        for (int i = 0; i < numero_maneja; i++) {

            int id = maneja_info[i].getInt("db_letter");
            if (po[id] == null) {  //Por precaucion
                po[id] = new ProductObject(maneja_info[i]);
            }
            maneja_codigos[i] = id;
            po[id].extraeLetras();
        }
    }

    public String getRetailModelingProducto(int i) {
        return po[i].getRetailModeling();
    }

    public double[] getLetrasProducto(int i) {
        return po[i].getLetras();
    }

    public String getNombreProducto(int i) {
        return po[i].getNombre();
    }

    //Extrae todo de las ventas
    public void extraeVende() throws IOException {
        extrae_JsonsEdificio();
        extrae_materiasVende();
        rellena_ProductObjectVende();
    }

    public double getPrecioPromedio(int i) {
        return po[i].getPrecioPromedio();
    }

}
