/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Start;

import static Servicios.ArbolDatos.*;
import Servicios.Consultas;
import Servicios.IniciarSesion;
import Servicios.StartJson;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author LENOVO
 */
@WebService(serviceName = "Start")
public class Start {

    StartJson js = new StartJson();
    IniciarSesion IniSesion = new IniciarSesion();
    Consultas Query = new Consultas();

    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    @WebMethod(operationName = "jsonTrans")
    public String jsonTrans(@WebParam(name = "json") String json) {
        return js.inicioProceso(json);
    }

    @WebMethod(operationName = "login")
    public String login(@WebParam(name = "json") String json) {
        return IniSesion.login(json);
    }

    @WebMethod(operationName = "jsonquery")
    public String jsonquery(@WebParam(name = "consulta") String consulta) {
        return Query.ConsultaQuery(consulta);
    }

    @WebMethod(operationName = "jsonreporte")
    public String jsonreporte(@WebParam(name = "consulta") String consulta) {
        return Query.ConsultaReporte(consulta);
    }

    @WebMethod(operationName = "jsonqueryMulti")
    public String jsonqueryMulti(@WebParam(name = "consulta") String consulta) {
        return Query.ConsultaMultiples(consulta);
    }

    @WebMethod(operationName = "carga_datos_arbol")
    public void carga_datos_arbol() {
        cargarDatos();
    }
}
