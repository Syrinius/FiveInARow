public class BattleField {
    private static BattleField[] horizontals;
    private static BattleField[] verticals;
    private static BattleField[] topLeftDiagonals;
    private static BattleField[] topRightDiagonals;

    public enum State {BLACK, WHITE, DRAW}
    public enum GameOver { WIN, DRAW, CONTINUE }
    public State state;
    public byte value;

    public static void Initialize() {
        Position.Initialize();
        horizontals = new BattleField[Settings.heightInput*(Settings.widthInput-Settings.tilesInARowToWin+1)];
        verticals = new BattleField[Settings.widthInput*(Settings.heightInput-Settings.tilesInARowToWin+1)];
        topLeftDiagonals = new BattleField[(Settings.widthInput-Settings.tilesInARowToWin+1)*(Settings.heightInput-Settings.tilesInARowToWin+1)];
        topRightDiagonals = new BattleField[(Settings.widthInput-Settings.tilesInARowToWin+1)*(Settings.heightInput-Settings.tilesInARowToWin+1)];
        Position.RecalculatePriorities();
    }

    private BattleField(Position.TileColor tileColor) {
        switch (tileColor) {
            case BLACK -> {
                this.state = State.BLACK;
                value = Settings.playerColor == 0 ? AISettings.battlefieldInitialValueForPlayer : AISettings.battlefieldInitialValueForAI;
            }
            case WHITE -> {
                this.state = State.WHITE;
                value = Settings.playerColor != 0 ? AISettings.battlefieldInitialValueForPlayer : AISettings.battlefieldInitialValueForAI;
            }
        }
    }

    /**
     * @param tileColor color of latest move, might change the battlefield to draw state and set its value to 0
     * @return return true if Battlefield ends the game, false if otherwise
     */
    private boolean Update(Position.TileColor tileColor) {
        switch (tileColor) {
            case BLACK -> {
                if (state == State.WHITE) {
                    state = State.DRAW;
                    value = 0;
                } else value += AISettings.battlefieldValueIncrease;
            }
            case WHITE -> {
                if (state == State.BLACK) {
                    state = State.DRAW;
                    value = 0;
                } else value += AISettings.battlefieldValueIncrease;
            }
        }
        return value == (Settings.tilesInARowToWin - 1) * AISettings.battlefieldValueIncrease + AISettings.battlefieldInitialValueForPlayer ||
                value == (Settings.tilesInARowToWin - 1) * AISettings.battlefieldValueIncrease + AISettings.battlefieldInitialValueForAI;
    }

    public static GameOver ChangeTile(int x, int y, Position.TileColor tileColor) {
        if (UpdateHorizontalBattlefields(x, y, tileColor)) return GameOver.WIN;
        if (CreateVerticalBattlefields(x, y, tileColor)) return GameOver.WIN;
        if (CreateTopLeftBattlefields(x, y, tileColor)) return GameOver.WIN;
        if (CreateTopRightBattlefields(x, y, tileColor)) return GameOver.WIN;
        if (Position.RecalculatePriorities()) return GameOver.DRAW;
        return GameOver.CONTINUE;
    }

    private static boolean UpdateHorizontalBattlefields(int x, int y, Position.TileColor tileColor) {
        int battleFieldReach = Settings.tilesInARowToWin - 1;
        int convertedPosition = x + (Settings.widthInput - battleFieldReach) * y;
        int rowLength = Settings.widthInput - battleFieldReach;
        int leftFit = Math.min(battleFieldReach, x);
        int rightFit = Math.max(0, x - rowLength + 1);
        for (int i = convertedPosition - leftFit; i <= convertedPosition - rightFit; i++) {
            int initialX = i % rowLength;
            if (horizontals[i] == null) {
                horizontals[i] = new BattleField(tileColor);
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    Position.grid[initialX + j][y].AddBattlefield(horizontals[i]);
                }
            } else {
                if (horizontals[i].Update(tileColor)) return true;
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    Position.grid[initialX + j][y].UpdateBattlefield(horizontals[i]);
                }
            }
        }
        return false;
    }

    private static boolean CreateVerticalBattlefields(int x, int y, Position.TileColor tileColor) {
        int battleFieldReach = Settings.tilesInARowToWin - 1;
        int convertedPosition = y + (Settings.heightInput - battleFieldReach) * x;
        int columnLength = Settings.heightInput - battleFieldReach;
        int topFit = Math.min(battleFieldReach, y);
        int bottomFit = Math.max(0, y - columnLength + 1);
        for (int i = convertedPosition - topFit; i <= convertedPosition - bottomFit; i++) {
            int initialY = i % columnLength;
            if (verticals[i] == null) {
                verticals[i] = new BattleField(tileColor);
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    Position.grid[x][initialY + j].AddBattlefield(verticals[i]);
                }
            } else {
                if (verticals[i].Update(tileColor)) return true;
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    Position.grid[x][initialY + j].UpdateBattlefield(verticals[i]);
                }
            }
        }
        return false;
    }

    private static boolean CreateTopLeftBattlefields(int x, int y, Position.TileColor tileColor) {
        int battleFieldReach = Settings.tilesInARowToWin - 1;
        int convertedPosition = x + (Settings.widthInput - battleFieldReach) * y;
        int rowLength = Settings.widthInput - battleFieldReach;
        int step = rowLength + 1;
        int topLeftFit = Math.min(battleFieldReach, Math.min(x, y));
        int bottomRightFit = Math.max(0, Math.max(x + Settings.tilesInARowToWin - Settings.widthInput, y + Settings.tilesInARowToWin - Settings.heightInput));
        for (int i = convertedPosition - topLeftFit * step; i <= convertedPosition - bottomRightFit * step; i+=step) {
            int initialX = i % rowLength;
            int initialY = i / rowLength;
            if (topLeftDiagonals[i] == null) {
                topLeftDiagonals[i] = new BattleField(tileColor);
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    //System.out.println(String.format("i: %d, x: %d, y: %d", i, initialX + j, initialY + j));
                    Position.grid[initialX + j][initialY + j].AddBattlefield(topLeftDiagonals[i]);
                }
            } else {
                if (topLeftDiagonals[i].Update(tileColor)) return true;
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    Position.grid[initialX + j][initialY + j].UpdateBattlefield(topLeftDiagonals[i]);
                }
            }
        }
        return false;
    }

    private static boolean CreateTopRightBattlefields(int x, int y, Position.TileColor tileColor) {
        int battleFieldReach = Settings.tilesInARowToWin - 1;
        int invertedX = Settings.widthInput - 1 - x;
        int convertedPosition = invertedX + (Settings.widthInput - battleFieldReach) * y;
        int rowLength = Settings.widthInput - battleFieldReach;
        int step = rowLength + 1;
        int topRightFit = Math.min(battleFieldReach, Math.min(invertedX, y));
        int bottomLeftFit = Math.max(0, Math.max(invertedX + Settings.tilesInARowToWin - Settings.widthInput, y + Settings.tilesInARowToWin - Settings.heightInput));
        for (int i = convertedPosition - topRightFit * step; i <= convertedPosition - bottomLeftFit * step; i+=step) {
            int initialX = Settings.widthInput - 1 - (i % rowLength);
            int initialY = i / rowLength;
            if (topRightDiagonals[i] == null) {
                topRightDiagonals[i] = new BattleField(tileColor);
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    //System.out.println(String.format("i: %d, x: %d, y: %d", i, initialX - j, initialY + j));
                    Position.grid[initialX - j][initialY + j].AddBattlefield(topRightDiagonals[i]);
                }
            } else {
                if (topRightDiagonals[i].Update(tileColor)) return true;
                for (int j = 0; j < Settings.tilesInARowToWin; j++){
                    Position.grid[initialX - j][initialY + j].UpdateBattlefield(topRightDiagonals[i]);
                }
            }
        }
        return false;
    }
}
