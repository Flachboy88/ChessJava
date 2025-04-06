package Jeu;

public class Tour extends Piece {
    private boolean aBouge; // Pour vérifier si le roque est possible

    public Tour(Couleur couleur, Position position) {
        super(couleur, position);
        this.aBouge = false;
    }

    @Override
    public boolean estMouvementValide(Position destination, Plateau plateau) {
        // La tour se déplace horizontalement ou verticalement
        return position.getX() == destination.getX() || position.getY() == destination.getY();
        // Note: Cette implémentation ne vérifie pas les obstacles sur le chemin
    }

    public boolean getABouge() {
        return aBouge;
    }

    public void setABouge(boolean aBouge) {
        this.aBouge = aBouge;
    }

    @Override
    public int getValeur() {
        return 50; // Valeur de la tour
    }

    @Override
    public String getSymbole() {
        return (couleur == Couleur.BLANC) ? "♖" : "♜";
    }
}