package Jeu;

public class Pion extends Piece {
    private boolean premierCoup; // Pour vérifier si le pion peut avancer de deux cases

    public Pion(Couleur couleur, Position position) {
        super(couleur, position);
        this.premierCoup = true;
    }

    @Override
    public boolean estMouvementValide(Position destination, Plateau plateau) {
        int dx = destination.getX() - position.getX();
        int dy;

        // Direction du mouvement selon la couleur
        if (couleur == Couleur.BLANC) {
            dy = position.getY() - destination.getY(); // Pour les blancs, on va vers le haut (y diminue)
        } else {
            dy = destination.getY() - position.getY(); // Pour les noirs, on va vers le bas (y augmente)
        }

        // Avancer d'une case
        if (dx == 0 && dy == 1) {
            return plateau.getPiece(destination) == null;
        }

        // Avancer de deux cases (premier coup uniquement)
        if (dx == 0 && dy == 2 && premierCoup) {
            Position intermediaire;
            if (couleur == Couleur.BLANC) {
                intermediaire = new Position(position.getX(), position.getY() - 1);
            } else {
                intermediaire = new Position(position.getX(), position.getY() + 1);
            }
            return plateau.getPiece(intermediaire) == null && plateau.getPiece(destination) == null;
        }

        // Prise en diagonale
        if (Math.abs(dx) == 1 && dy == 1) {
            Piece cible = plateau.getPiece(destination);
            return cible != null && cible.getCouleur() != this.couleur;
        }

        // TODO: Ajouter la prise en passant

        return false;
    }

    public void setAPremierCoup(boolean premierCoup) {
        this.premierCoup = premierCoup;
    }

    @Override
    public int getValeur() {
        return 10; // Valeur du pion
    }

    @Override
    public String getSymbole() {
        return (couleur == Couleur.BLANC) ? "♙" : "♟";
    }
}