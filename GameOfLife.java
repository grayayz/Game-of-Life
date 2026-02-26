package conwaygame;
import java.util.ArrayList;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {

        // WRITE YOUR CODE HERE
        StdIn.setFile(file);
        int r = StdIn.readInt();
        int c = StdIn.readInt();
        grid = new boolean[r][c];
        for (int i = 0; i < r; i++){
            for (int j = 0; j < c; j++){
                grid[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * 
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {

        // WRITE YOUR CODE HERE
        if (row >= 0 && row < grid.length && col >= 0 && col < grid[row].length) {
            return grid[row][col];
        } else {
            return false;
        }
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == ALIVE){
                    return true;
                }
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cellz
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors(int row, int col) {
        return getAliveNeightbors(row, col).size();
    }
    
    ///
    public ArrayList<int[]> getAliveNeightbors(int row, int col){
        ArrayList<int[]> aliveNeighbors = new ArrayList<>();
        int rows = grid.length;
        int cols = grid[0].length;
    
        // Directions for adjacent cells including diagonals
        int[][] directions = new int[][]{
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1}, {1, 0}, {1, 1}
        };
    
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            //someMethod(new int[]{2, 3});

    
            // Wrap newRow and newCol if out of bounds
            if (newRow < 0) newRow += rows;
            else if (newRow >= rows) newRow -= rows;
    
            if (newCol < 0) newCol += cols;
            else if (newCol >= cols) newCol -= cols;
    
            // Check if neighbor is alive
            if (grid[newRow][newCol] == ALIVE) {
                aliveNeighbors.add(new int[]{newRow,newCol});
            }
        }
        return aliveNeighbors;   
    }
    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        int row = grid.length;
        int col = grid[0].length;
        boolean[][] newGrid = new boolean[row][col];
        // WRITE YOUR CODE HERE
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                int aliveNeighbors =  numOfAliveNeighbors(i, j);
              /**RULES OF THE GAME:
               * ALIVE CELLS WITH NO NEIGHBORS/ONE NEIGHBOR DIES
               *DEAD CELLS WITH THREE NEIGHBORS BIRTHS
               * ALIVE CELLS WITH 2/3 NEIGHBORS SURVIVE
               * ALIVE CELLS WITH 4+ NEIGHBORS DIE OF OVERPOPULATION
               */
              if (grid[i][j] == ALIVE){
                if (aliveNeighbors <= 1 || aliveNeighbors >= 4){
                    newGrid[i][j] = DEAD;
                } else if (aliveNeighbors > 1 && aliveNeighbors <= 3){
                    newGrid[i][j] = ALIVE;
                }
              }
              else if (grid[i][j] == DEAD){
                if (aliveNeighbors == 3){
                    newGrid[i][j] = ALIVE;
                } else{
                    newGrid[i][j] = DEAD;
                }
              }
            }
        }

        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        grid = computeNewGrid();
        // WRITE YOUR CODE HERE
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        // WRITE YOUR CODE HERE
        for (int i = 0; i < n; i++){
            grid = computeNewGrid();
            totalAliveCells = getTotalAliveCells();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        // WRITE YOUR CODE HERE
        int row = grid.length;
        int col = grid[0].length;
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(row, col);
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (grid[i][j] == ALIVE) {
                    ArrayList<int[]> arr = getAliveNeightbors(i, j);
                    for (int k = 0; k < arr.size(); k++){
                        int[] coordinate = arr.get(k);
                        uf.union(coordinate[0], coordinate[1], i, j);
                    }
                    /** *
                     Union with adjacent cells (right and bottom neighbors as example)
                    if (j < col - 1 && grid[i][j + 1] == ALIVE) {
                        uf.union(i, j, i, j + 1);
                    }
                    if (i < row - 1 && grid[i + 1][j] == ALIVE) {
                        uf.union(i, j, i + 1, j);
                    }
                     Add checks for other neighbors as needed (left, top, diagonals)*/
                }
            }
        }                
        int[] roots = new int[row * col];
        int count = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (grid[i][j] == ALIVE) {
                    int root = uf.find(i, j);
                    boolean isUnique = true;
                    for (int k = 0; k < count; k++){
                        if (roots[k] == root){
                            isUnique = false;
                            break;
                        }
                    }
                    if (isUnique){
                        roots[count++] = root;
                    }
                }
            }
        }
    
        return count;
    }
}
