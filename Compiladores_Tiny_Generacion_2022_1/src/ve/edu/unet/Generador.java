package ve.edu.unet;

import ve.edu.unet.nodosAST.*;

public class Generador {
	/* Ilustracion de la disposicion de la memoria en
	 * este ambiente de ejecucion para el lenguaje Tiny
	 *
	 * |t1	|<- mp (Maxima posicion de memoria de la TM
	 * |t1	|<- desplazamientoTmp (tope actual)
	 * |free|
	 * |free|
	 * |...	|
	 * |x	|
	 * |y	|<- gp
	 * 
	 * */
	
	
	
	/* desplazamientoTmp es una variable inicializada en 0
	 * y empleada como el desplazamiento de la siguiente localidad
	 * temporal disponible desde la parte superior o tope de la memoria
	 * (la que apunta el registro MP).
	 * 
	 * - Se decrementa (desplazamientoTmp--) despues de cada almacenamiento y
	 * 
	 * - Se incrementa (desplazamientoTmp++) despues de cada eliminacion/carga en 
	 *   otra variable de un valor de la pila.
	 * 
	 * Pudiendose ver como el apuntador hacia el tope de la pila temporal
	 * y las llamadas a la funcion emitirRM corresponden a una inserccion 
	 * y extraccion de esta pila
	 */
	private static int desplazamientoTmp = 0;
	private static TablaSimbolos tablaSimbolos = null;
	
	public static void setTablaSimbolos(TablaSimbolos tabla){
		tablaSimbolos = tabla;
	}
	
	public static void generarCodigoObjeto(NodoBase raiz){
		System.out.println();
		System.out.println();
		System.out.println("------ CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA LA TM ------");
		System.out.println();
		System.out.println();
		generarPreludioEstandar(); //borra la posicion 0 de la direccion de memoria, el 1023 lo deja en 0
		generar(raiz);
		/*Genero el codigo de finalizacion de ejecucion del codigo*/   
		UtGen.emitirComentario("Fin de la ejecucion.");
		UtGen.emitirRO("HALT", 0, 0, 0, "");
		System.out.println();
		System.out.println();
		System.out.println("------ FIN DEL CODIGO OBJETO DEL LENGUAJE TINY GENERADO PARA LA TM ------");
	}
	
	//Funcion principal de generacion de codigo
	//prerequisito: Fijar la tabla de simbolos antes de generar el codigo objeto 
	private static void generar(NodoBase nodo){
	if(tablaSimbolos!=null){
		if (nodo instanceof  NodoIf){
			generarIf(nodo);
		}else if (nodo instanceof  NodoRepeat){
			generarRepeat(nodo);
		}else if (nodo instanceof  NodoAsignacion){
			generarAsignacion(nodo);
		}else if (nodo instanceof  NodoLeer){
			generarLeer(nodo);
		}else if (nodo instanceof  NodoEscribir){
			generarEscribir(nodo);
		}else if (nodo instanceof NodoValor){
			generarValor(nodo);
		}else if (nodo instanceof NodoIdentificador){
			generarIdentificador(nodo);
		}else if (nodo instanceof NodoOperacion) {
			generarOperacion(nodo);
		}else if (nodo instanceof NodoFor){
			generarFor(nodo);
		}else if (nodo instanceof NodoWhile){
			generarWhile(nodo);
		}else if (nodo instanceof NodoOperacionNot) {
			generarOperacionNot(nodo);
		}else if (nodo instanceof NodoDeclarationRow){
			generar(((NodoDeclarationRow) nodo).getDeclarationRow());
		}else if (nodo instanceof NodoDeclarationList){
			generar(((NodoDeclarationList) nodo).getDeclarationList());
		}else{
			System.out.println("BUG: Tipo de nodo a generar desconocido");
		}
		/*Si el hijo de extrema izquierda tiene hermano a la derecha lo genero tambien*/
		if(nodo.TieneHermano())
			generar(nodo.getHermanoDerecha());
	}else
		System.out.println("���ERROR: por favor fije la tabla de simbolos a usar antes de generar codigo objeto!!!");
}

