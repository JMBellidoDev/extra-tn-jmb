
package app.view.utils;

/** Clase de constantes del juego */
public class GameConstants {

  /** Constructor privado para evitar instanciación de clase */
  private GameConstants() {
  }

  /** Posición inicial seleccionada en el cursor */
  public static final int[] INITIAL_CURRENT_POSITION = new int[] { 1, 0 };

  /** Cantidad de filas del juego */
  public static final int ROW_QUANTITY = 11;

  /** Diámetro de cada bola */
  public static final int DIAMETER = 40;

  /** Espaciado entre bolas */
  public static final int SPACING = 10;

  /** Posición de la coordenada X en un array de posiciones */
  public static final int X_POSITION = 0;

  /** Posición de la coordenada Y en un array de posiciones */
  public static final int Y_POSITION = 1;

  /** Tipo de letra usado en el juego */
  public static final String LETTER_FAMILY = "Tahoma";

  /** Texto del botón select */
  public static final String BTN_SELECT_TEXT = "Seleccionar";

  /** Texto del botón select después de haber sido usado para seleccionar */
  public static final String BTN_SELECT_MODIFIED_TEXT = "Cambiar";

  /** Texto del botón check */
  public static final String BTN_CHECK_TEXT = "Check";

  /** Puntuación inicial del juego */
  public static final int INITIAL_SCORE = 1000;

  /** Tiempo inicial para el temporizador */
  public static final int INITIAL_TIME = 0;

  /** Puntuación perdida por cada segundo pasado de juego */
  public static final int SCORE_LOST_PER_SECOND = 3;

  /** Puntuación perdida por cada check realizado */
  public static final int SCORE_LOST_PER_CHECK = 100;

  /** Ruta hacia el fichero de persistencia */
  public static final String DB_PATH = "./src/main/resources/scores.db";

}
