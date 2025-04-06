package Jeu;

public class Coup {
    private Position depart;
    private Position arrivee;
    private Piece piece;
    private boolean isPetitRoque;
    private boolean isGrandRoque;
    private boolean isPriseEnPassant;
    private boolean isPromotion;
    private Piece promotionPiece;

    public Coup(Position depart, Position arrivee, Piece piece) {
        this.depart = depart;
        this.arrivee = arrivee;
        this.piece = piece;
        this.isPetitRoque = false;
        this.isGrandRoque = false;
        this.isPriseEnPassant = false;
        this.isPromotion = false;
        this.promotionPiece = null;
    }

    // Getters et setters
    public Position getDepart() {
        return depart;
    }

    public Position getArrivee() {
        return arrivee;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isPetitRoque() {
        return isPetitRoque;
    }

    public void setPetitRoque(boolean petitRoque) {
        isPetitRoque = petitRoque;
    }

    public boolean isGrandRoque() {
        return isGrandRoque;
    }

    public void setGrandRoque(boolean grandRoque) {
        isGrandRoque = grandRoque;
    }

    public boolean isPriseEnPassant() {
        return isPriseEnPassant;
    }

    public void setPriseEnPassant(boolean priseEnPassant) {
        isPriseEnPassant = priseEnPassant;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public void setPromotion(boolean promotion) {
        isPromotion = promotion;
    }

    public Piece getPromotionPiece() {
        return promotionPiece;
    }

    public void setPromotionPiece(Piece promotionPiece) {
        this.promotionPiece = promotionPiece;
    }

    /**
     * Convertit les coordonnées en notation algébrique (ex: e2-e4)
     * @return La notation algébrique du coup
     */
    public String getNotationAlgebrique() {
        if (isPetitRoque) {
            return "O-O";
        } else if (isGrandRoque) {
            return "O-O-O";
        }

        StringBuilder sb = new StringBuilder();

        // Convertir les positions en notation algébrique
        char departColonne = (char) ('a' + depart.getX());
        char departLigne = (char) ('8' - depart.getY());
        char arriveeColonne = (char) ('a' + arrivee.getX());
        char arriveeLigne = (char) ('8' - arrivee.getY());

        sb.append(departColonne).append(departLigne);
        sb.append("-");
        sb.append(arriveeColonne).append(arriveeLigne);

        // Ajouter la pièce de promotion si nécessaire
        if (isPromotion && promotionPiece != null) {
            sb.append("=");
            if (promotionPiece instanceof Reine) {
                sb.append("D");
            } else if (promotionPiece instanceof Tour) {
                sb.append("T");
            } else if (promotionPiece instanceof Fou) {
                sb.append("F");
            } else if (promotionPiece instanceof Cavalier) {
                sb.append("C");
            }
        }

        return sb.toString();
    }
}