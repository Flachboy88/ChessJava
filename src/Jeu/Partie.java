package Jeu;

import java.util.ArrayList;
import java.util.List;
import Joueur.Joueur;
import Utils.GestionnaireRegles;

public class Partie {
    private Plateau plateau;
    private Joueur joueurBlanc;
    private Joueur joueurNoir;
    private Joueur joueurCourant;
    private List<Coup> historiqueCoups;
    private boolean partieTerminee;
    private Joueur vainqueur;

    public Partie(Joueur joueurBlanc, Joueur joueurNoir) {
        this.plateau = new PlateauMatrice();
        this.joueurBlanc = joueurBlanc;
        this.joueurNoir = joueurNoir;
        this.joueurCourant = joueurBlanc; // Les blancs commencent
        this.historiqueCoups = new ArrayList<>();
        this.partieTerminee = false;
        this.vainqueur = null;

        // Initialiser le plateau
        plateau.initialiser();
    }

    public void jouerCoup(Coup coup) {
        // Vérifier si la partie est terminée
        if (partieTerminee) {
            return;
        }

        // Vérifier si le coup est valide
        if (!estCoupValide(coup)) {
            System.out.println("Coup invalide!");
            return;
        }

        // Exécuter le coup
        plateau.executerCoup(coup);

        // Ajouter le coup à l'historique
        historiqueCoups.add(coup);

        // Changer de joueur
        joueurCourant = (joueurCourant == joueurBlanc) ? joueurNoir : joueurBlanc;

        // Vérifier si le coup met fin à la partie pour le joueur qui doit maintenant jouer
        verifierFinPartie();
    }

    private boolean estCoupValide(Coup coup) {
        // Vérifier si la pièce appartient au joueur courant
        Piece piece = coup.getPiece();
        if (piece == null) {
            return false;
        }

        boolean estPieceBlanche = piece.getCouleur() == Piece.Couleur.BLANC;
        if ((estPieceBlanche && joueurCourant != joueurBlanc) ||
                (!estPieceBlanche && joueurCourant != joueurNoir)) {
            return false;
        }

        GestionnaireRegles gestionnaire = GestionnaireRegles.getInstance();
        return gestionnaire.estCoupLegal(coup, plateau);
    }

    private void verifierFinPartie() {
        // Utiliser le gestionnaire de règles pour vérifier l'état de la partie
        GestionnaireRegles gestionnaire = GestionnaireRegles.getInstance();

        // Obtenir la couleur du joueur qui doit jouer (joueurCourant)
        Piece.Couleur couleurJoueur = joueurCourant.getCouleur();

        // Afficher des informations de débogage
        System.out.println("DEBUG: Vérification de la fin de partie pour " +
                (couleurJoueur == Piece.Couleur.BLANC ? "les blancs" : "les noirs"));

        // Vérifier si le roi est en échec
        boolean estEnEchec = gestionnaire.estRoiEnEchec(couleurJoueur, plateau);
        System.out.println("DEBUG: Le roi est en échec: " + estEnEchec);

        // Vérifier si le joueur peut faire un coup légal
        List<Coup> coupsLegaux = gestionnaire.genererCoupsLegaux(couleurJoueur, plateau);
        System.out.println("DEBUG: Nombre de coups légaux possibles: " + coupsLegaux.size());

        // Déterminer le résultat
        int resultat = gestionnaire.verifierFinPartie(couleurJoueur, plateau);
        System.out.println("DEBUG: Résultat de la vérification: " + resultat);

        if (resultat != 0) {
            partieTerminee = true;
            System.out.println("DEBUG: La partie est terminée!");

            if (resultat == 1) {
                // Victoire des blancs
                vainqueur = joueurBlanc;
                System.out.println("ÉCHEC ET MAT ! Les blancs ont gagné !");
            } else if (resultat == -1) {
                // Victoire des noirs
                vainqueur = joueurNoir;
                System.out.println("ÉCHEC ET MAT ! Les noirs ont gagné !");
            } else {
                // Pat (match nul)
                vainqueur = null;
                System.out.println("PAT ! La partie est nulle.");
            }

            // Afficher l'état final et arrêter la boucle de jeu
            System.out.println("\nFIN DE LA PARTIE");
            System.out.println("Résultat final:");
            System.out.println(plateau.toString());
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public Joueur getJoueurCourant() {
        return joueurCourant;
    }

    public List<Coup> getHistoriqueCoups() {
        return new ArrayList<>(historiqueCoups); // Retourne une copie pour éviter la modification externe
    }

    public boolean estPartieTerminee() {
        return partieTerminee;
    }

    public Joueur getVainqueur() {
        return vainqueur;
    }

    public void setPartieTerminee(boolean partieTerminee) {
        this.partieTerminee = partieTerminee;
    }

    public void setVainqueur(Joueur vainqueur) {
        this.vainqueur = vainqueur;
    }
}