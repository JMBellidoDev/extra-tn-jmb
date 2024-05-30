
package app.model;

/** Puntuación del juego de un jugador concreto */
public class Score {

  /** ID de la puntuación */
  private long id;

  /** Nombre del jugador */
  private String player;

  /** Puntuación del jugador a almacenar */
  private int quantity;

  /**
   * Constructor de la clase
   * @param player   Nombre del jugador
   * @param quantity Cantidad de puntuación
   */
  public Score(String player, int quantity) {
    this.player = player;
    this.quantity = quantity;
  }

  /**
   * Getter - ID
   * @return long - ID
   */
  public long getId() {
    return id;
  }

  /**
   * Getter - player
   * @return String - player
   */
  public String getPlayer() {
    return player;
  }

  /**
   * Getter - quantity
   * @return int - quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Setter - ID
   * @param id ID
   */
  public void setId(long id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return String.format("Player: %s, Score: %d", player, quantity);
  }

}
