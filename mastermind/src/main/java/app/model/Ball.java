
package app.model;

import java.awt.Color;
import java.util.List;
import java.util.Objects;

/** Bola del juego MasterMind */
public class Ball {

  /** Lista de colores permitidos para generar una bola */
  private static final List<Color> ALLOWED_BALL_COLORS = List.of(Color.GREEN, Color.BLUE, Color.PINK, Color.MAGENTA,
      Color.RED, Color.ORANGE, Color.YELLOW, Color.CYAN);

  /** Color de la bola */
  private Color color;

  /** Constructor de la clase por defecto. Establece el color de la bola inicialmente a gris */
  public Ball() {
    this.color = Color.GRAY;
  }

  /**
   * Getter - color
   * @return Color - color
   */
  public Color getColor() {
    return color;
  }

  /**
   * Setter - color
   * @param color Color a establecer para la bola. Debe ser verde, azul, magenta, rojo, naranja o amarillo
   * @throws IllegalArgumentException En caso de que el color de la bola no sea correcto
   */
  public void setColor(Color color) {

    if (ALLOWED_BALL_COLORS.contains(color)) {
      this.color = color;

    } else {
      throw new IllegalArgumentException("Color seleccionado no correcto");
    }
  }

  /**
   * Getter - Lista de colores permitidos para una bola
   * @return List(Color)
   */
  public static List<Color> getAllowedBallColors() {
    return ALLOWED_BALL_COLORS;
  }

  @Override
  public int hashCode() {
    return Objects.hash(color);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    // Dos bolas serán iguales si tienen el mismo color
    Ball other = (Ball) obj;
    return Objects.equals(color, other.color);
  }

}
