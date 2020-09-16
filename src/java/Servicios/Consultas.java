/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Conecion.ConecionBD;
import Modelos.ConsultaMult;
import com.google.gson.Gson;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelos.objsql;

/**
 *
 * @author LENOVO
 */
public class Consultas implements Serializable {

    Gson gson = new Gson();

    public Consultas() {
        System.out.println("Inicia Consultas");
    }

    public String ConsultaQuery(String Jsonquery) {
        String consultas = "";
        consultas = "select row_to_json(Y.*) from (";
        consultas = consultas + Jsonquery;
        consultas = consultas + ")Y";
        System.out.println("Consulta : " + consultas);
        ArrayList<String> listCadena = new ArrayList();
        try (Connection con = ConecionBD.pool.getDataSource(1).getConnection();) {
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
        if (listCadena.size() <= 0) {
            listCadena.add("No hay Datos");
        }
        return gson.toJsonTree(listCadena).toString();
    }

    public String ConsultaMultiples(String Jsonquery) {
        String[] listaObj = gson.fromJson(Jsonquery, String[].class);
        String consultas = "";
        int numConsulta = 1;
        List<ConsultaMult> Respuesta = new ArrayList();
        for (String string : listaObj) {
            ArrayList<String> listCadena = new ArrayList();
            ConsultaMult consulta = new ConsultaMult();
            consulta.setConsulta("" + numConsulta);
            consultas = "";
            consultas = "select row_to_json(Y.*) from (";
            consultas = consultas + string;
            consultas = consultas + ")Y";
            System.out.println("Consulta : " + consultas);

            try (Connection con = ConecionBD.pool.getDataSource(1).getConnection();) {
                System.out.println("Inicio Consulta " + numConsulta);
                PreparedStatement preparedStatement = con.prepareStatement(consultas);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    listCadena.add(rs.getString(1));
                }
            } catch (SQLException ex) {
                System.out.println("Error en consulta : " + ex.toString());
                Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, "Error ConsultaQuery ", ex);
            }
            if (listCadena.size() <= 0) {
                listCadena.add("No hay Datos");
            }
            consulta.setRespuesta(listCadena);
            Respuesta.add(consulta);
            numConsulta++;
        }

        return gson.toJsonTree(Respuesta).toString();
    }

    public String ConsultaReporte(String Jsonquery) {
        String consultas = "";
        consultas = "select array_to_json(array_agg(Y.*)) from (";
        consultas = consultas + Jsonquery;
        consultas = consultas + ")Y";
        System.out.println("Consulta : " + consultas);
        String json = "";
        try (Connection con = ConecionBD.pool.getDataSource(1).getConnection();) {
            System.out.println("Inicio Consulta");
            PreparedStatement preparedStatement = con.prepareStatement(consultas);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                json = rs.getString(1);
            }
        } catch (SQLException ex) {
            System.out.println("Error en consulta : " + ex.toString());
            Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, "Error ConsultaQuery ", ex);
        }
        if (json.length() <= 0) {
            json = ("No hay Datos");
        }
        return gson.toJsonTree(json).toString();
    }

}
