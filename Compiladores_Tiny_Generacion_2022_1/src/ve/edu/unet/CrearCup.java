package ve.edu.unet;

import java.io.IOException;

import java_cup.internal_error;

public class CrearCup {

    public static void main(String[] args) throws internal_error, IOException, Exception {
        generarCup();

    }



    public static void generarCup() throws internal_error, IOException, Exception {
        String[] rutaCup = {"-parser","parser","C:/Development/Personal AJCU/UNET/proyecto-compi-2024/Compiladores_Tiny_Generacion_2022_1/src/especificacion/sintactico.cup"};
        java_cup.Main.main(rutaCup);
    }
}
