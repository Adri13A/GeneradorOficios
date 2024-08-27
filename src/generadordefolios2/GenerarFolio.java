/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordefolios2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author CAPUFE
 */
public class GenerarFolio {
    
  private ConexionBD conexion;
  private String departamento;
  private String IdDepartamento;
  String nombreUsuario; 
  private int anio;
  private String fechaFormateada;
  String IDempleado = System.getProperty("user.name");


    public GenerarFolio(String departamento, int anio, String fechaFormateada) throws SQLException{
        //Se crea una instancia de ConexinBD
        conexion = new ConexionBD();        
        conexion.conectar();
        this.departamento = departamento;
        this.anio = anio;
        this.fechaFormateada = fechaFormateada;
    }

    
    public String generarFolio(String nombreUsuario) throws SQLException{
  
        //Verificar si IDempleado es nulo o vacio
        if( IDempleado == null || IDempleado.isEmpty()){
            
            //Mostrar un mensaje de error o realizar una accion adecuada
            JOptionPane.showMessageDialog(null, "Error al obtener el ID empleado");
            return null; //Salir del método           
        }
        
        /*************************************************/
        //Crear la consulta SQL
        String sql = "SELECT IDdepartamento FROM empleados WHERE IDempleado = ? ";

        //Crear el objeto PreparedStatement y establecer el parámetro de nombre de usuario
        try(PreparedStatement stmt = conexion.getConnection().prepareStatement(sql)){
            stmt.setString(1, nombreUsuario); 

            //Ejecutar la consulta y obtener el resultado        
            try(ResultSet resultado = stmt.executeQuery()){
           
                if(resultado.next()){
                
                //Si se encontro un registo, obtener los valores de IdDepartamento
                IdDepartamento = resultado.getString("IDdepartamento");
                
                }else{
                    JOptionPane.showMessageDialog(null,"Usuario no encontrado");
                }  
            }    
        } 
        /*********************************************/
        
        String tabla;
       
        switch (departamento) {
            
            case "Gerencia":
                tabla = "datosgerencia";
                break;            
            case "Subgerencia de Administración":
                tabla = "datosadmin";
                break;
            case "Subgerencia Técnica":
                tabla = "datostec";
                break;
            case "Subgerencia de Operación":
                tabla = "datosopera";
                break;
            case "Subgerencia Juridica":
                tabla = "datosjuri";
                break;
            default:
                throw new IllegalArgumentException("Departamento no válido");
        }
        
        //Crear la consulta SQL para obtener el valor del contador   
        sql = "SELECT contador FROM " + tabla + " WHERE anio = ?";   //Puede ser el error en el año, que por eso sale el error de "IDempleado doesn´t have a defautl value"?

        //Crear el objeto PreparedStatement y establecer el parámetro del año en curso
        try(PreparedStatement stmt = conexion.getConnection().prepareStatement(sql)){
            stmt.setInt(1, anio); 

            //Ejecutar la consulta y obtener el resultado        
            try(ResultSet resultado = stmt.executeQuery()){
                if(resultado.next()){
                    
                    //Si se encontró un registro, obtener el valor del contador
                    int contador = resultado.getInt("contador");
                    
                    //Incrementar el contador y actualizar el valor en la tabla
                    contador++;
                    String updateSql = "UPDATE " + tabla + " SET contador = ? WHERE anio = ?";
                    
                    try(PreparedStatement updateStmt = conexion.getConnection().prepareStatement(updateSql)){
                        updateStmt.setInt(1, contador);
                        updateStmt.setInt(2, anio);
                        updateStmt.executeUpdate();
                    }
                    
                    //Empieza - Generación de los folios para el AÑO EN CURSO
                        //Generar el folio
                        String folio;
                        
                        if( departamento.equals("Gerencia")){
                            folio = "URMAZ/" + String.format("%03d", contador) + "/" + anio;
                            
                        }else{
                            // Generar el folio
                            folio = "URMAZ/" + IdDepartamento + "/" + String.format("%03d", contador) + "/" + anio;
                        }
                        //Insertar el folio y la fecha en la tabla consultasfolio
                        String insertarSql = "INSERT INTO consultasfolio (IDempleado, folio, fecha) VALUES (?, ?, ?)";
                            try(PreparedStatement insertStmt = conexion.getConnection().prepareStatement(insertarSql)){
                                insertStmt.setString(1, IDempleado);
                                insertStmt.setString(2, folio);
                                insertStmt.setString(3, fechaFormateada);
                                insertStmt.executeUpdate(); 
                            }
                            return folio;
                    //Termina - Generación de los folios para el año en curso
                
                }else{
                    
                    //Si no se encontró un registro para el año en curso, insertar uno nuevo con contador igual a 1
                    String insertSql = "INSERT INTO " + tabla + " (anio, contador) VALUES (?, 1)";
                    try(PreparedStatement insertStmt = conexion.getConnection().prepareStatement(insertSql)){
                        insertStmt.setInt(1, anio);
                        insertStmt.executeUpdate(); 
                    }
                    
                    //Empieza - Generación de los folios para un NUEVO AÑO
                        //Generar el folio
                        String folio;
                        
                        if( departamento.equals("Gerencia")){
                            folio = "URMAZ/" + "001/" + anio;
                        }else{
                            // Generar el folio
                            folio = "URMAZ/" + IdDepartamento + "/" + "001/" + anio;
                        }
                        //Insertar el folio y la fecha en la tabla consultasfolio
                        String insertarSql = "INSERT INTO consultasfolio (IDempleado, folio, fecha) VALUES (?, ?, ?)";
                            try(PreparedStatement insertStmt = conexion.getConnection().prepareStatement(insertarSql)){
                                insertStmt.setString(1, IDempleado);
                                insertStmt.setString(2, folio);
                                insertStmt.setString(3, fechaFormateada);
                                insertStmt.executeUpdate(); 
                            }
                        return folio;
                        //Termina - Generación de los folios para un NUEVO AÑO
                    
                }                 
            }    
        }   
        
        /*****************************************************/

    }
    
    public void cerrarConexion() throws SQLException{
        //Cerrar la conexión con la base de datos
        conexion.desconectar();
    }
}
