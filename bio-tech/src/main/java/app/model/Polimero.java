
package app.model;

/**
 * Clase Polímero que se compondrá de monómeros representados por letras A-Z / a-z. Los monómeros contiguos en la cadena se
 * anularán mutuamente si son son opuestos (A-a, z-Z, ...). No se anularán si son distintas letras o si son exactamente la
 * misma (aB, Az, aA)
 */
public class Polimero {

  /** Cadena de monómeros representados por letras A-Z a-z (No se incluye la Ñ) */
  private String monomeros;

  /**
   * Constructor de la clase
   * @param monomeros Cadena que representa a los monómeros del polímero
   */
  public Polimero(String monomeros) {
    this.monomeros = monomeros;
  }

  /**
   * Getter - monomeros
   * @return String - monomeros
   */
  public String getMonomeros() {
    return monomeros;
  }

  /**
   * Produce todas las reacciones del polímero en cadena
   * @return Polimero
   */
  public Polimero reacciona() {

    // Booleano que determina si ha habido reacción en la iteración
    boolean reaccionaEnIteracion = false;

    StringBuilder resultado = new StringBuilder();
    int i = 0;

    // Se recorren los monómeros y se buscan coincidencias para realizar la reacción
    do {

      // Si hay 0 o 1 caracteres, se añaden directamente y se termina
      if (monomeros.length() == 0 || monomeros.length() == 1) {
        resultado.append(monomeros);

      } else {

        // Si hay oposición de monómeros, se saltan 2 caracteres y hay reaccion
        if (hayOposicionDeMonomeros(i)) {
          i += 2;
          reaccionaEnIteracion = true;

          // Si no la hay, se almacena el caracter. Si es el penúltimo, se guarda el último también
        } else {
          resultado.append(monomeros.charAt(i));
          i += 1;
        }

        if (i == monomeros.length() - 1) {
          resultado.append(monomeros.charAt(i));
        }
      }

    } while (i < monomeros.length() - 1);

    monomeros = resultado.toString();

    // Si ha habido una reacción, se buscan de nuevo reacciones
    return reaccionaEnIteracion ? reacciona() : this;
  }

  @Override
  public String toString() {
    return monomeros;
  }

  /**
   * Realiza la comprobación de si existe oposición entre dos monómeros
   * @param i Posición del primer monómero a comprobar con el siguiente
   * @return boolean
   */
  private boolean hayOposicionDeMonomeros(int i) {

    return (Character.isLowerCase(monomeros.charAt(i)) && Character.isUpperCase(monomeros.charAt(i + 1))
        && monomeros.charAt(i) == Character.toLowerCase(monomeros.charAt(i + 1)))

        || (Character.isUpperCase(monomeros.charAt(i)) && Character.isLowerCase(monomeros.charAt(i + 1))
            && monomeros.charAt(i) == Character.toUpperCase(monomeros.charAt(i + 1)));
  }

}
