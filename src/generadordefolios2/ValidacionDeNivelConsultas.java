/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordefolios2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;

/**
 *
 * @author CAPUFE
 */
public class ValidacionDeNivelConsultas {
    
     private JComboBox<String> cmbDepartamento;
     private ConexionBD conexion;
     
      public ValidacionDeNivelConsultas( JComboBox<String> cmbDepartamento ) { 
        this.cmbDepartamento = cmbDepartamento;
        
        //Se crea una instancia de ConexinBD
        conexion = new ConexionBD();
      }
      
      public void validar(){
        String nombreUsuario = System.getProperty("user.name");
        int nivel = obtenerNivelUsuario(nombreUsuario);
        
        if ( nivel == 3 ){
            cmbDepartamento.setEnabled(true);    
        }else if( nivel == 2){
            cmbDepartamento.setEnabled(false); 
        }
    }
    
    private int obtenerNivelUsuario(String nombreUsuario){
        PreparedStatement ps = null;
        ResultSet rs = null;
        int nivel = 0;
        
        try{
            conexion.conectar();
            ps = conexion.getConnection().prepareStatement("SELECT nivel FROM empleados WHERE IDempleado = ? ");
            ps.setString(1, nombreUsuario);
            
            rs = ps.executeQuery();
            if (rs.next()){
                nivel = rs.getInt("nivel");
            }
        }catch(SQLException e){
            e.printStackTrace();
        } finally{
            try{
                if ( rs != null ){
                    rs.close();
                }
                if (ps != null){
                    ps.close();
                }
                if ( conexion != null ){
                    conexion.desconectar();
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
        return nivel;
    }
    
}
