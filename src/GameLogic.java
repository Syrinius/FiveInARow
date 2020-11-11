public class GameLogic {
    public enum Stage {SplashScreen, SettingsScreen, PlayingFieldSetup, PlayerTurn, NotPlayerTurn, GameEndScreen}
    public enum PositionType { white, black, empty }
    public static PositionType[][] grid;

    private static Stage stage = Stage.SplashScreen;

    public static void SetStage(Stage newStage) {
        stage = newStage;
        if (stage == Stage.PlayingFieldSetup) {
            grid = new PositionType[Settings.widthInput][Settings.heightInput];
            clearGrid();
        }
    }

    public static Stage GetStage() {
        return stage;
    }

    public static void clearGrid() {
        for(int i = 0; i < grid.length; i++) {
            for(int j = 0; j < grid[i].length; j++) {
                grid[i][j] = PositionType.empty;
            }
        }
    }
}
