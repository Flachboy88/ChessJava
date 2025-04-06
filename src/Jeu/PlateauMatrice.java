package Jeu;

public class PlateauMatrice extends Plateau {
    private Piece[][] cases; // Représentation matricielle du plateau

    public PlateauMatrice() {
        cases = new Piece[TAILLE][TAILLE];
    }

    @Override
    public Piece getPiece(Position position) {
        if (!estPositionValide(position)) {
            return null;
        }
        return cases[position.getY()][position.getX()];
    }

    @Override
    public void placerPiece(Piece piece, Position position) {
        if (!estPositionValide(position)) {
            return;
        }
        cases[position.getY()][position.getX()] = piece;
        if (piece != null) {
            piece.setPosition(position);
        }
    }

    @Override
    public void enleverPiece(Position position) {
        if (!estPositionValide(position)) {
            return;
        }
        cases[position.getY()][position.getX()] = null;
    }

    @Override
    public boolean estPositionValide(Position position) {
        int x = position.getX();
        int y = position.getY();
        return x >= 0 && x < TAILLE && y >= 0 && y < TAILLE;
    }

    @Override
    public Plateau copier() {
        PlateauMatrice copie = new PlateauMatrice();
        for (int y = 0; y < TAILLE; y++) {
            for (int x = 0; x < TAILLE; x++) {
                Position pos = new Position(x, y);
                Piece piece = getPiece(pos);
                if (piece != null) {
                    // Créer une nouvelle instance de la pièce
                    Piece nouvellePiece;
                    if (piece instanceof Roi) {
                        nouvellePiece = new Roi(piece.getCouleur(), new Position(x, y));
                        ((Roi) nouvellePiece).setABouge(((Roi) piece).getABouge());
                    } else if (piece instanceof Reine) {
                        nouvellePiece = new Reine(piece.getCouleur(), new Position(x, y));
                    } else if (piece instanceof Tour) {
                        nouvellePiece = new Tour(piece.getCouleur(), new Position(x, y));
                        ((Tour) nouvellePiece).setABouge(((Tour) piece).getABouge());
                    } else if (piece instanceof Fou) {
                        nouvellePiece = new Fou(piece.getCouleur(), new Position(x, y));
                    } else if (piece instanceof Cavalier) {
                        nouvellePiece = new Cavalier(piece.getCouleur(), new Position(x, y));
                    } else if (piece instanceof Pion) {
                        nouvellePiece = new Pion(piece.getCouleur(), new Position(x, y));
                        ((Pion) nouvellePiece).setAPremierCoup(false); // Par défaut, on considère que ce n'est plus le premier coup
                    } else {
                        continue; // Type de pièce inconnu
                    }
                    copie.placerPiece(nouvellePiece, pos);
                }
            }
        }
        return copie;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");

        for (int y = 0; y < TAILLE; y++) {
            sb.append(8 - y).append(" ");

            for (int x = 0; x < TAILLE; x++) {
                Piece piece = cases[y][x];
                if (piece == null) {
                    sb.append(". ");
                } else {
                    sb.append(piece.getSymbole()).append(" ");
                }
            }

            sb.append(8 - y).append("\n");
        }

        sb.append("  a b c d e f g h");
        return sb.toString();
    }
}