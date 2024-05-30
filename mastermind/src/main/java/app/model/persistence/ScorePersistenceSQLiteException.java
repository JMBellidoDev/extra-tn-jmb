
package app.model.persistence;

/** Excepción lanzada en caso de problema durante el uso de la base de datos */
public class ScorePersistenceSQLiteException extends RuntimeException {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /**
   * Constructor con mensaje
   * @param message Mensaje de error
   */
  public ScorePersistenceSQLiteException(String message) {
    super(message);
  }

}
