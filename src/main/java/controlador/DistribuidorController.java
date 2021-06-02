package controlador;

import modelo.MensajeFX;
import dao.DistribuyeJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import entidades.Distribuye;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.ValidaTextField;
import modelo.variablesPantalla;

/**
 * FXML Controller class
 *
 * @author telev
 */
public class DistribuidorController implements Initializable {

    @FXML
    private TextField textBuscar;
    @FXML
    private ImageView iconReset;
    @FXML
    private Label lblFormulario;
    @FXML
    private Label lblCampo1;
    @FXML
    private TextField textCampo1;
    @FXML
    private Label lblCampo2;
    @FXML
    private TextField textCampo2;
    @FXML
    private Label lblCampo3;
    @FXML
    private TextField textCampo3;
    @FXML
    private Label lblCampo4;
    @FXML
    private TextField textCampo4;
    @FXML
    private Label lblCampo5;
    @FXML
    private TextField textCampo5;
    @FXML
    private Label lblFecha;
    @FXML
    private DatePicker dateFecha;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Button btnGrabar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TableView<Distribuye> tablaDistribuidor;
    @FXML
    private TableColumn<Distribuye, String> colNombreDistribuidor;
    @FXML
    private TableColumn<Distribuye, String> colDireccionDistribuidor;
    @FXML
    private TableColumn<Distribuye, String> colCiudadDistribuidor;
    @FXML
    private TableColumn<Distribuye, String> colPaisDistribuidor;

