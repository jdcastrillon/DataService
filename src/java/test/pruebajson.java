/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import Conecion.ConecionBD;
import Servicios.IniciarSesion;
import Servicios.StartJson;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class pruebajson {

    public static void main(String[] args) throws InterruptedException {
//        System.out.println("fecha : " + new Date().getTime());
        Gson gson = new Gson();
//
//        objsql o1 = new objsql();
//        o1.setAccion("insert");
//        o1.setTabla("prueba");
//        o1.getCampos().put("numero", 10);
//        o1.getCampos().put("nombre", "Juan");
//
//        objsql o2 = new objsql();
//        o2.setAccion("insert");
//        o2.setTabla("prueba");
//        o2.getCampos().put("numero", 10);
//        o2.getCampos().put("nombre", "Juan");
////
//        objsql o3 = new objsql();
//        o3.setAccion("insert");
//        o3.setTabla("prueba");
//        o3.getCampos().put("numero", 10);
//        o3.getCampos().put("nombre", "JuanXXXX");
////
////        objsql o4 = new objsql();
////        o4.setAccion("insert");
////        o4.setTabla("prueba");
////        o4.getCampos().put("numero", 10);
////        o4.getCampos().put("nombre", "Juan");
//
////        objsql o2 = new objsql();
////        o2.setAccion("delete");
////        o2.setTabla("nada2");
////        o2.getCampos().put("trans", "103");
//        contenedor c = new contenedor();
//        c.setMetodo("trans");
//        c.getContenido().put("C1", o1);
//        c.getContenido().put("C2", o2);
//        c.getContenido().put("C3", o3);
////        c.getContenido().put("C4", o4);
//        
////        c.getContenido().put("C2", o2);
//
//        System.out.println("- " + c.toString());
//        String json = gson.toJson(c);
//        System.out.println("- " + json);

//        StartJson js = new StartJson();
//        js.inicioProceso(json);
        String jsonArray = "";
        pruebajson json = new pruebajson();
        //json.cargaDatos();
//        String r = gson.toJson(json.cargaDatos2());
//        System.out.println("" + r);
        String jsonArr = gson.toJsonTree(json.cargaDatos2()).toString();
        JsonParser parser = new JsonParser();
        JsonArray gsonArr = parser.parse(jsonArr).getAsJsonArray();
        //System.out.println("- " + j1.toString());
        for (JsonElement jsonElement : gsonArr) {
            System.out.println("-- " + jsonElement.getAsString());
            if (!jsonElement.getAsString().equalsIgnoreCase("No hay Datos")) {
//            Map<String, Object> map = (Map<String, Object>) jsonElement.;
                Map<String, Object> map = gson.fromJson(jsonElement.getAsString(), new TypeToken<Map<String, Object>>() {
                }.getType());
                System.out.println("cod_tipodoc : " + map.get("cod_tipodoc"));
            }
        }
        //System.out.println(gson.toJsonTree(json.cargaDatos2()));

    }

    public void cargaDatos() {
//        try (Connection con = ConecionBD.pool.getDataSource().getConnection();) {
//            System.out.println("Inicio Consulta");
//            PreparedStatement preparedStatement = con.prepareStatement("select row_to_json(Y.*) from (\n"
//                    + "select * from m_tipodocumentos)Y");
//            ResultSet rs = preparedStatement.executeQuery();
//
//            while (rs.next()) {
//                System.out.println("entro RS");
//                System.out.println("- " + rs.getString(1));
//            }
//        } catch (SQLException ex) {
//            System.out.println("Error en consulta : " + ex.toString());
//            Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, "Error validacionUsuario ", ex);
//        }
    }

    public ArrayList<String> cargaDatos2() {
        ArrayList<String> listCadena = new ArrayList();
//        try (Connection con = ConecionBD.pool.getDataSource().getConnection();) {
//            System.out.println("Inicio Consulta");
//            PreparedStatement preparedStatement = con.prepareStatement("select row_to_json(Y.*) from (\n"
//                    + "select * from m_tipodocumentos )Y");
//            ResultSet rs = preparedStatement.executeQuery();
//
//            while (rs.next()) {
////                System.out.println("entro RS");
////                System.out.println("- " + rs.getString(1));
//                listCadena.add(rs.getString(1));
//            }
//        } catch (SQLException ex) {
//            System.out.println("Error en consulta : " + ex.toString());
//            Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, "Error validacionUsuario ", ex);
//        }
//        if (listCadena.size() <= 0) {
//            listCadena.add("No hay Datos");
//        }
        return listCadena;
    }
}
