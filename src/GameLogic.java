public class GameLogic {
    public enum Stage { SPLASH_SCREEN, SETTINGS_SCREEN, PLAYING_FIELD_SETUP, PLAYER_TURN, NOT_PLAYER_TURN, GAME_END_SCREEN }
    public enum GameResult { PLAYER_WIN, AI_WIN, DRAW }
    public static GameResult result;

    static Stage stage = Stage.SPLASH_SCREEN;

    static SettingsScreen settingsScreen;
    static PlayingField playingField;
    static Position.TileColor playerPositionType;
    static Position.TileColor aiPositionType;

    public static void SetStage(Stage newStage) {
        stage = newStage;
        switch (stage) {
            case SETTINGS_SCREEN -> {
                if (settingsScreen == null) settingsScreen = new SettingsScreen();
                FiveInARow.frame.getContentPane().removeAll();
                FiveInARow.frame.getContentPane().add(settingsScreen);
                FiveInARow.frame.pack();
            }
            case PLAYING_FIELD_SETUP -> {
                BattleField.Initialize();
                if (Settings.playerColor == 0){
                    playerPositionType = Position.TileColor.BLACK;
                    aiPositionType = Position.TileColor.WHITE;
                } else {
                    playerPositionType = Position.TileColor.WHITE;
                    aiPositionType = Position.TileColor.BLACK;
                }
                if (playingField == null) playingField = new PlayingField();
                playingField.Reset();
                FiveInARow.frame.getContentPane().removeAll();
                FiveInARow.frame.getContentPane().add(playingField);
                FiveInARow.frame.pack();
                SetStage(Settings.playerColor == 0 ? Stage.PLAYER_TURN : Stage.NOT_PLAYER_TURN);
            }
            case PLAYER_TURN -> playingField.AddListeners();
            case NOT_PLAYER_TURN -> {
                playingField.RemoveListeners();
                AIMove();
            }
            case GAME_END_SCREEN -> {
                FiveInARow.frame.getContentPane().removeAll();
                FiveInARow.frame.getContentPane().add(GameEndScreen.GetGameEndScreen(result));
                FiveInARow.frame.pack();
            }
        }
    }

    public static void PlayerMove(int x, int y) {
        if(x >= Settings.widthInput || y >= Settings.heightInput || Position.grid[x][y].HasColor()) return;
        Position.grid[x][y].SetColor(playerPositionType);
        switch (BattleField.ChangeTile(x, y, playerPositionType)) {
            case DRAW -> {
                result = GameResult.DRAW;
                SetStage(Stage.GAME_END_SCREEN);
            }
            case WIN -> {
                result = GameResult.PLAYER_WIN;
                SetStage(Stage.GAME_END_SCREEN);
            }
            case CONTINUE -> {
                SetStage(Stage.NOT_PLAYER_TURN);
            }
        }
    }

    public static Stage GetStage() {
        return stage;
    }

    public static void AIMove(){
        Position.bestStrategy.SetColor(aiPositionType);
        switch (BattleField.ChangeTile(Position.bestStrategy.x, Position.bestStrategy.y, aiPositionType)) {
            case DRAW -> {
                result = GameResult.DRAW;
                SetStage(Stage.GAME_END_SCREEN);
            }
            case WIN -> {
                result = GameResult.AI_WIN;
                SetStage(Stage.GAME_END_SCREEN);
            }
            case CONTINUE -> {
                SetStage(Stage.PLAYER_TURN);
            }
        }
    }
}
