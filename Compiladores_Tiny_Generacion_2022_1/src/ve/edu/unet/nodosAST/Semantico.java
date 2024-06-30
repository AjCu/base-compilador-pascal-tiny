package ve.edu.unet.nodosAST;

import ve.edu.unet.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public class Semantico {
    private static TablaSimbolos tablaSimbolos = null;

    private static boolean declaration;
    public static List<NodoBase> identificadores = new ArrayList<>();

    public static void setTablaSimbolos(TablaSimbolos tabla) {
        tablaSimbolos = tabla;
    }

    public static void checkSemantico(NodoBase raiz) throws Exception {

        while (raiz != null) {

            if (raiz instanceof NodoDeclarationList) {
                checkSemantico(((NodoDeclarationList) raiz).getDeclarationList());

            } else if (raiz instanceof NodoDeclarationRow) {
                declaration = true;
                checkSemantico(((NodoDeclarationRow) raiz).getDeclarationRow());
                declaration = false;
            }if (raiz instanceof NodoIf) {

                checkLogicStatement(((NodoIf) raiz).getPrueba());
                checkSemantico(((NodoIf) raiz).getPrueba());
                checkSemantico(((NodoIf) raiz).getParteThen());

                if (((NodoIf) raiz).getParteElse() != null) {
                    checkSemantico(((NodoIf) raiz).getParteElse());
                }


            } else if (raiz instanceof NodoAsignacion) {
                checkAsignacion(raiz);

            } else if (raiz instanceof NodoOperacion) {
                //Caso de operacion base x+y
                checkValidOperation((NodoOperacion) raiz);

                checkSemantico(((NodoOperacion) raiz).getOpIzquierdo());
                checkSemantico(((NodoOperacion) raiz).getOpDerecho());

            } else if (raiz instanceof NodoOperacionNot) {

                checkCompatibility(tipoDec.booleano,((NodoOperacionNot) raiz).getOpDerecho(),"NOT");
                checkSemantico(((NodoOperacionNot) raiz).getOpDerecho());
            } else if (raiz instanceof NodoIdentificador) {
                checkDeclaration(raiz);
            }

            raiz = raiz.getHermanoDerecha();
        }
    }

    public static void checkDeclaration(NodoBase identificador) throws Exception {
        String variable = ((NodoIdentificador) identificador).getNombre();
        boolean containsKey = tablaSimbolos.ContainsKey(variable);
        if (!containsKey) {
            throw new Exception("ERROR: La variable '" + variable + "' no ha sido declarada.");
        }

        if(declaration){
            identificadores.add(identificador);
        }else{

            //actualizo identificador
            NodoIdentificador tmp = buscarIdentificador(identificadores,variable);
            ((NodoIdentificador) identificador).setIndiceInicio(tmp.getIndiceInicio());
            ((NodoIdentificador) identificador).setIndiceFin(tmp.getIndiceFin());
            ((NodoIdentificador) identificador).setValues(tmp.getValues());

        }

        //NodoVectorEntero asignacion
        if(((NodoIdentificador) identificador).getIndice() != null){
            checkArrayIndex(((NodoIdentificador) identificador),variable, identificadores);

        }
    }

    public static void checkArrayIndex(NodoIdentificador identificador, String name, List<NodoBase> arrayList) throws Exception {

        NodoBase index = identificador.getIndice();

        //Revisamos semantica del nodo indice
        checkSemantico(index);
        //Revisamos compatibilidad simulando una operacion con un entero
        checkCompatibility(tipoDec.entero,index,(name + "[index]"));

        if(!arrayList.isEmpty()){
            int indiceInicio, indiceFin, indiceCalculado;
            NodoIdentificador element = buscarIdentificador(arrayList,name);

            if (element != null){

                indiceInicio = element.getIndiceInicio();
                indiceFin = element.getIndiceFin();

                indiceCalculado = Util.calculateNodoValue(index,null);
                if (!(indiceCalculado >= indiceInicio && indiceCalculado <= indiceFin)) {
                    throw new Exception("Error: Indice del vector "+name+" no esta dentro del rango declarado");
                }

                identificador.setIndiceInicio(indiceInicio);
                identificador.setIndiceFin(indiceFin);

            }else{
                throw new Exception("Error: Problema desconocido con el vector " + name);
            }
        }

    }

    public static NodoIdentificador buscarIdentificador(List<NodoBase> lista, String nombre) {
        for (NodoBase nodoBase : lista) {

            NodoIdentificador element = (NodoIdentificador) nodoBase;

            if (element.getNombre().equals(nombre)) {
                return element;
            }
        }
        return null;
    }

    //Entra asi sea asignacion real o solo declaracion
    public static void checkAsignacion(NodoBase asignacion) throws Exception {
        NodoBase identificador = ((NodoAsignacion) asignacion).getIdentificador();
        NodoBase expresion = ((NodoAsignacion) asignacion).getExpresion();
        checkDeclaration(identificador);
        //Asignacion, cuando expresion es nula solo se esta declarando
        if (expresion != null) {

            checkSemantico(expresion);

            String variable = ((NodoIdentificador) identificador).getNombre();
            tipoDec tipo = tablaSimbolos.BuscarSimbolo(variable).getTipoDec();

            switch (tipo) {
                case entero: {
                    checkCompatibility(tipoDec.entero, expresion, variable);
                    break;
                }
                case booleano: {
                    checkCompatibility(tipoDec.booleano, expresion, variable);
                    break;
                }
            }


            //Asigno valor al identificador
            //vector
            if(((NodoIdentificador) identificador).getIndice() != null){

                ((NodoIdentificador) identificador).setValue(((NodoIdentificador) identificador).getIndice(),expresion);

            }else{ //variable simple

                ((NodoIdentificador) identificador).setValue(null,expresion);



            }

            if(!declaration){
                //actualizo el array de identificadores
                NodoIdentificador old = buscarIdentificador(identificadores,variable);
                identificadores.set(identificadores.indexOf(old), identificador);
            }
        }
    }

    //Compatibilidad de tipos
    public static void checkCompatibility(tipoDec type, NodoBase expresion, String variable) throws Exception {
        switch (type) {
            case entero:
                // x = 2
                if (expresion instanceof NodoValor)
                    return;

                //x = y+z
                else if (expresion instanceof NodoOperacion) {

                    tipoOp operacion = ((NodoOperacion) expresion).getOperacion();

                    if (!(operacion.equals(tipoOp.mas)
                        || operacion.equals(tipoOp.menos)
                        || operacion.equals(tipoOp.por)
                        || operacion.equals(tipoOp.entre)
                        || operacion.equals(tipoOp.igual)
                        || operacion.equals(tipoOp.menor)
                        || operacion.equals(tipoOp.mayor)
                        || operacion.equals(tipoOp.mod))) {
                        throw new Exception("Error: Operacion '" + operacion + "' no permitida sobre '" + variable + "' de tipo entero");
                    }
                }
                else if (expresion instanceof NodoIdentificador) {
                    String name = ((NodoIdentificador) expresion).getNombre();

                    checkDeclaration(expresion);

                    tipoDec tipo = tablaSimbolos.BuscarSimbolo(name).getTipoDec();

                    if(tipo != type){
                        throw new Exception("Error: incompatibilidad de variables, el valor adjunto a '" + variable + "' no es un entero");
                    }
                } else {
                    throw new Exception("Error: Operacion desconocida no permitida sobre " + variable);
                }
                break;

            case booleano:

                if (expresion instanceof NodoOperacionNot) {
                    return;
                }
                if (expresion instanceof NodoValor) {
                    int valor = ((NodoValor) expresion).getValor();
                    if (valor != 0 && valor != 1) {
                        throw new Exception("Error: El valor de la variable '" + variable + "' no es de tipo booleano (True/False)");
                    }
                    return;
                }else if (expresion instanceof NodoOperacion) {
                    tipoOp operacion = ((NodoOperacion) expresion).getOperacion();
                    if (!(operacion.equals(tipoOp.igual)
                        || operacion.equals(tipoOp.menor)
                        || operacion.equals(tipoOp.mayor)
                        || operacion.equals(tipoOp.and)
                        || operacion.equals(tipoOp.or))) {
                        throw new Exception("Error: Operacion '" + operacion + "' no permitida sobre '" + variable + "' de tipo booleano");
                    }
                } else if (expresion instanceof NodoIdentificador) {

                    checkDeclaration(expresion);
                    String name = ((NodoIdentificador) expresion).getNombre();
                    tipoDec tipo = tablaSimbolos.BuscarSimbolo(name).getTipoDec();

                    if(tipo != type){
                        throw new Exception("Error: incompatibilidad de variables, el valor adjunto a '" + variable + "' no es un booleano (True/False)");
                    }
                } else {
                    throw new Exception("Error: Operacion desconocida no permitida sobre booleano");
                }
                break;
            default:
                throw new Exception("Error: Operacion desconocida");
        }
    }

    public static void checkLogicStatement(NodoBase pruebaLogica) throws Exception {

        if (pruebaLogica instanceof NodoIdentificadorEntero) {
            throw new Exception("Error: La prueba no es de tipo booleano en if");
        } else if (pruebaLogica instanceof NodoValor) {
            int valor = ((NodoValor) pruebaLogica).getValor();
            //Manejamos los booleanos como nodoValor
            if (valor != 0 && valor != 1) {
                throw new Exception("Error: La prueba no es de tipo booleano (0/1) en if");
            }
        } else if (pruebaLogica instanceof NodoOperacion) {
            tipoOp operacion = ((NodoOperacion) pruebaLogica).getOperacion();
            if (operacion != tipoOp.igual &&
                operacion != tipoOp.menor &&
                operacion != tipoOp.mayor &&
                operacion != tipoOp.and &&
                operacion != tipoOp.or  &&
                operacion != tipoOp.mod) {
                throw new Exception("Error: Operacion '" + operacion + "' no permitida sobre IF");
            }
        } else {
            throw new Exception("Error: Operacion desconocida sobre IF");
        }
    }


    //y+x; y+2-4*x
    public static void checkValidOperation(NodoOperacion operation) throws Exception {
        NodoBase opIz = operation.getOpIzquierdo();
        NodoBase opDer = operation.getOpDerecho();

        if (opIz instanceof NodoIdentificador) {
            checkDeclaration(opIz);
            String variable = ((NodoIdentificador) opIz).getNombre();
            tipoDec tipo = tablaSimbolos.BuscarSimbolo(variable).getTipoDec();
            checkCompatibility(tipo, operation, variable);
        }

        if (opDer instanceof NodoIdentificador) {
            checkDeclaration(opDer);
            String variable = ((NodoIdentificador) opDer).getNombre();
            tipoDec tipo = tablaSimbolos.BuscarSimbolo(variable).getTipoDec();
            checkCompatibility(tipo, operation, variable);
        }
    }
}