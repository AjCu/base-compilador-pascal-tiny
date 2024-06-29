package ve.edu.unet.nodosAST;

public class Util {
	
	static int sangria = 0;
	
	//Imprimo en modo texto con sangrias el AST
	public static void imprimirAST(NodoBase raiz){
		  sangria+=2;
		  while (raiz != null) {
		    printSpaces();
		    if (raiz instanceof  NodoIf)
		    	System.out.println("If");
		    else if (raiz instanceof  NodoRepeat)
		    	System.out.println("Repeat");
		    
		    else if (raiz instanceof  NodoAsignacion)
		    	System.out.println("Asignacion a: "+((NodoAsignacion) raiz).getIdentificador());

		    else if (raiz instanceof  NodoLeer)  
		    	System.out.println("Lectura: "+((NodoLeer)raiz).getIdentificador());

		    else if (raiz instanceof  NodoEscribir)
		    	System.out.println("Escribir");

			else if (raiz instanceof  NodoDeclarationList) {
				System.out.println("Declaraciones: " + ((NodoDeclarationList) raiz).getDeclarationList());
				imprimirNodo(((NodoDeclarationList) raiz).getDeclarationList());
			}
			else if (raiz instanceof NodoDeclarationRow) {
				String tipo = "";

				if (((NodoDeclarationRow) raiz).getType() == tipoDec.entero) {
					tipo = "Entero";
				}

				if (((NodoDeclarationRow) raiz).getType() == tipoDec.booleano) {
					tipo = "Booleano";
				}

				System.out.println("Declaracion de variables tipo " + tipo);
				imprimirNodo(((NodoDeclarationRow) raiz).getDeclarationRow());
			}

			else if (raiz instanceof NodoOperacion
		    		|| raiz instanceof NodoValor
		    		|| raiz instanceof NodoIdentificador
					|| raiz instanceof NodoOperacionNot)
		    	imprimirNodo(raiz);
		    else System.out.println("Tipo de nodo desconocido");;
		    
		    /* Hago el recorrido recursivo */
		    if (raiz instanceof  NodoIf){
		    	printSpaces();
		    	System.out.println("**Prueba IF**");
		    	imprimirAST(((NodoIf)raiz).getPrueba());
		    	printSpaces();
		    	System.out.println("**Then IF**");
		    	imprimirAST(((NodoIf)raiz).getParteThen());
		    	if(((NodoIf)raiz).getParteElse()!=null){
		    		printSpaces();
		    		System.out.println("**Else IF**");
		    		imprimirAST(((NodoIf)raiz).getParteElse());
		    	}
		    }
		    else if (raiz instanceof  NodoRepeat){
		    	printSpaces();
		    	System.out.println("**Cuerpo REPEAT**");
		    	imprimirAST(((NodoRepeat)raiz).getCuerpo());
		    	printSpaces();
		    	System.out.println("**Prueba REPEAT**");
		    	imprimirAST(((NodoRepeat)raiz).getPrueba());
		    }
		    else if (raiz instanceof  NodoAsignacion)
		    	imprimirAST(((NodoAsignacion)raiz).getExpresion());
		    else if (raiz instanceof  NodoEscribir)
		    	imprimirAST(((NodoEscribir)raiz).getExpresion());
		    else if (raiz instanceof NodoOperacion){
		    	printSpaces();
		    	System.out.println("**Expr Izquierda Operacion**");
		    	imprimirAST(((NodoOperacion)raiz).getOpIzquierdo());
		    	printSpaces();
		    	System.out.println("**Expr Derecha Operacion**");		    	
		    	imprimirAST(((NodoOperacion)raiz).getOpDerecho());
		    }
		    raiz = raiz.getHermanoDerecha();
		  }
		  sangria-=2;
		}

/* Imprime espacios con sangria */
static void printSpaces()
{ int i;
  for (i=0;i<sangria;i++)
	  System.out.print(" ");
}

/* Imprime informacion de los nodos */
static void imprimirNodo( NodoBase raiz )
{
	if(	raiz instanceof NodoRepeat
		||	raiz instanceof NodoLeer
		||	raiz instanceof NodoEscribir  ){
		System.out.println("palabra reservada: "+ raiz.getClass().getName());
	}
	
	if(	raiz instanceof NodoAsignacion )
		System.out.println(":=");
	
	if(	raiz instanceof NodoOperacion ){
		tipoOp sel=((NodoOperacion) raiz).getOperacion();
		if(sel==tipoOp.menor)
			System.out.println("<"); 
		if(sel==tipoOp.igual)
			System.out.println("=");
		if(sel==tipoOp.mas)
			System.out.println("+");
		if(sel==tipoOp.menos)
			System.out.println("-");
		if(sel==tipoOp.por)
			System.out.println("*");
		if(sel==tipoOp.entre)
			System.out.println("/");
	}

	if(	raiz instanceof NodoValor ){
		System.out.println("NUM, val= "+ ((NodoValor)raiz).getValor());
	}

	if(	raiz instanceof NodoIdentificador ){
		System.out.println("ID, nombre= "+ ((NodoIdentificador)raiz).getNombre());
	}

}

	public static int calculateNodoValue(NodoBase raiz, NodoBase index){

		if(raiz instanceof NodoOperacion){
			NodoOperacion nodo = (NodoOperacion)raiz;
			NodoBase opIzquierdo = nodo.getOpIzquierdo();
			NodoBase opDerecho = nodo.getOpDerecho();
			tipoOp operacion = nodo.getOperacion();

			int valorIz = 0, valorDer = 0;
			if (opIzquierdo instanceof NodoOperacion || opIzquierdo instanceof NodoIdentificador){
				valorIz = calculateNodoValue(opIzquierdo,index);
			} else if(opIzquierdo instanceof NodoValor){
				valorIz = ((NodoValor) opIzquierdo).getValor();
			}

			if (opDerecho instanceof NodoOperacion || opDerecho instanceof NodoIdentificador){
				valorDer = calculateNodoValue(opDerecho,index);
			}else if(opDerecho instanceof NodoValor){
				valorDer = ((NodoValor) opDerecho).getValor();
			}

			switch (operacion) {
				case mas:

					return valorIz + valorDer;
				case menos:
					return valorIz - valorDer;

				case por:
					return valorIz * valorDer;

				case entre:
					return valorIz / valorDer;

				case mod:
					return valorIz % valorDer;

				default:
					System.out.println("Opción no reconocida");
			}

			return 0;
		}else if(raiz instanceof NodoIdentificador){
			return calculateNodoValue(((NodoIdentificador) raiz).getValue(index), ((NodoIdentificador) raiz).getIndice());
		}else if(raiz instanceof NodoValor){
			return ((NodoValor) raiz).getValor();
		}

		return 0;
	}

}
