
package app.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;

import app.model.Polimero;
import app.model.PolimeroTester;

/** Vista de la aplicación */
public class AppView {

  /** Frame de Polimeros */
  private JFrame frmPolimeros;

  /** Campo donde se va a introducir la cadena de monomeros del polímero */
  private JTextField txtFieldPolimero;

  /** Botón que realiza la acción entre monómeros */
  private JButton btnReaccion;

  /** Botón que calcula la dominancia del polímero */
  private JButton btnDominancia;

  /** Botón que realiza la acción de introducir un polímero */
  private JButton btnIntroducir;

  /** Panel del JFrame que contendrá el grafo del polímero */
  private ImagePanel panel;

  /** Label para introducir el polímero */
  private JLabel lblPolimeroText;

  /** Polímero introducido por el usuario */
  private Polimero polimero;

  /**
   * Método main
   * @param args Argumentos
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(() -> {

      try {
        AppView window = new AppView();
        window.frmPolimeros.setVisible(true);

      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }

  /** Constructor */
  public AppView() {

    initialize();
  }

  /** Inicializa la aplicación */
  private void initialize() {

    // Frame
    frmPolimeros = new JFrame();
    frmPolimeros.setTitle("Polímeros");
    frmPolimeros.setBounds(100, 100, 486, 514);
    frmPolimeros.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // Creación de botones
    creaEtiquetasYTextos();
    creaBotones();

    // GroupLayout
    GroupLayout groupLayout = new GroupLayout(frmPolimeros.getContentPane());
    panel = new ImagePanel();

    customizeHorizontalGroupLayout(groupLayout);
    customizeVerticalGroupLayout(groupLayout);

    frmPolimeros.getContentPane().setLayout(groupLayout);
  }

  /** Crea las etiquetas y textos necesarios para la aplicación */
  private void creaEtiquetasYTextos() {

    // Label del texto de Polímero */
    lblPolimeroText = new JLabel("Polímero:");
    lblPolimeroText.setFont(new Font(ViewConstants.TEXT_FONT, Font.BOLD, ViewConstants.TEXT_SIZE));

    // TextField para el Polímero (Monómeros)
    txtFieldPolimero = new JTextField();
    txtFieldPolimero.setFont(new Font(ViewConstants.TEXT_FONT, Font.BOLD, ViewConstants.TEXT_SIZE));
    txtFieldPolimero.setColumns(10);
  }

  /** Crea los botones necesarios para la aplicación */
  private void creaBotones() {

    // Botón para introducir un nuevo polímero
    btnIntroducir = new JButton("Introducir Polimero");
    btnIntroducir.setFont(new Font("Tahoma", Font.BOLD, ViewConstants.TEXT_SIZE));

    // Evento del botón introducir
    btnIntroducir.addActionListener(e -> creaEventoBtnIntroducir());

    // Botón de reacción
    btnReaccion = new JButton("Reacción");
    btnReaccion.setFont(new Font(ViewConstants.TEXT_FONT, Font.BOLD, ViewConstants.TEXT_SIZE));
    btnReaccion.setEnabled(false);

    // Evento del botón de reacción
    btnReaccion.addActionListener(e -> creaEventoBtnReaccion());

    // Botón de comprobación de dominancia
    btnDominancia = new JButton("Dominancia");
    btnDominancia.setFont(new Font("Tahoma", Font.BOLD, ViewConstants.TEXT_SIZE));
    btnDominancia.setEnabled(false);

    btnDominancia.addActionListener(e -> creaEventoBtnDominancia());

  }

  /**
   * Crea el evento para el botón Introducir Polimero. Habilita el botón Reacción, deshabilita el de dominancia y realiza el
   * grafo del Polímero
   */
  private void creaEventoBtnIntroducir() {

    // Se pinta el grafo del polímero
    String monomeros = txtFieldPolimero.getText();

    if (PolimeroTester.isMonomerosValido(monomeros)) {
      polimero = new Polimero(monomeros);

      pintaGrafo(polimero);

      // Se habilita el botón de reaccionar y deshabilita el de dominancia
      btnReaccion.setEnabled(true);
      btnDominancia.setEnabled(false);

    } else {
      JOptionPane.showMessageDialog(panel,
          "El polímero introducido tiene caracteres no permitidos. Deben ser letras A-Z y a-z sin incluir la Ñ");
    }

  }

  /**
   * Crea el evento para el botón Reacción. Sólo debe actuar después de introducir un Polímero. Habilita el botón de
   * dominancia, reduce el Polímero y lo dibuja
   */
  private void creaEventoBtnReaccion() {

    // Se produce la reacción
    polimero = polimero.reacciona();

    // Se pinta el grafo y se habilita el botón de dominancia
    pintaGrafo(polimero);
    btnDominancia.setEnabled(true);

  }

