package AgeOfWar.Logic;

public enum GameState {
    INTRO(1),       // Game is in the intro screen
    IN_GAME(2),     // Game is running
    GAME_OVER(3),   // Player lost
    ENEMY_WIN(4);   // Enemy won

    private final int stateCode;

    GameState(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getStateCode() {
        return stateCode;
    }

    public static GameState fromCode(int code) {
        for (GameState state : values()) {
            if (state.getStateCode() == code) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid state code: " + code);
    }
}
