
package app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.model.Ball;
import app.model.Row;
import app.model.Score;
import app.model.persistence.ScorePersistenceSQLite;
import app.model.persistence.ScorePersistenceSQLiteException;

/** Métodos que se pueden realizar en el juego */
public class GameActions {

  /** Logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(app.controller.GameActions.class);

  /** Sistema de persistencia SQLite */
  private ScorePersistenceSQLite persistence;

  /**
   * Constructor con sistema de persistencia
   * @param persistence Sistema de persistencia
   */
  public GameActions(ScorePersistenceSQLite persistence) {
    this.persistence = persistence;
  }

  /**
   * Método que genera una fila con colores aleatorios no repetidos y permitidos. Usado para generar tanto la solución como
   * la primera fila
   * @return Row - Fila con colores aleatorios no repetidos y permitidos
   */
  public Row generateRandomRow() {
    return Row.generateRandomRow();
  }

  /**
   * Método que clona una fila dada y genera una nueva
   * @param row Fila a clonar
   * @return Row
   */
  public Row cloneRow(Row row) {
    return row.cloneRow();
  }

  /**
   * Obtiene la nueva coordenada a partir de un movimiento realizado que será 0 (izquierda) o 1 (derecha). Si el movimiento
   * hiciese que la nueva coordenada se "saliese" del array, se daría la vuelta completa horizontalmente
   * @param coordinates Coordenada original
   * @param movement    Movimiento realizado. 0 izquierda, 1 derecha
   * @return La nueva coordenada
   */
  public int[] getNewCoordinates(int[] coordinates, int movement) {

    int[] newCoordinates = new int[2];

    newCoordinates[0] = coordinates[0];

    // Si el movimiento es a la izquierda, se verifica si x es 0 o no
    if (movement == 0) {

      if (coordinates[1] != 0) {
        newCoordinates[1] = coordinates[1] - 1;

      } else {
        newCoordinates[1] = Row.getBallQuantity() - 1;
      }

    } else {

      if (coordinates[1] != Row.getBallQuantity() - 1) {
        newCoordinates[1] = coordinates[1] + 1;

      } else {
        newCoordinates[1] = 0;
      }
    }

    return newCoordinates;
  }

  /**
   * Verifica si se ha ganado el juego, comprobando la fila actual con respecto a la fila solución
   * @param solution Fila solución
   * @param current  Fila actual
   * @return boolean
   */
  public boolean checkWin(Row solution, Row current) {
    return solution.equals(current);
  }

  /**
   * Realiza un intercambio entre las bolas de la posición 1 y 2
   * @param row       Fila en la que realizar el intercambio
   * @param position1 Posición de la primera bola a intercambiar
   * @param position2 Posición de la segunda bola a intercambiar
   * @return Row
   */
  public Row swapBallsFromRow(Row row, int position1, int position2) {

    Row changedRow = row.cloneRow();
    Ball ballPosition2 = row.getBalls()[position2];

    changedRow.getBalls()[position2] = changedRow.getBalls()[position1];
    changedRow.getBalls()[position1] = ballPosition2;

    LOGGER.debug("Bolas cambiadas de posición con éxito\n");

    return changedRow;
  }

  /**
   * Almacena una nueva puntuación
   * @param score Puntuación a almacenar
   * @return El ID generado de la puntuación
   * @throws ScorePersistenceSQLiteException En caso de error durante el acceso a la base de datos
   */
  public long saveScore(Score score) {
    return persistence.saveScore(score);
  }

  /**
   * Obtiene las 3 mejores puntuaciones almacenadas en formato cadena
   * @return String
   */
  public String getBestScores() {

    StringBuilder sb = new StringBuilder();
    persistence.getBest3Scores().forEach(score -> sb.append(score + "\n"));

    return sb.toString();
  }

}
