package modelo;

import java.awt.Dimension;
import java.awt.Toolkit;

public class variablesPantalla {

    public static int valorX;
    public static int resolucionX;
    public static int resolucionY;
    public static int max_X;
    public static int max_Y;

    public static String sistemaOperativo() {
        String sSistemaOperativo = System.getProperty("os.name");
        System.out.println(sSistemaOperativo);
        return sSistemaOperativo;
    }

    public static void calculaResolucionPantalla() {
        Toolkit t = Toolkit.getDefaultToolkit();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        System.out.println("Tu resolución es de " + screenSize.width + "x" + screenSize.height);
        variablesPantalla.resolucionX = (int) screenSize.width;
        variablesPantalla.resolucionY = (int) screenSize.height;

        if (variablesPantalla.resolucionX > 3800) {
            max_X = 2200;
            max_Y = 1200;
            System.out.println("resolución 4K");
        } else if (variablesPantalla.resolucionX > 2500) {
            max_X = 1800;
            max_Y = 1000;
            System.out.println("resolución 2K");
        } else if (variablesPantalla.resolucionX > 1800) {
            max_X = 1500;
            max_Y = 800;
            System.out.println("resolución FullHD");
        } else {
            max_X = 1240;
            max_Y = 640;
            System.out.println("resolución HD");
        }
    }
}
