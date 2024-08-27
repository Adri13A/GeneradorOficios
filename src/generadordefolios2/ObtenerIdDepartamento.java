/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordefolios2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author CAPUFE
 */
public class ObtenerIdDepartamento {
    
    private ConexionBD conexion;
    
    public int obtenerIdDepartamento( String nombreUsuario ) throws SQLException {
    int idDepartamento = 0;
    
    // Crear la consulta SQL para obtener el departamento del usuario
    String sql = "SELECT departamento FROM empleados WHERE IDempleado = ?";

    // Crear el objeto PreparedStatement y establecer el parámetro de nombre de usuario
    try(PreparedStatement stmt = conexion.getConnection().prepareStatement( sql )){
        stmt.setString( 1, nombreUsuario ); 

        // Ejecutar la consulta y obtener el resultado        
        try( ResultSet resultado = stmt.executeQuery() ){
            if(resultado.next()){
                
                // Si se encontró un registro, obtener el nombre del departamento
                String departamento = resultado.getString( "departamento" );
                
                // Crear la consulta SQL para obtener el ID del departamento
                String sqlId = "SELECT IDdepartamento FROM empleados WHERE departamento = ?";
                
                // Crear el objeto PreparedStatement y establecer el parámetro del nombre del departamento
                try( PreparedStatement stmtId = conexion.getConnection().prepareStatement( sqlId ) ){
                    stmtId.setString(1, departamento);
                    
                    // Ejecutar la consulta y obtener el resultado
                    try(ResultSet resultadoId = stmtId.executeQuery()){
                        if( resultadoId.next() ){
                            // Si se encontró un registro, obtener el ID del departamento
                            idDepartamento = resultadoId.getInt( "IDdepartamento" );
                        }
                    }
                }
            }
        }
    }
    
    return idDepartamento;
    
    }
}
