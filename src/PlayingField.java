import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;

public class PlayingField extends JPanel {

    MouseListener listener;

    Color background = new Color(118, 114, 114);
    Color lines = new Color(0, 0, 0);
    Color blackTransparent = new Color(0,0,0,128);
    Color blackOpaque = new Color(0,0,0, 255);
    Color whiteTransparent = new Color(255, 255, 255,128);
    Color whiteOpaque = new Color(255, 255, 255, 255);

    Color playerTransparent;

    public PlayingField() {
        setPreferredSize(new Dimension(Settings.widthInput * Settings.tileSize, Settings.heightInput * Settings.tileSize));
        listener = new MouseListener(this);
    }

    public void ResetColors() {
        playerTransparent = Settings.playerColor == 0 ? blackTransparent : whiteTransparent;
    }

    public void AddListeners() {
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void RemoveListeners() {
        removeMouseListener(listener);
        removeMouseMotionListener(listener);
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
            if (e.getButton() != MouseEvent.BUTTON1) return;
            lastX = e.getX();
            lastY = e.getY();
            GameLogic.PlayerMove(lastX/Settings.tileSize, lastY/Settings.tileSize);
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

        if (GameLogic.GetStage() == GameLogic.Stage.PlayerTurn) {
            g2.setColor(playerTransparent);
            g2.fillOval(listener.lastX/Settings.tileSize*Settings.tileSize,
                        listener.lastY/Settings.tileSize*Settings.tileSize,Settings.tileSize,Settings.tileSize);
        }
    }

    public void clear(Graphics g) {
        g.setColor(background);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
