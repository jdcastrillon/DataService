/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import Modelos.ListTransacciones;
import Modelos.TablaBD;
import static Servicios.ArbolDatos.buscarDatos;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import Modelos.objsql;

/**
 *
 * @author LENOVO
 */
public class StartJson implements Serializable {

    Gson gson = new Gson();
    OperacionesBD operacion = new OperacionesBD();

    public StartJson() {
        System.out.println("Inicia StartJson");
    }

    public String inicioProceso(String json) {
        System.out.println("Recibe WebService : " + json);
        objsql[] listaObj = gson.fromJson(json, objsql[].class);
        return Transaccion(listaObj);
    }

    public String Transaccion(objsql[] listaObj) {

        List<ListTransacciones> trans = new ArrayList();
        int base = 0;

        for (objsql obj : listaObj) {
            System.out.println(obj.toString());
            Map<Object, Object> mapCampos = gson.fromJson(obj.getDatos(), new TypeToken<Map<Object, Object>>() {
            }.getType());

            System.out.println("Accion " + mapCampos.get("accion"));
            System.out.println("Base " + mapCampos.get("base"));
            base = 1;
//             base = Integer.parseInt((String) mapCampos.get("base"));
            switch (obj.getAccion().trim()) {
                case "Nuevo":
                    trans.add(new ListTransacciones(obj.getTabla().trim(), lineaInsert(mapCampos, obj.getTabla(), base)));
                    break;
                case "Borrar":
                    trans.add(new ListTransacciones(obj.getTabla().trim(), lineaDelete(mapCampos, obj.getTabla(), base)));
                    break;
                case "Editar":
                    trans.add(new ListTransacciones(obj.getTabla().trim(), lineaUpdate(mapCampos, obj.getTabla(), base)));
                    break;
                case "Impacto":
                    trans.add(new ListTransacciones("Impacto", lineaImpacto(obj.getDatos(), obj.getTabla())));
                    break;
                case "Transaccion":
                    trans.add(new ListTransacciones("Transaccion",obj.getSql()));
                    break;
            }

        }
        return operacion.transacciones("Trans", trans, gson, base);

//        return "";
    }

    public String lineaInsert(Map<Object, Object> datos, String tabla, int base) {
        System.out.println("lineaInsert");
        List<TablaBD> camposBD = new ArrayList();
//        camposBD = buscarDatos(tabla, base);
//        if (camposBD.isEmpty()) {
//            System.out.println("No encontro datos en el arbol lineaInsert");
//            camposBD = operacion.camposTablaInsert(tabla, base);
//        }
        camposBD = operacion.camposTablaInsert(tabla, base);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");

        Object valores = "";
        String Fecha = "";
        String coma = ",";
        String preparacion = "";
        String campos = "";
        int ultimoRegistro = 0;
        String valorCampo = "";

        ultimoRegistro = camposBD.size();
        for (TablaBD tablaBD : camposBD) {
            //System.out.println("ultimoRegistro: " + ultimoRegistro);  
            if (tablaBD.getSecuencia().equalsIgnoreCase("NO")) {
                if (datos.get(tablaBD.getCampo()) == null) {
                    valorCampo = null;
                    valores += "''" + coma;
                } else {
                    valorCampo = datos.get(tablaBD.getCampo()).toString();

                    System.out.println("----> " + tablaBD.getCampo());
                    System.out.println("----> " + tablaBD.getTipoCampo());
                    System.out.println("----> " + valorCampo);
                    switch (tablaBD.getTipoCampo()) {
                        case "timestamp":
                            Fecha = datos.get(tablaBD.getCampo()).toString().replace("@fecha", "");
                            valores += "'" + dateFormat.format(new Date(Long.parseLong(Fecha))) + "'" + coma;
                            break;
                        case "date":
                            Fecha = valorCampo.replace("@fecha", "");
                            valores += "'" + dateFormat.format(new Date(Long.parseLong(Fecha))) + "'" + coma;
                            break;
                        case "varchar":
                            valores += "'" + ((datos.get(tablaBD.getCampo()) == null) ? "null" : datos.get(tablaBD.getCampo())) + "'" + coma;
                            break;
                        case "bpchar":
                            valores += "'" + ((datos.get(tablaBD.getCampo()) == null) ? "null" : datos.get(tablaBD.getCampo())) + "'" + coma;
                            break;
                        case "int4":
                            valores += "" + datos.get(tablaBD.getCampo()) + "" + coma;
                            break;
                        case "json":
                            JsonParser parser = new JsonParser();
                            JsonArray Jelementos = parser.parse(datos.get(tablaBD.getCampo()).toString()).getAsJsonArray();
                            for (JsonElement jsonElement : Jelementos) {
                                System.out.println("json : " + jsonElement);
                            }
                            valores += "to_json(" + datos.get(tablaBD.getCampo()).toString() + "::json)" + coma;
                            break;
                        default:
                            valores += "'" + datos.get(tablaBD.getCampo()) + "'" + coma;
                            break;
                    }
                }

                campos += tablaBD.getCampo() + coma;

                if (ultimoRegistro <= 2) {
                    coma = "";
                }
            }
            ultimoRegistro--;
//            System.out.println("AutoIncrement : " + tablaBD.getCampo() + " -> " + tablaBD.getSecuencia());

        }
        preparacion = "Insert into " + tabla + " (" + campos + ") values (" + valores + ")";
        System.out.println("preparacion : " + preparacion);

        return preparacion;
    }

