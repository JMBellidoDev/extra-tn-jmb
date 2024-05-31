
package app.view;

/** Clase de constantes necesarias para la vista */
public class ViewConstants {

  /** Constructor privado para evitar instanciar la clase */
  private ViewConstants() {
  }

  /** Fuente del texto usado en la aplicación */
  public static final String TEXT_FONT = "tahoma";

  /** Tamaño del texto */
  public static final int TEXT_SIZE = 13;

  /** Ruta hacia la imagen del grafo */
  public static final String GRAPH_PATH = "./src/main/resources/graph.png";

  /** Formato de la imagen del grafo */
  public static final String GRAPH_IMG_TYPE = "PNG";

  /** Dominancia negativa de un polímero */
  public static final int DOMINANCIA_NEGATIVA = 0;

  /** Dominancia neutra de un polímero */
  public static final int DOMINANCIA_NEUTRA = 1;

  /** Dominancia positiva de un polímero */
  public static final int DOMINANCIA_POSITIVA = 2;

}
