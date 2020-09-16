/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Conecion.ConecionBD;
import Modelos.TablaBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public final class ArbolDatos {

    static TreeMap<Integer, TreeMap<Integer, TreeMap<String, List>>> map_esquema = new TreeMap<>();
    //Base-Version-Tablas-List
    static int secuencia = 0;

    public static void cargarDatos() {
        System.out.println("ENTRO A CARGAR DATOS");
        map_esquema.clear();
        TreeMap<Integer, TreeMap<String, List>> Nodo_Secuencia = new TreeMap<>();
        TreeMap<String, List> Nodo_tablas = new TreeMap<>();
        List<String> lista_Tablas = new ArrayList();
        lista_Tablas = estructuraBase(1);

        for (String tabla : lista_Tablas) {
            List<TablaBD> camposBD = new ArrayList();
            camposBD = OperacionesBD.camposTablaInsertArbol(tabla, 1);

            Nodo_tablas.put(tabla, camposBD);
        }

        int s = secuencia;
        if (secuencia == 0) {
            secuencia = 1;
        } else {
            secuencia++;
        }
        System.out.println("Secuencia : " + secuencia);
        Nodo_Secuencia.put(secuencia, Nodo_tablas);
        map_esquema.put(1, Nodo_Secuencia);
        System.out.println("Datos en Esquema Size : " + map_esquema.size());
        recorrerArbol();

    }

    public static List<String> estructuraBase(int base) {
        String consultas = "";
        consultas = "SELECT tablename FROM pg_catalog.pg_tables where schemaname='public'";
//        consultas = "SELECT tablename FROM pg_catalog.pg_tables where schemaname='public' and tablename like 'm_%'\n"
//                + "and tablename='m_tipodocumentos'";
        System.out.println("Consulta : " + consultas);
        ArrayList<String> listCadena = new ArrayList();
        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            System.out.println("Inicio Consulta");
            PreparedStatement preparedStatement = con.prepareStatement(consultas);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                listCadena.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println("Error en consulta : " + ex.toString());
            Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, "Error ConsultaQuery ", ex);
        }
        return listCadena;
    }

    public static List<TablaBD> buscarDatos(String tabla, int base) {
        System.out.println("Inicio a buscar Datos tabla :  " + tabla);
        List<TablaBD> camposBD = new ArrayList();
        System.out.println("Secuencia : " + secuencia);
        if (map_esquema.get(base).get(secuencia).get(tabla).size()>0) {
            camposBD = map_esquema.get(base).get(secuencia).get(tabla);
        }
        for (TablaBD tablaBD : camposBD) {
            System.out.println("-- " + tablaBD.toString());
        }
        return camposBD;
    }

    public static void recorrerArbol() {
        TreeMap<Integer, TreeMap<Integer, TreeMap<String, List>>> map_esquema2 = new TreeMap<>();
        map_esquema2 = ArbolDatos.map_esquema;
        Set set = map_esquema2.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
            System.out.println("Key : " + mentry.getKey());
            System.out.println("valor : " + mentry.getValue());
        }
        
        System.out.println("_-----------------------------------------------");
        
        Set set2 = map_esquema2.get(1).get(secuencia).entrySet();
        Iterator iterator2 = set2.iterator();

        while (iterator2.hasNext()) {
            Map.Entry mentry2 = (Map.Entry) iterator2.next();
            System.out.println("Key : " + mentry2.getKey());
            System.out.println("valor : " + mentry2.getValue());
        }
        if (map_esquema.get(1).get(secuencia).get("m_tipodocumentos").size()>0) {
            System.out.println("Existe Key");
        } else {
            System.out.println("No existe Key");
        }
    }

}
