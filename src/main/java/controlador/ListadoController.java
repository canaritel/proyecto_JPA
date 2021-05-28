package controlador;

import dao.JuegoJpaController;
import entidades.Juego;
import java.io.IOException;
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
    private int x;                       //ancho del item
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
            System.out.println(listadoJuego.toString());
        } catch (Exception e) {
            System.err.println("error en cargarListado " + e);
        }
    }

    private void mostrarListado() {
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < listadoJuego.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/fxml/item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ItemController itemController = fxmlLoader.getController();
                itemController.enviaDatos(listadoJuego.get(i));
                if (column == columnaItem) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //añadimos (child,column,row)
                //set grid ancho
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid alto
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);
                grid.autosize();
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
        totalPrecioLabel.setText("€" + String.valueOf(valorJuegos));
    }

    private void calculaNumeroItems() {
        columnaItem = variablesPantalla.valorX / anchoItem;
    }

    private void calculaAnchoVentana() {
        x = (int) (scroll.getWidth());
    }

    @FXML
    private void accionMoverVentana(MouseEvent event) {
        calculaAnchoVentana();
        if (x != variablesPantalla.valorX) {
            variablesPantalla.valorX = x;
            calculaNumeroItems();
            grid.getChildren().clear();
            mostrarListado();
        }
    }

}
