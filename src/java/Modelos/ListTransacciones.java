/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.io.Serializable;

/**
 *
 * @author LENOVO
 */
public class ListTransacciones implements Serializable {

    private String tabla;
    private String trans;

    public ListTransacciones() {
    }

    public ListTransacciones(String tabla, String trans) {
        this.tabla = tabla;
        this.trans = trans;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getTrans() {
        return trans;
    }

    public void setTrans(String trans) {
        this.trans = trans;
    }

}
