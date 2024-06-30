# Compilador Pascal a Tiny

Este proyecto es un compilador escrito en Java que transforma código escrito en Pascal a código de la máquina virtual Tiny. Se basa en una base de código previa que transformaba código de otro lenguaje a Tiny.

## Requisitos

Para ejecutar este proyecto, necesitas:

- Java Development Kit (JDK)
- Librerías de JFlex y Cup correctamente enlazadas

## Estructura del Proyecto

- **parser.java**: Punto de entrada del compilador. Este archivo recibe como parámetro un archivo de pruebas en Pascal y muestra el código transformado en Tiny en la consola.
- **lexico.flex**: Archivo que define el analizador léxico. Para regenerar la clase después de hacer modificaciones, ejecuta `CrearLexico.java`.
- **sintatico.cup**: Archivo que define el analizador sintáctico y la gramática. Para regenerar las clases `parser.java` y `sym.java` después de hacer modificaciones, ejecuta `CrearCup.java`.

## Ejecución

Para ejecutar el compilador, asegúrate de que todas las librerías necesarias están correctamente enlazadas y sigue estos pasos:

1. Compila el proyecto.
2. Ejecuta el archivo `parser.java` con el siguiente comando, proporcionando el archivo de pruebas en Pascal como argumento:

   ```sh
   java parser <archivo_de_pruebas.pas>
   
## Modificación del Analizador Léxico

Si necesitas realizar cambios en el analizador léxico:

1. Modifica el archivo `lexico.flex`.
2. Ejecuta `CrearLexico.java` para regenerar la clase `Lexica.Java`:

   ```sh
   java CrearLexico
   

## Modificación del Analizador Sintáctico y Gramática

Si necesitas realizar cambios en el analizador sintáctico o en la gramática:

1. Modifica el archivo `sintatico.cup`.
2. Ejecuta `CrearCup.java` para regenerar las clases `parser.java` y `sym.java`:

   ```sh
   java CrearCup

