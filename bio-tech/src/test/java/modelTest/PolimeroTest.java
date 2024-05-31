
package modelTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import app.model.Polimero;
import app.model.PolimeroTester;

/** Tests para la clase Polimero */
public class PolimeroTest {

  @ParameterizedTest
  @CsvSource({ "AaDegjJjj, Degjj", "BBbcAIi, BcA", "caaaAaARrBiI, caaB", "paiIIiolaA, paol", "A, A", "AA, AA" })
  void reaccionaTest(String monomerosInicial, String monomerosEsperado) {

    Polimero polimero = new Polimero(monomerosInicial);

    assertEquals(monomerosEsperado, polimero.reacciona().getMonomeros());

  }

  @ParameterizedTest
  @CsvSource({ "AaDeEdgjJjj, gjj", "BBbcCbAIi, A", "caaaRrRAaRrrBiI, caaaB", })
  void reaccionaConRecursividadTest(String monomerosInicial, String monomerosEsperado) {

    Polimero polimero = new Polimero(monomerosInicial);

    assertEquals(monomerosEsperado, polimero.reacciona().getMonomeros());
  }

  @ParameterizedTest
  @CsvSource({ "AaDeEdgjJjjJJ, 1", "AaDeEdgjJ, 1", "bbbcCbAIi, 0", "caaaRrRAaRrrBBBBiI, 1", "cRrRAaRrrBBBBiI, 2" })
  void dominanciaPositivaTest(String monomerosInicial, int dominanciaEsperada) {

    Polimero polimero = new Polimero(monomerosInicial);

    assertEquals(dominanciaEsperada, PolimeroTester.getDominancia(polimero));
  }

}
