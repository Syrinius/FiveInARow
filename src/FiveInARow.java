import javax.swing.*;

public class FiveInARow {

    static JFrame frame;
    static SettingsScreen settingsScreen;
    static PlayingField playingField;

    public static void main (String[] args){
        frame = new JFrame("FiveInARow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        settingsScreen = new SettingsScreen();
        frame.getContentPane().add(settingsScreen);
        frame.pack();
        frame.setVisible(true);
    }

    public static void startPlaying() {
        GameLogic.SetStage(GameLogic.Stage.PlayingFieldSetup);
        frame.getContentPane().remove(settingsScreen);
        playingField = new PlayingField();
        frame.getContentPane().add(playingField);
        frame.pack();
    }
}
