import java.util.ArrayList;
import java.util.List;

public class Position {

    public static Position[][] grid;

    public int x;
    public int y;
    public int potentialBattlefields;
    public enum TileColor {BLACK, WHITE, EMPTY}
    public TileColor tileColor = TileColor.EMPTY;
    private int priority;
    public List<BattleField> validBattleFields = new ArrayList<>();
    private boolean invalidated;

    static Position bestStrategy;
    static int bestStrategyValue;

    public static void Initialize() {
        grid = new Position[Settings.widthInput][Settings.heightInput];
        for (int x = 0; x < Settings.widthInput; x++) {
            for (int y = 0; y < Settings.heightInput; y++) {
                grid[x][y] = new Position(x, y);
            }
        }
    }

    private Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.invalidated = true;
        this.potentialBattlefields = PotentialBattleFields(x,y);
    }

    public static int PotentialBattleFields(int x, int y){
        int xProximity = Math.min(Settings.tilesInARowToWin, Math.min(x, Settings.widthInput - 1 - x));
        int yProximity = Math.min(Settings.tilesInARowToWin, Math.min(y, Settings.widthInput - 1 - y));
        return xProximity + yProximity + Math.min(xProximity, yProximity) + Math.max(0, xProximity + yProximity - Settings.tilesInARowToWin);
    }

    public void UpdateBattlefield(BattleField updatedBattleField) {
        if (priority == -1) return;
        if (updatedBattleField.state == BattleField.State.DRAW) {
            validBattleFields.remove(updatedBattleField);
        }
        invalidated = true;
    }

    public void AddBattlefield(BattleField newBattleField) {
        if (priority == -1) return;
        validBattleFields.add(newBattleField);
        potentialBattlefields--;
        invalidated = true;
    }

    public void SetColor(TileColor tileColor) {
        this.tileColor = tileColor;
        priority = -1;
        validBattleFields = null;
        invalidated = false;
    }

    public boolean HasColor() {
        return this.tileColor != TileColor.EMPTY;
    }

    /**
     * @return true if the game is a draw or false if the game should continue
     */
    public static boolean RecalculatePriorities() {
        bestStrategy = null;
        bestStrategyValue = 0;
        double randomMagicNumber = 2;
        for (Position[] positions : grid) {
            for (Position position : positions) {
                if (!position.HasColor()) randomMagicNumber = position.RecalculatePriority(randomMagicNumber);
            }
        }
        return bestStrategyValue == 0;
    }

    private double RecalculatePriority(double randomMagicNumber) {
        if (invalidated) {
            priority = potentialBattlefields;
            for (BattleField battlefield : validBattleFields) priority += battlefield.value * battlefield.value * battlefield.value;
            invalidated = false;
        }
        if (bestStrategyValue < priority) {
            bestStrategy = this;
            bestStrategyValue = priority;
            randomMagicNumber = 2;
        } else if (bestStrategyValue == priority) {
            if(Math.random() < 1d/randomMagicNumber) bestStrategy = this;
            randomMagicNumber++;
        }
        return randomMagicNumber;
    }
}