    public String lineaUpdate(Map<Object, Object> datos, String tabla, int base) {
        List<TablaBD> camposBD = new ArrayList();
//        camposBD = buscarDatos(tabla, base);
//        if (camposBD.isEmpty()) {
//            System.out.println("No encontro datos en el arbol lineaUpdate");
//            camposBD = operacion.camposTablaUpdate(tabla, base);
//        }
        camposBD = operacion.camposTablaUpdate(tabla, base);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd hh:mm:ss");

        Object valores = "";
        String Fecha;
        String coma = ",";
        String separacion = "and ";
        String preparacion = "";
        String campos = "";
        int ultimoRegistro = 0;
        int ultimoRegistroPk = 0;

//        ultimoRegistro = camposBD.size();
        for (TablaBD tablaBD : camposBD) {
            if (tablaBD.getPk().equalsIgnoreCase("S")) {
                ultimoRegistroPk++;
            } else {
                ultimoRegistro++;
            }
        }
        for (TablaBD tablaBD : camposBD) {
            System.out.println("ultimoRegistro: " + ultimoRegistro);
            if (tablaBD.getPk().equalsIgnoreCase("N")) {
                if (ultimoRegistro <= 0) {
                    coma = "";
                }
                if (datos.get(tablaBD.getCampo()) == null) {
                    valores += tablaBD.getCampo() + "=''" + coma;
                } else {
                    switch (tablaBD.getTipoCampo()) {
                        case "timestamp":
                            Fecha = datos.get(tablaBD.getCampo()).toString().replace("@fecha", "");
                            valores += tablaBD.getCampo() + "='" + dateFormat.format(new Date(Long.parseLong(Fecha))) + "'" + coma;
                            break;
                        case "date":
                            Fecha = datos.get(tablaBD.getCampo()).toString().replace("@fecha", "");
                            valores += tablaBD.getCampo() + "='" + dateFormat.format(new Date(Long.parseLong(Fecha))) + "'" + coma;
                            break;
                        case "varchar":
                            // valores += "'" + Contentido.get(tablaBD.getCampo()) + "'" + coma;
                            valores += tablaBD.getCampo() + "='" + datos.get(tablaBD.getCampo()) + "'" + coma;
                            break;
                        case "int4":
                            //valores += "" + Contentido.get(tablaBD.getCampo()) + "" + coma;
                            valores += tablaBD.getCampo() + "=" + datos.get(tablaBD.getCampo()) + "" + coma;
                            break;
                        default:
                            valores += tablaBD.getCampo() + "='" + datos.get(tablaBD.getCampo()) + "'" + coma;
                            break;
                    }
                }
            } else {
                if (ultimoRegistroPk <= 1) {
                    separacion = "";
                }
                switch (tablaBD.getTipoCampo()) {
                    case "int4":
                        campos += tablaBD.getCampo() + "=" + new BigDecimal(datos.get(tablaBD.getCampo()).toString()) + "" + separacion;
                        break;
                    case "serial":
                        campos += tablaBD.getCampo() + "=" + new BigDecimal(datos.get(tablaBD.getCampo()).toString()) + "" + separacion;
                        break;
                    case "varchar":
                        campos += tablaBD.getCampo() + "='" + datos.get(tablaBD.getCampo()).toString() + "' " + separacion;
                        break;
                }

                ultimoRegistroPk--;
            }
            ultimoRegistro--;
        }
        preparacion = "Update " + tabla + " set " + valores + " where " + campos + "";
        System.out.println("preparacion : " + preparacion);

        return preparacion;
    }

