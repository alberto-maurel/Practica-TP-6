# Practica-TP-5

Correcciones respecto a la práctica 4:

Claridad:

+ Todas las guías de estilo de java que conozco piden, en los bloques encadenados (if-else, try-catch, ... )

  Hemos revisado la práctica y modificado los bloques encadenados para adaptarnos a este formato.
  Esperamos no habernos dejado ninguno.

+ No llames a super() sin argumentos. Es implícito e innecesario (todas las clases descienden de Object,
  que tiene su propio constructor; pero no te veo escribir super() en constructores de clases sin extends ... )

  Se incluye el commit de Junction. No hemos visto esto en ningún otro lugar de la práctica.


+ Evitad líneas de > 80 caracteres de ancho (o incluso 100, según guía de estilo; recomiendo 80). 
  Por ejemplo, en MostCrowded y los constructores de Car os pasais varios pueblos.

  Hemos revisado la práctica y reducido aquellas líneas que eran demasiado largas. Esperamos no habernos dejado
  ninguna.

+ No useis snake_case, usad camelCase (mirando a las variables en Car)

  Se sustituye snake_case por lowerCamelCase en vehicle. No lo hemos visto en ningún otro lugar de la práctica
  programado por nosotros.

+ Usad this(argumentos) para llamar a un constructor desde otro (ver commit en Car)

  Se incluye el commit. Se realiza esto mismo en SimulatedObject y no hemos más clases en las que sea
  posible aplicarlo.

+ No useis aux1 y aux2 como identificadores en calcularVelocidadBase. Son nombres que no comunican
  nada. Mejor que se vea la fórmula. Ver commit.

  Se incluye el commit y además se modifica calcularVelocidadBase en Highway para solucionar el 
  mismo problema.
  
  
Diseño:
 
 + El coste de llamar a vuestro insertaEvento para N eventos es, en el caso peor,O(N2). Habría sido mejor usar una estructura              ordenada... como por ejemplo, un MultiTreeMap.
  
  
 + Deberíais haber usado Ini.store() para generar la salida, en lugar de reimplementarlo en TrafficSimulator.writeReport.

   Lamentablemente no vimos en la anterior entrega que teníamos esa función (la cual habría simplificado algo el trabajo).
   Nos gustaría haber modificado en esta entrega eso, pero dado que la función funciona correctamente, consideramos que 
   preferirías que priorizasemos el resto de cambios y las funcionalidades añadidas a esta modificación. Intentaremos traerlo
   en la entrega de la práctica 6

 + No entiendo porqué capturais la excepción en writeReport si luego no añadis contexto y sólo la relanzais.
   Para eso, no la captureis: hace lo mismo y es más legible.

   Se ha modificado writeReport, además de otro lugar en el que hemos visto dicho manejo de excepciones.


• Evitad duplicación de código (= copia-pega). Por ejemplo, buscarCarreteraAtascada es muy similar a
buscarCarreteraAtascadaIni – sacad factor común!
3


• Collections.unmodifiableX tiene un coste no-nulo. Es mejor cachear el resultado la primera vez que se
llama y devolverlo en llamadas futuras a llamarlo cada vez.


• No entiendo vuestro Junction. Usais Map<String, Queue...> y Map<String, Road>, pero lo que
realmente os simplificaría la vida sería Map<Road, Queue...> y Map<Junction, Road>. ¡Usad lo que
más sentido tenga! (y si os veis iterando un mapa para buscar algo... lo estais usando mal).


 + Evitad concatenar cadenas dentro de un bucle. Usad en su lugar StringBuilder. Ver commit.

   Se incluye el commit. Además, se revisan las antiguas funciones para sustituir string por StringBuilder 
   (como el fillReportDetails de Road) y en las nuevas funciones (por ejemplo el describable de las junctions) 
   se emplea StringBuilder.

• Manejo de excepciones: si no sabes cómo manejar algo, no lo manejes (pero añade contexto), y pásalo
para arriba:

  
Comentarios respecto a la práctica 5:

- Cuando el output no está redirigido hacia la zona de reports, se muestra por consola. Lo hemos dejado así porque
  consideramos que es el funcionamiento lógico de la aplicación, para que si el usuario no lo tiene redirigido no 
  vea la salida, mientras que los desarrolladores podemos seguir viendo la salida para verificar el correcto funcionamiento.
  

Opcionales incluidos en la práctica:
- Redirección de outputs
- Selección de los elementos de los cuales queremos generar el output en dicho paso en el botón 
  "generarOutput"
- Poner en el grafo adecuadamente el color del semáforo (pensábamos que esto era algo que había
  que hacer, pero el otro día en clase nos pareció entender que no era así)
