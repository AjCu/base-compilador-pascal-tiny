package ve.edu.unet;

import ve.edu.unet.nodosAST.*;

import java.util.*;

public class TablaSimbolos {
	private HashMap<String, RegistroSimbolo> tabla;
	private int direccion;  //Contador de las localidades de memoria asignadas a la tabla
	
	public TablaSimbolos() {
		super();
		tabla = new HashMap<String, RegistroSimbolo>();
		direccion=0;
	}

	public void cargarTabla(NodoBase raiz){
		while (raiz != null) {
	    if (raiz instanceof NodoIdentificador){
			if (raiz instanceof NodoIdentificadorEntero) {

				NodoIdentificadorEntero tmp = (NodoIdentificadorEntero)raiz;

				int size = (tmp.getIndiceFin() - tmp.getIndiceInicio() + 1);
//				int size = 1;
//				if(tmp.getIndiceFin() > 0){
//					size += tmp.getIndiceFin();
//				}

				InsertarSimbolo(((NodoIdentificador) raiz).getNombre(), -1, tipoDec.entero,size);
			}

			if (raiz instanceof NodoIdentificadorBooleano) {

				InsertarSimbolo(((NodoIdentificador) raiz).getNombre(), -1, tipoDec.booleano, 1);
			}


			//Asigno tamano a el vector values del identificador
			((NodoIdentificador) raiz).setValuesSize(((NodoIdentificador) raiz).getRealSize());

		}

		if (raiz instanceof NodoDeclarationList) {
			cargarTabla(((NodoDeclarationList) raiz).getDeclarationList());
		}

		if (raiz instanceof NodoDeclarationRow) {

			if (((NodoDeclarationRow) raiz).getDeclarationRow() instanceof NodoAsignacion){

				NodoAsignacion nodoHijo = (NodoAsignacion) ((NodoDeclarationRow) raiz).getDeclarationRow();

				while (nodoHijo != null){

					//Evaluamos si el NodoDeclarationRow tiene un valor (NodoBase) asociado, es decir, si hubo asignacion,
					//si es asi modificamos nodo expresion del NodoAsignacion y le pasamos ese nodo
					//Si no hay asignacion si no solo declaracion, no va a entrar y por lo tanto el nodo expresion
					//quedara nulo

					NodoIdentificador identificador = (NodoIdentificador)(nodoHijo.getIdentificador());

					//Solo si tengo valor para la asignacion y son variables simples
					if (((NodoDeclarationRow) raiz).getValue() != null && identificador.getIndiceFin() <= 0){
						nodoHijo.setExpresion((NodoBase)((NodoDeclarationRow) raiz).getValue());

					}

					String nombre = (identificador).getNombre();

					NodoBase indice = (identificador).getIndice();

					if(((NodoDeclarationRow) raiz).getType() == tipoDec.entero){

						nodoHijo.setIdentificador(
							new NodoIdentificadorEntero(
									nombre,
									((NodoDeclarationRow) raiz).getIndiceInicio(),
									((NodoDeclarationRow) raiz).getIndiceFin(),
									indice
							)
						);


					} else if (((NodoDeclarationRow) raiz).getType() == tipoDec.booleano) {

						nodoHijo.setIdentificador(new NodoIdentificadorBooleano(nombre));
					}
					cargarTabla(((NodoDeclarationRow) raiz).getDeclarationRow());
					nodoHijo = (NodoAsignacion) nodoHijo.getHermanoDerecha();
				}

			}
		}

	    /* Hago el recorrido recursivo */
	    if (raiz instanceof  NodoIf){
	    	cargarTabla(((NodoIf)raiz).getPrueba());
	    	cargarTabla(((NodoIf)raiz).getParteThen());
	    	if(((NodoIf)raiz).getParteElse()!=null){
	    		cargarTabla(((NodoIf)raiz).getParteElse());
	    	}
	    }
	    else if (raiz instanceof  NodoRepeat){
	    	cargarTabla(((NodoRepeat)raiz).getCuerpo());
	    	cargarTabla(((NodoRepeat)raiz).getPrueba());
	    }
	    else if (raiz instanceof  NodoAsignacion) {
			cargarTabla(((NodoAsignacion) raiz).getIdentificador());
			if(((NodoAsignacion) raiz).getExpresion() != null) {
				cargarTabla(((NodoAsignacion) raiz).getExpresion());
			}
		}
	    else if (raiz instanceof  NodoEscribir)
	    	cargarTabla(((NodoEscribir)raiz).getExpresion());
	    else if (raiz instanceof NodoOperacion){
	    	cargarTabla(((NodoOperacion)raiz).getOpIzquierdo());
	    	cargarTabla(((NodoOperacion)raiz).getOpDerecho());
	    }
	    raiz = raiz.getHermanoDerecha();
	  }
	}
	
	//true es nuevo no existe se insertara, false ya existe NO se vuelve a insertar 
	public boolean InsertarSimbolo(String identificador, int numLinea, tipoDec tipo, int size){
		RegistroSimbolo simbolo;
		if(tabla.containsKey(identificador)){
			return false;
		}else{
			simbolo= new RegistroSimbolo(identificador,numLinea,direccion, tipo);
			tabla.put(identificador,simbolo);
			direccion += size;
			return true;			
		}
	}
	
	public RegistroSimbolo BuscarSimbolo(String identificador){
		RegistroSimbolo simbolo=(RegistroSimbolo)tabla.get(identificador);
		return simbolo;
	}

	public boolean ContainsKey(String Clave) {
		return tabla.containsKey(Clave);
	}

	public void ImprimirClaves(){
		System.out.println("*** Tabla de Simbolos ***");
		for( Iterator <String>it = tabla.keySet().iterator(); it.hasNext();) { 
            String s = (String)it.next();
	    	System.out.println("Consegui Key: "+ s +" de tipo " + BuscarSimbolo(s).getTipoDec() +" con direccion: " + BuscarSimbolo(s).getDireccionMemoria());
		}
	}

	public int getDireccion(String Clave){
		return BuscarSimbolo(Clave).getDireccionMemoria();
	}
	
	/*
	 * TODO:
	 * 1. Crear lista con las lineas de codigo donde la variable es usada.
	 * */
}
