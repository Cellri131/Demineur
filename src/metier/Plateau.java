package metier;
import java.util.Random;

public class Plateau {
    public final int size;
    public final int nbMines;
    public final Case[][] cases;
    public boolean gameOver = false;
    public boolean firstClick = true;

    public Plateau(int size) {
        this.size = size;
        this.nbMines = Math.max(1, size * size / 6);
        this.cases = new Case[size][size];
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                cases[y][x] = new Case();
    }

    public void placerMines(int avoidX, int avoidY) {
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                cases[y][x].reset();
        Random rand = new Random();
        int placed = 0;
        while (placed < nbMines) {
            int x = rand.nextInt(size);
            int y = rand.nextInt(size);
            if ((x == avoidX && y == avoidY) || cases[y][x].isMine) continue;
            cases[y][x].isMine = true;
            placed++;
        }
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (cases[y][x].isMine) continue;
                int count = 0;
                for (int dy = -1; dy <= 1; dy++)
                    for (int dx = -1; dx <= 1; dx++) {
                        int nx = x + dx, ny = y + dy;
                        if (nx >= 0 && nx < size && ny >= 0 && ny < size && cases[ny][nx].isMine)
                            count++;
                    }
                cases[y][x].number = count;
            }
        }
    }

    public boolean checkWin() {
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                if (!cases[y][x].isMine && !cases[y][x].revealed)
                    return false;
        gameOver = true;
        return true;
    }
}
