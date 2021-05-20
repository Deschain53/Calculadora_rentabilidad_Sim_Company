/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;
import clases.LeeJSON;
import java.io.IOException;
import org.json.JSONObject;
/**
 *
 * @author donyo
 */


public class NombresPEN {
    private String[] nombreEN = new String[200];
    private LeeJSON rj = new LeeJSON();
    private String url_productos = "https://www.simcompanies.com/api/v3/es/encyclopedia/resources/1/";
    private JSONObject producto; 
            
    public NombresPEN(){
        
nombreEN[66] = "Seeds";
nombreEN[3] = "Apples";
nombreEN[4] = "Oranges";
nombreEN[5] = "Grapes";
nombreEN[6] = "Grain";
nombreEN[72] = "Sugarcane";
nombreEN[40] = "Cotton";
nombreEN[106] = "Wood";
nombreEN[101] = "Reinforced concrete";
nombreEN[102] = "Bricks";
nombreEN[103] = "Cement";
nombreEN[44] = "Sand";
nombreEN[104] = "Clay";
nombreEN[105] = "Limestone";
nombreEN[107] = "Steel beams";
nombreEN[108] = "Planks";
nombreEN[109] = "Windows";
nombreEN[110] = "Tools";
nombreEN[111] = " Construction units ";       
nombreEN[41] = "Fabric";
nombreEN[60] = "Underwear";
nombreEN[61] = "Gloves";
nombreEN[62] = "Dress";
nombreEN[63] = "Stiletto Heel";
nombreEN[64] = "Handbags";
nombreEN[65] = "Sneakers";
nombreEN[70] = "Luxury watch";
nombreEN[71] = "Necklace";        
nombreEN[14] = "Minerals";
nombreEN[15] = "Bauxite";
nombreEN[68] = "Gold ore";
nombreEN[42] = "Iron ore";
nombreEN[16] = "Silicon";
nombreEN[17] = "Chemicals";
nombreEN[18] = "Aluminium";
nombreEN[69] = "Golden bars";
nombreEN[43] = "Steel";
nombreEN[45] = "Glass";
nombreEN[76] = "Carbon composite";
nombreEN[67] = "Xmas crackers";
nombreEN[80] = "Flight computer";
nombreEN[81] = "Cockpit";
nombreEN[82] = "Attitude control";
nombreEN[98] = "Quadcopter";
nombreEN[99] = "Satellite";
nombreEN[48] = "Electric motor";
nombreEN[52] = "Combustion engine";
nombreEN[85] = "Solid fuel booster";
nombreEN[86] = "Rocket engine";
nombreEN[88] = "Ion drive";
nombreEN[89] = "Jet engine";
nombreEN[49] = "Luxury car interior";
nombreEN[50] = "Basic interior";
nombreEN[51] = "Car body";
nombreEN[53] = "Economy e-car";
nombreEN[54] = "Luxury e-car";
nombreEN[55] = "Economy car";
nombreEN[56] = "Luxury car";
nombreEN[57] = "Truck";
nombreEN[112] = "Bulldozer";
nombreEN[77] = "Fuselage";
nombreEN[78] = "Wing";
nombreEN[84] = "Propellant tank";
nombreEN[87] = "Heat shield";
nombreEN[90] = "Sub-orbital 2nd stage";
nombreEN[92] = "Orbital booster";
nombreEN[93] = "Starship";
nombreEN[10] = "Crude oil";
nombreEN[74] = "Methane";
nombreEN[11] = "Petrol";
nombreEN[12] = "Diesel";
nombreEN[19] = "Plastic";
nombreEN[75] = "Carbon fibers";
nombreEN[83] = "Rocket fuel";
nombreEN[95] = "Jumbo jet";
nombreEN[96] = "Luxury jet";
nombreEN[97] = "Single engine plane";
nombreEN[91] = "Sub-orbital rocket";
nombreEN[94] = "BFR";    

nombreEN [1] = "Electricity";
nombreEN [2] = "Water";
nombreEN [7] = "Steak";
nombreEN [8] = "Sausages";
nombreEN [9] = "Eggs";
nombreEN [13] = "Transport";
nombreEN [20] = "Processors";
nombreEN [21] = "Electronic components";
nombreEN [22] = "Batteries";
nombreEN [23] = "Screens";
nombreEN [24] = "Smartphones";
nombreEN [25] = "Tablets";
nombreEN [26] = "Laptops";
nombreEN [27] = "Monitors";
nombreEN [28] = "Televisions";
nombreEN [29] = "Agricultural research";
nombreEN [30] = "Energy research";
nombreEN [31] = "Mining research";
nombreEN [32] = "Electronic research";
nombreEN [33] = "Livestock research";
nombreEN [34] = "Chemical research";
nombreEN [35] = "Software";
nombreEN [46] = "Leather";
nombreEN [47] = "On-board computer";
nombreEN [58] = "Automotive Research";
nombreEN [59] = "Fashion research";
nombreEN [73] = "Ethanol";
nombreEN [79] = "High grade electronics";
nombreEN [100] = "Aerospace research";
nombreEN [113] = "Investigation of materials";
}     
    
