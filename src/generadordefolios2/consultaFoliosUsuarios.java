/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordefolios2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author CAPUFE
 */
public class consultaFoliosUsuarios {
    
    private ConexionBD conexion;
    String nombreUsuario = System.getProperty("user.name");

    public void consultaFoliosEnLista(JList<String> foliosLista) throws SQLException{    
        
        //Se crea una instancia de ConexinBD
        conexion = new ConexionBD();
       
        conexion.conectar();
        
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        
        //Crear la conuslta SQL para obtener los datos
        String sql = "SELECT folio, fecha FROM consultasfolio WHERE IDempleado = '" + nombreUsuario + "'";
        
        //Crear el objeto PreparedStatement y ejecutar la consulta
        try(PreparedStatement stmt = conexion.getConnection().prepareStatement( sql )){
            try( ResultSet resultado = stmt.executeQuery()){
            
                while( resultado.next() ){
                    String folio = resultado.getString("folio");
                    String fecha = resultado.getString("fecha");
                    modeloLista.addElement(folio + "     " + fecha);                
                }
            }
        }catch(SQLException e){
            //Manejo de la excepcion
            System.out.println("Error al consulta folios: " + e.getMessage());
        }
        
        //Agregar un mensaje si no hay datos que consultar
        if( modeloLista.isEmpty() ){
                modeloLista.addElement("No hay oficios para mostrar.");
        }
        
        //Asignar el modelo de la lista con los folios cargados
        foliosLista.setModel(modeloLista);
    }
}
