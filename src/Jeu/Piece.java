package Jeu;

public abstract class Piece {
    public enum Couleur {
        BLANC, NOIR
    }

    protected Couleur couleur;
    protected Position position;

    public Piece(Couleur couleur, Position position) {
        this.couleur = couleur;
        this.position = position;
    }

    public Couleur getCouleur() {
        return couleur;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    // Méthode abstraite pour vérifier si un mouvement est valide pour cette pièce
    public abstract boolean estMouvementValide(Position destination, Plateau plateau);

    // Méthode abstraite pour obtenir la valeur de la pièce (utile pour l'IA)
    public abstract int getValeur();

    // Méthode abstraite pour obtenir le symbole de la pièce (pour l'affichage)
    public abstract String getSymbole();
}