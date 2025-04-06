package Jeu;

public abstract class Plateau {
    public static final int TAILLE = 8; // Taille standard d'un échiquier

    // Méthodes abstraites à implémenter par les sous-classes
    public abstract Piece getPiece(Position position);
    public abstract void placerPiece(Piece piece, Position position);
    public abstract void enleverPiece(Position position);
    public abstract boolean estPositionValide(Position position);
    public abstract Plateau copier(); // Pour créer une copie du plateau (utile pour l'IA)

    // Méthode pour déplacer une pièce
    public void deplacerPiece(Position depart, Position arrivee) {
        Piece piece = getPiece(depart);
        if (piece != null) {
            enleverPiece(depart);
            placerPiece(piece, arrivee);
            piece.setPosition(arrivee);
        }
    }

    // Méthode pour exécuter un coup
    public void executerCoup(Coup coup) {
        if (coup.isPetitRoque()) {
            // Exécuter petit roque
            // TODO: Implémentation du petit roque
        } else if (coup.isGrandRoque()) {
            // Exécuter grand roque
            // TODO: Implémentation du grand roque
        } else if (coup.isPriseEnPassant()) {
            // Exécuter prise en passant
            // TODO: Implémentation de la prise en passant
        } else if (coup.isPromotion()) {
            // Exécuter promotion
            deplacerPiece(coup.getDepart(), coup.getArrivee());
            enleverPiece(coup.getArrivee());
            placerPiece(coup.getPromotionPiece(), coup.getArrivee());
        } else {
            // Coup standard
            deplacerPiece(coup.getDepart(), coup.getArrivee());
        }

        // Mettre à jour l'état des pièces si nécessaire
        Piece piece = getPiece(coup.getArrivee());
        if (piece instanceof Roi) {
            ((Roi) piece).setABouge(true);
        } else if (piece instanceof Tour) {
            ((Tour) piece).setABouge(true);
        } else if (piece instanceof Pion) {
            ((Pion) piece).setAPremierCoup(false);
        }
    }

    // Méthode pour initialiser le plateau avec les pièces en position initiale
    public void initialiser() {
        // Placer les pièces blanches
        placerPiece(new Tour(Piece.Couleur.BLANC, new Position(0, 7)), new Position(0, 7));
        placerPiece(new Cavalier(Piece.Couleur.BLANC, new Position(1, 7)), new Position(1, 7));
        placerPiece(new Fou(Piece.Couleur.BLANC, new Position(2, 7)), new Position(2, 7));
        placerPiece(new Reine(Piece.Couleur.BLANC, new Position(3, 7)), new Position(3, 7));
        placerPiece(new Roi(Piece.Couleur.BLANC, new Position(4, 7)), new Position(4, 7));
        placerPiece(new Fou(Piece.Couleur.BLANC, new Position(5, 7)), new Position(5, 7));
        placerPiece(new Cavalier(Piece.Couleur.BLANC, new Position(6, 7)), new Position(6, 7));
        placerPiece(new Tour(Piece.Couleur.BLANC, new Position(7, 7)), new Position(7, 7));

        for (int i = 0; i < 8; i++) {
            placerPiece(new Pion(Piece.Couleur.BLANC, new Position(i, 6)), new Position(i, 6));
        }

        // Placer les pièces noires
        placerPiece(new Tour(Piece.Couleur.NOIR, new Position(0, 0)), new Position(0, 0));
        placerPiece(new Cavalier(Piece.Couleur.NOIR, new Position(1, 0)), new Position(1, 0));
        placerPiece(new Fou(Piece.Couleur.NOIR, new Position(2, 0)), new Position(2, 0));
        placerPiece(new Reine(Piece.Couleur.NOIR, new Position(3, 0)), new Position(3, 0));
        placerPiece(new Roi(Piece.Couleur.NOIR, new Position(4, 0)), new Position(4, 0));
        placerPiece(new Fou(Piece.Couleur.NOIR, new Position(5, 0)), new Position(5, 0));
        placerPiece(new Cavalier(Piece.Couleur.NOIR, new Position(6, 0)), new Position(6, 0));
        placerPiece(new Tour(Piece.Couleur.NOIR, new Position(7, 0)), new Position(7, 0));

        for (int i = 0; i < 8; i++) {
            placerPiece(new Pion(Piece.Couleur.NOIR, new Position(i, 1)), new Position(i, 1));
        }
    }
}