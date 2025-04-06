package Jeu;

public class Position {
    private int x; // Colonne (0-7)
    private int y; // Ligne (0-7)

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Constructeur alternatif avec notation échecs (ex: "e4")
    public Position(String notation) {
        if (notation.length() != 2) {
            throw new IllegalArgumentException("Format de position invalide");
        }
        this.x = notation.charAt(0) - 'a';
        this.y = 8 - (notation.charAt(1) - '0');
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toNotation() {
        char colonne = (char) ('a' + x);
        char ligne = (char) ('1' + (7 - y));
        return "" + colonne + ligne;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Position)) return false;
        Position other = (Position) obj;
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return x * 8 + y;
    }

    @Override
    public String toString() {
        return toNotation();
    }
}