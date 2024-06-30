package ve.edu.unet;

import java.io.File;

public class CrearLexico {

    public static void main(String[] args) {
        String rutaFlexFile = "C:/Development/Personal AJCU/UNET/proyecto-compi-2024/Compiladores_Tiny_Generacion_2022_1/src/especificacion/lexico.flex";
        generarLexer(rutaFlexFile);

    }

    public static void generarLexer(String ruta) {
        File file = new File(ruta);
        JFlex.Main.generate(file);
    }
}
