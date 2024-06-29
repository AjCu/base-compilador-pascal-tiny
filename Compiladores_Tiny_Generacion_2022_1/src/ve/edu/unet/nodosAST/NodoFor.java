package ve.edu.unet.nodosAST;

public class NodoFor extends NodoBase {

    private NodoBase condition;
    private NodoBase final_value;
    private NodoBase cuerpo;

    public NodoFor() {
        super();
        this.condition = null;
        this.final_value = null;
        this.cuerpo = null;
    }
    public NodoFor(NodoBase condition, NodoBase final_value, NodoBase cuerpo) {
        super();
        this.condition = condition;
        this.final_value = final_value;
        this.cuerpo = cuerpo;
    }


    public NodoBase getCondition() { return this.condition; }
    public void setCondition(NodoBase condition) {
        this.condition = condition;
    }
    public NodoBase getFinalValue() {
        return this.final_value;
    }
    public void setFinalValue(NodoBase FinalValue) {
        this.final_value = final_value;
    }
    public NodoBase getCuerpo() {
        return this.cuerpo;
    }
    public void setCuerpo(NodoBase cuerpo) { this.cuerpo = cuerpo; }
}
