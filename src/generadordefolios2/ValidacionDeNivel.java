/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generadordefolios2;

import com.toedter.calendar.JDateChooser;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author CAPUFE
 */
public class ValidacionDeNivel {
   
    private JComboBox<String> CmbDepartamentos;
    private JDateChooser jCalendario;
    private JButton BtnMostarVentana2;
    private JButton btnGenFolio;
    private JTextField EPorTextF;
    private JButton agregarAnioBtn;
    private ConexionBD conexion;
    private JTextArea TxtAreaAsunto;
    private JTextField dirigidoTxtF;
    
    
    

    public ValidacionDeNivel(JComboBox<String> CmbDepartamentos, JDateChooser jCalendario, JButton BtnMostarVentana2, JTextField EPorTextF, JButton agregarAnioBtn, JButton btnGenFolio, JTextArea TxtAreaAsunto, JTextField dirigidoTxtF) { 
        this.CmbDepartamentos = CmbDepartamentos;
        this.jCalendario = jCalendario;
        this.BtnMostarVentana2 = BtnMostarVentana2;
        this.EPorTextF = EPorTextF;
        this.agregarAnioBtn = agregarAnioBtn;
        this.btnGenFolio = btnGenFolio;
        this.TxtAreaAsunto = TxtAreaAsunto;
        this.dirigidoTxtF = dirigidoTxtF;
        
        //Se crea una instancia de ConexinBD
        conexion = new ConexionBD();
    }
    
    public void validar(){
        String nombreUsuario = System.getProperty("user.name");
        int nivel = obtenerNivelUsuario(nombreUsuario);
        
        if ( nivel == 3 ){
            CmbDepartamentos.setEnabled(false);
            jCalendario.setEnabled(true);
            BtnMostarVentana2.setEnabled(true);
            EPorTextF.setEnabled(false);
            agregarAnioBtn.setEnabled(true);
            
        }else if( nivel == 2){
            CmbDepartamentos.setEnabled(false);
            jCalendario.setEnabled(true);
            BtnMostarVentana2.setEnabled(true);
            EPorTextF.setEnabled(false);
            agregarAnioBtn.setEnabled(true);
        }else if( nivel == 1){
            CmbDepartamentos.setEnabled(false);
            jCalendario.setEnabled(false);
            BtnMostarVentana2.setEnabled(false);
            EPorTextF.setEnabled(false);
            agregarAnioBtn.setEnabled(false);
        }else{
            CmbDepartamentos.setEnabled(false);
            jCalendario.setEnabled(false);
            BtnMostarVentana2.setEnabled(false);
            EPorTextF.setEnabled(false);
            agregarAnioBtn.setEnabled(false);
            btnGenFolio.setEnabled(false);
            TxtAreaAsunto.setEnabled(false);
            dirigidoTxtF.setEnabled(false);
        }
            
    }
    
    private int obtenerNivelUsuario(String nombreUsuario){
        //Connection conexion = null;
        
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
