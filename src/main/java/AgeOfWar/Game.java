package AgeOfWar;

import AgeOfWar.Graphics.BackGroundScreens;
import AgeOfWar.Graphics.GameGraphics;
import AgeOfWar.Logic.MainLogic;

public class Game {
    public static void main(String[] args) {
        // Initialize the necessary objects
        MainLogic logic = new MainLogic(); // Create the MainLogic object
        BackGroundScreens backGroundScreens = new BackGroundScreens(); // Create the Background Screens
        // Create the GameGraphics window, passing the mainLogic object
        GameGraphics window = new GameGraphics();
    }//
}
