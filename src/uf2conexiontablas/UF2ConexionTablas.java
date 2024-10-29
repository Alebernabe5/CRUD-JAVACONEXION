package uf2conexiontablas;

//Importacion de librerias
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class UF2ConexionTablas {

    //Declaraciones de constantes
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; // Ajusta la versión del controlador según sea necesario
    private static final String USER = "root"; //Sirve para acceder a la BBDD
    private static final String PASSWORD = ""; //Sirve para acceder a la BBDD
    private static final String DB = "animales"; // nombre de la BD
    private static final String URL = "jdbc:mysql://localhost:3306/" + DB; //URL de conexionJDBC a la BBDD

    //Cargar controlador JDBC para establecer conexion BBDD
    private static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(DRIVER);
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Scanner sc = new Scanner(System.in);

        //Variable de control para el ciclo del menu
        boolean salir = false;

        //Menu de inicio
        while (!salir) {
            System.out.println();
            System.out.println("***************************************");
            System.out.println("***************************************");
            System.out.println("*************TABLA ANIMALES************");
            System.out.println("***************************************");
            System.out.println("***************************************");
            System.out.println("\n");
            System.out.println("1.Crear tabla");
            System.out.println("2.Crear animal");
            System.out.println("3.Consultar animales");
            System.out.println("4.Consultar un animal");
            System.out.println("5.Modificar un animal");
            System.out.println("6.Eliminar un animal");
            System.out.println("7.Eliminar tabla");
            System.out.println("8.Salir");
            System.out.println("Elige una opcion:");
            int opcion = Integer.parseInt(sc.nextLine());

            //Ejecucción de las opciones indicadas
            switch (opcion) {
                case 1:
                    crearTabla(sc);
                    break;
                case 2:
                    crearAnimal(sc);
                    break;
                case 3:
                    consultartabla(sc);
                    break;
                case 4:
                    consultarAnimal(sc);
                    break;
                case 5:
                    modificarAnimal(sc);
                    break;
                case 6:
                    eliminarAnimal(sc);
                    break;
                case 7:
                    eliminarTabla(sc);
                    break;
                case 8:
                    salir = true;
                    System.out.println("Has salido del programa");
                    break;
                default:
                    System.out.println("Opción no valida, elige un valor correctamente.");
            }
        }

        //Cierre de objeto scanner
        sc.close();
    }

    // 1. Crear tabla
    public static void crearTabla(Scanner scanner) throws SQLException, ClassNotFoundException {
        System.out.println("Introduce el nombre de la tabla que deseas crear:");
        String nombreTabla = scanner.nextLine(); //Almacenar variable tabla

        //Declaraciond e variable para crear instruccion
        String sql = "CREATE TABLE IF NOT EXISTS " + nombreTabla + " ("
                + "Nombre VARCHAR(50) PRIMARY KEY, "
                + "Tipo VARCHAR(25), "
                + "Habitat VARCHAR(50), "
                + "Desplazamiento VARCHAR(25), "
                + "Alimentacion VARCHAR(50), "
                + "NPatas INT)";

        //Llamar al metodo coenction para crear conexion con BBDD
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) { //Stm permite crear consulta SQL
            stmt.executeUpdate(sql); //Ejecuta la consulta SQL
            System.out.println("Tabla " + nombreTabla + " creada o ya existe.");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    // 2. Crear animal
public static void crearAnimal(Scanner sc) throws SQLException, ClassNotFoundException {
    System.out.println("Nombre de la tabla donde deseas insertar el animal:");
    String nombreTabla = sc.nextLine();

    System.out.println("Nombre del animal:");
    String nombre = sc.nextLine();
    System.out.println("Tipo de animal:");
    String tipo = sc.nextLine();
    System.out.println("Habitat del animal:");
    String habitat = sc.nextLine();
    System.out.println("Como se desplaza este animal:");
    String desplazamiento = sc.nextLine();
    System.out.println("Alimentacion de este animal:");
    String alimentacion = sc.nextLine();
    System.out.println("Numero de patas de este animal:");
    int NPatas = sc.nextInt();
    sc.nextLine(); // Importante hacer el salto de línea después de un número entero

    // Construir la consulta SQL dinámicamente con el nombre de la tabla
    String sql = "INSERT INTO " + nombreTabla + " (Nombre, Tipo, Habitat, Desplazamiento, Alimentacion, NPatas) VALUES (?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
        // Establecer posiciones SQL para las variables
        pstm.setString(1, nombre);
        pstm.setString(2, tipo);
        pstm.setString(3, habitat);
        pstm.setString(4, desplazamiento);
        pstm.setString(5, alimentacion);
        pstm.setInt(6, NPatas);
        pstm.executeUpdate();
        System.out.println("Animal creado correctamente en la tabla " + nombreTabla + "!!.");
    }
}


    // 3. Leer todos los animales
    public static void consultartabla(Scanner sc) throws SQLException, ClassNotFoundException {
        //Consulta para leer los datos de la tabla
        System.out.println("¿Cual es la tabla que deseas leer?");
        String nombreTabla = sc.nextLine(); // Lee el nombre de la tabla que el usuario introduce

        // Consulta para leer los datos de la tabla
        String sql = "SELECT * FROM " + nombreTabla; // Se usa el nombre de la tabla proporcionado por el usuario

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) { //Buble para recorrer los registros
                System.out.println("Nombre: " + rs.getString("Nombre")
                        + ", Tipo: " + rs.getString("Tipo")
                        + ", Habitat: " + rs.getString("Habitat")
                        + ", Desplazamiento: " + rs.getString("Desplazamiento")
                        + ", Alimentacion: " + rs.getString("Alimentacion")
                        + ", Numero de patas: " + rs.getInt("NPatas"));
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la tabla: " + e.getMessage());
        }
    }

    // 4. Leer un animal
    public static void consultarAnimal(Scanner sc) throws SQLException, ClassNotFoundException {
        System.out.println("¿De que tabla deseas leer el animal?");
        String nombreTabla = sc.nextLine();
        System.out.println("Ingrese el nombre de un animal:");
        String nombre = sc.nextLine();

        String sql = "SELECT * FROM " + nombreTabla + " WHERE Nombre = ?"; //Define consulta de la tabla que se elija
        // Realiza la conexión y prepara la consulta
        try (Connection conn = getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, nombre); // Asigna el nombre del animal al parámetro en la consulta
            try (ResultSet rs = pstm.executeQuery()) {//Ejecuta la consulta
                if (rs.next()) {
                    System.out.println("Nombre: " + rs.getString("Nombre")
                            + ", Tipo: " + rs.getString("Tipo")
                            + ", Habitat: " + rs.getString("Habitat")
                            + ", Desplazamiento: " + rs.getString("Desplazamiento")
                            + ", Alimentacion: " + rs.getString("Alimentacion")
                            + ", Numero de patas: " + rs.getInt("NPatas"));
                } else {
                    System.out.println("Animal NO encontrado.");
                }
            }
        }

    }
    // 5. Modificar un animal
    public static void modificarAnimal(Scanner sc) throws SQLException, ClassNotFoundException {
        // Pregunta al usuario por el nombre de la tabla
        System.out.print("¿De que tabla deseas modificar el animal? ");
        String nombreTabla = sc.nextLine(); // Lee el nombre de la tabla 

        // Pregunta al usuario por el nombre del animal a modificar
        System.out.print("Ingrese el nombre del animal a modificar: ");
        String nombre = sc.nextLine();

        // Solicita los nuevos valores para los campos del animal
        System.out.print("Nuevo tipo: ");
        String tipo = sc.nextLine();
        System.out.print("Nuevo habitat: ");
        String habitat = sc.nextLine();
        System.out.print("Nuevo desplazamiento: ");
        String desplazamiento = sc.nextLine();
        System.out.print("Nueva alimentacion: ");
        String alimentacion = sc.nextLine();
        System.out.print("Nuevo numero de patas: ");
        int NPatas = sc.nextInt();
        sc.nextLine(); // Limpia el buffer de entrada

        // Construye la consulta de actualización SQL usando el nombre de la tabla
        String sql = "UPDATE " + nombreTabla + " SET Tipo = ?, Habitat = ?, Desplazamiento = ?, Alimentacion = ?, NPatas = ? WHERE Nombre = ?";

        // Realiza la conexión y prepara la consulta
        try (Connection conn = getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
            // Asigna los nuevos valores a los parámetros de la consulta
            pstm.setString(1, tipo);
            pstm.setString(2, habitat);
            pstm.setString(3, desplazamiento);
            pstm.setString(4, alimentacion);
            pstm.setInt(5, NPatas);
            pstm.setString(6, nombre);

            // Ejecuta la actualización y verifica si se actualizó alguna fila
            int rowsUpdated = pstm.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("ANIMAL ACTUALIZADO CORRECTAMENTE.");
            } else {
                System.out.println("Animal NO encontrado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el animal: " + e.getMessage());
        }
    }

    // 6. Eliminar un animal
