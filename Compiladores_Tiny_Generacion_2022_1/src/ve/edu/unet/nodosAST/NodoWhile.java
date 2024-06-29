package ve.edu.unet.nodosAST;

public class NodoWhile extends NodoBase {
    private NodoBase expretion;
    private NodoBase cuerpo;

    public NodoWhile() {
        this.expretion = null;
        this.cuerpo = null;
    }

    public NodoWhile (NodoBase expretion,NodoBase cuerpo) {
        super();
        this.expretion = expretion;
        this.cuerpo = cuerpo;
    }

    public NodoBase getExpretion() { return this.expretion; }
    public void setExpretion(NodoBase expretion) { this.expretion = expretion; }
    public NodoBase getCuerpo() { return this.cuerpo; }
    public void setCuerpo(NodoBase cuerpo) { this.cuerpo = cuerpo; }
}