  /**
   * Crea el evento para el botón que comprueba la Dominancia. Sólo debe actuar sobre un Polímero reducido. Avisa al usuario
   * de la dominancia obtenida del Polímero
   */
  private void creaEventoBtnDominancia() {

    // Se comprueba la dominancia y se avisa al usuario
    int dominancia = PolimeroTester.getDominancia(polimero);

    String dominanciaStr = switch (dominancia) {
      case 2 -> "positiva";
      case 1 -> "neutra";
      default -> "negativa";
    };

    JOptionPane.showMessageDialog(panel, String.format("El polímero %s tiene dominancia %s", polimero, dominanciaStr));

  }

  private void pintaGrafo(Polimero polimero) {

    DefaultDirectedGraph<String, DefaultEdge> g = generaGrafo(polimero);

    // Se construye el grafo con un adaptador externo de JGraphX
    JGraphXAdapter<String, DefaultEdge> graphAdapter = new JGraphXAdapter<>(g);
    mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
    layout.execute(graphAdapter.getDefaultParent());

    // Se pinta la imagen del grafo
    BufferedImage image = mxCellRenderer.createBufferedImage(graphAdapter, null, 1.3, Color.WHITE, true, null);
    File imgFile = new File(ViewConstants.GRAPH_PATH);

    try {
      imgFile.createNewFile();
      ImageIO.write(image, ViewConstants.GRAPH_IMG_TYPE, imgFile);

      panel.setImagen();
      panel.repaint();

    } catch (IOException e) {
      JOptionPane.showMessageDialog(panel, "No se pudo generar el grafo");
    }
  }

  /**
   * Genera un nuevo grafo de los monómeros que componen al polímero dado
   * @param polimero Polímero sobre el que trabajar
   * @return DefaultDirectedGraph
   */
  private DefaultDirectedGraph<String, DefaultEdge> generaGrafo(Polimero polimero) {

    DefaultDirectedGraph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

    // Se obtienen los monómeros y se añaden como vértices al grafo
    char[] monomeros = polimero.getMonomeros().toCharArray();
    List<String> monomerosUnicos = generaMonomerosUnicosDibujo(monomeros);

    for (int i = 0; i < monomeros.length; i++) {
      g.addVertex(monomerosUnicos.get(i));

      if (i > 0) {
        g.addEdge(monomerosUnicos.get(i - 1), monomerosUnicos.get(i));
      }
    }
    return g;
  }

  /**
   * Genera una lista de monómeros únicos expresamente para ser dibujados
   * @param monomeros Array de monómeros a diferenciar
   * @return List(String)
   */
  private List<String> generaMonomerosUnicosDibujo(char[] monomeros) {

    List<String> resultado = new LinkedList<>();

    // Se comprueba si
    for (char monomero : monomeros) {
      StringBuilder monomeroStr = new StringBuilder(Character.toString(monomero));

      while (resultado.contains(monomeroStr.toString())) {
        monomeroStr.append('\'');
      }

      resultado.add(monomeroStr.toString());
    }

    return resultado;
  }

  /**
   * Personaliza el groupLayout horizontalmente añadiendo y estableciendo todos los elementos
   * @param groupLayout GroupLayout
   */
  private void customizeHorizontalGroupLayout(GroupLayout groupLayout) {

    groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)

        .addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.LEADING)

            // Se añade el JPanel
            .addGroup(groupLayout.createSequentialGroup().addGap(23).addComponent(panel, GroupLayout.PREFERRED_SIZE, 434,
                GroupLayout.PREFERRED_SIZE))

            // Se añaden el label + textField
            .addGroup(groupLayout.createSequentialGroup().addGap(66)
                .addComponent(lblPolimeroText, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(txtFieldPolimero, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE))

            // Se añaden los botones reaccion y dominancia
            .addGroup(groupLayout.createSequentialGroup().addGap(56)
                .addComponent(btnReaccion, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE).addGap(53)
                .addComponent(btnDominancia, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))

            // Se añade el botón introducir polimero
            .addGroup(groupLayout.createSequentialGroup().addGap(87).addComponent(btnIntroducir, GroupLayout.PREFERRED_SIZE,
                286, GroupLayout.PREFERRED_SIZE)))

            .addContainerGap(502, Short.MAX_VALUE)));
  }

  /**
   * Personaliza el groupLayout verticalmente añadiendo y estableciendo todos los elementos
   * @param groupLayout GroupLayout
   */
  private void customizeVerticalGroupLayout(GroupLayout groupLayout) {

    groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)

        // Se añade el JPanel
        .addGroup(groupLayout.createSequentialGroup().addContainerGap(37, Short.MAX_VALUE)
            .addComponent(panel, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE).addGap(18)

            // Se añaden el label + textField
            .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblPolimeroText).addComponent(
                txtFieldPolimero, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
            .addGap(18).addComponent(btnIntroducir, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE).addGap(15)

            // Se añaden los botones reaccion y dominancia
            .addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnReaccion)
                .addComponent(btnDominancia, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
            .addGap(55)));
  }
}
