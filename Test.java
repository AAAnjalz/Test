
import java.util.Random;
import java.util.Scanner;

public class Test{
    static final String COMMANDS = "ulpdrq";
    static int playerScore = 0; // To store player's score
    static boolean gamePlaying = true; // Shows whether the game is being played or it's over.
    static int coins = 15; // Number of coins available in the game.
    static int numEnemies = 2; // Number of enemies in the game.
    final static int MAX_ENEMIES = 10;
    final static int NUM_ROWS = 8; // Number of rows in the map.
    final static int NUM_COLS = 16; // Number of columns in the map.
    final static int[] xs = new int[100]; // Stores column.
    final static int[] ys = new int[100]; // Stores row.
    static int playerX = 0; // Player's X coordinate
    static int playerY = 0; // Player's Y coordinate
    static int[] coinXs = new int[10]; // Adjust size as needed
    static int[] coinYs = new int[10];
    static int numCoins = 10; // Actual number of coins placed
    static int numRocks = 5;
    static int numWalls;
    static int[] enemiesX = new int[10];
    static int[] enemiesY = new int[10];

    public static void main(String[] args) {
        Random rand = new Random();
        Scanner sc = new Scanner(System.in);

        numWalls = placeWalls(xs, ys, 0);
        numWalls = placeManyRandomly(xs, ys, numWalls, numRocks, rand);

        placeCoins(rand, coinXs, coinYs, numCoins);
        placeEnemies(rand, enemiesX, enemiesY, numEnemies);

        map(NUM_COLS, NUM_ROWS, xs, ys, numWalls,coinXs,coinYs,numCoins, playerX, playerY, enemiesX, enemiesY, numEnemies);

        System.out.println("Please enter a command: ");
        String userInput = sc.nextLine().toLowerCase();

        while (!(userInput.equals("q"))) {
            String[] userCommand = userInput.split("");
            String[] validCommands = COMMANDS.split("");

            if (!(validateInput(userCommand, validCommands))) {
                System.out.println("Please enter a valid command: ");
            } else {
                processCommands(userCommand,rand);
                map(NUM_COLS, NUM_ROWS, xs, ys, numWalls,coinXs,coinYs,numCoins, playerX, playerY, enemiesX, enemiesY, numEnemies);
                
            }

            if (gamePlaying) {
                moveEnemies(rand,enemiesX, enemiesY, numEnemies,  playerX, playerY, xs, ys, numWalls);
                userInput = sc.nextLine().toLowerCase();
            } else {
                break;
            }
        }
    }

