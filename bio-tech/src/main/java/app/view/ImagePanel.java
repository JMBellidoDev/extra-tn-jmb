
package app.view;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/** JPanel adaptado para cargar una imagen */
public class ImagePanel extends JPanel {

  /** SerialVersionUID */
  private static final long serialVersionUID = 1L;

  /** Imagen a cargar en archivo */
  private transient BufferedImage imagen;

  /**
   * Setter - imagen. Establece la imagen del grafo de la ruta constante
   * @throws IOException
   */
  public void setImagen() throws IOException {

    // Carga la imagen desde el archivo
    imagen = ImageIO.read(new File(ViewConstants.GRAPH_PATH));
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Se dibuja la imagen en el JPanel
    if (imagen != null) {
      g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
    }
  }

}
