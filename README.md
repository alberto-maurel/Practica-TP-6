# Practica-TP-6

## Correcciones práctica p5
<a href="#funcionalidad">1. Funcionalidad</a><br>
<a href="#claridad">2. Claridad</a><br>
<a href="#diseno">3. Diseño</a>

<a name="funcionalidad" id="funcionalidad"></a>
### Funcionalidad

• Todo parece funcionar, aunque me sorprende la forma en que mostrais los mensajes de error. En particular,
  esperaba que usaseis un ShowMessage con icono de error, en lugar de vuestro

<a name="claridad" id="claridad"></a>
### Claridad
• Usa las cosas de la forma más abstracta posible. Así:

                ArrayList<Junction> unmodifiableJunction = (ArrayList<Junction>) controlador.getJunctions();
                    for(Junction j: unmodifiableJunction){
                        junctions.add(j.getId());
                    }
                               ... mejor ...
                    
  Me da igual que sea ArrayList, o UnmodifiableLoQueSe -- incluso me
vale con Iterable


                    for(Junction j: controlador.getJunctions()){
                        junctions.add(j.getId());
                    }

   <i>Corregido en SimulatorLayout =D</i>         

• A los usuarios no les interesa la línea y el fichero de una excepción. Son más importantes otros datos:
¿porqué ha fallado? ¿qué estaba haciendo cuando falló? ...y luego, como última cosa, puedes incluir
información de depuración.

<a name="diseno" id="diseno"></a>
### Diseño

• Separad por responsabilidades. Los templates, y los menús de popup, no deberían formar parte de las
responsabilidades de vuestro SimulatorLayout - deberían estar en una clase dedicada con el componente
de edición de textos.

• Vuestro método error lo llamais siempre con una cadena formada mediante campos presentes en una
excepción. ¿Porqué no le pasais la excepción directamente, en lugar de copiar y pegar siempre el mismo
código?.

• No entiendo porqué no simplificais vuestro habilitarBotones. En particular, podríais poner

                   boolean habilitar = controlador.hayEventosCargados() || controlador.getPasos() + 1 > 0;
                        
  y luego usar 
  
                   accionX.setEnabled(habilitar);
                  
  en lugar de duplicarlo en un gran if/else.

  <i>Corregido en la p6. Ya nos lo arreglaste tú en clase, pero ya no podíamos subirlo en la entrega de la p5.
  Lo que hacíamos era en vez de decidir si habilitarlo o no dentro de habilitarBotones, que el método tenga
  un parámetro que le indique si habilitar o no, y luego dependiendo de desde donde se llame, se le pasa true o false</i>


• Teneis un problema de concepto sobre herencia en Java. Ésto:

              if(this instanceof MostCrowded) {
                  MostCrowded auxJunction = (MostCrowded) this;
                  sb.append(auxJunction.toStringGreen());
              } else if(this instanceof RoundRobin) {
                  RoundRobin auxJunction = (RoundRobin) this;
                  sb.append(auxJunction.toStringGreen());
              } else {
                  sb.append(toStringGreen());
              }
              
... equivale a ...

              sb.append(toStringGreen());
              
... ya que cada objeto resuelve las llamadas no-estáticas mirando primero entre sus métodos, y sólo cuando no
consigue encontrar uno, recurriendo a implementaciones heredadas.

<i>Éramos conscientes de que el código de abajo era mucho mejor que el de arriba, pero haciendo pruebas no conseguimos
que funcionase (al depurarlo no hacía bien los casts y las junctions avanzadas las cogía como junctions, no sacando el numerito
al lado indicando el tiempo que le quedaba al semáforo). Bastante sorprendidos hemos comprobado hoy que el código que nos
has puesto tú si que funciona en la versión actual de la práctica (te aseguramos que habíamos probado si no eso algo muy
similar y no nos funcionaba correctamente). Fallito técnico D=</i>

• Evita referenciar recursos (fuera de los tests) vía new File("src/main/resources/algo.png"). Usa
en su lugar

              this.getClass().getClassLoader().getResourceAsStream("algo.png")
              
... que funciona aunque no haya código disponible, ya que usa el mismo mecanismo usado por las clases para
cargarse a sí mismas  

## Práctica 6
Lo único seguro en esta vida es la muerte, pero estamos bastante seguros de que funciona todo
### Extras práctica 6
Este y <a href="https://github.com/lauracastilla/Practica-TP-5/blob/master/LEEME.md" target=_blank>este</a> leeme tan bonitos no suman puntos
