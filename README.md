# Formación Dual. Proyectos Extra

## mastermind
Se trata de un proyecto que simula el juego Master Mind. Funciona de la siguiente forma:

![image](https://github.com/JMBellidoDev/extra-tn-jmb/assets/166582366/f90c415c-5ec6-4e68-8488-7100749905f4)

* El juego solicitará un nombre de jugador al usuario.
* El juego tendrá una combinación de colores ganadora secreta o clave maestra.
* El jugador partirá de una combinación aleatoria de colores y, por cada ronda, podrá realizar los cambios que desee dentro de una misma fila usando el botón "Seleccionar". Las bolas seleccionadas serán marcadas con un borde de color azul para cambiarlas por otras, y sobre la que se encuentre el cursor se marcará con un borde negro.
* Una vez se pulse sobre el botón "Check", el juego marcará con un recuadro de color verde las posiciones de las bolas correctas en la última fila y habilitará la siguiente para seguir jugando.
* El jugador dispondrá de 10 intentos de "Check" para ganar el juego, colocando cada bola en su posición correcta.
* El temporizador restará 3 puntos por cada segundo de juego. Cada check restará 100 puntos por uso. La puntuación final será de 1000 menos las cantidades ya declaradas.
* Una vez el jugador coloque en un orden correcto todas las bolas, ganará el juego
* Una vez finalice el juego, se informará al usuario de las 3 mejores puntuaciones del sistema y de la suya personal. Cada usuario o nick podrá disponer de un máximo de 3 puntuaciones en el sistema (las más altas), de forma que se borrarán automáticamente las más bajas según se vaya jugando.

Disfrutad del juego! :blush:

![image](https://github.com/JMBellidoDev/extra-tn-jmb/assets/166582366/a423399b-1d24-4573-8e67-07db9d129b3b)

## bio-tech
Se trata de un proyecto que simula un Polímero. Funciona de la siguiente forma:

* La aplicación solicitará una cadena de monómeros que definirá el polímero. Será una cadena de letras A-Za-z sin incluir la Ñ.

![image](https://github.com/JMBellidoDev/extra-tn-jmb/assets/166582366/ddd46c3b-dd21-434e-affd-1f69aead8a81)

* El siguiente paso a realizar es provocar la reacción del polímero. La reacción se producirá entre monómeros opuestos dispuestos de forma consecutiva, es decir, entre monómeros representados por una letra mayúsculas y la misma pero en minúscula. Esta reacción se realizará de forma recursiva siempre que se produzca al menos una reacción en cada iteración. Dicho de otra forma, siempre que haya una reacción como mínimo, se volverá a comprobar si hay más.

![image](https://github.com/JMBellidoDev/extra-tn-jmb/assets/166582366/9671fa8d-7fc6-4e76-91b7-a9ca03ae8029)

* Existirá una dominancia relacionada con la cantidad de monomeros iguales consecutivos una vez se haya producido la reacción.
  - Si hay dos monómeros representados por una letra minúscula situados de forma consecutiva, se considerará que la dominancia es negativa.
  - Si hay dos monómeros representados por una letra mayúscula situados de forma consecutiva, se considerará que la dominancia es positiva.
  - En caso de haber de ambos tipos, la predominante será la que más se repita.
  - Si no se da ninguno de los casos anteriores, o ante igualdad de dominancias, se considerará que la dominancia es neutra.

* Dominancia neutra
  
![image](https://github.com/JMBellidoDev/extra-tn-jmb/assets/166582366/e349b66b-7c59-4c1a-a482-6b5e3ddc78ff)

* Dominancia Positiva
  
![image](https://github.com/JMBellidoDev/extra-tn-jmb/assets/166582366/7e216887-f118-403b-a48b-877d9b2c5a68)

* Dominancia Negativa
  
![image](https://github.com/JMBellidoDev/extra-tn-jmb/assets/166582366/e1d8c958-ea82-4842-9623-b4cbc52f374e)

