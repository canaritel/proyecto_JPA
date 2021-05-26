package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SplashController implements Initializable {

    @FXML
    private ImageView imagen;
    @FXML
    private Label lblTexto;

    private static Scene scene;   //variable de clase Scene donde se produce la acción con los elementos creados
    private static Stage stage;   //variable de clase Stage que es la ventana actual

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void cargaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Principal.fxml")); //cargamos la vista FXML
            Parent root = loader.load();  //instanciamos y cargamos el FXML en el padre
            PrincipalController ctrPrincipal = loader.getController(); //instanciamos el controlador haciendo uso del nuevo método getController
            scene = new Scene(root); //creamos la nueva escena
            stage = new Stage();    //creamos la nueva ventana
            stage.setScene(scene); //establecemos la escena
            stage.setTitle("JavaFX JPA Hibernate"); //Insertamos un título a la aplicación
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/javafx3.png")));  //Cargamos el icono en la ventana
            stage.setResizable(true); //permitimos que la ventana cambie de tamaño
            stage.show(); //mostramos la ventana
            cerrar();//cerramos la ventana anterior
        } catch (IOException ex) {
            System.err.println("Error cargando VentanaPrincipal en SplashController " + ex);
        }
    }

    public void cerrar() {
        Stage myStage = (Stage) this.lblTexto.getScene().getWindow();  //creamos una nueva ventana temporal capturando de cualquier btn/txt la scena y ventana
        //se entiende que los btn o txt forman parte de la ventana que deseamos cerrar.
        myStage.close();  //cerramos la ventana y de esta forma solo veremos la nueva ventana
    }

}
