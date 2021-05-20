/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import frames.BarraProgreso;

/**
 *
 * @author donyo
 */
public class MuestraBarraProgreso extends Thread {
    public void run(){
        BarraProgreso bp = new BarraProgreso();
        //bp.setVisible(true);               
    }
}
