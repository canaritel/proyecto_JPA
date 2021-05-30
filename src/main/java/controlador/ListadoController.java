package controlador;

import dao.JuegoJpaController;
import entidades.Juego;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.variablesPantalla;

public class ListadoController implements Initializable {

    @FXML
    private HBox ventanaParaScrollHBox;
    @FXML
    private Label totalJuegosLabel;
    @FXML
    private Label totalPrecioLabel;
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;

    private final int anchoItem = 245;   //ancho del item que insertamos en la ventana
    private int ventanaAncho;            //ancho actual de la ventana
    private int columnaItem;             //número de columna a mostrar en la ventana
    private List<Juego> listadoJuego;    //la lista donde almacenamos los datos leidos
    private JuegoJpaController juegoDAO; //creamos un objeto para conectar con nuestros métodos DAO
    //Inicializamos nuestro objeto para la persistencia. Usamos los datos declarados dentro de nuestro fichero persistence.xml
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("BD_Hibernate"); //creamos el objeto para conectar con nuestro sistema de Persistencia JPA

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (juegoDAO == null) {
            juegoDAO = new JuegoJpaController(emf);
        }
        cargarListado();
        calculaNumeroJuegos();
        calculaPrecioJuegos();
        calculaNumeroItems();
        mostrarListado();
    }

    private void cargarListado() {
        try {
            listadoJuego = juegoDAO.findJuegoEntities();
        } catch (Exception e) {
            System.err.println("error en cargarListado " + e);
        }
    }

    private void mostrarListado() {
        int columna = 0;
        int fila = 1;
        try {
            for (int i = 0; i < listadoJuego.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.enviaDatos(listadoJuego.get(i));
                if (columna == columnaItem) {
                    columna = 0;
                    fila++;
                }

                grid.add(anchorPane, columna++, fila); //añadimos (hijo,fila, columna)
                //seteamos grid ancho
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //seteamos grid alto
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                //grid.autosize();
                GridPane.setMargin(anchorPane, new Insets(10)); //margen entre los item grid
            }
        } catch (IOException e) {
        }
    }

    private void calculaNumeroJuegos() {
        int numRegistros = listadoJuego.size();
        totalJuegosLabel.setText(String.valueOf(numRegistros));
    }

    private void calculaPrecioJuegos() {
        double valorJuegos = 0;
        for (Juego juego : listadoJuego) {
            valorJuegos = valorJuegos + juego.getPrecio().doubleValue();
        }
        BigDecimal formatNumber = new BigDecimal(valorJuegos);
        formatNumber = formatNumber.setScale(2, RoundingMode.CEILING);   //solo permitimos 2 decimales
        totalPrecioLabel.setText("€" + formatNumber);
    }

    private void calculaNumeroItems() {
        columnaItem = (int) variablesPantalla.valorX / anchoItem;
    }

    private void calculaAnchoVentana() {
        ventanaAncho = (int) (scroll.getWidth());
    }

    @FXML
    private void accionMoverVentana(MouseEvent event) {
        calculaAnchoVentana();
        if (ventanaAncho != variablesPantalla.valorX) {
            variablesPantalla.valorX = ventanaAncho;
            calculaNumeroItems();
            grid.getChildren().clear();
            mostrarListado();
        }
    }

}
