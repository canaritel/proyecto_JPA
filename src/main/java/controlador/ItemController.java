package controlador;

import entidades.Juego;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemController implements Initializable {

    @FXML
    private ImageView img;
    @FXML
    private Label tituloLabel;
    @FXML
    private Label precioLabel;
    @FXML
    private Label sistemaOperativoLabel;
    @FXML
    private Label usuarioLabel;
    @FXML
    private Label distribuyeLabel;

    private Juego juego;
    private Image image;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void enviaDatos(Juego juego) {
        this.juego = juego;
        tituloLabel.setText(juego.getTitulo());
        precioLabel.setText("€" + juego.getPrecio());
        sistemaOperativoLabel.setText(juego.getSistemaOperativo());
        usuarioLabel.setText(juego.getUsuario().toString());
        distribuyeLabel.setText(juego.getDistribuidor().toString());

        if (juego.getImagen() == null) {
            image = new Image(getClass().getResourceAsStream("/images/juego_resident-evil-2-remake.jpg"));
        } else {
            image = new Image(new ByteArrayInputStream(juego.getImagen()));
        }
        img.setImage(image);
    }

}