    public String getNamePEN(int i){
        return nombreEN[i];
    }
    
    /*public void getPerdidos() throws IOException{
        for (int i = 40; i < 120; i++) {
            if(nombreEN[i] == null){
                producto = LeeJSON.readJsonFromUrl(url_productos + i);
                System.out.println("nombreEN[" + i + "] = \""+ producto.getString("name") +"\"");          
            }
        }
    }*/
    
    public boolean existe(int codigoProducto){
        boolean exist = false;
        if(nombreEN[codigoProducto] != null){
            exist = true;
        }
        return exist;
    }
    
    public int numeroProductos(){
        return  nombreEN.length;  //Regresa el numero de productos totales
    }
    
}

/*nombreEN[80]Flight computer
nombreEN[81]Cockpit
nombreEN[82]Attitude control
nombreEN[98]Quadcopter
nombreEN[99]Satellite
nombreEN[80]Flight computer
nombreEN[81]Cockpit
nombreEN[82]Attitude control
nombreEN[98]Quadcopter
nombreEN[99]Satellite
nombreEN[66]Seeds
nombreEN[3]Apples
nombreEN[4]Oranges
nombreEN[5]Grapes
nombreEN[6]Grain
nombreEN[72]Sugarcane
nombreEN[40]Cotton
nombreEN[106]Wood

nombreEN[101]Reinforced concrete
nombreEN[102]Bricks
nombreEN[103]Cement
nombreEN[44]Sand
nombreEN[104]Clay
nombreEN[105]Limestone

nombreEN[107]Steel beams
nombreEN[108]Planks
nombreEN[109]Windows
nombreEN[110]Tools
nombreEN[111]Construction units
nombreEN[41]Fabric
nombreEN[60]Underwear
nombreEN[61]Gloves
nombreEN[62]Dress
nombreEN[63]Stiletto Heel
nombreEN[64]Handbags
nombreEN[65]Sneakers
nombreEN[70]Luxury watch
nombreEN[71]Necklace

nombreEN[14]Minerals
nombreEN[15]Bauxite
nombreEN[68]Gold ore
nombreEN[42]Iron ore

nombreEN[16]Silicon
nombreEN[17]Chemicals
nombreEN[18]Aluminium
nombreEN[69]Golden bars
nombreEN[43]Steel
nombreEN[45]Glass
nombreEN[76]Carbon composite
nombreEN[67]Xmas crackers
nombreEN[80]Flight computer
nombreEN[81]Cockpit
nombreEN[82]Attitude control
nombreEN[98]Quadcopter
nombreEN[99]Satellite
nombreEN[48]Electric motor
nombreEN[52]Combustion engine
nombreEN[85]Solid fuel booster
nombreEN[86]Rocket engine
nombreEN[88]Ion drive
nombreEN[89]Jet engine
nombreEN[49]Luxury car interior
nombreEN[50]Basic interior
nombreEN[51]Car body
nombreEN[53]Economy e-car
nombreEN[54]Luxury e-car
nombreEN[55]Economy car
nombreEN[56]Luxury car
nombreEN[57]Truck
nombreEN[112]Bulldozer
nombreEN[77]Fuselage
nombreEN[78]Wing
nombreEN[84]Propellant tank
nombreEN[87]Heat shield
nombreEN[90]Sub-orbital 2nd stage
nombreEN[92]Orbital booster
nombreEN[93]Starship
nombreEN[10]Crude oil
nombreEN[74]Methane
nombreEN[11]Petrol
nombreEN[12]Diesel
nombreEN[19]Plastic
nombreEN[75]Carbon fibers
nombreEN[83]Rocket fuel
nombreEN[95]Jumbo jet
nombreEN[96]Luxury jet
nombreEN[97]Single engine plane
nombreEN[91]Sub-orbital rocket
nombreEN[94]BFR*/