    public String lineaDelete(Map<Object, Object> datos, String tabla, int base) {

        List<TablaBD> camposBD = new ArrayList();

        camposBD = operacion.camposTablaDelete(tabla, base);

        String coma = " and ";
        Object valores = "";
        String Fecha;
        int ultimoRegistro = 0;

        ultimoRegistro = camposBD.size();
        for (TablaBD key : camposBD) {
            if (ultimoRegistro <= 1) {
                coma = "";
            }
            System.out.println("LLAVE : " + key);
            switch (key.getTipoCampo()) {
                case "timestamp":
                    Fecha = datos.get(key.getCampo()).toString().replace("@fecha", "");
                    valores += key.getCampo() + "=" + datos.get(key.getCampo()).toString() + coma;
                    break;
                case "varchar":
                    valores += key.getCampo() + "='" + datos.get(key.getCampo()).toString() + "'" + coma;
                    break;
                case "int4":
                    valores += key.getCampo() + "=" + new BigDecimal(datos.get(key.getCampo()).toString()) + "" + coma;
                    break;
                case "serial":
                    valores += key.getCampo() + "=" + new BigDecimal(datos.get(key.getCampo()).toString()) + "" + coma;
                    break;
                default:
                    valores += key.getCampo() + "='" + datos.get(key.getCampo()).toString() + "'" + coma;
                    break;
            }
            ultimoRegistro--;
        }

//        Set set = Contentido.entrySet();
//        Iterator iterator = set.iterator();
//
//        while (iterator.hasNext()) {
//            Map.Entry mentry = (Map.Entry) iterator.next();
//            for (String key : keys) {
//                if (key.equalsIgnoreCase(mentry.getKey().toString())) {
//                    System.out.println(" Campo valido" + mentry.getValue().toString());
//                    if (mentry.getValue() instanceof String) {
//                        valores += mentry.getKey().toString() + "='" + mentry.getValue().toString() + "'" + coma;
//                    } else {
//                        valores += mentry.getKey().toString() + "=" + mentry.getValue().toString() + coma;
//                    }
//                }
//            }
//        }
//        if (valores.toString().length() > 0) {
//            valores = valores.toString().substring(0, valores.toString().length() - 4);
//        }
        //operacion.llavesPrimary(tabla);
        System.out.println("return : " + "delete from " + tabla + " where " + valores);
        return ("delete from " + tabla + " where " + valores);
    }

    public String lineaImpacto(String datos, String tabla) {
        System.out.println("lineaImpacto");
        String preparacion = "";
        preparacion = "select " + tabla + "('" + datos + "')";
        System.out.println("preparacion : " + preparacion);
        return preparacion;
    }

    public static void recorrerArbol() {
        TreeMap<Integer, TreeMap<Integer, TreeMap<String, List>>> map_esquema2 = new TreeMap<>();
        map_esquema2 = ArbolDatos.map_esquema;
        Set set = map_esquema2.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry mentry = (Map.Entry) iterator.next();
//            key = (int) mentry.getKey();

        }
    }

}
