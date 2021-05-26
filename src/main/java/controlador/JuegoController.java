package controlador;

import dao.DistribuyeJpaController;
import dao.JuegoJpaController;
import dao.UsuarioJpaController;
import dao.exceptions.NonexistentEntityException;
import entidades.Distribuye;
import entidades.Juego;
import entidades.Usuario;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import modelo.MensajeFX;
import modelo.ValidaTextField;

public class JuegoController implements Initializable {

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
    private Label lblCampo3;
    @FXML
    private Label lblCampo4;
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
    private ImageView iconoInsertJugador;
    @FXML
    private ImageView iconoInfoJugador;
    @FXML
    private ImageView iconoInsertDistribuidor;
    @FXML
    private ImageView iconoInfoDistribuidor;
    @FXML
    private ComboBox<String> cmbSO;
    @FXML
    private ComboBox<Usuario> cmbJugador;
    @FXML
    private ComboBox<Distribuye> cmbDistribuidor;
    @FXML
    private TableView<Juego> tablaJuego;
    @FXML
    private TableColumn<Juego, String> colTituloJuego;
    @FXML
    private TableColumn<Juego, String> colSOJuego;
    @FXML
    private TableColumn<Juego, Integer> colJugadorJuego;
    @FXML
    private TableColumn<Juego, String> colDistribuidorJuego;
    @FXML
    private TableColumn<Juego, BigDecimal> colPrecioJuego;
    @FXML
    private TableColumn<Juego, LocalDate> colFechaJuego;

    private String camposPendientes; //para guardar los campos no validados
    private ObservableList<Juego> itemsJuegos;  //creamos una lista de tipo observableList, se usará para la tabla
    private ObservableList<Juego> itemsFiltro; //para guardar el resultado de los filtros de búsquedas
    private ObservableList<Usuario> obsUsuarios;
    private ObservableList<Distribuye> obsDistribuye;
    private JuegoJpaController juegoDAO; //creamos un objeto para conectar con nuestros métodos DAO
    private UsuarioJpaController usuarioDAO;
    private DistribuyeJpaController distribuyeDAO;
    private List<Juego> resultJuego; //la lista donde almacenamos los datos leidos
    private List<Usuario> resultUsuario;
    private List<Distribuye> resultDistribuye;
    private Juego objetoJuego;      //creamos un objeto de tipo Juego
    private Usuario objUsuario;
    private Distribuye objDistribuidor;
    //Inicializamos nuestro objeto para la persistencia. Usamos los datos declarados dentro de nuestro fichero persistence.xml "IMPORTANTE PONER EL MISMO NOMBRE SINO DARÁ ERROR"
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("BD_Hibernate"); //creamos el objeto para conectar con nuestro sistema de Persistencia JPA

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (juegoDAO == null) {
            juegoDAO = new JuegoJpaController(emf);
        }
        if (usuarioDAO == null) {
            usuarioDAO = new UsuarioJpaController(emf);
        }
        if (distribuyeDAO == null) {
            distribuyeDAO = new DistribuyeJpaController(emf);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        dateFecha.setShowWeekNumbers(false);
        inicializaTablaJuego();
        inicializaCombo();
        cargarComboJugador();
        cargarComboDistruidor();
        textosJuego();
        limpiarCampos();
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
    private void posicionTabla(MouseEvent event) {
        obtenFilaJuego();
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
            eliminaJuego();
        }

        if (evt.equals(btnEditar)) {
            editaJuego();
        }

        if (evt.equals(btnGrabar)) {
            creaJuego();
        }
    }

    @FXML
    private void accionIcono(MouseEvent event) {
        Object evt = event.getSource();  //capturamos el evento cuando se pulsa en los iconos habilitados

        if (evt.equals(iconoInfoJugador)) {
            ventabaPopupUsuario();
        }
        if (evt.equals(iconoInfoDistribuidor)) {
            ventabaPopupDistribuidor();
        }

    }

    @FXML
    private void accionTeclaPulsada(KeyEvent event) {
        Object evt = event.getSource();  //capturamos el evento cuando se pulsa una tecla en los campos textfield
        String caracter;
        caracter = event.getCharacter(); //guardamos como string el caracter pulsado

        if (evt.equals(textCampo1)) {      //si el evento es el campo nombre Juego
            ValidaTextField.textoString(caracter, textCampo1, 100);
        }

        if (evt.equals(textCampo5)) {       //si el evento es el campo precio
            ValidaTextField.textoInt(caracter, textCampo5, 6);
        }
    }

