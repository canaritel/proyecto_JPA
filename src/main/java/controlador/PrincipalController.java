package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.variablesPantalla;

public class PrincipalController implements Initializable {

    @FXML
    private VBox vboxCentroPrincipal;
    @FXML
    private Label lblTituloListado;
    @FXML
    private Label lblTituloJugador;
    @FXML
    private Label lblTituloJuego;
    @FXML
    private Label lblTituloDistribuidor;
    @FXML
    private HBox opcionJugador;
    @FXML
    private HBox opcionListado;
    @FXML
    private HBox opcionJuego;
    @FXML
    private HBox opcionDistribuidor;
    @FXML
    private Label lblVersion;
    @FXML
    private Label txtListado;
    @FXML
    private Label textJugador;
    @FXML
    private Label textJuegos;
    @FXML
    private Label textDistribuidor;

    private ImageView imageView;
    private Image fondoImagen;
    private VBox ventana;
    private Stage stage;
    private RotateTransition rt;    //objeto para la animación
    private TranslateTransition tt; //objeto para la animación
    private FadeTransition ft;      //objeto para la animación
    private int x, y;               //tamaño ventana actual

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fondoImagen = new Image(getClass().getResourceAsStream("/images/java_programmer.gif")); //cargamos una imagen para mostrar y animar
        mostrarFondo();
        lblVersion.setText("Java: " + System.getProperty("java.version") + " / JavaFX: " + System.getProperty("javafx.version"));
    }

    @FXML
    private void accionMouseSeleccion(MouseEvent event) {
        Object evt = event.getSource();

        if (evt.equals(opcionListado)) {
            moverImagen();
            cargaVistaListado();
            opcionListado.setStyle("-fx-background-color: #f3f3f3;");
        } else {
            opcionListado.setStyle(null);
        }

        if (evt.equals(opcionJugador)) {
            moverImagen();
            cargaVistaJugador();
            opcionJugador.setStyle("-fx-background-color: #f3f3f3;");
        } else {
            opcionJugador.setStyle(null);
        }

        if (evt.equals(opcionJuego)) {
            moverImagen();
            cargaVistaJuego();
            opcionJuego.setStyle("-fx-background-color: #f3f3f3;");
        } else {
            opcionJuego.setStyle(null);
        }

        if (evt.equals(opcionDistribuidor)) {
            moverImagen();
            cargarVistaDistribuidor();
            opcionDistribuidor.setStyle("-fx-background-color: #f3f3f3;");
        } else {
            opcionDistribuidor.setStyle(null);
        }
    }

    @FXML
    private void accionMouseDentro(MouseEvent event) {
        Object evt = event.getSource();
        girarImagen();

        if (evt.equals(opcionListado)) {
            lblTituloListado.setEffect(new DropShadow(2.0, Color.BLACK));
            txtListado.setEffect(new DropShadow(1.5, Color.BLUE));
        }
        if (evt.equals(opcionJugador)) {
            lblTituloJugador.setEffect(new DropShadow(2.0, Color.BLACK));
            textJugador.setEffect(new DropShadow(1.5, Color.BLUE));
        }
        if (evt.equals(opcionJuego)) {
            lblTituloJuego.setEffect(new DropShadow(2.0, Color.BLACK));
            textJuegos.setEffect(new DropShadow(1.5, Color.BLUE));
        }
        if (evt.equals(opcionDistribuidor)) {
            lblTituloDistribuidor.setEffect(new DropShadow(2.0, Color.BLACK));
            textDistribuidor.setEffect(new DropShadow(1.5, Color.BLUE));
        }
    }

    @FXML
    private void accionMouseFuera(MouseEvent event) {
        lblTituloListado.setEffect(null);
        lblTituloDistribuidor.setEffect(null);
        lblTituloJuego.setEffect(null);
        lblTituloJugador.setEffect(null);
        txtListado.setEffect(null);
        textJuegos.setEffect(null);
        textJugador.setEffect(null);
        textDistribuidor.setEffect(null);
    }

    private void cargaVistaListado() {
        moverImagen();
        tt.setOnFinished((e) -> {  //Cuando finaliza moverImagen cargamos la vista Listado
            try {
                this.calcularTamanio();
                ventana = FXMLLoader.load(getClass().getResource("/fxml/Listado.fxml"));
                vboxCentroPrincipal.getChildren().setAll(ventana);
                vboxCentroPrincipal.setVisible(true);

            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void cargaVistaJugador() {
        moverImagen();
        tt.setOnFinished((e) -> {  //Cuando finaliza moverImagen cargamos la vista
            try {
                ventana = FXMLLoader.load(getClass().getResource("/fxml/Jugador.fxml"));
                vboxCentroPrincipal.getChildren().setAll(ventana);
                vboxCentroPrincipal.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void cargarVistaDistribuidor() {
        moverImagen();
        tt.setOnFinished((e) -> {  //Cuando finaliza moverImagen cargamos la vista distribuidor
            try {
                ventana = FXMLLoader.load(getClass().getResource("/fxml/Distribuidor.fxml"));
                vboxCentroPrincipal.getChildren().setAll(ventana);
                vboxCentroPrincipal.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void cargaVistaJuego() {
        moverImagen();
        tt.setOnFinished((e) -> {  //Cuando finaliza moverImagen cargamos la vista Juego
            try {
                ventana = FXMLLoader.load(getClass().getResource("/fxml/Juego.fxml"));
                vboxCentroPrincipal.getChildren().setAll(ventana);
                vboxCentroPrincipal.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void mostrarFondo() {
        // Añado imagen de fondo a la ventana
        imageView = new ImageView();
        imageView.setImage(fondoImagen);
        imageView.setFitWidth(500);
        imageView.setFitHeight(400);
        //cargamos todo en nuestra ventana VBox
        vboxCentroPrincipal.getChildren().setAll(imageView);
        vboxCentroPrincipal.setAlignment(Pos.CENTER);
        vboxCentroPrincipal.setVisible(true);
    }

    private void girarImagen() {
        if (rt == null) {
            rt = new RotateTransition();
        }

        if (ventana == null) {  //si el objeto ventana está vacío mostramos la animación
            //creamos animación de tipo rotate transición
            rt.setNode(imageView);
            rt.setDuration(new Duration(1000));
            rt.setByAngle(360);
            rt.setCycleCount(1);
            rt.setAutoReverse(true);
            rt.play();  //ejecutamos la animación
        }
    }

    private void moverImagen() {
        if (tt == null) {
            tt = new TranslateTransition();
        }

        if (ventana == null) {  //si el objeto ventana está vacío mostramos la animación    
            //creamos animación de translate transición
            tt.setNode(imageView);
            tt.setDuration(new Duration(1000));
            tt.setFromX(0f);
            tt.setToX(2000f);
            tt.setCycleCount(1);
            tt.play();  //ejecutamos la animación
        } else { //si la ventana no está vacía (después de la primera carga) aplicampos el efecto a la ventana
            tt.setNode(ventana);
            tt.setDuration(new Duration(500));
            tt.setFromX(0f);
            tt.setToX(2000f);
            tt.setCycleCount(1);
            tt.play();  //ejecutamos la animación
        }
    }

    public void calcularTamanio() {
        stage = new Stage();
        stage = (Stage) lblTituloJuego.getScene().getWindow();
        y = (int) (vboxCentroPrincipal.getHeight());
        x = (int) (vboxCentroPrincipal.getWidth());
        variablesPantalla.valorX = x - 10;
    }

}
