package Joueur;

import Jeu.Coup;
import Jeu.Partie;
import Jeu.Piece;

public abstract class Joueur {
    private String nom;
    private Piece.Couleur couleur;

    public Joueur(String nom, Piece.Couleur couleur) {
        this.nom = nom;
        this.couleur = couleur;
    }

    /**
     * Méthode abstraite pour obtenir le prochain coup du joueur
     * @param partie L'état actuel de la partie
     * @return Le coup choisi par le joueur
     */
    public abstract Coup choisirCoup(Partie partie);

    /**
     * Retourne le nom du joueur
     * @return Le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom du joueur
     * @param nom Le nouveau nom du joueur
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la couleur du joueur
     * @return La couleur du joueur
     */
    public Piece.Couleur getCouleur() {
        return couleur;
    }

    /**
     * Modifie la couleur du joueur
     * @param couleur La nouvelle couleur du joueur
     */
    public void setCouleur(Piece.Couleur couleur) {
        this.couleur = couleur;
    }

    /**
     * Représentation textuelle du joueur
     * @return Une chaîne de caractères représentant le joueur
     */
    @Override
    public String toString() {
        return nom + " (" + (couleur == Piece.Couleur.BLANC ? "Blancs" : "Noirs") + ")";
    }
}