import Jeu.Partie;
import Jeu.Coup;
import Jeu.Piece;
import Joueur.Joueur;
import Joueur.JoueurHumain;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Bienvenue dans le jeu d'échecs!");

        // Création des joueurs
        Joueur joueurBlanc = creerJoueur(Piece.Couleur.BLANC);
        Joueur joueurNoir = creerJoueur(Piece.Couleur.NOIR);

        // Création de la partie
        Partie partie = new Partie(joueurBlanc, joueurNoir);

        // Boucle principale de jeu
        while (!partie.estPartieTerminee()) {
            System.out.println("\n====================");
            System.out.println("Tour de " + partie.getJoueurCourant());

            // Obtention du coup du joueur courant
            Coup coup = partie.getJoueurCourant().choisirCoup(partie);

            // Exécution du coup
            partie.jouerCoup(coup);

            // Affichage du plateau après le coup
            System.out.println("\nÉtat du plateau après le coup:");
            System.out.println(partie.getPlateau().toString());

            // Affichage de l'historique des coups
            System.out.println("\nHistorique des coups:");
            for (Coup coupHistorique : partie.getHistoriqueCoups()) {
                System.out.println("- " + coupHistorique.getNotationAlgebrique());
            }
        }

        // Affichage du résultat de la partie
        afficherResultat(partie);
    }

    /**
     * Crée un joueur en demandant son nom
     * @param couleur La couleur du joueur
     * @return Le joueur créé
     */
    private static Joueur creerJoueur(Piece.Couleur couleur) {
        String couleurTexte = (couleur == Piece.Couleur.BLANC) ? "blanc" : "noir";
        System.out.print("Entrez le nom du joueur " + couleurTexte + ": ");
        String nom = scanner.nextLine().trim();

        return new JoueurHumain(nom, couleur);
    }

    /**
     * Affiche le résultat de la partie
     * @param partie La partie terminée
     */
    private static void afficherResultat(Partie partie) {
        System.out.println("\n====================");
        System.out.println("Partie terminée!");

        Joueur vainqueur = partie.getVainqueur();
        if (vainqueur != null) {
            System.out.println("Le joueur " + vainqueur.getNom() + " (" +
                    (vainqueur.getCouleur() == Piece.Couleur.BLANC ? "Blancs" : "Noirs") +
                    ") a gagné!");
        } else {
            System.out.println("Match nul!");
        }
    }
}