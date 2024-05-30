
package app.model.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.model.Score;

/** Sistema de persistencia para las puntuaciones en formato SQLite */
public class ScorePersistenceSQLite {

  /** Logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(app.model.persistence.ScorePersistenceSQLite.class);

  /** URL de conexión a la base de datos */
  private static final String URL_TEMPLATE = "jdbc:sqlite:%s";

  /** Número máximo de puntuaciones que se pueden almacenar por cada jugador */
  private static final int MAX_SCORES_PER_PLAYER = 3;

  /** Sentencia SQL para guardar una puntuación */
  private static final String SQL_SAVE_SCORE = "INSERT INTO score (player, score) VALUES (?, ?)";

  /** Sentencia SQL para obtener las 3 mejores puntuaciones */
  private static final String SQL_GET_BEST_SCORES = "SELECT * FROM score ORDER BY score DESC LIMIT 3";

  /** Sentencia SQL de búsqueda de todas las puntuaciones dado un jugador */
  private static final String SQL_FIND_ALL_BY_PLAYER = "SELECT * FROM score WHERE player = ?";

  /** Sentencia SQL de búsqueda de un registro por id */
  private static final String SQL_FIND_BY_ID = "SELECT * FROM score WHERE id = ?";

  /** Sentencia SQL de eliminación de un registro por id */
  private static final String SQL_DELETE_BY_ID = "DELETE FROM score WHERE id = ?";

  /** Ruta hacia la base de datos SQLite */
  private String dbPath;

  /**
   * Constructor de la clase
   * @param dbPath Ruta hacia el archivo de base de datos
   */
  public ScorePersistenceSQLite(String dbPath) {
    this.dbPath = dbPath;
  }

  /**
   * Guarda una puntuación de un jugador dado. Sólo se almacenan 3 puntuaciones por jugador, por lo que si se intenta
   * almacenar una cuarta, se elimina la de puntuación más baja. Si se intenta almacenar una puntuación más baja que las 3
   * guardadas, no se realizará el proceso de guardado
   * @param score No null
   * @return long - El id de la puntuación tras ser almacenada. Será -1L si no se ha almacenado debido a que no cumple los
   *         requisitos del método
   * @throws ScorePersistenceSQLiteException En caso de que haya algún problema en la conexión con la base de datos
   */
  public long saveScore(Score score) {

    if (score == null) {
      throw new ScorePersistenceSQLiteException("No se pudo almacenar la puntuación. Los parámetros no pueden ser null");
    }

    // Se obtienen las puntuaciones y se comprueba si se puede almacenar
    List<Score> scores = findAllScoresByPlayer(score.getPlayer());

    long id;

    if (scores.size() < MAX_SCORES_PER_PLAYER) {
      id = saveScoreWithoutChecking(score);

    } else {
      id = saveScoreCheckingList(score, scores);
    }

    String msg = String.format("Puntuación almacenada con id: %d%n", id);
    LOGGER.debug(msg);

    return id;

  }

