/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package database;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import database.Class.*;
import database.objects.*;

/**
 *
 * @author onairo
 */

public class DataBase {
    private final String user = "SQL_Admin";
    private final String password = "zjKgjWK/xRyQcJyu+bU4";
    private final String url = "jdbc:mysql://localhost:3306/Tienda_Online";
    
    protected final Connection conexion;
    
    public DataBase() throws SQLException {
        conexion = DriverManager.getConnection(url, user, password);
    }
    
    public Connection getConnection() {
        return this.conexion;
    }
    
    public static void main(String[] args) {
        try {
            DataBase db = new DataBase();
            Person person = new Person(db.getConnection());
            
            //Agregar información de usuario
            person.insertInfo("Juan", "García", "123456789", "1234567890", "Calle 123", "Bogotá");
            person.insertInfo("María", "López", "987654321", "0987654321", "Avenida Principal", "Medellín");
            
            //Obtener datos
            PersonData data = person.getInfo("123456789");
            System.out.println(data.getPhone());
            
            //Actualizar información
            Json[] jsonArray = {
                new Json("nombre", "Juan"),
                new Json("apellido", "García"),
                new Json("cedula", "123456789"),
                new Json("telefono", "7874567890"),
                new Json("direccion", "Calle 124"),
                new Json("ciudad", "Bogotá"),
            };
            person.updateInfo("123456789", jsonArray);
            
            //Actualizar puntos fidelida
            person.setPoints("987654321", 200, "add");
            System.out.println(person.getPoints("987654321"));
            
            //Restar puntos
            person.setPoints("987654321", 100, "subtract");
            System.out.println(person.getPoints("987654321"));

            //Crear cuenta (web)
            person.createAccount("example@email.com", "3c6ac7d3473a1ada16da63e4430d08e385ff7c6f80432b12fc9931cd9ec0348d", "JT7nTqUuTI", "123456789");
            
            //Actualizar
            Json[] newAccountData = {
                new Json("hash", "8a005d04e732a3a987b3fc73b81bda7b00d00598f545695bbcbb24f1767eb01a"),
                new Json("salt", "Z2mcG2Onuq")
            };
            person.updateAccount("example@email.com", newAccountData);
            
            //Obtener datos
            PersonAccount accountdata = person.getAccount("example@email.com");
            System.out.println(accountdata.getPersonID());
            
            //Factura
            Invoice invoice = new Invoice(db.getConnection());
            
            //Métodos de pago
            invoice.newMethod("Efectivo"); //Agregar un nuevo método
            invoice.newMethod("Tarjeta de crédito"); //Agregar un nuevo método
            
            PaymentMethod[] methods = invoice.getAllMethods(); //Obtener todos lo métodos de pago
            for (PaymentMethod item : methods) {
                System.out.println("ID método: " + item.getID());
                System.out.println("Nombre método: " + item.getMethod());
            }
            
            invoice.insertInfo("987654321", "2024-05-31", "16:27:10", 20000, 5000, 15000, 1); //Nueva factura
            
            String uuid = invoice.uuidCode();  //Actualizar información de la factura
            Json[] updateInvoice = {
                new Json("fecha", "2024-06-03"),
                new Json("hora", "15:22:10")
            }; 
            invoice.updateInfo(uuid, updateInvoice);
            
            InvoiceData invoiceData =  invoice.getInfo(uuid);
            System.out.println("Invoice Data > Person ID: " + invoiceData.getPersonID() + "Invoice ID: " + invoiceData.getInvoiceID());
            
            invoice.deleteInfo(uuid); //Eliminar factura por su id
            
            //Eliminar métodos de pago
            invoice.deleteMethod(1); 
            invoice.deleteMethod(2); 
            
            //Eliminar datos Persona
            person.deleteInfo("123456789");
            person.deleteInfo("987654321");
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Person.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
