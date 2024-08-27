/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordefolios2;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;


/**
 *
 * @author CAPUFE
 */
public class ConexionBD {

    private static final String url = "jdbc:mysql://PURMZTTJ9SW1CPF.capufe.gob.mx:3306/generadorfolios";
    private static final String usuario = "admin";
    private static final String contraseña = "S0porteMzt";

    private Connection conexion;

    public ConexionBD() {
        conexion = null;
    }

    public void conectar() {
        try {
            conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Desconexión exitosa de la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al desconectar de la base de datos: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conexion;
    }
}