    public static void map(int NUM_COLS, int NUM_ROWS, int[] xs, int[] ys, int numWalls, int[] coinXs, int[] coinYs, int numCoins, int playerX, int playerY, int[] enemiesX, int[] enemiesY, int numEnemies) {
        System.out.print("SCORE: " + playerScore + "   ");
        if (gamePlaying) {
            System.out.println("(GAMEPLAYING " + ", COINS: " + numCoins + ", ENEMIES: " + numEnemies + ")");
        } else {
            System.out.println("(GAMEOVER " + ", COINS: " + numCoins + ", ENEMIES: " + numEnemies + ")");
        }
    
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (search(xs, ys, numWalls, j, i) != -1) {
                    System.out.print("X"); // Wall or object
                } else if (search(enemiesX, enemiesY, numEnemies, j, i) != -1) {
                    System.out.print("E"); // Enemy
                } else if (j == playerX && i == playerY) {
                    System.out.print("P"); // Player
                } else if (search(coinXs, coinYs, numCoins, j, i) != -1) {
                    System.out.print("."); // Coin
                } else {
                    System.out.print(" "); // Empty space
                }
            }
            System.out.println();
    }
}

    public static boolean validateInput(String[] userInput, String[] validInput) {
        for (int i = 0; i < userInput.length; i++) {
            if (userInput[i].equals(" ")) {
                continue;
            }
            boolean isValid = false;
            for (int j = 0; j < validInput.length; j++) {
                if (userInput[i].equals(validInput[j])) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                return false;
            }
        }
        return true;
    }

    public static void processCommands(String[] userCommand, Random rand) {
        for (int i = 0; i < userCommand.length; i++) {
            int newX = playerX;
            int newY = playerY;
    
            switch (userCommand[i]) {
                case "u":
                    System.out.println("Move up");
                    newY--;
                    break;
                case "d":
                    System.out.println("Move down");
                    newY++;
                    break;
                case "l":
                    System.out.println("Move left");
                    newX--;
                    break;
                case "r":
                    System.out.println("Move right");
                    newX++;
                    break;
                case "p":
                    System.out.println("Printing game board!");
                    break;
                case "q":
                    System.out.println("Quitting game...");
                    gamePlaying = false;
                    return; // Exit the method to stop processing further commands.
                default:
                    System.out.println("Please enter a valid command.");
                    continue; // Skip further processing for invalid commands.
            }
    
            // Check if the move is valid
            if (newX >= 0 && newX < NUM_COLS 
                    && newY >= 0 && newY < NUM_ROWS ) {
                // Update position
                boolean blockedByRocks = search(xs, ys, numRocks, newX, newY) != -1;
                boolean blockedByWall = search(xs, ys, numWalls, newX, newY) != -1;
                
                if(!blockedByRocks && !blockedByWall){
                    playerX = newX;
                    playerY = newY;
                    System.out.println("Player position: (" + playerX + ", " + playerY + ")");
                }
    
                // Check for coin collection
                int newNumCoins = remove(coinXs, coinYs, numCoins, playerX, playerY);
                if (newNumCoins < numCoins) {
                    numCoins = newNumCoins; // Update the coin count
                    playerScore += 1;
                    System.out.println("You collected a coin!");
                }else if(blockedByWall || blockedByRocks){
                    System.out.println("You cannot move there!");
                }

                int N = 2; // Set the value of N (1 in N chance)
                if (rand.nextInt(N) == 0) {  // 1 in N chance
                    System.out.println("Adding new coins to the board...");
                    int coinsToAdd = rand.nextInt(3) + 1; // Random number of coins (1-3)
                    placeCoins(rand, coinXs, coinYs, coinsToAdd);
                }

            } else {
                System.out.println("You can't move there!");
            }
        }
    }
    

    public static int placeWalls(int[] xs, int[] ys, int n) {
        for (int i = 0; i < NUM_COLS; i++) {
            n = insert(xs, ys, n, i, 0); // Top edge
        }
        for (int i = 0; i < NUM_COLS; i++) {
            n = insert(xs, ys, n, i, NUM_ROWS - 1); // Bottom edge
        }
        for (int i = 0; i < NUM_ROWS; i++) {
            n = insert(xs, ys, n, 0, i); // Left edge
        }
        for (int i = 0; i < NUM_ROWS; i++) {
            n = insert(xs, ys, n, NUM_COLS - 1, i); // Right edge
        }
        return n;
    }

    public static int search(int[] xs, int[] ys, int numWalls, int x, int y) {
        int i = 0;
        for (i = 0; i < numWalls; i++) {
            if (xs[i] == x && ys[i] == y) {
                return i; // Found a wall or object
            }
        }
        return -1; // Empty space
    }

    public static int insert(int[] xs, int[] ys, int numWalls, int x, int y) {
        if (numWalls < xs.length) {
            xs[numWalls] = x;
            ys[numWalls] = y;
            return numWalls + 1; // Return updated number of walls
        }
        return numWalls;
    }

    // Updated method to place player and rocks (or other objects) randomly
    public static int placeManyRandomly(int[] xs, int[] ys, int numWalls, int coordCount, Random rand) {
        int maxFails = 5; // Maximum consecutive failed attempts allowed
        int fails = 0;
        boolean playerPlaced = false;

        // Try placing the player first
        while (!playerPlaced && fails < maxFails) {
            int x = rand.nextInt(NUM_COLS - 2) + 1; // Random x inside walls
            int y = rand.nextInt(NUM_ROWS - 2) + 1; // Random y inside walls

            if (search(xs, ys, numWalls, x, y) == -1 && search(coinXs, coinYs, numCoins, x, y) == -1 ) { // Check if the location is empty
                playerX = x;
                playerY = y;
                playerPlaced = true; // Player placed successfully
            } else {
                fails++; // Increment consecutive failed attempts
            }
        }

        if (!playerPlaced) {
            System.out.println("Too many failed attempts while placing player. Stopping...");
            return numWalls;
        }

        // Now place the random objects (e.g., rocks)
        while (coordCount > 0 && fails < maxFails) {
            int x = rand.nextInt(NUM_COLS - 2) + 1; // Random x inside walls
            int y = rand.nextInt(NUM_ROWS - 2) + 1; // Random y inside walls

            if (search(xs, ys, numWalls, x, y) == -1 || search(enemiesX, enemiesY, numEnemies, x, y)==-1 ) { // Check if the location is empty
                numWalls = insert(xs, ys, numWalls, x, y); // Place the rock (or object)
                coordCount--; // Decrease the remaining objects count
                fails = 0; // Reset fails counter since placement succeeded
            } else {
                fails++; // Increment consecutive failed attempts
            }
        }

        // If placement fails too many times for the rocks, return early
        if (fails >= maxFails) {
            System.out.println("Too many failed attempts while placing rocks. Stopping...");
        }

        return numWalls; // Return the updated number of walls (including placed objects)
    }
    public static void placeCoins(Random rand, int[] coinXs, int[] coinYs, int totalCoins) {
        int placedCoins = 0; // Coins successfully placed
        int maxFails = 5;
        int fails = 0;
    
        while (placedCoins < totalCoins && fails < maxFails) {
            int x = rand.nextInt(NUM_COLS - 2) + 1; // Random x inside walls
            int y = rand.nextInt(NUM_ROWS - 2) + 1; // Random y inside walls
    
            // Check for conflicts with walls or already-placed coins
            if (search(xs, ys, totalCoins, x, y) == -1 && search(coinXs, coinYs, placedCoins, x, y) == -1) {
                coinXs[placedCoins] = x;
                coinYs[placedCoins] = y;
                placedCoins++;
                fails = 0; // Reset fails after successful placement
            } else {
                fails++;
            }
        }
    
        if (fails >= maxFails) {
            System.out.println("Failed to place all coins. Coins placed: " + placedCoins);
        }
    }

    public static int remove(int[] xs, int[] ys, int n, int x, int y) {
        for (int i = 0; i < n; i++) {
            if (xs[i] == x && ys[i] == y) {
                for (int j = i; j < n - 1; j++) {
                    xs[j] = xs[j + 1];
                    ys[j] = ys[j + 1];
                }
                return n - 1; // Decrease the count of elements
            }
        }
        return n; // No removal if not found
    }

    public static void placeEnemies(Random rand, int[] enemiesX, int[] enemiesY, int numEnemies) {
        int placedEnemies = 0;
        int maxFails = 5;
        int fails = 0;

        while (placedEnemies < numEnemies && fails < maxFails) {
            int x = rand.nextInt(NUM_COLS - 2) + 1; // Random x inside walls
            int y = rand.nextInt(NUM_ROWS - 2) + 1; // Random y inside walls

            // Ensure no conflicts with walls, coins, or other enemies
            if (search(xs, ys, numWalls, x, y) == -1 
                    && search(coinXs, coinYs, numCoins, x, y) == -1 
                    && search(enemiesX, enemiesY, placedEnemies, x, y) == -1
                    && (x != playerX || y != playerY))  {
                enemiesX[placedEnemies] = x;
                enemiesY[placedEnemies] = y;
                placedEnemies++;
                fails = 0; // Reset fails counter after success
            } else {
                fails++;
            }
        }


        if (fails >= maxFails) {
            System.out.println("Failed to place all enemies. Enemies placed: " + placedEnemies);
        }
    }


    public static int getBestDirection(int enemyX, int enemyY, int playerX, int playerY){
        //0 is for moving up, 1 is for moving down, 2 is for left and 3 is for right
        double minDistance = 10000;
        int bestDirection = -1;

        // Move Up
        int newX = enemyX;
        int newY = enemyY -1;
        double distance = Math.sqrt(Math.pow(playerX - newX, 2) + Math.pow(playerY - newY, 2));
        if (distance < minDistance) {
            minDistance = distance;
            bestDirection = 0; 
        }

        // Down
        newX = enemyX;
        newY = enemyY + 1;
        distance = Math.sqrt(Math.pow(playerX - newX, 2) + Math.pow(playerY - newY, 2));
        if (distance < minDistance) {
        minDistance = distance;
        bestDirection = 1; // Down
    }

        // Left
        newX = enemyX - 1;
        newY = enemyY;
        distance = Math.sqrt(Math.pow(playerX - newX, 2) + Math.pow(playerY - newY, 2));
        if (distance < minDistance) {
        minDistance = distance;
        bestDirection = 2; // Left
    }

        // Right
        newX = enemyX + 1;
        newY = enemyY;
        distance = Math.sqrt(Math.pow(playerX - newX, 2) + Math.pow(playerY - newY, 2));
        if (distance < minDistance) {
        minDistance = distance;
        bestDirection = 3; // Right
        }

    return bestDirection;
}