    private void inicializaTablaJuego() {
        //Los valores de los campos a referenciar no son los creados en la BD, son los que el Hibernate declara como private dentro de las entidades
        //sino hacemos lo anteriormente mencionado nos dará error al cargar los datos en la tabla
        PropertyValueFactory<Juego, String> valorCol1a = new PropertyValueFactory<>("titulo");
        this.colTituloJuego.setCellValueFactory(valorCol1a);
        PropertyValueFactory<Juego, String> valorCol2a = new PropertyValueFactory<>("sistemaOperativo");
        this.colSOJuego.setCellValueFactory(valorCol2a);
        PropertyValueFactory<Juego, Integer> valorCol3a = new PropertyValueFactory<>("usuario");
        this.colJugadorJuego.setCellValueFactory(valorCol3a);
        PropertyValueFactory<Juego, String> valorCol4a = new PropertyValueFactory<>("distribuidor");
        this.colDistribuidorJuego.setCellValueFactory(valorCol4a);
        PropertyValueFactory<Juego, BigDecimal> valorCol5a = new PropertyValueFactory<>("precio");
        this.colPrecioJuego.setCellValueFactory(valorCol5a);
        PropertyValueFactory<Juego, LocalDate> valorCol6a = new PropertyValueFactory<>("fechaJuego");
        this.colFechaJuego.setCellValueFactory(valorCol6a);

        actualizaListaJuegos();
        tablaJuego.refresh();
        tablaJuego.setItems(itemsJuegos);
    }

    private void textosJuego() {
        lblFormulario.setText("Formulario Juego");
        lblCampo1.setText("Título Juego");
        lblCampo2.setText("Sistema Operativo");
        lblCampo3.setText("Jugador");
        lblCampo4.setText("Distribuidor");
        lblCampo5.setText("Precio €");
        lblFecha.setText("Fecha compra");
        dateFecha.setVisible(true);
        textCampo5.setVisible(true);
        lblFecha.setVisible(true);
        cmbSO.setPromptText("Elija opción");
        cmbJugador.setPromptText("Elija opción");
        cmbDistribuidor.setPromptText("Elija opción");
    }

    private void filtroLista(String filtro) {
        if (itemsFiltro == null) {
            itemsFiltro = FXCollections.observableArrayList();
        }
        if (filtro.isEmpty()) {
            tablaJuego.refresh();
            tablaJuego.setItems(itemsJuegos);
        } else {
            itemsFiltro.clear();
            for (Juego d : itemsJuegos) {
                if (d.getTitulo().toLowerCase().contains(filtro.toLowerCase())
                        || String.valueOf(d.getFechaJuego()).contains(filtro)
                        || d.getSistemaOperativo().toLowerCase().contains(filtro.toLowerCase())) {
                    itemsFiltro.add(d);
                }
            }
            tablaJuego.setItems(itemsFiltro);
        }
    }

    private void obtenFilaJuego() {
        if (objetoJuego == null) {
            objetoJuego = new Juego();
        }
        objetoJuego = tablaJuego.getSelectionModel().getSelectedItem();
        if (objetoJuego != null) {  //si no es NULL capturamos los datos de la fila
            textCampo1.setText(objetoJuego.getTitulo());
            cmbSO.getSelectionModel().select(objetoJuego.getSistemaOperativo());
            cmbJugador.getSelectionModel().select(objetoJuego.getUsuario());
            cmbDistribuidor.getSelectionModel().select(objetoJuego.getDistribuidor());
            textCampo5.setText(String.valueOf(objetoJuego.getPrecio()));
            dateFecha.setValue(convertToLocalDateViaSqlDate(objetoJuego.getFechaJuego()));
            cancelarRegistro();
        }
    }

    private void nuevoRegistro() {
        btnGrabar.setVisible(true);
        btnCancelar.setVisible(true);
        btnEditar.setDisable(true);
        btnEliminar.setDisable(true);
        btnNuevo.setDisable(true);
    }

    private void actualizaListaJuegos() {
        try {
            resultJuego = juegoDAO.findJuegoEntities();
            if (itemsJuegos == null) {
                itemsJuegos = FXCollections.observableList(resultJuego);
            } else {
                itemsJuegos.clear();
                itemsJuegos.addAll(resultJuego);
            }
        } catch (Exception e) {
            System.err.println("error en actualizarListaJuegos " + e);
        }
    }

