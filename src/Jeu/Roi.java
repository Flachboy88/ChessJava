package Jeu;

public class Roi extends Piece {
    private boolean aBouge; // Pour vérifier si le roque est possible

    public Roi(Couleur couleur, Position position) {
        super(couleur, position);
        this.aBouge = false;
    }

    @Override
    public boolean estMouvementValide(Position destination, Plateau plateau) {
        // Vérifie si le roi se déplace d'une seule case dans n'importe quelle direction
        int dx = Math.abs(destination.getX() - position.getX());
        int dy = Math.abs(destination.getY() - position.getY());

        // Mouvement de base du roi (une case dans n'importe quelle direction)
        boolean mouvementBasic = (dx <= 1 && dy <= 1 && (dx != 0 || dy != 0));

        // Vérifier si la case d'arrivée contient une pièce de même couleur
        Piece pieceDestination = plateau.getPiece(destination);
        if (pieceDestination != null && pieceDestination.getCouleur() == this.couleur) {
            return false; // Ne peut pas capturer ses propres pièces
        }

        // TODO: Ajouter la logique pour le roque

        return mouvementBasic;
    }

    public boolean getABouge() {
        return aBouge;
    }

    public void setABouge(boolean aBouge) {
        this.aBouge = aBouge;
    }

    @Override
    public int getValeur() {
        return 900; // Valeur du roi (très élevée car c'est la pièce la plus importante)
    }

    @Override
    public String getSymbole() {
        return (couleur == Couleur.BLANC) ? "♔" : "♚";
    }
}