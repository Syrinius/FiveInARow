import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PlayingField extends JPanel {
    public MouseListener listener;

    public Color background = new Color(118, 114, 114);
    public Color lines = new Color(0, 0, 0);
    public Color blackTransparent = new Color(0,0,0,128);
    public Color blackOpaque = new Color(0,0,0, 255);
    public Color whiteTransparent = new Color(255, 255, 255,128);
    public Color whiteOpaque = new Color(255, 255, 255, 255);

    public PlayingField() {
        setPreferredSize(new Dimension(Settings.widthInput * Settings.tileSize, Settings.heightInput * Settings.tileSize));
        listener = new MouseListener(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    class MouseListener implements MouseInputListener {

        public int lastX;
        public int lastY;
        private PlayingField parent;

        public MouseListener(PlayingField parent) {
            this.parent = parent;
        }

        @Override
        public void mouseDragged(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
            parent.repaint();
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            lastX = e.getX();
            lastY = e.getY();
            if (e.getButton() == MouseEvent.BUTTON1) {
                GameLogic.grid[lastX/Settings.tileSize][lastY/Settings.tileSize] = GameLogic.PositionType.black;
            } else {
                GameLogic.grid[lastX/Settings.tileSize][lastY/Settings.tileSize] = GameLogic.PositionType.white;
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}
    }

    @Override
    public void paint(Graphics g) {
        clear(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(Settings.tileSize/10f));
        g2.setColor(lines);
        for (int i = 0;i<Settings.widthInput;i++){
            g2.drawLine(Settings.tileSize/2+i*Settings.tileSize,0,Settings.tileSize/2+i*Settings.tileSize,Settings.heightInput*Settings.tileSize);
        }
        for (int i = 0;i<Settings.heightInput;i++){
            g2.drawLine(0,Settings.tileSize/2+i*Settings.tileSize,Settings.widthInput*Settings.tileSize,Settings.tileSize/2+i*Settings.tileSize);
        }

        //render positions
        for(int i = 0; i < GameLogic.grid.length; i++) {
            for(int j = 0; j < GameLogic.grid[i].length; j++) {
                switch (GameLogic.grid[i][j]) {
                    case black:
                        g2.setColor(blackOpaque);
                        g2.fillOval(i*Settings.tileSize,j*Settings.tileSize,Settings.tileSize,Settings.tileSize);
                        break;
                    case white:
                        g2.setColor(whiteOpaque);
                        g2.fillOval(i*Settings.tileSize,j*Settings.tileSize,Settings.tileSize,Settings.tileSize);
                        break;
                    case empty:
                        break;
                }
            }
        }



        //preview
        g2.setColor(blackTransparent);
        g2.fillOval(listener.lastX/Settings.tileSize*Settings.tileSize,listener.lastY/Settings.tileSize*Settings.tileSize,Settings.tileSize,Settings.tileSize);
    }

    public void clear(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