	private static void generarIf(NodoBase nodo){
    	NodoIf n = (NodoIf)nodo;
		int localidadSaltoElse,localidadSaltoEnd,localidadActual;
		if(UtGen.debug)	UtGen.emitirComentario("-> if");
		/*Genero el codigo para la parte de prueba del IF*/
		generar(n.getPrueba());
		localidadSaltoElse = UtGen.emitirSalto(1);
		UtGen.emitirComentario("If: el salto hacia el else debe estar aqui");
		/*Genero la parte THEN*/
		generar(n.getParteThen());
		localidadSaltoEnd = UtGen.emitirSalto(1);
		UtGen.emitirComentario("If: el salto hacia el final debe estar aqui");
		localidadActual = UtGen.emitirSalto(0);
		UtGen.cargarRespaldo(localidadSaltoElse);
		UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadActual, "if: jmp hacia else");
		UtGen.restaurarRespaldo();
		/*Genero la parte ELSE*/
		if(n.getParteElse()!=null){
			generar(n.getParteElse());
    	}
		localidadActual = UtGen.emitirSalto(0);
		UtGen.cargarRespaldo(localidadSaltoEnd);
		UtGen.emitirRM_Abs("LDA", UtGen.PC, localidadActual, "if: jmp hacia el final");
		UtGen.restaurarRespaldo();
		
