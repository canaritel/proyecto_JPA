package controlador;

import modelo.MensajeFX;
import dao.UsuarioJpaController;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import entidades.Usuario;
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

public class JugadorController implements Initializable {

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
    private TableView<Usuario> tablaUsuario;
    @FXML
    private TableColumn<Usuario, String> colNombreUsuario;
    @FXML
    private TableColumn<Usuario, String> colApellidoUsuario;
    @FXML
    private TableColumn<Usuario, Integer> colEdadUsuario;
    @FXML
    private TableColumn<Usuario, String> colTelefonoUsuario;

    private String camposPendientes; //para guardar los campos no validados
    private ObservableList<Usuario> itemsUsuarios;  //creamos una lista de tipo observableList, se usará para la tabla
    private ObservableList<Usuario> itemsFiltro; //para guardar el resultado de los filtros de búsquedas
    private Usuario objetoUsuario;      //creamos un objeto de tipo Usuario
    private UsuarioJpaController usuarioDAO; //creamos un objeto para conectar con nuestros métodos DAO
    private List<Usuario> result; //la lista donde almacenamos los datos leidos
    //Inicializamos nuestro objeto para la persistencia. Usamos los datos declarados dentro de nuestro fichero persistence.xml "IMPORTANTE PONER EL MISMO NOMBRE SINO DARÁ ERROR"
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("BD_Hibernate"); //creamos el objeto para conectar con nuestro sistema de Persistencia JPA

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioJpaController(emf);
        }
        if (objetoUsuario == null) {
            objetoUsuario = new Usuario();
        }
        inicializaTablaUsuario();
        textosUsuario();
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
            eliminaUsuario();
        }

        if (evt.equals(btnEditar)) {
            editaUsuario();
        }

        if (evt.equals(btnGrabar)) {
            creaUsuario();
        }
    }

    @FXML
    private void posicionTabla(MouseEvent event) {
        obtenFilaUsuario();
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

    private void inicializaTablaUsuario() {
        //asignamos a cada columna de la tabla el valor de su campo referenciado en Usuario
        PropertyValueFactory<Usuario, String> valorCol1 = new PropertyValueFactory<>("nombre");
        this.colNombreUsuario.setCellValueFactory(valorCol1);
        PropertyValueFactory<Usuario, String> valorCol2 = new PropertyValueFactory<>("apellidos");
        this.colApellidoUsuario.setCellValueFactory(valorCol2);
        PropertyValueFactory<Usuario, Integer> valorCol3 = new PropertyValueFactory<>("edad");
        this.colEdadUsuario.setCellValueFactory(valorCol3);
        PropertyValueFactory<Usuario, String> valorCol4 = new PropertyValueFactory<>("telefono");
        this.colTelefonoUsuario.setCellValueFactory(valorCol4);

        actualizaListaUsuarios();
        tablaUsuario.refresh();
        tablaUsuario.setItems(itemsUsuarios);
    }

    private void actualizaListaUsuarios() {
        if (itemsFiltro == null) {
            itemsFiltro = FXCollections.observableArrayList();
        }
        try {
            result = usuarioDAO.findUsuarioEntities();
            if (itemsUsuarios == null) {
                itemsUsuarios = FXCollections.observableList(result);
            } else {
                itemsUsuarios.clear();
                itemsUsuarios.addAll(result);
            }
        } catch (Exception e) {
            System.err.println("error en actualizarListaUsuarios " + e);
        }
    }

    public void filtroLista(String filtro) {
        if (filtro.isEmpty()) {
            tablaUsuario.refresh();
            tablaUsuario.setItems(itemsUsuarios);
        } else {
            itemsFiltro.clear();
            for (Usuario d : itemsUsuarios) {
                if (d.getNombre().toLowerCase().contains(filtro.toLowerCase())
                        || d.getApellidos().toLowerCase().contains(filtro.toLowerCase())
                        || String.valueOf(d.getEdad()).contains(filtro)
                        || d.getTelefono().toLowerCase().contains(filtro.toLowerCase())) {
                    itemsFiltro.add(d);
                }
            }
            tablaUsuario.setItems(itemsFiltro);
        }
    }

    private void obtenFilaUsuario() {
        if (objetoUsuario == null) {
            objetoUsuario = new Usuario();
        }
        objetoUsuario = tablaUsuario.getSelectionModel().getSelectedItem();
        if (objetoUsuario != null) {  //si no es NULL capturamos los datos de la fila
            textCampo1.setText(objetoUsuario.getNombre());
            textCampo2.setText(objetoUsuario.getApellidos());
            textCampo3.setText(String.valueOf(objetoUsuario.getEdad()));
            textCampo4.setText(objetoUsuario.getTelefono());
            cancelarRegistro();
        }
    }

    private void nuevoRegistro() {
        //  if (textCampo1.isDisable()) {
        //      limpiarCampos();
        //  }
        btnGrabar.setVisible(true);
        btnCancelar.setVisible(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnNuevo.setDisable(true);
    }

    private void cancelarRegistro() {
        btnGrabar.setVisible(false);
        btnCancelar.setVisible(false);
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnNuevo.setDisable(false);
    }

    private void textosUsuario() {
        lblFormulario.setText("Formulario Jugador");
        lblCampo1.setText("Nombre Jugador");
        lblCampo2.setText("Apellidos");
        lblCampo3.setText("Edad");
        lblCampo4.setText("Teléfono");
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
        textCampo1.setDisable(false);
    }

    private boolean validarCampos() {
        boolean respuesta = true;
        camposPendientes = "";
        if (textCampo1.getText().isEmpty()) {
            textCampo1.requestFocus();
            camposPendientes = camposPendientes + "Nombre Jugador\n";
            respuesta = false;
        }
        if (textCampo2.getText().isEmpty()) {
            textCampo2.requestFocus();
            camposPendientes = camposPendientes + "Apellidos\n";
            respuesta = false;
        }
        if (textCampo3.getText().isEmpty()) {
            textCampo3.requestFocus();
            camposPendientes = camposPendientes + "Edad\n";
            respuesta = false;
        }
        if (textCampo4.getText().isEmpty()) {
            textCampo4.requestFocus();
            camposPendientes = camposPendientes + "Teléfono\n";
            respuesta = false;
        }

        if (!esNumerico(textCampo3.getText())) {
            textCampo3.requestFocus();
            camposPendientes = camposPendientes + "La edad debe ser un campo numérico mayor a 0\n";
            respuesta = false;
        }

        return respuesta;
    }

    private void grabarRegistroUsuario() {
        try {
            usuarioDAO.create(objetoUsuario);
            MensajeFX.printTexto("Registro grabado correctamente", "INFO", obtenPosicionX_Y());
        } catch (Exception e) {
            System.err.println("Error al grabar el registro " + e);
        }
    }

    private void editarRegistroUsuario() {
        try {
            usuarioDAO.edit(objetoUsuario);
            MensajeFX.printTexto("Registro editado correctamente", "INFO", obtenPosicionX_Y());
        } catch (NonexistentEntityException ex) {
            MensajeFX.printTexto("Error al editar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("No existe el registro a editar " + ex);
        } catch (Exception ex) {
            MensajeFX.printTexto("Error al editar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("Error al editar registro " + ex);
        }
    }

    private void eliminarRegistroUsuario() {
        try {
            if (MensajeFX.printTexto("Confirme si desea eliminar este registro", "CONFIRM", obtenPosicionX_Y())) {
                usuarioDAO.destroy(objetoUsuario.getIdUsuario());
            }
        } catch (NonexistentEntityException | IllegalOrphanException ex) {
            MensajeFX.printTexto("Error al eliminar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("Error al eliminar el registro " + ex);
        }
    }

    private void convertirObjeto() {
        if (objetoUsuario == null) {
            objetoUsuario = new Usuario();
        }
        objetoUsuario.setNombre(textCampo1.getText().trim());
        objetoUsuario.setApellidos(textCampo2.getText().trim());
        objetoUsuario.setEdad(Integer.valueOf(textCampo3.getText().trim()));
        objetoUsuario.setTelefono(textCampo4.getText().trim());
    }

    private void funcionCancelar() {
        cancelarRegistro();
        limpiarCampos();
        textBuscar.setText("");
        filtroLista("");
    }

    private void creaUsuario() {
        if (validarCampos()) {
            convertirObjeto();
            if (!usuarioDAO.existeUsuario(objetoUsuario)) {
                grabarRegistroUsuario();
                limpiarCampos();
                cancelarRegistro();
                actualizaListaUsuarios();
            } else {
                MensajeFX.printTexto("Existe un registro con los mismos datos", "WARNING", obtenPosicionX_Y());
            }
        } else {
            MensajeFX.printTexto("Los siguientes campos están incorrectos:\n" + camposPendientes, "WARNING", obtenPosicionX_Y());
        }
    }

    private void editaUsuario() {
        if (validarCampos()) {
            convertirObjeto();
            if (!usuarioDAO.existeUsuario(objetoUsuario)) {
                editarRegistroUsuario();
                limpiarCampos();
                cancelarRegistro();
                actualizaListaUsuarios();
            } else {
                MensajeFX.printTexto("Existe un registro con los mismos datos", "WARNING", obtenPosicionX_Y());
            }
        } else {
            MensajeFX.printTexto("Los siguientes campos están incorrectos:\n" + camposPendientes, "WARNING", obtenPosicionX_Y());
        }
    }

    private void eliminaUsuario() {
        if (!textCampo1.getText().isEmpty()) {
            eliminarRegistroUsuario();
            limpiarCampos();
            actualizaListaUsuarios();
        } else {
            MensajeFX.printTexto("Debe seleccionar un registro de la tabla", "WARNING", obtenPosicionX_Y());
        }
    }

    private boolean esNumerico(String cadena) {
        boolean respuesta = false;
        try {
            if (Integer.parseInt(cadena) > 0) {
                respuesta = true;
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return respuesta;
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

    @FXML
    private void accionTeclaPulsada(KeyEvent event) {
        Object evt = event.getSource();  //capturamos el evento cuando se pulsa una tecla en los campos textfield
        String caracter;
        caracter = event.getCharacter(); //guardamos como string el caracter pulsado

        if (evt.equals(textCampo1)) {      //si el evento es el campo nombre jugador
            ValidaTextField.textoString(caracter, textCampo1, 100); //validamos el textfiled de tipo String con los parámetros caracter, textfield y tamaño máximo
        }

        if (evt.equals(textCampo2)) {       //si el  evento es campo apellidos
            ValidaTextField.textoString(caracter, textCampo2, 100);
        }

        if (evt.equals(textCampo3)) {    //si el evento es el campo edad
            ValidaTextField.textoInt(caracter, textCampo3, 3);
        }

        if (evt.equals(textCampo4)) {       //si el evento es el campo telefono
            ValidaTextField.textoInt(caracter, textCampo4, 9);
        }
    }

}
