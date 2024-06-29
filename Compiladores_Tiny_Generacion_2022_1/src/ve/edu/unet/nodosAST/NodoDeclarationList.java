package ve.edu.unet.nodosAST;

public class NodoDeclarationList extends NodoBase {

    private NodoBase declarationList;

    public NodoDeclarationList(NodoBase declarationList) {
        super();
        this.declarationList = declarationList;
    }

    public NodoBase getDeclarationList() {
        return declarationList;
    }

    public void setDeclarationList(NodoBase declarationList) {
        this.declarationList = declarationList;
    }
}
