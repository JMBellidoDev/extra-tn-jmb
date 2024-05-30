
package app.view;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.controller.GameActions;
import app.model.Row;
import app.model.Score;
import app.model.persistence.ScorePersistenceSQLite;
import app.model.persistence.ScorePersistenceSQLiteException;
import app.view.utils.GameConstants;

public class Game {

  /** Logger */
  private static final Logger LOGGER = LoggerFactory.getLogger(app.view.Game.class);

  /** Temporizador */
  private Timer timer;

  /** Nombre del jugador */
  private String player;

  /** JFrame */
  private JFrame frame;

  /** Botón para seleccionar una bola */
  private JButton btnSelect;

  /** Botón para mover hacia la izquierda la posición del cursor actual */
  private JButton btnLeft;

  /** Botón para mover hacia la derecha la posición del cursor actual */
  private JButton btnRight;

  /** Botón para comprobar la fila con la solución */
  private JButton btnCheck;

  /** Etiqueta Temporizador */
  private JLabel lblTimer;

  /** Etiqueta Puntuación */
  private JLabel lblScore;

  /** Array de filas del juego */
  private Row[] rows;

  /** Acciones permitidas para el juego */
  private GameActions gameActions;

  /** Fila solución del juego */
  private Row solutionRow;

  /** Fila inicial del juego */
  private Row initialRow;

  /** Elemento seleccionado del juego */
  private int[] selectedElement;

  /** Elemento en el que se encuentra el cursor actualmente */
  private int[] currentElement;

  /** Panel grid de bolas */
  private BallGridPanel ballPanel;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {

      try {
        Game window = new Game();
        window.frame.setVisible(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  /**
   * Create the application.
   */
  public Game() {

    LOGGER.info("Inicio del juego\n");

    // Se generan la clase de acciones del juego y la fila solución e inicial
    gameActions = new GameActions(new ScorePersistenceSQLite(GameConstants.DB_PATH));

    solutionRow = gameActions.generateRandomRow();
    initialRow = gameActions.generateRandomRow();

    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {

    // Nombre del jugador
    player = JOptionPane.showInputDialog("Introduzca el nombre del jugador");

    // JFrame
    frame = new JFrame();
    frame.setBounds(100, 100, 530, 790);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Creación del array de filas inicial
    initializeRows();

    // Panel personalizado con las bolas
    currentElement = GameConstants.INITIAL_CURRENT_POSITION;
    ballPanel = new BallGridPanel(solutionRow, rows, currentElement);

    // Botones
    createButtons();

    // Etiquetas necesarias para el temporizador y la puntuación
    createLabels();

    // GroupLayout y personalización
    GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
    prepareGroupLayout(groupLayout);

    // Inicialización del temporizador y definición de su evento
    setTimerEvent();

    frame.getContentPane().setLayout(groupLayout);
  }

  /** Inicializa las primeras filas necesarias para el juego (solución, inicio y primera fila de juego) */
  private void initializeRows() {
    rows = new Row[GameConstants.ROW_QUANTITY];
    rows[0] = initialRow;
    rows[1] = gameActions.cloneRow(initialRow);

    for (int i = 2; i < 11; i++) {
      rows[i] = Row.generateGrayRow();
    }
  }

  /** Creación de los botones necesarios para el juego */
  private void createButtons() {

    // Botón Izquierda
    createBtnLeft();

    // Botón Derecha
    createBtnRight();

    // Botón seleccionar / modificar
    btnSelect = new JButton(GameConstants.BTN_SELECT_TEXT);
    btnSelect.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 12));

    // Evento del botón Select
    btnSelect.addActionListener(e -> createBtnSelectEvent());

    // Botón check del resultado
    btnCheck = new JButton(GameConstants.BTN_CHECK_TEXT);
    btnCheck.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 12));

    // Evento del botón Check
    btnCheck.addActionListener(e -> createBtnCheckEvent());

    LOGGER.debug("Los botones han sido creados correctamente\n");
  }

