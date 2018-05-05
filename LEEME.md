# Practica-TP-5

Correcciones respecto a la práctica 4:

Claridad:

+ Todas las guías de estilo de java que conozco piden, en los bloques encadenados (if-else, try-catch, ... )
  Hemos revisado la práctica y modificado los bloques encadenados para adaptarnos a este formato.
  Esperamos no habernos dejado ninguno.

+ No llames a super() sin argumentos. Es implícito e innecesario (todas las clases descienden de Object,
  que tiene su propio constructor; pero no te veo escribir super() en constructores de clases sin extends ... )
- Se incluye el commit de Junction. No hemos visto esto en ningún otro lugar de la práctica.


+ Evitad líneas de > 80 caracteres de ancho (o incluso 100, según guía de estilo; recomiendo 80). 
  Por ejemplo, en MostCrowded y los constructores de Car os pasais varios pueblos.
- Hemos revisado la práctica y reducido aquellas líneas que eran demasiado largas. Esperamos no habernos dejado
  ninguna.

+ No useis snake_case, usad camelCase (mirando a las variables en Car)
- Se sustituye snake_case por lowerCamelCase en vehicle. No lo hemos visto en ningún otro lugar de la práctica
  programado por nosotros.

+ Usad this(argumentos) para llamar a un constructor desde otro (ver commit en Car)
- Se incluye el commit. Se realiza esto mismo en SimulatedObject y no hemos más clases en las que sea
  posible aplicarlo.

+ No useis aux1 y aux2 como identificadores en calcularVelocidadBase. Son nombres que no comunican
  nada. Mejor que se vea la fórmula. Ver commit.
- Se incluye el commit y además se modifica calcularVelocidadBase en Highway para solucionar el 
  mismo problema.

Opcionales incluidos en la práctica:
- Redirección de outputs
- Selección de los elementos de los cuales queremos generar el output en dicho paso en el botón 
  "generarOutput"
- Poner en el grafo adecuadamente el color del semáforo (pensábamos que esto era algo que había
  que hacer, pero el otro día en clase nos pareció entender que no era así)
