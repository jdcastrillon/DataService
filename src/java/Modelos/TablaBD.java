/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

/**
 *
 * @author LENOVO
 */
public class TablaBD {

    private String campo;
    private String tipoCampo;
    private String pk;
    private String secuencia;

    public TablaBD() {
    }

    public TablaBD(String campo, String tipoCampo, String pk, int num) {
        this.campo = campo;
        this.tipoCampo = tipoCampo;
        this.pk = pk;

    }

    public TablaBD(String campo, String tipoCampo, String secuencia) {
        this.campo = campo;
        this.tipoCampo = tipoCampo;
        this.secuencia = secuencia;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getTipoCampo() {
        return tipoCampo;
    }

    public void setTipoCampo(String tipoCampo) {
        this.tipoCampo = tipoCampo;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(String secuencia) {
        this.secuencia = secuencia;
    }

    @Override
    public String toString() {
        return "TablaBD{" + "campo=" + campo + ", tipoCampo=" + tipoCampo + ", pk=" + pk + ", secuencia=" + secuencia + '}';
    }

}
