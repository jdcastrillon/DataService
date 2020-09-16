/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conecion;

/**
 *
 * @author LENOVO
 */
public class ConecionBD {

    public static final PoolConeciones pool = new PoolConeciones();

    public PoolConeciones getConecion() {
        return pool;
    }

}
