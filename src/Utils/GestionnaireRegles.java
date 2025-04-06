package Utils;

import Jeu.Coup;
import Jeu.Piece;
import Jeu.Plateau;
import Jeu.Position;
import Jeu.Roi;

import java.util.ArrayList;
import java.util.List;

public class GestionnaireRegles {
    // Singleton pour le gestionnaire de règles
    private static GestionnaireRegles instance;

    private GestionnaireRegles() {
        // Constructeur privé pour le pattern Singleton
    }

    public static GestionnaireRegles getInstance() {
        if (instance == null) {
            instance = new GestionnaireRegles();
        }
        return instance;
    }

    /**
     * Vérifie si un coup est légal selon les règles des échecs
     * @param coup Le coup à vérifier
     * @param plateau L'état actuel du plateau
     * @return true si le coup est légal, false sinon
     */
    public boolean estCoupLegal(Coup coup, Plateau plateau) {
        Position depart = coup.getDepart();
        Position arrivee = coup.getArrivee();
        Piece piece = coup.getPiece();

        // Vérifier que les positions sont valides
        if (!estPositionValide(depart) || !estPositionValide(arrivee)) {
            return false;
        }

        // Vérifier qu'il y a bien une pièce à la position de départ
        if (plateau.getPiece(depart) == null) {
            return false;
        }

        // Vérifier que la pièce à la position d'arrivée n'est pas de la même couleur
        Piece pieceArrivee = plateau.getPiece(arrivee);
        if (pieceArrivee != null && pieceArrivee.getCouleur() == piece.getCouleur()) {
            return false;
        }

        // Vérifier si la pièce peut faire ce mouvement selon ses règles spécifiques
        if (!piece.estMouvementValide(arrivee, plateau)) {
            return false;
        }

        // Vérifier s'il y a des pièces sur le chemin (sauf pour le cavalier)
        if (!estCheminLibre(coup, plateau)) {
            return false;
        }

        // Vérifier si le coup ne laisse pas le roi du joueur en échec
        if (laisseRoiEnEchec(coup, plateau)) {
            return false;
        }

        // Vérifier les règles spéciales (roque, prise en passant, etc.)
        if (coup.isPetitRoque() || coup.isGrandRoque()) {
            return estRoquePossible(coup, plateau);
        }

        if (coup.isPriseEnPassant()) {
            return estPriseEnPassantPossible(coup, plateau);
        }

        return true;
    }

    private boolean estPositionValide(Position pos) {
        return pos.getX() >= 0 && pos.getX() < Plateau.TAILLE &&
                pos.getY() >= 0 && pos.getY() < Plateau.TAILLE;
    }

    /**
     * Vérifie s'il n'y a pas de pièces sur le chemin entre la position de départ et d'arrivée
     * @param coup Le coup à vérifier
     * @param plateau L'état actuel du plateau
     * @return true si le chemin est libre, false sinon
     */
    private boolean estCheminLibre(Coup coup, Plateau plateau) {
        Piece piece = coup.getPiece();
        Position depart = coup.getDepart();
        Position arrivee = coup.getArrivee();

        // Le cavalier peut sauter par-dessus les pièces
        if (piece instanceof Jeu.Cavalier) {
            return true;
        }

        int dx = Integer.compare(arrivee.getX() - depart.getX(), 0);
        int dy = Integer.compare(arrivee.getY() - depart.getY(), 0);

        // Parcourir le chemin entre la position de départ et d'arrivée
        Position pos = new Position(depart.getX() + dx, depart.getY() + dy);
        while (!pos.equals(arrivee)) {
            if (plateau.getPiece(pos) != null) {
                return false; // Une pièce bloque le chemin
            }
            pos = new Position(pos.getX() + dx, pos.getY() + dy);
        }

        return true;
    }

    /**
     * Vérifie si un coup laisse le roi du joueur en échec
     * @param coup Le coup à vérifier
     * @param plateau L'état actuel du plateau
     * @return true si le roi est en échec après le coup, false sinon
     */
    private boolean laisseRoiEnEchec(Coup coup, Plateau plateau) {
        // Créer une copie du plateau pour simuler le coup
        Plateau plateauTemporaire = plateau.copier();

        // Exécuter le coup sur la copie
        plateauTemporaire.executerCoup(coup);

        // Vérifier si le roi du joueur est en échec
        Piece.Couleur couleurJoueur = coup.getPiece().getCouleur();
        return estRoiEnEchec(couleurJoueur, plateauTemporaire);
    }

