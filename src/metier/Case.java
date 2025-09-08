package metier;

public class Case {
    public boolean isMine = false;
    public int number = 0;
    public boolean revealed = false;
    public boolean flagged = false;

    public void reset() {
        isMine = false;
        number = 0;
        revealed = false;
        flagged = false;
    }
}
