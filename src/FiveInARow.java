import javax.swing.*;

public class FiveInARow {

    public static JFrame frame;

    public static void main (String[] args){
        frame = new JFrame("FiveInARow");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameLogic.SetStage(GameLogic.Stage.SETTINGS_SCREEN);
        frame.setVisible(true);
    }
}
