package Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:mysql://127.0.0.1:3306/venta_autos";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "4766";

    //constructor
    private ConexionDB() {
    }

    //metodos
    public static Connection getConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }
}
