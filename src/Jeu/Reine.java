package Jeu;

public class Reine extends Piece {
    public Reine(Couleur couleur, Position position) {
        super(couleur, position);
    }

    @Override
    public boolean estMouvementValide(Position destination, Plateau plateau) {
        // La reine se déplace comme une tour OU un fou
        // Mouvement horizontal/vertical (comme une tour)
        boolean mouvementTour = (position.getX() == destination.getX() || position.getY() == destination.getY());

        // Mouvement diagonal (comme un fou)
        int dx = Math.abs(destination.getX() - position.getX());
        int dy = Math.abs(destination.getY() - position.getY());
        boolean mouvementFou = (dx == dy);

        return mouvementTour || mouvementFou;
        // Note: Cette implémentation ne vérifie pas les obstacles sur le chemin
    }

    @Override
    public int getValeur() {
        return 90; // Valeur de la reine
    }

    @Override
    public String getSymbole() {
        return (couleur == Couleur.BLANC) ? "♕" : "♛";
    }
}