public static void moveEnemies(Random rand, int[] enemiesX, int[] enemiesY, int numEnemies, 
                               int playerX, int playerY, int[] xs, int[] ys, int numWalls) {
    for (int i = 0; i < numEnemies; i++) {
        int currentX = enemiesX[i];
        int currentY = enemiesY[i];

        // Decide whether to move "smartly" or randomly (50% probability each)
        int direction;
        boolean random = rand.nextBoolean();
        if (random) {
            direction = getBestDirection(currentX, currentY, playerX, playerY);
        } else {
            direction = rand.nextInt(4); // Random direction
        }

        // Calculate the new position based on the chosen direction
        int newX = currentX, newY = currentY;
        if (direction == 0) { // Up
            newY -= 1;
        } else if (direction == 1) { // Down
            newY += 1;
        } else if (direction == 2) { // Left
            newX -= 1;
        } else if (direction == 3) { // Right
            newX += 1;
        }
        // System.out.println("Checking position: Enemy (" + newX + ", " + newY + ") Player (" + playerX + ", " + playerY + ")");
        
        // Check if the new position is valid
        boolean isValidMove = 
            search(xs, ys, numWalls, newX, newY) == -1 && // Not a wall
            search(enemiesX, enemiesY, numEnemies, newX, newY) == -1//Not another enemy
            && search(coinXs, coinYs, numCoins, newX, newY) == -1; // Not a coin
            
        if (isValidMove) {
            if (newX == playerX && newY == playerY) {
                System.out.println("GAME OVER!");
                enemiesX[i] = newX;
                enemiesY[i] = newY;
                gamePlaying = false;
                break;
            }
            // Move the enemy
            enemiesX[i] = newX;
            enemiesY[i] = newY;

            // Check if the enemy caught the player

            int M = 8;  // Set the value of M (1 in M chance to clone)
            if (rand.nextInt(M) == 0) {  // 1 in M chance
                cloneEnemy(newX, newY);
            }
        }

    }
}
public static void cloneEnemy(int x, int y) {
    // Add a new enemy at the same positions
if(numEnemies < MAX_ENEMIES) {
    enemiesX[numEnemies] = x;
    enemiesY[numEnemies] = y;
    numEnemies++;
    System.out.println("New enemy added at (" + x + ", " + y + ")");
}
    
}
}




        
    
    
    

        
    
    
    
    


                
        
