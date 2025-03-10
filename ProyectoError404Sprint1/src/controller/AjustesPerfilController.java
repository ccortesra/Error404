/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Modelo.Alerta;
import Modelo.Conexion;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author tique
 */
public class AjustesPerfilController implements Initializable {

    private Connection bd;
    private Alerta alarma = new Alerta();
    PostController info = new PostController();
    @FXML
    private Button btnAjustesPerfil;
    @FXML
    private Label lblBioPerfil;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextArea txtBiografia;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField txtGenero;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        bd = Conexion.getBd();
    }

    @FXML
    private void guardarPerfil(MouseEvent event) throws SQLException {

        if (txtEmail.getText().split(" ").equals("") | txtBiografia.getText().split(" ").equals("") | txtTelefono.getText().split(" ").equals("")
                | txtDireccion.getText().split(" ").equals("") | txtGenero.getText().split(" ").equals("")) {
            alarma.Error("Porfavor llene todos los campos");
        } else {
            PreparedStatement ps = bd.prepareStatement("INSERT INTO PERFIL (EMAILPUBLICO,BIOGRAFIA,TELEFONO,DIRECCION,GENERO) VALUES(?,?,?,?,?)");

            ps.setString(1, txtEmail.getText());
            ps.setString(2, txtBiografia.getText());
            ps.setString(3, (txtTelefono.getText()));
            ps.setString(4, txtDireccion.getText());
            ps.setString(5, txtGenero.getText());
            ps.executeUpdate();
            System.out.println("Agregado PERFIL correctamente");

            ResultSet rs = bd.createStatement().executeQuery("SELECT ID_PERFIL from PERFIL ORDER BY ID_PERFIL DESC");
            rs.next(); //por defecto diremos que es el último ingresado, igual no es como que varios usuarios vayan a llenar datos al mismo tiempo
            PreparedStatement ps1 = bd.prepareStatement("UPDATE USUARIO SET ID_PERFIL = ? WHERE ID_USUARIO = ?" );
            ps1.setString(1,rs.getString("ID_PERFIL"));
            ps1.setString(2,Integer.toString(info.getNoCredenciales()));
            ps1.executeUpdate();
            System.out.println("Agregado PERFIL en Usuario correctamente");
            
            alarma.Information("Datos actualizados con exito");
            Stage stage = new Stage();
            Node source = (Node) event.getSource();
            stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }

}