  /** Crea el botón que modifica la posición del cursor hacia la izquierda */
  private void createBtnLeft() {
    btnLeft = new JButton("<==");
    btnLeft.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 12));

    // Evento para el botón izquierda
    btnLeft.addActionListener(e -> {
      currentElement = gameActions.getNewCoordinates(currentElement, 0);
      ballPanel.setCurrent(currentElement);
      ballPanel.repaint();
    });
  }

  /** Crea el botón que modifica la posición del cursor hacia la derecha */
  private void createBtnRight() {
    btnRight = new JButton("==>");
    btnRight.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 12));

    // Evento para el botón derecha
    btnRight.addActionListener(e -> {
      currentElement = gameActions.getNewCoordinates(currentElement, 1);
      ballPanel.setCurrent(currentElement);
      ballPanel.repaint();
    });
  }

  /** Crea el evento para el botón Select (Seleccionar) */
  private void createBtnSelectEvent() {

    // Si no hay nada seleccionado, se selecciona y se cambia el texto del botón
    if (selectedElement == null) {

      selectedElement = new int[] { currentElement[0], currentElement[1] };
      ballPanel.setSelected(selectedElement);

      btnSelect.setText(GameConstants.BTN_SELECT_MODIFIED_TEXT);

      // Si hay elemento seleccionado, se intercambian y se restablecen elementos
    } else {
      Row swapped = gameActions.swapBallsFromRow(rows[selectedElement[0]], selectedElement[1], currentElement[1]);
      btnSelect.setText(GameConstants.BTN_SELECT_TEXT);

      ballPanel.setRows(swapped, currentElement[0]);

      selectedElement = null;
      ballPanel.setSelected(null);
    }

    ballPanel.repaint();

  }

  /** Crea el evento para el botón Check */
  private void createBtnCheckEvent() {

    int numberOfRow = currentElement[0];

    // Se comprueba si se ha ganado
    if (gameActions.checkWin(solutionRow, rows[currentElement[0]])) {

      JOptionPane.showMessageDialog(ballPanel, "Has ganado!!");

      // Se establece la siguiente línea para que el usuario vea el orden correcto ya formado y se finaliza
      Row nextRow = rows[numberOfRow].cloneRow();
      ballPanel.setRows(nextRow, numberOfRow + 1);

      ballPanel.repaint();

      // Se para el temporizador
      timer.stop();

      endGame();

    } else {

      // Se itera el juego si aún hay filas libres. Sino, el usuario ha perdido
      iterateNextRowNoWin(numberOfRow);

    }

  }

  /**
   * Itera la siguiente fila para seguir jugando ya que el usuario no ha ganado en esta ronda
   * @param numberOfRow Numero de fila actual
   */
  private void iterateNextRowNoWin(int numberOfRow) {

    if (numberOfRow != rows.length - 1) {

      // Se restablecen elementos y se avanza en las filas
      Row nextRow = rows[numberOfRow].cloneRow();
      ballPanel.setRows(nextRow, numberOfRow + 1);

      currentElement = new int[] { currentElement[0] + 1, 0 };
      ballPanel.setCurrent(currentElement);

      selectedElement = null;
      ballPanel.setSelected(selectedElement);

      btnSelect.setText(GameConstants.BTN_SELECT_TEXT);

      // Se pierden 100 puntos por check
      lblScore.setText(String.valueOf(Integer.parseInt(lblScore.getText()) - GameConstants.SCORE_LOST_PER_CHECK));

      // Se repinta el panel
      ballPanel.repaint();

    } else {
      // Se para el temporizador
      timer.stop();

      // Se informa al usuario y se finaliza el juego
      JOptionPane.showMessageDialog(ballPanel, "Has pedido!! :(\nPuntuación: 0");
      lblScore.setText("0");

      endGame();
    }
  }

  /** Finaliza el juego */
  private void endGame() {

    // Desactiva todos los botones
    btnSelect.setEnabled(false);
    btnLeft.setEnabled(false);
    btnRight.setEnabled(false);
    btnCheck.setEnabled(false);

    LOGGER.info("Parada de temporizador\n");

    // Se almacena la puntuación y se muestra la puntuación obtenida y las almacenadas
    try {
      gameActions.saveScore(new Score(player, Integer.parseInt(lblScore.getText())));
      JOptionPane.showMessageDialog(ballPanel, gameActions.getBestScores() + "\nPuntuación: " + lblScore.getText());

    } catch (ScorePersistenceSQLiteException e) {
      JOptionPane.showMessageDialog(ballPanel, "Se produjo un error durante el almacenamiento de puntuaciones");
    }

    LOGGER.debug("Persistencia de datos realizada con éxito\n");
    LOGGER.info("Fin del juego\n");
  }

  /** Creación de las etiquetas de puntuación y temporizador */
  private void createLabels() {
    lblScore = new JLabel(String.valueOf(GameConstants.INITIAL_SCORE));
    lblScore.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 14));

    lblTimer = new JLabel(GameConstants.INITIAL_TIME + "s");
    lblTimer.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 14));
  }

  /**
   * Personaliza el groupLayout junto con las etiquetas necesarias
   * @param groupLayout GroupLayout del JFrame principal
   */
  private void prepareGroupLayout(GroupLayout groupLayout) {

    JLabel lblScoreText = new JLabel("Puntuación:");
    lblScoreText.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 14));

    JLabel lblTimerText = new JLabel("Tiempo:");
    lblTimerText.setFont(new Font(GameConstants.LETTER_FAMILY, Font.BOLD, 14));

    // Componentes horizontales
    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)

        .addGroup(groupLayout.createSequentialGroup()

            .addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)

                .addGroup(groupLayout.createSequentialGroup().addContainerGap().addComponent(ballPanel,
                    GroupLayout.PREFERRED_SIZE, 430, GroupLayout.PREFERRED_SIZE))

                .addGroup(groupLayout.createSequentialGroup().addGap(70)
                    .addComponent(lblTimerText, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(lblTimer, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
                    .addComponent(lblScoreText, GroupLayout.PREFERRED_SIZE, 116, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(lblScore, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE).addGap(50))

                .addGroup(Alignment.LEADING,
                    groupLayout.createSequentialGroup().addGap(40).addComponent(btnLeft).addGap(14)
                        .addComponent(btnSelect, GroupLayout.PREFERRED_SIZE, 142, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(btnCheck, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(btnRight, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(272, Short.MAX_VALUE)));

    // Componentes verticales
    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING)

        .addGroup(groupLayout.createSequentialGroup().addGap(30)
            .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblTimerText).addComponent(lblTimer)
                .addComponent(lblScoreText).addComponent(lblScore))
            .addContainerGap(30, Short.MAX_VALUE))

        .addGroup(groupLayout.createSequentialGroup().addGap(80)
            .addComponent(ballPanel, GroupLayout.PREFERRED_SIZE, 565, GroupLayout.PREFERRED_SIZE).addGap(31)

            .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
                .addComponent(btnSelect, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addComponent(btnLeft)
                .addComponent(btnRight, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addComponent(btnCheck))
            .addContainerGap(95, Short.MAX_VALUE)));
  }

  /** Crea y establece el evento del temporizador */
  private void setTimerEvent() {
    timer = new Timer(1000, e -> {

      // Se establece el tiempo añadiendo 1 segundo al actual y se repinta el panel
      lblTimer.setText(
          String.valueOf(Integer.parseInt(lblTimer.getText().substring(0, lblTimer.getText().length() - 1)) + 1) + "s");

      // Se eliminan 5 puntos de la puntuación total por segundo tardado
      lblScore.setText(String.valueOf(Integer.parseInt(lblScore.getText()) - GameConstants.SCORE_LOST_PER_SECOND));

    });

    timer.start();

    LOGGER.debug("Temporizador iniciado con éxito\n");
  }
}