    /**
     * Vérifie si le roi d'une couleur donnée est en échec
     * @param couleur La couleur du roi à vérifier
     * @param plateau L'état actuel du plateau
     * @return true si le roi est en échec, false sinon
     */
    public boolean estRoiEnEchec(Piece.Couleur couleur, Plateau plateau) {
        // Trouver la position du roi
        Position positionRoi = trouverPositionRoi(couleur, plateau);
        if (positionRoi == null) {
            return false; // Pas de roi trouvé (ne devrait pas arriver dans une partie normale)
        }

        // Vérifier si une pièce adverse peut atteindre la position du roi
        for (int y = 0; y < Plateau.TAILLE; y++) {
            for (int x = 0; x < Plateau.TAILLE; x++) {
                Position pos = new Position(x, y);
                Piece piece = plateau.getPiece(pos);

                if (piece != null && piece.getCouleur() != couleur) {
                    // Vérifier si cette pièce adverse peut atteindre le roi
                    if (piece.estMouvementValide(positionRoi, plateau) && estCheminLibre(new Coup(pos, positionRoi, piece), plateau)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Trouve la position du roi d'une couleur donnée
     * @param couleur La couleur du roi à trouver
     * @param plateau L'état actuel du plateau
     * @return La position du roi, ou null s'il n'est pas trouvé
     */
    private Position trouverPositionRoi(Piece.Couleur couleur, Plateau plateau) {
        for (int y = 0; y < Plateau.TAILLE; y++) {
            for (int x = 0; x < Plateau.TAILLE; x++) {
                Position pos = new Position(x, y);
                Piece piece = plateau.getPiece(pos);

                if (piece instanceof Roi && piece.getCouleur() == couleur) {
                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Vérifie si un roque est possible
     * @param coup Le coup de roque à vérifier
     * @param plateau L'état actuel du plateau
     * @return true si le roque est possible, false sinon
     */
    private boolean estRoquePossible(Coup coup, Plateau plateau) {
        Piece piece = coup.getPiece();

        // Vérifier que la pièce est bien un roi
        if (!(piece instanceof Roi)) {
            return false;
        }

        Roi roi = (Roi) piece;
        Position posRoi = coup.getDepart();
        Piece.Couleur couleur = roi.getCouleur();

        // Vérifier que le roi et la tour concernée n'ont pas encore bougé
        if (roi.getABouge()) {
            return false;
        }

        // Déterminer la position de la tour concernée et les cases intermédiaires
        Position posTour;
        List<Position> casesIntermediaires = new ArrayList<>();

        if (coup.isPetitRoque()) {
            // Petit roque (côté roi)
            posTour = new Position(7, posRoi.getY());
            casesIntermediaires.add(new Position(5, posRoi.getY()));
            casesIntermediaires.add(new Position(6, posRoi.getY()));
        } else {
            // Grand roque (côté reine)
            posTour = new Position(0, posRoi.getY());
            casesIntermediaires.add(new Position(1, posRoi.getY()));
            casesIntermediaires.add(new Position(2, posRoi.getY()));
            casesIntermediaires.add(new Position(3, posRoi.getY()));
        }

        // Vérifier que la tour est bien présente et n'a pas bougé
        Piece tour = plateau.getPiece(posTour);
        if (!(tour instanceof Jeu.Tour) || ((Jeu.Tour) tour).getABouge()) {
            return false;
        }

        // Vérifier que les cases intermédiaires sont vides
        for (Position pos : casesIntermediaires) {
            if (plateau.getPiece(pos) != null) {
                return false;
            }
        }

        // Vérifier que le roi n'est pas en échec, et que les cases intermédiaires
        // ne sont pas sous attaque
        if (estRoiEnEchec(couleur, plateau)) {
            return false;
        }

        // Vérifier les cases que le roi traverse
        for (Position pos : casesIntermediaires) {
            // Simuler le déplacement du roi sur cette case
            Plateau plateauTemp = plateau.copier();
            plateauTemp.enleverPiece(posRoi);
            plateauTemp.placerPiece(roi, pos);

            // Vérifier si le roi serait en échec
            if (estRoiEnEchec(couleur, plateauTemp)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Vérifie si une prise en passant est possible
     * @param coup Le coup de prise en passant à vérifier
     * @param plateau L'état actuel du plateau
     * @return true si la prise en passant est possible, false sinon
     */
    private boolean estPriseEnPassantPossible(Coup coup, Plateau plateau) {
        // TODO: Implémenter la vérification pour la prise en passant
        return false;
    }

    /**
     * Vérifie si la partie est terminée (échec et mat, pat, etc.)
     * @param couleurJoueur La couleur du joueur dont c'est le tour
     * @param plateau L'état actuel du plateau
     * @return 0 si la partie continue, 1 si victoire des blancs, -1 si victoire des noirs, 2 si pat
     */
    public int verifierFinPartie(Piece.Couleur couleurJoueur, Plateau plateau) {
        // Vérifier s'il y a échec
        boolean echec = estRoiEnEchec(couleurJoueur, plateau);

        // Vérifier si le joueur peut faire un coup légal
        if (!peutFaireUnCoupLegal(couleurJoueur, plateau)) {
            if (echec) {
                // Échec et mat - le joueur actuel a perdu
                // Si le joueur blanc a perdu, c'est -1 (victoire des noirs)
                // Si le joueur noir a perdu, c'est 1 (victoire des blancs)
                return (couleurJoueur == Piece.Couleur.BLANC) ? -1 : 1;
            } else {
                // Pat
                return 2;
            }
        }

        // La partie continue
        return 0;
    }

    /**
     * Vérifie si un joueur peut faire au moins un coup légal
     * @param couleur La couleur du joueur
     * @param plateau L'état actuel du plateau
     * @return true si au moins un coup légal est possible, false sinon
     */
    private boolean peutFaireUnCoupLegal(Piece.Couleur couleur, Plateau plateau) {
        // Parcourir toutes les pièces du joueur
        for (int y = 0; y < Plateau.TAILLE; y++) {
            for (int x = 0; x < Plateau.TAILLE; x++) {
                Position depart = new Position(x, y);
                Piece piece = plateau.getPiece(depart);

                if (piece != null && piece.getCouleur() == couleur) {
                    // Essayer tous les déplacements possibles pour cette pièce
                    for (int destY = 0; destY < Plateau.TAILLE; destY++) {
                        for (int destX = 0; destX < Plateau.TAILLE; destX++) {
                            Position arrivee = new Position(destX, destY);
                            Coup coup = new Coup(depart, arrivee, piece);

                            // Si un coup légal est trouvé, retourner true
                            if (estCoupLegal(coup, plateau)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false; // Aucun coup légal n'a été trouvé
    }

    /**
     * Génère tous les coups légaux possibles pour un joueur
     * @param couleur La couleur du joueur
     * @param plateau L'état actuel du plateau
     * @return Une liste de tous les coups légaux possibles
     */
    public List<Coup> genererCoupsLegaux(Piece.Couleur couleur, Plateau plateau) {
        List<Coup> coupsLegaux = new ArrayList<>();

        // Parcourir toutes les pièces du joueur
        for (int y = 0; y < Plateau.TAILLE; y++) {
            for (int x = 0; x < Plateau.TAILLE; x++) {
                Position depart = new Position(x, y);
                Piece piece = plateau.getPiece(depart);

                if (piece != null && piece.getCouleur() == couleur) {
                    // Essayer tous les déplacements possibles pour cette pièce
                    for (int destY = 0; destY < Plateau.TAILLE; destY++) {
                        for (int destX = 0; destX < Plateau.TAILLE; destX++) {
                            Position arrivee = new Position(destX, destY);
                            Coup coup = new Coup(depart, arrivee, piece);

                            // Vérifier si la case d'arrivée contient une pièce de même couleur
                            Piece pieceArrivee = plateau.getPiece(arrivee);
                            if (pieceArrivee != null && pieceArrivee.getCouleur() == couleur) {
                                continue; // Ne peut pas capturer ses propres pièces
                            }

                            coup = new Coup(depart, arrivee, piece);

                            // Ajouter le coup à la liste s'il est légal
                            if (estCoupLegal(coup, plateau)) {
                                coupsLegaux.add(coup);
                            }
                        }
                    }

                    // Vérifier les coups spéciaux (roque, prise en passant)
                    if (piece instanceof Roi && !((Roi) piece).getABouge()) {
                        // Petit roque
                        Coup petitRoque = new Coup(depart, new Position(6, depart.getY()), piece);
                        petitRoque.setPetitRoque(true);
                        if (estCoupLegal(petitRoque, plateau)) {
                            coupsLegaux.add(petitRoque);
                        }

                        // Grand roque
                        Coup grandRoque = new Coup(depart, new Position(2, depart.getY()), piece);
                        grandRoque.setGrandRoque(true);
                        if (estCoupLegal(grandRoque, plateau)) {
                            coupsLegaux.add(grandRoque);
                        }
                    }

                    // TODO: Ajouter la logique pour la prise en passant

                    // TODO: Ajouter la logique pour la promotion des pions
                }
            }
        }

        return coupsLegaux;
    }
}