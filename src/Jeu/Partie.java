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
            return;
        }

        // Exécuter le coup
        plateau.executerCoup(coup);

        // Ajouter le coup à l'historique
        historiqueCoups.add(coup);

        // Vérifier si le coup met fin à la partie
        verifierFinPartie();

        // Changer de joueur
        if (joueurCourant == joueurBlanc) {
            joueurCourant = joueurNoir;
        } else {
            joueurCourant = joueurBlanc;
        }
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
        Piece.Couleur couleurJoueur = joueurCourant == joueurBlanc ?
                Piece.Couleur.BLANC :
                Piece.Couleur.NOIR;

        int resultat = gestionnaire.verifierFinPartie(couleurJoueur, plateau);

        if (resultat != 0) {
            partieTerminee = true;

            if (resultat == 1) {
                // Victoire des blancs
                vainqueur = joueurBlanc;
            } else if (resultat == -1) {
                // Victoire des noirs
                vainqueur = joueurNoir;
            } else {
                // Pat (match nul)
                vainqueur = null;
            }
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