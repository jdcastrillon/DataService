/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Conecion.ConecionBD;
import Modelos.JsonResponseTrans;
import Modelos.ListTransacciones;
import Modelos.TablaBD;
import com.google.gson.Gson;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class OperacionesBD implements Serializable {

    public String transacciones(String proceso, List<ListTransacciones> trans, Gson gson, int base) {
        JsonResponseTrans Jrt = new JsonResponseTrans();
        Jrt.setProceso(proceso);
        String mns = "";
        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            con.setAutoCommit(false);
            System.out.println("Conecto bien");

            for (ListTransacciones tran : trans) {
                if (tran.getTabla().equalsIgnoreCase("Impacto")) {
                    mns = executeTrans2(con, tran.getTrans(), mns);
                } else {
                    mns = executeTrans(con, tran.getTrans(), mns);
                }
            }
            System.out.println("MNS : " + mns);
            con.commit();
            con.setAutoCommit(true);

            Jrt.setEstado("OK");
            Jrt.setMns(mns);
        } catch (SQLException ex) {
            Jrt.setEstado("Error");
            Jrt.setMns(ex.getMessage());
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error Crear Actividad ", ex);
        } catch (Exception ex) {
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        return gson.toJson(Jrt);
    }

    public String executeTrans(Connection con, String trans, String mns) throws Exception {
        try (Statement statement = con.createStatement();) {
            System.out.println("Conecto executeTrans : " + trans);
            int result = statement.executeUpdate(trans);
            System.out.println("Result int : " + result);
            mns = result > 0 ? "OK" : "";
        } catch (SQLException ex) {
            System.out.println("Error Proceso");
            mns = ex.getMessage();
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error executeTrans ", ex);
            throw ex;
        }
        return mns;
    }

    public String executeTrans2(Connection con, String trans, String mns) throws Exception {
        try (Statement statement = con.createStatement();) {
            System.out.println("Conecto executeTrans2 : " + trans);
            PreparedStatement preparedStatement = con.prepareStatement(trans);
            ResultSet rs = preparedStatement.executeQuery();
            mns = "OK";
        } catch (SQLException ex) {
            System.out.println("********************************************************************");
            System.out.println("Error Proceso : " + ex.getMessage());
            System.out.println("********************************************************************");
            mns = ex.getMessage();
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error executeTrans ", ex);
            throw ex;
        }
        return mns;
    }

    public List<String> llavesPrimary(String tabla,int base) {
        List<String> keys = new ArrayList();
        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            con.setAutoCommit(false);
            System.out.println("Conecto bien");

            DatabaseMetaData metaDatos = con.getMetaData();

            ResultSet rs = metaDatos.getPrimaryKeys(null, null, tabla);
            while (rs.next()) {
                keys.add(rs.getString("COLUMN_NAME"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error llavesPrimary ", ex);
        }
        return keys;
    }

    public List<TablaBD> camposTablaInsert(String tabla,int base) {
        List<TablaBD> campos = new ArrayList();
        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            con.setAutoCommit(false);
            System.out.println("Conecto bien tabla: " + tabla);
            DatabaseMetaData metaDatos = con.getMetaData();

            System.out.println("-***********************************************");
            ResultSet rs = metaDatos.getColumns("public", null, tabla, null);

            while (rs.next()) {
                campos.add(new TablaBD(rs.getString("COLUMN_NAME"), rs.getString(6), rs.getString("IS_AUTOINCREMENT")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error llavesPrimary ", ex);
        }
        return campos;
    }

    public List<TablaBD> camposTablaUpdate(String tabla,int base) {
        List<TablaBD> campos = new ArrayList();
        List<String> keys = new ArrayList();
        keys = llavesPrimary(tabla,base);

        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            con.setAutoCommit(false);
            System.out.println("Conecto bien tabla: " + tabla);
            DatabaseMetaData metaDatos = con.getMetaData();

            System.out.println("-***********************************************");
            ResultSet rs = metaDatos.getColumns("public", null, tabla, null);

            while (rs.next()) {
                TablaBD tablaUpdate = new TablaBD(rs.getString("COLUMN_NAME"), rs.getString(6), "N", 0);
                for (String key : keys) {
                    if (key.equalsIgnoreCase(rs.getString("COLUMN_NAME"))) {
                        System.out.println(" pk : " + rs.getString("COLUMN_NAME"));
                        System.out.println(".. " + tablaUpdate.toString());
                        tablaUpdate.setPk("S");
                        break;
                    }
                }
                campos.add(tablaUpdate);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error llavesPrimary ", ex);
        }
        return campos;
    }

    public List<TablaBD> camposTablaDelete(String tabla,int base) {
        List<TablaBD> campos = new ArrayList();
        List<String> keys = new ArrayList();
        keys = llavesPrimary(tabla,base);

        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            con.setAutoCommit(false);
            System.out.println("Conecto bien tabla: " + tabla);
            DatabaseMetaData metaDatos = con.getMetaData();

            System.out.println("-***********************************************");
            ResultSet rs = metaDatos.getColumns("public", null, tabla, null);

            while (rs.next()) {

                for (String key : keys) {
                    if (key.equalsIgnoreCase(rs.getString("COLUMN_NAME"))) {
                        TablaBD tablaUpdate = new TablaBD(rs.getString("COLUMN_NAME"), rs.getString(6), "N", 0);
                        System.out.println(" pk : " + rs.getString("COLUMN_NAME"));
                        System.out.println(".. " + tablaUpdate.toString());
                        tablaUpdate.setPk("S");
                        campos.add(tablaUpdate);
                        break;
                    }
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error llavesPrimary ", ex);
        }
        return campos;
    }
    
    //Metodos para Arbol
     public static List<TablaBD> camposTablaInsertArbol(String tabla,int base) {
        List<TablaBD> campos = new ArrayList();
        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            con.setAutoCommit(false);
            System.out.println("Conecto bien tabla: " + tabla);
            DatabaseMetaData metaDatos = con.getMetaData();

            System.out.println("-***********************************************");
            ResultSet rs = metaDatos.getColumns("public", null, tabla, null);

            while (rs.next()) {
                campos.add(new TablaBD(rs.getString("COLUMN_NAME"), rs.getString(6), rs.getString("IS_AUTOINCREMENT")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OperacionesBD.class.getName()).log(Level.SEVERE, "Error llavesPrimary ", ex);
        }
        return campos;
    }

}