    private void inicializaCombo() {
        cmbSO.getItems().add("Windows 10/7");
        cmbSO.getItems().add("Mac OS");
        cmbSO.getItems().add("Linux");
        cmbSO.getItems().add("Android");
        cmbSO.getItems().add("iOS");
    }

    private void cargarComboJugador() {
        //resultUsuario = usuarioDAO.ListUsuarioByNombre();
        resultUsuario = usuarioDAO.findUsuarioEntities();
        if (obsUsuarios == null) {
            obsUsuarios = FXCollections.observableList(resultUsuario);
        } else {
            obsUsuarios.clear();
            obsUsuarios.addAll(resultUsuario);
        }
        cmbJugador.setItems(obsUsuarios);
    }

    private void cargarComboDistruidor() {
        //resultDistribuye = distribuyeDAO.ListDistribuidorByNombre();
        resultDistribuye = distribuyeDAO.findDistribuyeEntities();
        if (obsDistribuye == null) {
            obsDistribuye = FXCollections.observableList(resultDistribuye);
        } else {
            obsDistribuye.clear();
            obsDistribuye.addAll(resultDistribuye);
        }
        cmbDistribuidor.setItems(obsDistribuye);
    }

    private void creaJuego() {
        if (validarCampos()) {
            this.convertirObjeto();
            if (!juegoDAO.existeJuego(objetoJuego)) {
                grabarRegistroJuego();
                limpiarCampos();
                cancelarRegistro();
                actualizaListaJuegos();
            } else {
                MensajeFX.printTexto("Existe un registro con el mismo Título", "WARNING", obtenPosicionX_Y());
            }
        } else {
            MensajeFX.printTexto("Los siguientes campos están incorrectos:\n" + camposPendientes, "WARNING", obtenPosicionX_Y());
        }
    }

    private void editaJuego() {
        if (validarCampos()) {
            this.convertirObjeto();
            editarRegistroJuego();
            limpiarCampos();
            cancelarRegistro();
            actualizaListaJuegos();
        } else {
            MensajeFX.printTexto("Los siguientes campos están incorrectos:\n" + camposPendientes, "WARNING", obtenPosicionX_Y());
        }
    }

    private void eliminaJuego() {
        if (!textCampo1.getText().isEmpty()) {
            eliminarRegistroJuego();
            limpiarCampos();
            actualizaListaJuegos();
        } else {
            MensajeFX.printTexto("Debe seleccionar un registro de la tabla", "WARNING", obtenPosicionX_Y());
        }
    }

    private boolean validarCampos() {
        boolean respuesta = true;
        camposPendientes = "";

        if (textCampo1.getText().isEmpty()) {
            textCampo1.requestFocus();
            camposPendientes = camposPendientes + "Título Juego\n";
            respuesta = false;
        }
        if (cmbSO.getValue() == null) {
            cmbSO.requestFocus();
            camposPendientes = camposPendientes + "Sistema Operativo\n";
            respuesta = false;
        }
        if (cmbJugador.getValue() == null) {
            cmbJugador.requestFocus();
            camposPendientes = camposPendientes + "Jugador\n";
            respuesta = false;
        }
        if (cmbDistribuidor.getValue() == null) {
            cmbDistribuidor.requestFocus();
            camposPendientes = camposPendientes + "Distribuidor\n";
            respuesta = false;
        }
        if (dateFecha.getValue() == null) {
            dateFecha.requestFocus();
            camposPendientes = camposPendientes + "Fecha\n";
            respuesta = false;
        }
        if (textCampo5.getText().isEmpty()) {
            textCampo5.requestFocus();
            camposPendientes = camposPendientes + "Precio\n";
            respuesta = false;
        }
        if (!esNumerico(textCampo5.getText())) {
            textCampo5.requestFocus();
            camposPendientes = camposPendientes + "Debe introducir un precio correcto\n";
            respuesta = false;
        }

        return respuesta;
    }

