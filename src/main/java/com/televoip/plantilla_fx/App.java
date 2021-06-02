package com.televoip.plantilla_fx;

import controlador.SplashController;
import dao.TestInicioJpaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class App extends Application {

    private SplashController controlador;
    private Parent root;
    private TestInicioJpaController inicioDAO;
    //Al declarar el objeto EMF antes de usarlo logramos que se reduzca el tiempo de carga la primera vez que se accede
    //Inicializamos nuestro objeto para la persistencia. Usamos los datos declarados dentro de nuestro fichero persistence.xml "IMPORTANTE PONER EL MISMO NOMBRE SINO DARÁ ERROR"
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("BD_Hibernate"); //creamos el objeto para conectar con nuestro sistema de Persistencia JPA

    @Override
    public void init() {  //Primer método que se ejecuta al instanciar la clase
        System.out.println("Método init()");
        if (inicioDAO == null) {
            inicioDAO = new TestInicioJpaController(emf);
        }
        inicioDAO.getTestInicioCount(); //lo declaramos para poder inicializar el sistema de persistencia y cargue antes los siguientes procesos
    }

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Splash.fxml"));
            //instanciamos y cargamos el FXML en el padre
            root = loader.load();
            controlador = loader.getController();
            Scene scene = new Scene(root, 640, 480, Color.TRANSPARENT);
            root.setStyle("-fx-background-color: transparent;");
            stage = new Stage();    //creamos la nueva ventana
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println("Error cargando método Start en App " + ex);
        }
        //Efecto en la ventana Splash con fadein
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(5), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);
        fadeIn.play();
        fadeIn.setOnFinished((e) -> {  //Cuando finaliza la FadeIn cargamos la vista Principal
            controlador.cargaPrincipal();
        });
    }

    @Override
    public void stop() { //Creamos procesos de finalización
        System.out.println("Método stop()");
    }

    public static void runApp(String[] args) {
        launch(args);
    }

    public void cerrar() {
        Platform.exit(); //Es ideal para cerrar la aplicación. Además ejecuta el proceso stop()
    }

}
