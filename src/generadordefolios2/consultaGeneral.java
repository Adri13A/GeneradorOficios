package generadordefolios2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class consultaGeneral {

    private ConexionBD conexion;
    private String departamento;
    private String nombreUsuario = System.getProperty("user.name");
    private JTable tablaConsulta;
    private DefaultTableModel modeloTabla;
    private ResultSetMetaData metadata;
    private TableModelListener tablaListener; // Variable para almacenar el listener de la tabla

    public consultaGeneral(String departamentoSeleccionado) {
        try {
            conexionABaseDeDatos();
            departamento = departamentoSeleccionado;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    public consultaGeneral() {
        try {
            conexionABaseDeDatos();
            PreparedStatement statement = conexion.getConnection().prepareStatement("SELECT departamento FROM empleados WHERE IDempleado = ?");
            statement.setString(1, nombreUsuario);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                departamento = resultado.getString("departamento");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    private void conexionABaseDeDatos() throws SQLException {
        conexion = new ConexionBD();
        conexion.conectar();
    }

    public void mostrarDatos(JTable tablaConsulta) {
        try {
            Statement stmt = conexion.getConnection().createStatement();
            String tabla = null;

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

            String sql = "SELECT folio, nombre, apellido, departamento, dirigidoA, elaboradoPor, asunto, fecha FROM " + tabla;

            ResultSet resultado = stmt.executeQuery(sql);
            metadata = resultado.getMetaData();

            int numColumnas = metadata.getColumnCount();

            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    // Permite la edición solo en las columnas 4 y 6 (contando desde 0)
                    return column == 4 || column == 5 || column == 6 ;
                    //return true;
                }
            };

            for (int i = 1; i <= numColumnas; i++) {
                model.addColumn(metadata.getColumnName(i));
            }

            while (resultado.next()) {
                Object[] rowData = new Object[numColumnas];
                for (int i = 1; i <= numColumnas; i++) {
                    rowData[i - 1] = resultado.getObject(i);
                }
                model.addRow(rowData);
            }

            tablaConsulta.setModel(model);
            this.tablaConsulta = tablaConsulta;
            this.modeloTabla = model;

            tablaListener = new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE) {
                        int fila = e.getFirstRow();
                        int columna = e.getColumn();
                        Object nuevoValor = tablaConsulta.getValueAt(fila, columna);

                        try {
                            String nombreColumna = metadata.getColumnName(columna + 1);
                            int columnaSeleccionada = tablaConsulta.getColumn(nombreColumna).getModelIndex();
                            actualizarDato(fila, columnaSeleccionada, nuevoValor.toString());
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(null, "Error al actualizar el dato: " + ex.getMessage());
                        }
                    }
                }
            };

            tablaConsulta.getModel().addTableModelListener(tablaListener);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al consultar: " + e.getMessage());
        }
    }

    public void actualizarDato(int filaSeleccionada, int columnaSeleccionada, String nuevoValor) {
    try {
        if (tablaConsulta != null && modeloTabla != null) {
            String tabla = null;

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

            String nombreColumna = modeloTabla.getColumnName(columnaSeleccionada);
            String folio = modeloTabla.getValueAt(filaSeleccionada, 0).toString();

            int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de actualizar el dato?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion == JOptionPane.YES_OPTION) {
                String sql = "UPDATE " + tabla + " SET " + nombreColumna + " = ? WHERE folio = ?";
                PreparedStatement ps = conexion.getConnection().prepareStatement(sql);
                ps.setString(1, nuevoValor);
                ps.setString(2, folio);

                int filasActualizadas = ps.executeUpdate();

                if (filasActualizadas > 0) {
                    tablaConsulta.getModel().removeTableModelListener(tablaListener); // Eliminar el listener temporalmente
                    tablaConsulta.getModel().setValueAt(nuevoValor, filaSeleccionada, columnaSeleccionada);
                    tablaConsulta.getModel().addTableModelListener(tablaListener); // Agregar nuevamente el listener
                    JOptionPane.showMessageDialog(null, "Dato actualizado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Error al actualizar el dato");
                }

                ps.close();
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar el dato: " + e.getMessage());
    }
}


    public void guardarCambios() {
        try {
            conexion.getConnection().commit();
            JOptionPane.showMessageDialog(null, "Cambios guardados correctamente");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al guardar los cambios: " + ex.getMessage());
        }
    }

    public void deshacerCambios() {
        try {
            conexion.getConnection().rollback();
            JOptionPane.showMessageDialog(null, "Cambios deshechos correctamente");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al deshacer los cambios: " + ex.getMessage());
        }
    }
}
