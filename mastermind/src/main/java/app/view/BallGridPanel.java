
package app.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import app.model.Row;
import app.view.utils.GameConstants;

/** Panel grid que llena el espacio con 11 filas de 8 bolas grises */
public class BallGridPanel extends JPanel {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** Filas de bolas del juego */
  private transient Row[] rows;

  /** Fila solución del juego */
  private transient Row solutionRow;

  /** Array con las posiciones x e y donde se encuentra el cursor actualmente */
  private int[] current;

  /** Array con las posiciones x e y seleccionadas para realizar una modificación */
  private int[] selected;

  /**
   * Constructor al que se le pasa un array de colores para cada bola
   * @param solutionRow Fila solución del juego
   * @param rows        Array de filas con las bolas de cada posición
   * @param current     Array con las posiciones x e y donde se encuentra el cursor actualmente
   */
  public BallGridPanel(Row solutionRow, Row[] rows, int[] current) {
    this.solutionRow = solutionRow;
    this.rows = rows;
    this.current = current;
  }

  // Se sobreescribe el método paintComponent para dibujar en el JPanel
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Dibuja la cuadrícula de bolas
    for (int row = 0; row < GameConstants.ROW_QUANTITY; row++) {
      for (int col = 0; col < Row.getBallQuantity(); col++) {

        // Establece el color para las bolas
        g.setColor(rows[row].getBalls()[col].getColor());

        // Se calculan las posiciones x e y
        int x = col * (GameConstants.DIAMETER + GameConstants.SPACING);
        int y = row * (GameConstants.DIAMETER + GameConstants.SPACING);

        // Se dibuja la bola
        g.fillOval(x, y, GameConstants.DIAMETER, GameConstants.DIAMETER); // Dibuja la bola

        // Se recuadran las bolas correctas. Si la fila es la actual, se hace la comprobación con la anterior (por si se
        // hacen modificaciones no dar soluciones anticipadas)
        paintBallSolution(g, row, col, x, y);

        // Se recuadran las bolas, primero la seleccionada (azul) y luego la actual (negro)
        paintBallBorder(g, row, col, x, y);

      }
    }
  }

  /**
   * Setter - Row. Establece una fila en una posición determinada
   * @param row      Fila a establecer
   * @param position Posición de la fila a establecer
   */
  public void setRows(Row row, int position) {
    this.rows[position] = row;
  }

  /**
   * Setter - current. Establece la posición actual del cursor
   * @param current Posición actual del cursor
   */
  public void setCurrent(int[] current) {
    this.current = current;
  }

  /**
   * Setter - selected. Establece la posición de la bola seleccionada
   * @param selected Posición de la bola seleccionada
   */
  public void setSelected(int[] selected) {
    this.selected = selected;
  }

  /**
   * Pinta un recuadro alrededor de la bola de la fila y columna indicada en caso de estar seleccionada o ser la bola en la
   * que se encuentra el cursor.
   * @param g   Graphics. Realiza el pintado del recuadro
   * @param row Fila de la bola
   * @param col Columna de la bola
   * @param x   Pixel horizontal para realizar el pintado
   * @param y   Pixel vertical para realizar el pintado
   */
  private void paintBallBorder(Graphics g, int row, int col, int x, int y) {

    boolean hasToPaint = false;

    // Se comprueba si es en la que se encuentra el cursor actualmente
    if (current != null && row == current[GameConstants.X_POSITION] && col == current[GameConstants.Y_POSITION]) {

      g.setColor(Color.BLACK);
      hasToPaint = true;
    }

    // Se comprueba si está seleccionada.
    if (selected != null && row == selected[GameConstants.X_POSITION] && col == selected[GameConstants.Y_POSITION]) {

      g.setColor(Color.BLUE);
      hasToPaint = true;
    }

    if (hasToPaint) {
      // Dibuja el cuadrado
      Graphics2D g2d = (Graphics2D) g;
      g2d.setStroke(new BasicStroke(GameConstants.BORDER_STROKE));

      g2d.drawRect(x - 5, y - 5, GameConstants.DIAMETER + 10, GameConstants.DIAMETER + 10);
    }

  }

  /**
   * Pinta un recuadro alrededor de la bola de la fila y columna indicada en caso de estar en la posición adecuada
   * @param g   Graphics. Realiza el pintado del recuadro
   * @param row Fila de la bola
   * @param col Columna de la bola
   * @param x   Pixel horizontal para realizar el pintado
   * @param y   Pixel vertical para realizar el pintado
   */
  private void paintBallSolution(Graphics g, int row, int col, int x, int y) {

    if (solutionRow != null

        // Fila != actual, comprobación de color
        && ((row != current[GameConstants.X_POSITION]
            && solutionRow.getBalls()[col].getColor().equals(rows[row].getBalls()[col].getColor()))

            // Fila == actual, comprobación de color de la fila anterior
            || (row == current[GameConstants.X_POSITION]
                && solutionRow.getBalls()[col].getColor().equals(rows[row - 1].getBalls()[col].getColor())))) {

      Graphics2D g2d = (Graphics2D) g;

      g2d.setColor(Color.GREEN);

      // Dibuja el cuadrado
      g2d.setStroke(new BasicStroke(GameConstants.BORDER_STROKE));
      g2d.drawRect(x - 5, y - 5, GameConstants.DIAMETER + 10, GameConstants.DIAMETER + 10);
    }

  }
}