		if(UtGen.debug)	UtGen.emitirComentario("<- if");
	}
	
	private static void generarRepeat(NodoBase nodo){
    	NodoRepeat n = (NodoRepeat)nodo;
		int localidadSaltoInicio;
		if(UtGen.debug)	UtGen.emitirComentario("-> repeat");
			localidadSaltoInicio = UtGen.emitirSalto(0);
			UtGen.emitirComentario("repeat: el salto hacia el final (luego del cuerpo) del repeat debe estar aqui");
			/* Genero el cuerpo del repeat */
			generar(n.getCuerpo());
			/* Genero el codigo de la prueba del repeat */
			generar(n.getPrueba());
			UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadSaltoInicio, "repeat: jmp hacia el inicio del cuerpo");
		if(UtGen.debug)	UtGen.emitirComentario("<- repeat");
	}

	private static void generarFor(NodoBase nodo){

		NodoRepeat n = (NodoRepeat)nodo;
		int localidadSaltoInicio;
		int localidadSaltoFinal;
		int localidadSaltoActual;

		if(UtGen.debug)	UtGen.emitirComentario("-> for");

		if(UtGen.debug)	UtGen.emitirComentario("<- for");





		if(UtGen.debug)	UtGen.emitirComentario("-> repeat");
		localidadSaltoInicio = UtGen.emitirSalto(0);
		UtGen.emitirComentario("repeat: el salto hacia el final (luego del cuerpo) del repeat debe estar aqui");
		/* Genero el cuerpo del repeat */
		generar(n.getCuerpo());
		/* Genero el codigo de la prueba del repeat */
		generar(n.getPrueba());
		UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadSaltoInicio, "repeat: jmp hacia el inicio del cuerpo");
		if(UtGen.debug)	UtGen.emitirComentario("<- repeat");
	}

	private static void generarWhile(NodoBase nodo){

		NodoWhile n = (NodoWhile) nodo;
		int localidadInicio, localidadFin, localidadActual;
		if (UtGen.debug) UtGen.emitirComentario("While");

		localidadInicio = UtGen.emitirSalto(0);
		if (UtGen.debug) UtGen.emitirComentario("a generar condicion");
		generar(n.getExpretion());

		localidadFin = UtGen.emitirSalto(1);

		if (UtGen.debug) UtGen.emitirComentario("A generar Cuerpo");
		generar(n.getCuerpo());
		localidadActual = UtGen.emitirSalto(0);

		UtGen.emitirRM_Abs("LDA", UtGen.PC, localidadInicio, "Salto a la evaluacion de condicion del while");
		UtGen.cargarRespaldo(localidadFin);
		UtGen.emitirRM_Abs("JEQ", UtGen.AC, localidadActual + 1, "While Salto hacia Fuera del cuerpo");
		UtGen.restaurarRespaldo();

	}
	
	private static void generarAsignacion(NodoBase nodo){
		NodoAsignacion n = (NodoAsignacion)nodo;
		if(n.getExpresion() != null) {

			if(((NodoIdentificador) n.getIdentificador()).getIndice() != null) {

				if(UtGen.debug)	UtGen.emitirComentario("-> asignacion vector");

//			3: IN 0,0,0      * Reg[0] = valor teclado
				int direccionBaseVector, direccionExpresion;
				direccionBaseVector = tablaSimbolos.getDireccion(((NodoIdentificador) n.getIdentificador()).getNombre());

//			4: LDA 3,20(5)   * Reg[3] = 20+0(&base) = 20  (cargo base)
				UtGen.emitirRM("LDA", UtGen.AC2, direccionBaseVector, UtGen.GP, "asignacion vector: cargo direccion base de " + ((NodoIdentificador) n.getIdentificador()).getNombre());

//			*cargo el indice
//			5: LDC 2,1(0)    * Reg[2] = 1
				//Cargo indice en Registro 0
				generar(((NodoIdentificador) n.getIdentificador()).getIndice());

				//Correccion de indice



//			6: ADD 3,3,2     * r[3]=r[3] (direccion base vector)+r[2] (indice)
				UtGen.emitirRO("ADD", UtGen.AC2, UtGen.AC2, UtGen.AC, "asignacion vector: sumo direccion base vector + indice");

				UtGen.emitirRM("LDC", UtGen.AC1, ((NodoIdentificador) n.getIdentificador()).getIndiceInicio(), UtGen.GP, "identificador vector: cargar constante: "+((NodoIdentificador) n.getIdentificador()).getIndiceInicio());

				UtGen.emitirRO("SUB", UtGen.AC2, UtGen.AC2, UtGen.AC1, "identificador vector: resto indice - indiceInicio");


				/* Genero el codigo para la expresion a la derecha de la asignacion */
				generar(n.getExpresion());
				/* Ahora almaceno el valor resultante */
				direccionExpresion = tablaSimbolos.getDireccion(((NodoIdentificador) n.getIdentificador()).getNombre());

//			7: ST 0,0(3)     * dMem[(Reg[3]+0)] = Reg[0]
				UtGen.emitirRM("ST", UtGen.AC, 0, UtGen.AC2, "asignacion vector: almaceno el valor en "+((NodoIdentificador) n.getIdentificador()).getNombre() );

				if(UtGen.debug)	UtGen.emitirComentario("<- asignacion vector");

			}else{
				int direccion;
				if(UtGen.debug)	UtGen.emitirComentario("-> asignacion");
				/* Genero el codigo para la expresion a la derecha de la asignacion */
				generar(n.getExpresion());
				/* Ahora almaceno el valor resultante */
				direccion = tablaSimbolos.getDireccion(((NodoIdentificador) n.getIdentificador()).getNombre());

				UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "asignacion: almaceno el valor para el id "+((NodoIdentificador) n.getIdentificador()).getNombre());
				if(UtGen.debug)	UtGen.emitirComentario("<- asignacion");
			}

		} else {
			UtGen.emitirComentario("declaracion de " + ((NodoIdentificador) n.getIdentificador()).getNombre());
		}
	}
	
	private static void generarLeer(NodoBase nodo){
		NodoLeer n = (NodoLeer)nodo;
		int direccion;
		if(UtGen.debug)	UtGen.emitirComentario("-> leer");
		UtGen.emitirRO("IN", UtGen.AC, 0, 0, "leer: lee un valor entero ");
		direccion = tablaSimbolos.getDireccion(n.getIdentificador());
		UtGen.emitirRM("ST", UtGen.AC, direccion, UtGen.GP, "leer: almaceno el valor entero leido en el id "+n.getIdentificador());
		if(UtGen.debug)	UtGen.emitirComentario("<- leer");
	}
	
	private static void generarEscribir(NodoBase nodo){
		NodoEscribir n = (NodoEscribir)nodo;
		if(UtGen.debug)	UtGen.emitirComentario("-> escribir");
		/* Genero el codigo de la expresion que va a ser escrita en pantalla */
		generar(n.getExpresion());
		/* Ahora genero la salida */
		UtGen.emitirRO("OUT", UtGen.AC, 0, 0, "escribir: genero la salida de la expresion");
		if(UtGen.debug)	UtGen.emitirComentario("<- escribir");
	}
	
	private static void generarValor(NodoBase nodo){
    	NodoValor n = (NodoValor)nodo;
    	if(UtGen.debug)	UtGen.emitirComentario("-> constante");
    	UtGen.emitirRM("LDC", UtGen.AC, n.getValor(), 0, "cargar constante: "+n.getValor());
    	if(UtGen.debug)	UtGen.emitirComentario("<- constante");
	}
	
	private static void generarIdentificador(NodoBase nodo){
		NodoIdentificador n = (NodoIdentificador)nodo;

		if(n.getIndice() != null) {
			if(UtGen.debug)	UtGen.emitirComentario("-> identificador vector");

			int direccionBaseVector, direccionExpresion;
			direccionBaseVector = tablaSimbolos.getDireccion( n.getNombre());

//			8: LDA 3,20(5)   * Reg[3] = 20+0(&base) = 20  (cargo base)
			UtGen.emitirRM("LDA", UtGen.AC2, direccionBaseVector, UtGen.GP, "identificador vector: cargo direccion base de " + n.getNombre());

//			*cargo el indice
//			9: LDC 2,0(0)    * Reg[2] = 0
			//Cargo indice en Registro 0
			generar(n.getIndice());

//			10: ADD 3,3,2    * r[3]=r[3] (direccion base vector)+r[2] (indice)
			UtGen.emitirRO("ADD", UtGen.AC2, UtGen.AC2, UtGen.AC, "identificador vector: sumo direccion base vector + indice");
//
//			UtGen.emitirRM("LDC", UtGen.AC1, n.getIndiceInicio(), UtGen.GP, "identificador vector: cargar constante: "+n.getIndiceInicio());
//
//			UtGen.emitirRO("SUB", UtGen.AC2, UtGen.AC2, UtGen.AC1, "identificador vector: resto indice - indiceInicio");

//			11: LD 0,0(3)    * r[0]=dMem[(reg[3]+0)]
			UtGen.emitirRM("LD", UtGen.AC, 0, UtGen.AC2, "cargar valor de identificador: "+n.getNombre());

			if(UtGen.debug)	UtGen.emitirComentario("<- identificador vector");

		}else{
			int direccion;
			if(UtGen.debug)	UtGen.emitirComentario("-> identificador");
			direccion = tablaSimbolos.getDireccion(n.getNombre());// si no existe daria un error semantico, aqui se debe agregar la regla semantica
			UtGen.emitirRM("LD", UtGen.AC, direccion, UtGen.GP, "cargar valor de identificador: "+n.getNombre());
			if(UtGen.debug)	UtGen.emitirComentario("-> identificador");
		}

	}

	private static void generarOperacionNot(NodoBase nodo) {
		NodoOperacionNot n = (NodoOperacionNot) nodo;

		if (UtGen.debug) UtGen.emitirComentario("-> Operacion: " + n.getOperacionNot());

		// Generar la expresion a la derecha de la operacion
		generar(n.getOpDerecho());

		if (n.getOperacionNot() == tipoOp.not) {
			//salta 2 instrucciones si el resultado de la operacion de la derecha del not es igual a cero (AC == 0)
			UtGen.emitirRM("JEQ", UtGen.AC, 2, UtGen.PC, "op: NOT - salta si (AC=0)");
			UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
			UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 para evitar el caso verdadero");
			UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
		} else {
			UtGen.emitirComentario("BUG: tipo de operacion not desconocida");
		}

		if (UtGen.debug) UtGen.emitirComentario("<- Operacion: " + n.getOperacionNot());
	}

	private static void generarOperacion(NodoBase nodo){
		NodoOperacion n = (NodoOperacion) nodo;
		if(UtGen.debug)	UtGen.emitirComentario("-> Operacion: " + n.getOperacion());
		/* Genero la expresion izquierda de la operacion */
		generar(n.getOpIzquierdo());
		/* Almaceno en la pseudo pila de valor temporales el valor de la operacion izquierda */
		UtGen.emitirRM("ST", UtGen.AC, desplazamientoTmp--, UtGen.MP, "op: push en la pila tmp el resultado expresion izquierda");
		/* Genero la expresion derecha de la operacion */
		generar(n.getOpDerecho());
		/* Ahora cargo/saco de la pila el valor izquierdo */
		UtGen.emitirRM("LD", UtGen.AC1, ++desplazamientoTmp, UtGen.MP, "op: pop o cargo de la pila el valor izquierdo en AC1");
		switch(n.getOperacion()){
			case	mas:	UtGen.emitirRO("ADD", UtGen.AC, UtGen.AC1, UtGen.AC, "op: +");		
							break;
			case	menos:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: -");
							break;
			case	por:	UtGen.emitirRO("MUL", UtGen.AC, UtGen.AC1, UtGen.AC, "op: *");
							break;
			case	entre:	UtGen.emitirRO("DIV", UtGen.AC, UtGen.AC1, UtGen.AC, "op: /");
							break;		
			case	menor:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: <");
							UtGen.emitirRM("JLT", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC<0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case	igual:	UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "op: ==");
							UtGen.emitirRM("JEQ", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla if verdadero (AC==0)");
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case 	and:
							//Reviso el primer caso del and (izq), si es falso (igual a cero) salto 2 lineas
							UtGen.emitirRM("JEQ", UtGen.AC1, 2, UtGen.PC, "voy dos instrucciones mas alla si hay corto circuito (AC1 == 0)"); //  significa que la exp logica es falsa

							//Primer caso es verdadero
							//Multiplico resultado de caso izq y caso der, el resultado se guarda en AC
							UtGen.emitirRO("MUL", UtGen.AC, UtGen.AC1, UtGen.AC, "operacion logica AND, se Multiplica AC * AC1");
							//Si el resultado anterior es 0 (resultado falso) continuo ejecucion, si no salto 2 lineas (caso falso)
							UtGen.emitirRM("JNE", UtGen.AC, 2, UtGen.PC, "voy dos instrucciones mas alla si es verdadero (AC != 0)");

							//Salto aqui si el caso del and es falso
							//Dejo el resultado en el registro cero (resultado falso)
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							//Salto una linea para evitar escribir el codigo si el resultado hubiera sido verdadero
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");

							//Caso verdadero
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");

							break;
			case or:
							//Suma los resultados de cada operacion (a resultado se refiere a si cada operacion es verdadero o falso 1, 0)
							//El resultado de la suma me dira el resultado de mi operacion OR
							UtGen.emitirRO("ADD", UtGen.AC, UtGen.AC1, UtGen.AC, "operacion logica suma entre AC y AC1");
							//Salta 2 lineas mas alla si es diferente de 0 (AC = 1), caso verdadero
							UtGen.emitirRM("JNE", UtGen.AC, 2, UtGen.PC, "verificar que AC sea diferente de 0");
							//Caso falso
							UtGen.emitirRM("LDC", UtGen.AC, 0, UtGen.AC, "caso de falso (AC=0)");
							UtGen.emitirRM("LDA", UtGen.PC, 1, UtGen.PC, "Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)");
							//Caso verdadero
							UtGen.emitirRM("LDC", UtGen.AC, 1, UtGen.AC, "caso de verdadero (AC=1)");
							break;
			case mod:
							UtGen.emitirRM("ST", UtGen.AC1, 0, UtGen.GP, "guardo el valor AC1 (opizq) en la dirMem 0");
							UtGen.emitirRM("ST", UtGen.AC, 1, UtGen.GP, "guardo el valor AC (opder) en la dirMem 1");
							UtGen.emitirRO("DIV", UtGen.AC, UtGen.AC1, UtGen.AC, "operacion division y guarda en registro 0");
							UtGen.emitirRM("ST", UtGen.AC, 2, UtGen.GP, "guardo el resultado que esta en reg 0 en la dirMem 2");
							//Cargo el valor del opder en reg 1
							UtGen.emitirRM("LD", UtGen.AC1, 1, UtGen.GP, "cargo en reg 1 (AC1) el valor en la dirMem 1");
							//Multiplico el resultado de la division por el opder
							UtGen.emitirRO("MUL", UtGen.AC, UtGen.AC1, UtGen.AC, "multiplicacion de valor de division por divisor");
							//Cargo el valor de opizq guardado en la dirMem 0 en reg 1
							UtGen.emitirRM("LD", UtGen.AC1, 0, UtGen.GP, "cargo valor AC1 en la dirMem 0");
							//Resto el resultado de la multiplicacion con el opizq
							UtGen.emitirRO("SUB", UtGen.AC, UtGen.AC1, UtGen.AC, "operacion resta de multiplicacion y valor anterior");
							break;

			default:
							UtGen.emitirComentario("BUG: tipo de operacion desconocida");
		}
		if(UtGen.debug)	UtGen.emitirComentario("<- Operacion: " + n.getOperacion());
	}
	
	//TODO: enviar preludio a archivo de salida, obtener antes su nombre
	private static void generarPreludioEstandar(){
		UtGen.emitirComentario("Compilacion TINY para el codigo objeto TM");
		UtGen.emitirComentario("Archivo: "+ "NOMBRE_ARREGLAR");
		/*Genero inicializaciones del preludio estandar*/
		/*Todos los registros en tiny comienzan en cero*/
		UtGen.emitirComentario("Preludio estandar:");
		UtGen.emitirRM("LD", UtGen.MP, 0, UtGen.AC, "cargar la maxima direccion desde la localidad 0");
		UtGen.emitirRM("ST", UtGen.AC, 0, UtGen.AC, "limpio el registro de la localidad 0");
	}

}
