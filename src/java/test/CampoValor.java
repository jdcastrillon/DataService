/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

/**
 *
 * @author LENOVO
 */
public class CampoValor {
    private String campo;
    private Object valor;

    public CampoValor() {
    }

    public CampoValor(String campo, Object valor) {
        this.campo = campo;
        this.valor = valor;
    }
    

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
      
}
