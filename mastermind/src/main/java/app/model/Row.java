
package app.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/** Fila de bolas del juego. Contendrá 6 bolas */
public class Row {

  /** Cantidad de bolas en una fila */
  private static final int BALL_QUANTITY = 8;

  /** Generador de números aleatorios */
  private static final Random RANDOM_GENERATOR = new Random();

  /** Bolas */
  private Ball[] balls = new Ball[BALL_QUANTITY];

  /** Constructor privado para evitar inicialización externa */
  private Row() {
  }

  /**
   * Genera una fila de bolas grises
   * @return Row - Fila de bolas grises
   */
  public static Row generateGrayRow() {
    Row row = new Row();

    // Rellenamos el array de bolas grises
    for (int i = 0; i < BALL_QUANTITY; i++) {
      row.balls[i] = new Ball();
    }

    return row;

  }

  /**
   * Genera una fila de bolas con colores aleatorios permitidos no repetidos
   * @return Row - Array de bolas con colores aleatorios permitidos no repetidos
   */
  public static Row generateRandomRow() {

    Row row = new Row();

    // Lista de colores permitidos para generar una fila
    List<Color> allowedColors = new ArrayList<>(Ball.getAllowedBallColors());

    // Rellenamos el array de bolas
    for (int i = 0; i < BALL_QUANTITY; i++) {

      // Se genera un número aleatorio entre 0 y la longitud de la lista de colores y se crea la bola con el color
      row.balls[i] = new Ball();

      int randomNum = RANDOM_GENERATOR.nextInt(0, BALL_QUANTITY - i);
      row.balls[i].setColor(allowedColors.get(randomNum));

      // Se elimina el de la lista para no repetirlo
      allowedColors.remove(randomNum);

    }
    return row;
  }

  /**
   * Clona una fila y devuelve otra exactamente igual
   * @return Row - La fila clonada
   */
  public Row cloneRow() {

    // Se crea la fila y se copian las bolas una a una
    Row row = new Row();

    for (int i = 0; i < balls.length; i++) {

      Ball ball = new Ball();

      if (balls[i] != null) {
        ball.setColor(balls[i].getColor());
      }

      row.balls[i] = ball;
    }

    return row;
  }

  /**
   * Getter - balls
   * @return Ball[] - balls
   */
  public Ball[] getBalls() {
    return balls;
  }

  /**
   * Setter - balls
   * @param balls Ball[]
   */
  public void setBalls(Ball[] balls) {
    this.balls = balls;
  }

  /**
   * Getter - ball quantity
   * @return int - ball quantity
   */
  public static int getBallQuantity() {
    return BALL_QUANTITY;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + Arrays.hashCode(balls);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    // Dos filas serán iguales si todas las bolas son iguales en las mismas posiciones
    Row other = (Row) obj;
    return Arrays.equals(balls, other.balls);
  }

}
