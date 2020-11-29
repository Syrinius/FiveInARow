import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class GameEndScreen extends JPanel {

    JLabel resultDeclarationLabel;
    JLabel encouragingWordsLabel;
    JButton playAgainButton;
    JButton changeSettingsButton;

    static GameEndScreen singleton;

    public static GameEndScreen GetGameEndScreen(GameLogic.GameResult gameResult) {
        if (singleton == null) singleton = new GameEndScreen();
        singleton.Reset(gameResult);
        return singleton;
    }

    private void Reset(GameLogic.GameResult gameResult) {
        switch (gameResult) {
            case PLAYER_WIN -> {
                resultDeclarationLabel.setText("Player wins as " + (Settings.playerColor == 0 ? "black" : "white"));
                encouragingWordsLabel.setText("Congratulations!");
            }
            case DRAW -> {
                resultDeclarationLabel.setText("It's a draw");
                encouragingWordsLabel.setText("Better luck next time!");
            }
            case AI_WIN -> {
                resultDeclarationLabel.setText("AI wins as " + (Settings.playerColor != 0 ? "black" : "white"));
                encouragingWordsLabel.setText("Better luck next time!");
            }
        }
    }

    private GameEndScreen() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        resultDeclarationLabel = AddLabel("                     ");
        encouragingWordsLabel = AddLabel("                      ");
        playAgainButton = AddButton("Play Again", e -> GameLogic.SetStage(GameLogic.Stage.PLAYING_FIELD_SETUP));
        changeSettingsButton = AddButton("Change Settings", e -> GameLogic.SetStage(GameLogic.Stage.SETTINGS_SCREEN));
    }

    JButton AddButton(String name, ActionListener listener){
        JPanel container = new JPanel();
        add(container);
        JButton button = new JButton(name);
        container.add(button);
        button.addActionListener(listener);
        return button;
    }

    public JLabel AddLabel(String name){
        JPanel container = new JPanel();
        container.setLayout(new FlowLayout(FlowLayout.LEFT));
        add(container);
        JLabel label = new JLabel(name);
        label.setFont(label.getFont().deriveFont(20f));
        container.add(label);
        return label;
    }
}
