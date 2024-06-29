*      Compilacion TINY para el codigo objeto TM
*      Archivo: NOMBRE_ARREGLAR
*      Preludio estandar:
0:       LD       6,0(0)      cargar la maxima direccion desde la localidad 0
1:       ST       0,0(0)      limpio el registro de la localidad 0
*      -> asignacion
*      -> constante
2:       LDC       0,5(0)      cargar constante: 5
*      <- constante
3:       ST       0,0(5)      asignacion: almaceno el valor para el id x
*      <- asignacion
*      While
*      a generar condicion
*      -> Operacion: menor
*      -> constante
4:       LDC       0,1(0)      cargar constante: 1
*      <- constante
5:       ST       0,0(6)      op: push en la pila tmp el resultado expresion izquierda
*      -> identificador
6:       LD       0,0(5)      cargar valor de identificador: x
*      -> identificador
7:       LD       1,0(6)      op: pop o cargo de la pila el valor izquierdo en AC1
8:       SUB       0,1,0      op: <
9:       JLT       0,2(7)      voy dos instrucciones mas alla if verdadero (AC<0)
10:       LDC       0,0(0)      caso de falso (AC=0)
11:       LDA       7,1(7)      Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)
12:       LDC       0,1(0)      caso de verdadero (AC=1)
*      <- Operacion: menor
*      A generar Cuerpo
*      -> escribir
*      -> identificador
14:       LD       0,0(5)      cargar valor de identificador: x
*      -> identificador
15:       OUT       0,0,0      escribir: genero la salida de la expresion
*      <- escribir
*      -> asignacion
*      -> Operacion: menos
*      -> identificador
16:       LD       0,0(5)      cargar valor de identificador: x
*      -> identificador
17:       ST       0,0(6)      op: push en la pila tmp el resultado expresion izquierda
*      -> constante
18:       LDC       0,1(0)      cargar constante: 1
*      <- constante
19:       LD       1,0(6)      op: pop o cargo de la pila el valor izquierdo en AC1
20:       SUB       0,1,0      op: -
*      <- Operacion: menos
21:       ST       0,0(5)      asignacion: almaceno el valor para el id x
*      <- asignacion
22:       LDA       7,-19(7)      Salto a la evaluacion de condicion del while
13:       JEQ       0,9(7)      While Salto hacia Fuera del cuerpo
*      -> escribir
*      -> identificador
23:       LD       0,0(5)      cargar valor de identificador: x
*      -> identificador
24:       OUT       0,0,0      escribir: genero la salida de la expresion
*      <- escribir
*      While
*      a generar condicion
*      -> Operacion: menor
*      -> identificador
25:       LD       0,0(5)      cargar valor de identificador: x
*      -> identificador
26:       ST       0,0(6)      op: push en la pila tmp el resultado expresion izquierda
*      -> constante
27:       LDC       0,20(0)      cargar constante: 20
*      <- constante
28:       LD       1,0(6)      op: pop o cargo de la pila el valor izquierdo en AC1
29:       SUB       0,1,0      op: <
30:       JLT       0,2(7)      voy dos instrucciones mas alla if verdadero (AC<0)
31:       LDC       0,0(0)      caso de falso (AC=0)
32:       LDA       7,1(7)      Salto incodicional a direccion: PC+1 (es falso evito colocarlo verdadero)
33:       LDC       0,1(0)      caso de verdadero (AC=1)
*      <- Operacion: menor
*      A generar Cuerpo
*      -> escribir
*      -> identificador
35:       LD       0,0(5)      cargar valor de identificador: x
*      -> identificador
36:       OUT       0,0,0      escribir: genero la salida de la expresion
*      <- escribir
*      -> asignacion
*      -> Operacion: mas
*      -> identificador
37:       LD       0,0(5)      cargar valor de identificador: x
*      -> identificador
38:       ST       0,0(6)      op: push en la pila tmp el resultado expresion izquierda
*      -> constante
39:       LDC       0,2(0)      cargar constante: 2
*      <- constante
40:       LD       1,0(6)      op: pop o cargo de la pila el valor izquierdo en AC1
41:       ADD       0,1,0      op: +
*      <- Operacion: mas
42:       ST       0,0(5)      asignacion: almaceno el valor para el id x
*      <- asignacion
43:       LDA       7,-19(7)      Salto a la evaluacion de condicion del while
34:       JEQ       0,9(7)      While Salto hacia Fuera del cuerpo
*      Fin de la ejecucion.
44:       HALT       0,0,0