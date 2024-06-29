package ve.edu.unet.nodosAST;

public class NodoOperacionNot extends NodoBase {
    private NodoBase opDerecho;
    private tipoOp operacion;

    public NodoOperacionNot(tipoOp tipoOperacion, NodoBase opDerecho) {
        super();
        this.opDerecho = opDerecho;
        this.operacion = tipoOperacion;
    }

    public NodoOperacionNot(tipoOp tipoOperacion) {
        super();
        this.opDerecho = null;
        this.operacion = tipoOperacion;
    }

    public NodoBase getOpDerecho() {
        return opDerecho;
    }

    public void setOpDerecho(NodoBase opDerecho) {
        this.opDerecho = opDerecho;
    }

    public tipoOp getOperacionNot() {
        return operacion;
    }

}