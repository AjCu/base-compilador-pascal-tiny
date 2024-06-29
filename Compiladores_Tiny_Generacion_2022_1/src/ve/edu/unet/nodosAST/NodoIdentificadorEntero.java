package ve.edu.unet.nodosAST;

public class NodoIdentificadorEntero extends NodoIdentificador {

    public NodoIdentificadorEntero(String nombre) {
        super(nombre);
    }

    public NodoIdentificadorEntero(String nombre, int indiceInicio, int indiceFin, NodoBase indice) {

        super(nombre, indiceInicio, indiceFin, indice);
    }

    public NodoIdentificadorEntero() {
    }

}
