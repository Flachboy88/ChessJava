package Jeu;

public class Fou extends Piece {
    public Fou(Couleur couleur, Position position) {
        super(couleur, position);
    }

    @Override
    public boolean estMouvementValide(Position destination, Plateau plateau) {
        // Le fou se déplace en diagonale
        int dx = Math.abs(destination.getX() - position.getX());
        int dy = Math.abs(destination.getY() - position.getY());
        return dx == dy;
        // Note: Cette implémentation ne vérifie pas les obstacles sur le chemin
    }

    @Override
    public int getValeur() {
        return 30; // Valeur du fou
    }

    @Override
    public String getSymbole() {
        return (couleur == Couleur.BLANC) ? "♗" : "♝";
    }
}