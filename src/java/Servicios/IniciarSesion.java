/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Conecion.ConecionBD;
import Modelos.JsonResponseTrans;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LENOVO
 */
public class IniciarSesion implements Serializable {

    Gson gson = new Gson();

    public IniciarSesion() {
        System.out.println("Inicia IniciarSesion");
    }

    public String login(String loginJson) {
        System.out.println("Login--------- : " + loginJson);
        String mns = "";
        Map<String, Object> map = gson.fromJson(loginJson, new TypeToken<Map<String, Object>>() {
        }.getType());
        System.out.println("Prueba : " + map.get("login"));
        System.out.println("base : " + map.get("base"));
        return validacionUsuario((Map<String, Object>) map.get("login"), Integer.parseInt((String) map.get("base")));
    }

    public String validacionUsuario(Map<String, Object> objJson, int base) {
        JsonResponseTrans Jrt = new JsonResponseTrans();
        System.out.println("Recibe : " + objJson.toString());
        System.out.println("Usuario : " + objJson.get("usuario"));
        System.out.println("Clave : " + objJson.get("clave"));
        String resultJson = "{+}";
        Jrt.setProceso("login");
        try (Connection con = ConecionBD.pool.getDataSource(base).getConnection();) {
            System.out.println("Inicio Consulta");
            PreparedStatement preparedStatement = con.prepareStatement("select to_json(Y.*) from (\n"
                    + "select a.usuario,b.cod_documento,b.nombrecompleto from m_usuarios a , m_persona b  where \n"
                    + "a.cod_persona=b.cod_persona and \n"
                    + "usuario='" + objJson.get("usuario") + "' and passowrd='" + objJson.get("clave") + "')Y");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.println("-----------");

            if (rs.next()) {
                System.out.println("entro RS");
                resultJson = rs.getString(1);
                Jrt.setEstado("true");
                Jrt.setMns(resultJson);
            } else {
                Jrt.setMns("Usuario No existe");
                Jrt.setEstado("false");
            }
            resultJson = gson.toJson(Jrt);
        } catch (SQLException ex) {
            Jrt.setMns("Error Conecion Base de Datos");
            Jrt.setEstado("false");
            resultJson = gson.toJson(Jrt);
            System.out.println("Error en consulta : " + ex.toString());
            Logger.getLogger(IniciarSesion.class.getName()).log(Level.SEVERE, "Error validacionUsuario ", ex);
        }
        System.out.println("Resultafo Json : " + resultJson);
        return resultJson;
    }

}