  /**
   * Almacena las 3 mejores puntuaciones obtenidas
   * @return List(Score)
   * @throws ScorePersistenceSQLiteException En caso de que haya algún problema en la conexión con la base de datos
   */
  public List<Score> getBest3Scores() {

    List<Score> bestScores = new LinkedList<>();

    // Se realiza la conexión y se lanza la consulta
    try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_BEST_SCORES)) {

      ResultSet resultSet = preparedStatement.executeQuery();

      // Se recogen resultados y se almacenan en una lista para ser devueltos
      while (resultSet.next()) {
        bestScores.add(getScoreByResultSet(resultSet));
      }

      LOGGER.debug("Se pudieron obtener las 3 mejores puntuaciones\n");

      return bestScores;

    } catch (SQLException e) {

      LOGGER.error("No se han podido obtener las 3 mejores puntuaciones de la DB\n");
      throw new ScorePersistenceSQLiteException("Fallo al obtener la información de la DB");
    }
  }

  /**
   * Realiza una búsqueda de puntuación dado un id
   * @param id ID de la puntuación
   * @return Score - Será null si no se encuentra
   * @throws ScorePersistenceSQLiteException En caso de que haya algún problema en la conexión con la base de datos
   */
  public Score findById(long id) {

    Score score;

    // Se realiza la conexión y la búsqueda de la puntuación
    try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {

      preparedStatement.setLong(1, id);
      ResultSet resultSet = preparedStatement.executeQuery();

      // Se verifica la búsqueda
      if (resultSet.next()) {
        score = getScoreByResultSet(resultSet);

      } else {
        score = null;
      }

      resultSet.close();
      return score;

    } catch (SQLException e) {
      LOGGER.error("No se pudo realizar la conexión con la base de datos\n");

      throw new ScorePersistenceSQLiteException(e.getMessage());
    }
  }

  /**
   * Guarda la puntuación sin verificar la db para eliminar otras puntuaciones, es decir, realiza directamente la inserción
   * de puntuación
   * @param score Puntuación a almacenar
   * @return long - ID generada de la puntuación
   * @throws ScorePersistenceSQLiteException En caso de que haya algún problema en la conexión con la base de datos
   */
  private long saveScoreWithoutChecking(Score score) {

    long id = -1L;

    // Se realiza la conexión y se lanza la sentencia de guardado
    try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_SCORE, Statement.RETURN_GENERATED_KEYS)) {

      preparedStatement.setString(1, score.getPlayer());
      preparedStatement.setInt(2, score.getQuantity());

      // Se verifica la inserción
      int inserted = preparedStatement.executeUpdate();

      if (inserted == 1) {
        // Se obtiene la clave generada
        ResultSet rsGeneratedKeys = preparedStatement.getGeneratedKeys();

        if (rsGeneratedKeys.next()) {
          id = rsGeneratedKeys.getLong(1);
        }

        rsGeneratedKeys.close();
      }
      return id;

    } catch (SQLException e) {

      LOGGER.error("No se pudo almacenar la puntuación sin check previo\n");
      throw new ScorePersistenceSQLiteException("Error al almacenar la puntuación. " + e.getMessage());
    }

  }

  /**
   * Realiza el guardado comprobando la lista aportada. Si la puntuación es más alta que alguna de ellas, se elimina la más
   * baja y se inserta la nueva. Si es más baja, no se almacena ninguna puntuación
   * @param score  Puntuación a añadir
   * @param scores Lista de puntuaciones almacenadas del mismo jugador
   * @return long - ID de la puntuación almacenada. Será -1L si no se almacena
   * @throws ScorePersistenceSQLiteException En caso de ocurrir un problema al recuperar la información
   */
  private long saveScoreCheckingList(Score score, List<Score> scores) {

    long id = -1L;

    // Se obtiene la puntuación mínima almacenada y, si la nueva es mayor, se elimina la más baja y se almacena la nueva
    int minStoredScore = scores.stream().map(Score::getQuantity).reduce(scores.get(0).getQuantity(),
        (sc1, sc2) -> (sc1 < sc2) ? sc1 : sc2);

    if (score.getQuantity() > minStoredScore) {
      Optional<Long> idScoreToDelete = scores.stream().filter(sc -> sc.getQuantity() == minStoredScore).map(Score::getId)
          .findAny();

      if (idScoreToDelete.isPresent()) {
        deleteScore(idScoreToDelete.get());
        id = saveScoreWithoutChecking(score);

      } else {
        LOGGER.error("No se pudo almacenar la puntuación con check previo\n");
        throw new ScorePersistenceSQLiteException("No se pudo almacenar la puntuación");
      }
    }
    return id;
  }

  /**
   * Elimina una puntuación dado su id.
   * @param id ID de la puntuación a eliminar
   * @return Score - Será null si no se encuentra la puntuación
   * @throws ScorePersistenceSQLiteException En caso de que haya algún problema en la conexión con la base de datos
   */
  private Score deleteScore(long id) {

    // Se comprueba si existe en la db
    Score score = findById(id);

    if (score == null) {
      return null;
    }
    // Se realiza la conexión y se lanza la sentencia de eliminación
    try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BY_ID)) {

      preparedStatement.setLong(1, id);
      int updated = preparedStatement.executeUpdate();

      // Se verifica la eliminación
      if (updated == 1) {
        String msg = String.format("Puntuación con id: %d eliminada%n", id);
        LOGGER.debug(msg);

        return score;

      } else {
        // Se lanza el error para capturarlo y redirigirlo
        throw new SQLException();
      }

    } catch (SQLException e) {
      String msg = String.format("No se pudo eliminar la puntuación con id: %d%n", id);
      LOGGER.error(msg);

      throw new ScorePersistenceSQLiteException(e.getMessage());
    }
  }

  /**
   * Obtiene una lista con las 3 puntuaciones del jugador dado
   * @param player Jugador
   * @return List(Score)
   * @throws ScorePersistenceSQLiteException En caso de que haya algún problema en la conexión con la base de datos
   */
  private List<Score> findAllScoresByPlayer(String player) {

    List<Score> scores = new LinkedList<>();

    // Se realiza la conexión y se lanza la consulta
    try (Connection connection = getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_ALL_BY_PLAYER)) {

      preparedStatement.setString(1, player);
      ResultSet resultSet = preparedStatement.executeQuery();

      // Se obtienen y almacenan las puntuaciones
      while (resultSet.next()) {
        scores.add(getScoreByResultSet(resultSet));
      }

      resultSet.close();
      return scores;

    } catch (SQLException e) {
      LOGGER.error("No se pudo conectar con la base de datos\n");

      throw new ScorePersistenceSQLiteException(e.getMessage());
    }

  }

  /**
   * Obtiene una puntuación dado el resultSet
   * @param resultSet ResultSet obtenido de una consulta
   * @return Score
   * @throws SQLException Excepción lanzada en caso de error durante la extracción de datos
   */
  private Score getScoreByResultSet(ResultSet resultSet) throws SQLException {

    // Se pueden extraer todos los datos sin comprobar null debido al diseño de la db
    long id = resultSet.getLong("id");
    String player = resultSet.getString("player");
    int score = resultSet.getInt("score");

    Score sc = new Score(player, score);
    sc.setId(id);

    return sc;
  }

  /**
   * Obtiene una conexión dado el URL de la base de datos
   * @return Connection
   * @throws SQLException En caso de error durante el proceso
   */
  private Connection getConnection() throws SQLException {
    String url = String.format(URL_TEMPLATE, dbPath);
    return DriverManager.getConnection(url);
  }

}
