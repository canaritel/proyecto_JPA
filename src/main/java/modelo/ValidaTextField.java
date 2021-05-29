package modelo;

import javafx.scene.control.TextField;

public class ValidaTextField {

    //Método para campos de tipo String, convertiendo a mayúscula y comprobando tamaño
    public static void textoString(String caracter, TextField txtCampo, int tamanio) {
        if (" ".equals(caracter) && txtCampo.getLength() <= 1) {  //no permitimos espacios en blanco
            txtCampo.deletePreviousChar(); //borramos si el caractere es un espacio en blanco
        }
        char palabra = caracter.charAt(0);       //convertimos a tipo char el caracter String
        if (txtCampo.getLength() > tamanio) {    //comprobamos el tamaño del textfield es mayor al tamaño permitido
            txtCampo.deletePreviousChar();       //eliminamos el último caracter
            txtCampo.end();                     //nos posicionamos al final del textfield
        } else if (palabra >= 'a') {            //si el caracter palabra es mayor/igual a 'a' es que es una letra del alfabeto
            // caracter = caracter.toUpperCase();  //la convertimos a mayúscula
            // txtCampo.deletePreviousChar();      //eliminamos el último caracter
            // txtCampo.setText(txtCampo.getText() + caracter);  //añadimos el texfield el caracter en mayúscula
            // txtCampo.end();
        }
    }

    //Método para campos de tipo Int, permitiendo solo números y comprobando tamaño, sin permitir espacios en blanco
    public static void textoInt(String caracter, TextField txtCampo, int tamanio) {
        if (" ".equals(caracter)) {  //no permitimos espacios en blanco
            txtCampo.deletePreviousChar(); //borramos si el caractere es un espacio en blanco
        } else {
            char palabra = caracter.charAt(0);                      //convertimos a tipo char el caracter String
            if (txtCampo.getLength() > tamanio) {                   //comprobamos el tamaño del textfield es mayor al tamaño permitido
                txtCampo.deletePreviousChar();                      //eliminamos el último caracter
                txtCampo.end();
            } else if (palabra >= '0' && palabra <= '9') {          //si el caracter palabra es mayor/igual a 0 y menor/igual a 9  es que es un número
                txtCampo.deletePreviousChar();                      //eliminamos el último caracter
                txtCampo.setText(txtCampo.getText() + caracter);    //añadimos el texfield el caracter con el número
                txtCampo.end();
            } else if (palabra >= 'a' || palabra >= 'A') {          //si el caracter palabra el igual/mayor 'a' o 'A' es que es una letra
                txtCampo.deletePreviousChar();                      //eliminamos el último caracter
                txtCampo.end();
            } else if (palabra == '.') {
                txtCampo.end();
            }
        }
    }

}
