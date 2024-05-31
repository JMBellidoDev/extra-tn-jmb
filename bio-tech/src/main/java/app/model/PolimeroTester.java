
package app.model;

/** Clase que realiza las comprobaciones necesarias sobre la clase Polimero */
public class PolimeroTester {

  /** Expresión regular que debe cumplir toda cadena de monómeros */
  private static final String MONOMEROS_REGEX = "[A-Za-z]+";

  /** Constructor privado para evitar instanciación de clase */
  private PolimeroTester() {
  }

  /**
   * Realiza las comprobaciones necesarias para verificar si una cadena de monómeros es correcta
   * @param monomeros Cadena de monómeros no null representados por caracteres A-Z, a-z sin incluir la Ñ
   * @return boolean
   */
  public static boolean isMonomerosValido(String monomeros) {
    return monomeros != null && monomeros.matches(MONOMEROS_REGEX);
  }

  /**
   * Obtiene la dominancia del polímero que se aporta por parámetro. Será positiva si hay dos monómeros positivos
   * (mayúsculas) iguales consecutivos y negativa si hay dos monómeros negativos (minúsculas) iguales consecutivos. En caso
   * de que existan de ambos tipos, se comprobará cuál es la dominancia mayor. Si no hay dominancia o ambas son iguales, será
   * neutra
   * @param polimero Polímero a calcular la dominancia
   * @return 0 si es negativa, 1 si es neutra y 2 si es positiva
   */
  public static int getDominancia(Polimero polimero) {

    int dominanciaPositiva = 0;
    int dominanciaNegativa = 0;

    // Se recorren los monómeros verificando la dominancia
    char[] monomeros = polimero.getMonomeros().toCharArray();

    for (int i = 0; i < monomeros.length - 1; i++) {

      if (monomeros[i] == monomeros[i + 1] && Character.isUpperCase(monomeros[i])) {
        dominanciaPositiva++;

      } else if (monomeros[i] == monomeros[i + 1] && Character.isLowerCase(monomeros[i])) {
        dominanciaNegativa++;
      }
    }

    // Se verifica la dominancia
    return dominanciaPositiva > dominanciaNegativa ? 2 : dominanciaPositiva == dominanciaNegativa ? 1 : 0;

  }

}
