package ve.edu.unet.nodosAST;

public class NodoDeclarationRow extends NodoBase {

    private tipoDec type;
    private NodoBase declarationRow;

    private NodoBase value;

    private int indiceInicio, indiceFin;

    public NodoDeclarationRow(tipoDec type, NodoBase declarationRow) {
        super();
        this.type = type;
        this.declarationRow = declarationRow;
        this.value = null;
        this.indiceInicio = 0;
        this.indiceFin = 0;
    }

    public NodoDeclarationRow(tipoDec type, NodoBase declarationRow, NodoBase value) {
        super();
        this.type = type;
        this.declarationRow = declarationRow;
        this.value = value;
        this.indiceInicio = 0;
        this.indiceFin = 0;
    }

    public NodoDeclarationRow(tipoDec type, NodoBase declarationRow, int indiceInicio, int indiceFin) {
        super();
        this.type = type;
        this.declarationRow = declarationRow;
        this.value = null;
        this.indiceInicio = indiceInicio;
        this.indiceFin = indiceFin;
    }

    public tipoDec getType() {
        return type;
    }

    public void setType(tipoDec type) {
        this.type = type;
    }

    public NodoBase getDeclarationRow() {
        return declarationRow;
    }

    public NodoBase getValue() {
        return value;
    }

    public void setDeclarationRow(NodoBase declarationRow) {
        this.declarationRow = declarationRow;
    }

    public int getIndiceInicio() {
        return indiceInicio;
    }

    public int getIndiceFin() {
        return indiceFin;
    }
}
