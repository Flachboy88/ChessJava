package Jeu;

public class Cavalier extends Piece {
    public Cavalier(Couleur couleur, Position position) {
        super(couleur, position);
    }

    @Override
    public boolean estMouvementValide(Position destination, Plateau plateau) {
        // Le cavalier se déplace en L (2 cases dans une direction, puis 1 case perpendiculairement)
        int dx = Math.abs(destination.getX() - position.getX());
        int dy = Math.abs(destination.getY() - position.getY());
        return (dx == 1 && dy == 2) || (dx == 2 && dy == 1);
    }

    @Override
    public int getValeur() {
        return 30; // Valeur du cavalier
    }

    @Override
    public String getSymbole() {
        return (couleur == Couleur.BLANC) ? "♘" : "♞";
    }
}