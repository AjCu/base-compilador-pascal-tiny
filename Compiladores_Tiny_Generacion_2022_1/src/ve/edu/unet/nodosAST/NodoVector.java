package ve.edu.unet.nodosAST;

public class NodoVector extends NodoBase{
    private NodoBase nombre;
    private NodoBase inicio;
    private NodoBase fin;
    private tipoDec tipo;

    public NodoVector(NodoBase nombre, NodoBase inicio, NodoBase fin, tipoDec tipo) {
        super();
        this.nombre = nombre;
        this.inicio = inicio;
        this.fin = fin;
        this.tipo = tipo;
    }

    public NodoBase getNombre() {
        return nombre;
    }

    public void setNombre(NodoBase nombre) {
        this.nombre = nombre;
    }

    public NodoBase getInicio() {
        return inicio;
    }

    public void setInicio(NodoBase inicio) {
        this.inicio = inicio;
    }

    public NodoBase getFin() {
        return fin;
    }

    public void setFin(NodoBase fin) {
        this.fin = fin;
    }

    public tipoDec getTipo() {
        return tipo;
    }

    public void setTipo(tipoDec tipo) {
        this.tipo = tipo;
    }
}