    public void convertirObjeto() {
        if (objetoJuego == null) {
            objetoJuego = new Juego();
        }
        //objetoJuego.setIdJuego();
        objetoJuego.setTitulo(textCampo1.getText().trim());
        objetoJuego.setSistemaOperativo(cmbSO.getValue());
        objetoJuego.setUsuario(cmbJugador.getValue());
        objetoJuego.setDistribuidor(cmbDistribuidor.getValue());
        double valorPrecio = Double.valueOf(textCampo5.getText());
        objetoJuego.setPrecio(BigDecimal.valueOf(valorPrecio));
        objetoJuego.setFechaJuego(java.sql.Date.valueOf(dateFecha.getValue()));  //convertimos un campo datepicker en Date SQL
    }

    private void grabarRegistroJuego() {
        try {
            System.out.println("Grabando: " + objetoJuego.toString());
            juegoDAO.create(objetoJuego);
            MensajeFX.printTexto("Registro grabado correctamente", "INFO", obtenPosicionX_Y());
        } catch (Exception e) {
            System.err.println("Error al grabar el registro " + e);
        }
    }

    private void editarRegistroJuego() {
        try {
            juegoDAO.edit(objetoJuego);
            MensajeFX.printTexto("Registro editado correctamente", "INFO", obtenPosicionX_Y());
        } catch (NonexistentEntityException ex) {
            MensajeFX.printTexto("Error al editar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("No existe el registro a editar " + ex);
        } catch (Exception ex) {
            MensajeFX.printTexto("Error al editar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("Error al editar registro " + ex);
        }
    }

    private void eliminarRegistroJuego() {
        try {
            if (MensajeFX.printTexto("Confirme si desea eliminar este registro", "CONFIRM", obtenPosicionX_Y())) {
                juegoDAO.destroy(objetoJuego.getIdJuego());
            }
        } catch (NonexistentEntityException ex) {
            MensajeFX.printTexto("Error al eliminar el registro", "ERROR", obtenPosicionX_Y());
            System.err.println("Error al eliminar el registro " + ex);
        }
    }

    private void cancelarRegistro() {
        btnGrabar.setVisible(false);
        btnCancelar.setVisible(false);
        btnEditar.setDisable(false);
        btnEliminar.setDisable(false);
        btnNuevo.setDisable(false);
    }

    private void funcionCancelar() {
        cancelarRegistro();
        textBuscar.setText("");
        filtroLista("");
        limpiarCampos();
    }

    private void limpiarCampos() {
        textCampo1.setText("");
        textCampo5.setText("");
        cmbSO.setValue(null);
        cmbJugador.setValue(null);
        cmbDistribuidor.setValue(null);
        dateFecha.setValue(null);
        cmbSO.setPromptText("Elija opción");
        cmbJugador.setPromptText("Elija opción");
        cmbDistribuidor.setPromptText("Elija opción");
    }

    private void ventabaPopupUsuario() {
        if (!cmbJugador.getSelectionModel().isEmpty()) {
            objUsuario = cmbJugador.getSelectionModel().getSelectedItem();
            MensajeFX.printCampos("Jugador: " + objUsuario.getNombre()
                    + "\nApellidos: " + objUsuario.getApellidos()
                    + "\nEdad: " + objUsuario.getEdad()
                    + "\nTeléfono: " + objUsuario.getTelefono(), obtenPosicionX_Y(), 5);
        }
    }

    private void ventabaPopupDistribuidor() {
        if (!cmbDistribuidor.getSelectionModel().isEmpty()) {
            objDistribuidor = cmbDistribuidor.getSelectionModel().getSelectedItem();
            MensajeFX.printCampos("Distribuidor: " + objDistribuidor.getIdDistribuidor()
                    + "\nDirección: " + objDistribuidor.getDireccion()
                    + "\nCiudad: " + objDistribuidor.getCiudad()
                    + "\nPaís: " + objDistribuidor.getPais(), obtenPosicionX_Y(), 5);
        }
    }

    private boolean esNumerico(String cadena) {
        boolean respuesta = false;
        try {
            if (Double.parseDouble(cadena) > 0) {
                respuesta = true;
            }
            if (Double.parseDouble(cadena) > 99999) {
                respuesta = false;
                camposPendientes = camposPendientes + "Debe introducir un precio menor a 99999€\n";
            }
        } catch (NumberFormatException nfe) {
            return false;
        }
        return respuesta;
    }

    private LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

//este método obtiene la posición de la actual ventana en coordenadas x, y
    //vamos a usar estos datos para posicionar la ventana de mensajes en la pantalla correctamente
    private double[] obtenPosicionX_Y() {
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
