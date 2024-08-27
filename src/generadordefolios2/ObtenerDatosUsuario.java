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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/**
 *
 * @author CAPUFE
 */
public class ObtenerDatosUsuario {

    private String puestoUsuario;
    String nombreUsuario;
    private ConexionBD conexion;
    String departamento;
    
    public ObtenerDatosUsuario() throws SQLException{  
        //Se crea una instancia de ConexinBD
        conexion = new ConexionBD();
        
        conexion.conectar();
    }
   
    public void obtenerDatosUsuario(String nombreUsuario, JLabel nombreLabel, JLabel apellidoLabel, JComboBox CmbDepartamentos, JTextField EPorTextF) throws SQLException{
        //Crear la consulta SQL
        String sql = "SELECT nombre, apellido, departamento, puesto FROM empleados WHERE IDempleado = ? ";

        //Crear el objeto PreparedStatement y establecer el parámetro de nombre de usuario
        try(PreparedStatement stmt = conexion.getConnection().prepareStatement(sql)){
            stmt.setString(1, nombreUsuario); 

            //Ejecutar la consulta y obtener el resultado        
            try(ResultSet resultado = stmt.executeQuery()){
           
                if(resultado.next()){
                
                //Si se encontro un registo, obtener los valores de nombre, apellido, puesto y departamento
                String nombre = resultado.getString("nombre");
                String apellido = resultado.getString("apellido");
                String puesto = resultado.getString("puesto");
                String departamento = resultado.getString("departamento");
                
                //Se asigna el valor del puesto a la variable puestoUsuario
                puestoUsuario = puesto;

            
                //Asignar los valores a los campos correspondientes del formulario
                nombreLabel.setText(nombre);
                apellidoLabel.setText(apellido);
                CmbDepartamentos.setSelectedItem(departamento);
                EPorTextF.setText(nombre +" "+ apellido);
                
                
                }else{
                    JOptionPane.showMessageDialog(null,"Usuario no encontrado");
                }  
            }    
        
        }   
    }
    
    public void obtenerDatosUsuarioVentanaConsulta(String nombreUsuario, JLabel Lblnombre, JComboBox<String> cmbDepartamento) throws SQLException{
        //Crear la consulta SQL
        String sql = "SELECT nombre, apellido, departamento FROM empleados WHERE IDempleado = ? ";

        //Crear el objeto PreparedStatement y establecer el parámetro de nombre de usuario
        try(PreparedStatement stmt = conexion.getConnection().prepareStatement(sql)){
            stmt.setString(1, nombreUsuario); 

            //Ejecutar la consulta y obtener el resultado        
            try(ResultSet resultado = stmt.executeQuery()){
           
                if(resultado.next()){
                
                //Si se encontro un registo, obtener los valores de nombre, apellido, puesto y departamento
                String nombre = resultado.getString("nombre");
                String apellido = resultado.getString("apellido");
                String departamento = resultado.getString("departamento");
                
                
                //Asignar los valores al elemeto
                Lblnombre.setText(nombre +" "+ apellido);
                cmbDepartamento.setSelectedItem(departamento);

                }else{
                    JOptionPane.showMessageDialog(null,"Usuario no encontrado");
                }  
            }    
        
        }   
    }
    
    public String getPuestoUsuario(){
        return puestoUsuario;
    }

    public void cerrarConexion() throws SQLException{
        //Cerrar la conexion con la base de datos
        conexion.desconectar();
    }
}
