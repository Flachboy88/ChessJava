package Joueur;

import Jeu.Coup;
import Jeu.Partie;
import Jeu.Piece;
import Jeu.Plateau;
import Jeu.Position;
import Utils.GestionnaireRegles;

import java.util.List;
import java.util.Scanner;

public class JoueurHumain extends Joueur {
    private Scanner scanner;

    public JoueurHumain(String nom, Piece.Couleur couleur) {
        super(nom, couleur);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Coup choisirCoup(Partie partie) {
        System.out.println("\nC'est à " + this + " de jouer.");
        System.out.println(partie.getPlateau().toString());

        Coup coup = null;
        boolean coupValide = false;

        while (!coupValide) {
            try {
                System.out.print("Entrez votre coup (ex: e2-e4): ");
                String input = scanner.nextLine().trim();

                // Option pour afficher les coups possibles
                if (input.equalsIgnoreCase("help")) {
                    afficherCoupsPossibles(partie);
                    continue;
                }

                // Vérifier le format de l'entrée
                if (!input.matches("[a-h][1-8]-[a-h][1-8]")) {
                    System.out.println("Format invalide. Utilisez la notation: a1-h8");
                    continue;
                }

                // Convertir l'entrée en positions
                Position depart = convertirNotation(input.substring(0, 2));
                Position arrivee = convertirNotation(input.substring(3, 5));

                // Vérifier qu'il y a bien une pièce à la position de départ
                Piece piece = partie.getPlateau().getPiece(depart);
                if (piece == null) {
                    System.out.println("Aucune pièce à cette position.");
                    continue;
                }

                // Vérifier que la pièce appartient au joueur
                if (piece.getCouleur() != this.getCouleur()) {
                    System.out.println("Cette pièce ne vous appartient pas.");
                    continue;
                }

                // Créer le coup et vérifier s'il est valide
                coup = new Coup(depart, arrivee, piece);

                // Vérifier les cas spéciaux (roque, promotion, etc.)
                if (piece instanceof Jeu.Roi) {
                    // Petit roque
                    if (depart.getX() == 4 && arrivee.getX() == 6) {
                        coup.setPetitRoque(true);
                    }
                    // Grand roque
                    else if (depart.getX() == 4 && arrivee.getX() == 2) {
                        coup.setGrandRoque(true);
                    }
                }

                // Vérifier promotion du pion
                if (piece instanceof Jeu.Pion) {
                    int derniereLigne = (getCouleur() == Piece.Couleur.BLANC) ? 0 : 7;
                    if (arrivee.getY() == derniereLigne) {
                        coup.setPromotion(true);

                        // Demander en quelle pièce promouvoir
                        Piece piecePromotion = demanderPromotion(arrivee);
                        coup.setPromotionPiece(piecePromotion);
                    }
                }

                // Vérifier si le coup est légal
                GestionnaireRegles gestionnaire = GestionnaireRegles.getInstance();
                if (gestionnaire.estCoupLegal(coup, partie.getPlateau())) {
                    coupValide = true;
                } else {
                    System.out.println("Coup illégal. Essayez à nouveau.");
                }

            } catch (Exception e) {
                System.out.println("Erreur: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return coup;
    }

    /**
     * Convertit une notation algébrique (ex: "e4") en Position
     * @param notation La notation algébrique
     * @return La position correspondante
     */
    private Position convertirNotation(String notation) {
        int x = notation.charAt(0) - 'a';
        int y = 8 - (notation.charAt(1) - '0');
        return new Position(x, y);
    }

    /**
     * Affiche les coups possibles pour le joueur
     * @param partie L'état actuel de la partie
     */
    private void afficherCoupsPossibles(Partie partie) {
        List<Coup> coupsPossibles = GestionnaireRegles.getInstance()
                .genererCoupsLegaux(this.getCouleur(), partie.getPlateau());

        System.out.println("\nCoups possibles:");
        for (Coup coup : coupsPossibles) {
            System.out.println("- " + coup.getNotationAlgebrique());
        }
        System.out.println();
    }

    /**
     * Demande au joueur en quelle pièce promouvoir un pion
     * @param position La position du pion à promouvoir
     * @return La nouvelle pièce choisie
     */
    private Piece demanderPromotion(Position position) {
        System.out.println("Promotion du pion. Choisissez une pièce:");
        System.out.println("1. Dame");
        System.out.println("2. Tour");
        System.out.println("3. Fou");
        System.out.println("4. Cavalier");

        int choix = 0;
        while (choix < 1 || choix > 4) {
            try {
                System.out.print("Votre choix (1-4): ");
                choix = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrée invalide. Veuillez entrer un chiffre entre 1 et 4.");
            }
        }

        switch (choix) {
            case 1:
                return new Jeu.Reine(this.getCouleur(), position);
            case 2:
                return new Jeu.Tour(this.getCouleur(), position);
            case 3:
                return new Jeu.Fou(this.getCouleur(), position);
            case 4:
                return new Jeu.Cavalier(this.getCouleur(), position);
            default:
                return new Jeu.Reine(this.getCouleur(), position); // Par défaut: Dame
        }
    }
}