public static void eliminarAnimal(Scanner sc) throws SQLException, ClassNotFoundException {
    // Pregunta al usuario por el nombre de la tabla
    System.out.print("¿De qué tabla deseas eliminar el animal? ");
    String nombreTabla = sc.nextLine(); // Lee el nombre de la tabla proporcionado por el usuario

    // Pregunta al usuario por el nombre del animal a eliminar
    System.out.print("Ingrese el nombre del animal a eliminar: ");
    String nombre = sc.nextLine();

    // Construye la consulta de eliminación SQL usando el nombre de la tabla
    String sql = "DELETE FROM " + nombreTabla + " WHERE Nombre = ?";

    // Realiza la conexión y prepara la consulta
    try (Connection conn = getConnection(); PreparedStatement pstm = conn.prepareStatement(sql)) {
        // Asigna el valor al parámetro de la consulta
        pstm.setString(1, nombre);

        // Ejecuta la eliminación y verifica si se eliminó alguna fila
        int rowsDeleted = pstm.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Animal eliminado.");
        } else {
            System.out.println("Animal NO encontrado.");
        }
    } catch (SQLException e) {
        System.out.println("Error al eliminar el animal: " + e.getMessage());
    }
}


    // 7. Eliminar una tabla
    public static void eliminarTabla(Scanner sc) throws SQLException, ClassNotFoundException {
        System.out.println("Introduce el nombre de la tabla que deseas eliminar:");
        String nombreTabla = sc.nextLine(); // Leer el nombre de la tabla desde la entrada del usuario

        String sql = "DROP TABLE IF EXISTS " + nombreTabla; // Construir la consulta SQL

        try (Connection conn = getConnection(); Statement stm = conn.createStatement()) {
            stm.executeUpdate(sql);
            System.out.println("Tabla " + nombreTabla + " eliminada.");
        } catch (SQLException e) {
            System.out.println("Error al eliminar la tabla: " + e.getMessage());
        }
    }
}