    private String camposPendientes;  //los campos no validados
    private ObservableList<Distribuye> itemsDistribuye;  //creamos una lista de tipo observableList, se usará para la tabla
    private ObservableList<Distribuye> itemsFiltro; //para guardar el resultado de los filtros de búsquedas
    private Distribuye objetoDistribuye;   //creamos un objeto de tipo Distribuye
    private DistribuyeJpaController distribuyeDAO; //creamos un objeto para conectar con nuestros métodos DAO
    private List<Distribuye> result; //la lista donde almacenamos los datos leidos
    //Inicializamos nuestro objeto para la persistencia. Usamos los datos declarados dentro de nuestro fichero persistence.xml
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("BD_Hibernate"); //creamos el objeto para conectar con nuestro sistema de Persistencia JPA

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (distribuyeDAO == null) {
            distribuyeDAO = new DistribuyeJpaController(emf);
        }
        if (objetoDistribuye == null) {
            objetoDistribuye = new Distribuye();
        }
        inicializaTablaDistribuidor();
        textosDistribuye();
        limpiarCampos();
    }

    @FXML
    private void accionBoton(ActionEvent event) {
        Object evt = event.getSource();

        if (evt.equals(btnNuevo)) {
            nuevoRegistro();
        }

        if (evt.equals(btnCancelar)) {
            funcionCancelar();
        }

        if (evt.equals(btnLimpiar)) {
            funcionCancelar();
        }

        if (evt.equals(btnEliminar)) {
            eliminaDistribuidor();
        }

        if (evt.equals(btnEditar)) {
            editaDistribuidor();
        }

        if (evt.equals(btnGrabar)) {
            creaDistribuidor();
        }
    }

    @FXML
    private void posicionTabla(MouseEvent event) {
        obtenFilaDistruidor();
        desactivarCampos(false);
        textCampo1.setDisable(true);
    }

    @FXML
    private void accionTeclado(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            textBuscar.setText("");
            filtroLista("");
        } else {
            filtroLista(textBuscar.getText());
        }
    }

    @FXML
    private void accionMouseBuscar(MouseEvent event) {
        Object evt = event.getSource();

        if (evt.equals(iconReset)) {
            textBuscar.setText("");
            filtroLista("");
        }
    }

    @FXML
    private void accionTeclaPulsada(KeyEvent event) {
        Object evt = event.getSource();  //capturamos el evento cuando se pulsa una tecla en los campos textfield
        String caracter;
        caracter = event.getCharacter(); //guardamos como string el caracter pulsado

        if (evt.equals(textCampo1)) {      //si el evento es el campo nombre distribuidor
            ValidaTextField.textoString(caracter, textCampo1, 50);
        }

        if (evt.equals(textCampo2)) {       //si el  evento es dirección
            ValidaTextField.textoString(caracter, textCampo2, 100);
        }

        if (evt.equals(textCampo3)) {    //si el evento es el campo ciudad
            ValidaTextField.textoString(caracter, textCampo3, 50);
        }

        if (evt.equals(textCampo4)) {       //si el evento es el campo país
            ValidaTextField.textoString(caracter, textCampo4, 30);
        }
    }

    private void inicializaTablaDistribuidor() {
        //asignamos a cada columna de la tabla el valor de su campo referenciado en Distribuye
        PropertyValueFactory<Distribuye, String> valorCol1 = new PropertyValueFactory<>("idDistribuidor");
        this.colNombreDistribuidor.setCellValueFactory(valorCol1);
        PropertyValueFactory<Distribuye, String> valorCol2 = new PropertyValueFactory<>("direccion");
        this.colDireccionDistribuidor.setCellValueFactory(valorCol2);
        PropertyValueFactory<Distribuye, String> valorCol3 = new PropertyValueFactory<>("ciudad");
        this.colCiudadDistribuidor.setCellValueFactory(valorCol3);
        PropertyValueFactory<Distribuye, String> valorCol4 = new PropertyValueFactory<>("pais");
        this.colPaisDistribuidor.setCellValueFactory(valorCol4);

        actualizaListaDistribuidor();
        tablaDistribuidor.setPrefHeight(variablesPantalla.resolucionY);  //establecemos el alto de la tabla por defecto
        tablaDistribuidor.refresh();
        tablaDistribuidor.setItems(itemsDistribuye);
    }

    private void actualizaListaDistribuidor() {
        if (itemsFiltro == null) {
            itemsFiltro = FXCollections.observableArrayList();
        }
        try {
            result = distribuyeDAO.findDistribuyeEntities();
            if (itemsDistribuye == null) {
                itemsDistribuye = FXCollections.observableList(result);
            } else {
                itemsDistribuye.clear();
                itemsDistribuye.addAll(result);
            }
        } catch (Exception e) {
            System.err.println("error en actualizarListaDistribuidor " + e);
        }
    }

    public void filtroLista(String filtro) {
        if (filtro.isEmpty()) {
            tablaDistribuidor.refresh();
            tablaDistribuidor.setItems(itemsDistribuye);
        } else {
            itemsFiltro.clear();
            for (Distribuye d : itemsDistribuye) {
                if (d.getIdDistribuidor().toLowerCase().contains(filtro.toLowerCase())
                        || d.getDireccion().toLowerCase().contains(filtro.toLowerCase())
                        || d.getCiudad().toLowerCase().contains(filtro.toLowerCase())
                        || d.getPais().toLowerCase().contains(filtro.toLowerCase())) {
                    itemsFiltro.add(d);
                }
            }
            tablaDistribuidor.setItems(itemsFiltro);
        }

    }

    private void obtenFilaDistruidor() {
        if (objetoDistribuye == null) {
            objetoDistribuye = new Distribuye();
        }
        objetoDistribuye = tablaDistribuidor.getSelectionModel().getSelectedItem();
        if (objetoDistribuye != null) {  //si no es NULL capturamos los datos de la fila
            textCampo1.setText(objetoDistribuye.getIdDistribuidor());
            textCampo2.setText(objetoDistribuye.getDireccion());
            textCampo3.setText(objetoDistribuye.getCiudad());
            textCampo4.setText(objetoDistribuye.getPais());
            cancelarRegistro();
        }
    }

    private void nuevoRegistro() {
        btnGrabar.setVisible(true);
        btnCancelar.setVisible(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnNuevo.setDisable(true);
        limpiarCampos();
        desactivarCampos(false);
        objetoDistribuye = new Distribuye();
    }

    private void cancelarRegistro() {
        btnGrabar.setVisible(false);
        btnCancelar.setVisible(false);
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnNuevo.setDisable(false);
    }

    private void textosDistribuye() {
        lblFormulario.setText("Formulario Distribuidor");
        lblCampo1.setText("Nombre Distribuidor");
        lblCampo2.setText("Dirección");
        lblCampo3.setText("Ciudad");
        lblCampo4.setText("País");
        lblCampo5.setVisible(false);
        dateFecha.setVisible(false);
        textCampo5.setVisible(false);
        lblFecha.setVisible(false);
    }

    private void limpiarCampos() {
        textCampo1.setText("");
        textCampo2.setText("");
        textCampo3.setText("");
        textCampo4.setText("");
        textCampo5.setText("");
        desactivarCampos(true);
    }

    private void desactivarCampos(boolean valor) {
        textCampo1.setDisable(valor);
        textCampo2.setDisable(valor);
        textCampo3.setDisable(valor);
        textCampo4.setDisable(valor);
        textCampo5.setDisable(valor);
    }

    private boolean validarCampos() {
        boolean respuesta = true;
        camposPendientes = "";
        if (textCampo1.getText().isEmpty()) {
            textCampo1.requestFocus();
            camposPendientes = camposPendientes + "Nombre Distribuidor\n";
            respuesta = false;
        }
        if (textCampo2.getText().isEmpty()) {
            textCampo2.requestFocus();
            camposPendientes = camposPendientes + "Dirección\n";
            respuesta = false;
        }
        if (textCampo3.getText().isEmpty()) {
            textCampo3.requestFocus();
            camposPendientes = camposPendientes + "Ciudad\n";
            respuesta = false;
        }
        if (textCampo4.getText().isEmpty()) {
            textCampo4.requestFocus();
            camposPendientes = camposPendientes + "País\n";
            respuesta = false;
        }

        return respuesta;
    }

    private void grabarRegistroDistribuidor() {
        try {
            distribuyeDAO.create(objetoDistribuye);
            MensajeFX.printTexto("Registro grabado correctamente", "INFO", obtenPosicionX_Y());
        } catch (Exception e) {
            System.err.println("Error al grabar el registro " + e);
        }
    }

    private void editaRegistroDistribuidor() {
        try {
            distribuyeDAO.edit(objetoDistribuye);
            MensajeFX.printTexto("Registro editado correctamente", "INFO", obtenPosicionX_Y());
        } catch (NonexistentEntityException ex) {
            MensajeFX.printTexto("Error al editar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("No existe el registro a editar " + ex);
        } catch (Exception ex) {
            MensajeFX.printTexto("Error al editar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("Error al editar registro " + ex);
        }
    }

    private void eliminarRegistroDistribuidor() {
        try {
            if (MensajeFX.printTexto("Confirme si desea eliminar este registro", "CONFIRM", obtenPosicionX_Y())) {
                distribuyeDAO.destroy(objetoDistribuye.getIdDistribuidor());
            }
        } catch (NonexistentEntityException | IllegalOrphanException ex) {
            MensajeFX.printTexto("Error al eliminar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("Error al eliminar el registro " + ex);
        }
    }

    private void convertirObjeto() {
        if (objetoDistribuye == null) {
            objetoDistribuye = new Distribuye();
        }
        objetoDistribuye.setIdDistribuidor(textCampo1.getText().trim());
        objetoDistribuye.setDireccion(textCampo2.getText().trim());
        objetoDistribuye.setCiudad(textCampo3.getText().trim());
        objetoDistribuye.setPais(textCampo4.getText().trim());
    }

    private void funcionCancelar() {
        cancelarRegistro();
        limpiarCampos();
        textBuscar.setText("");
        filtroLista("");
    }

    private void creaDistribuidor() {
        if (validarCampos()) {
            convertirObjeto();
            System.out.println("compruebo si existe al crear el distribuidor");
            if (!distribuyeDAO.existeDistribuidor(objetoDistribuye)) {
                grabarRegistroDistribuidor();
                limpiarCampos();
                cancelarRegistro();
                actualizaListaDistribuidor();
            } else {
                MensajeFX.printTexto("Existe un registro con los mismos datos", "WARNING", obtenPosicionX_Y());
            }
        } else {
            MensajeFX.printTexto("Los siguientes campos están vacíos:\n" + camposPendientes, "WARNING", obtenPosicionX_Y());
        }
    }

    private void editaDistribuidor() {
        if (!textCampo1.getText().isEmpty()) {
            if (validarCampos()) {
                convertirObjeto();
                if (!distribuyeDAO.existeDistribuidorAll(objetoDistribuye)) {
                    editaRegistroDistribuidor();
                    limpiarCampos();
                    cancelarRegistro();
                    actualizaListaDistribuidor();
                } else {
                    MensajeFX.printTexto("Existe un registro con los mismos datos", "WARNING", obtenPosicionX_Y());
                }
            } else {
                MensajeFX.printTexto("Los siguientes campos están vacíos:\n" + camposPendientes, "WARNING", obtenPosicionX_Y());
            }
        } else {
            MensajeFX.printTexto("Debe seleccionar un registro de la tabla", "WARNING", obtenPosicionX_Y());
        }
    }

    private void eliminaDistribuidor() {
        if (!textCampo1.getText().isEmpty()) {
            eliminarRegistroDistribuidor();
            limpiarCampos();
            actualizaListaDistribuidor();
        } else {
            MensajeFX.printTexto("Debe seleccionar un registro de la tabla", "WARNING", obtenPosicionX_Y());
        }
    }

    //este método obtiene la posición de la actual ventana en coordenadas x, y
    //vamos a usar estos datos para posicionar la ventana de mensajes en la pantalla correctamente
    public double[] obtenPosicionX_Y() {
        double[] posicionxy = new double[2];
        //creamos una nueva ventana temporal capturando de cualquier btn/lbl la escena y ventana
        //se entiende que los btn o lbl forman parte de la ventana que deseamos obtener datos
        Stage myStage = (Stage) this.lblCampo1.getScene().getWindow();
        int frmX = 500 / 2; //tamaño ancho componente FrmAlumno
        int frmY = 480 / 2; //tamaño alto componente FrmAlumno
        int x = (int) (myStage.getWidth() / 2);
        int y = (int) (myStage.getHeight() / 2);
        posicionxy[0] = myStage.getX() + (x - frmX);
        posicionxy[1] = myStage.getY() + (y - frmY);
        return posicionxy;
    }

}
