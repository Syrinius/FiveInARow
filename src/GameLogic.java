import java.util.Arrays;

public class GameLogic {
    public enum Stage {SplashScreen, SettingsScreen, PlayingFieldSetup, PlayerTurn, NotPlayerTurn, GameEndScreen}
    public enum PositionType { white, black, empty }
    public enum GameResult {playerWin, aiWin, draw }
    public static PositionType[][] grid;

    static Stage stage = Stage.SplashScreen;

    static SettingsScreen settingsScreen;
    static PlayingField playingField;
    static PositionType playerPositionType;
    static PositionType aiPositionType;

    public static void SetStage(Stage newStage) {
        stage = newStage;
        switch (stage) {
            case SettingsScreen -> {
                if (settingsScreen == null) settingsScreen = new SettingsScreen();
                FiveInARow.frame.getContentPane().removeAll();
                FiveInARow.frame.getContentPane().add(settingsScreen);
                FiveInARow.frame.pack();
            }
            case PlayingFieldSetup -> {
                grid = new PositionType[Settings.widthInput][Settings.heightInput];
                clearGrid();
                if (Settings.playerColor == 0){
                    playerPositionType = PositionType.black;
                    aiPositionType = PositionType.white;
                } else {
                    playerPositionType = PositionType.white;
                    aiPositionType = PositionType.black;
                }
                if (playingField == null) playingField = new PlayingField();
                playingField.ResetColors();
                FiveInARow.frame.getContentPane().removeAll();
                FiveInARow.frame.getContentPane().add(playingField);
                FiveInARow.frame.pack();
                SetStage(Settings.playerColor == 0 ? Stage.PlayerTurn : Stage.NotPlayerTurn);
            }
            case PlayerTurn -> playingField.AddListeners();
            case NotPlayerTurn -> playingField.RemoveListeners();
            case GameEndScreen -> {
                FiveInARow.frame.getContentPane().removeAll();
                FiveInARow.frame.getContentPane().add(GameEndScreen.GetGameEndScreen(GameResult.aiWin)); //TODO: implement this shit
                FiveInARow.frame.pack();
            }
        }
    }

    public static void PlayerMove(int x, int y) {
        if(grid[x][y] != PositionType.empty) return;
        grid[x][y] = playerPositionType;
        if (gameEndCondition()) SetStage(Stage.GameEndScreen);
        else SetStage(Stage.NotPlayerTurn);
    }

    public static boolean gameEndCondition(){ //TODO: implement this shit as well
        return true;
    }

    public static Stage GetStage() {
        return stage;
    }

    public static void clearGrid() {
        for (PositionType[] positionTypes : grid) Arrays.fill(positionTypes, PositionType.empty);
    }
}
