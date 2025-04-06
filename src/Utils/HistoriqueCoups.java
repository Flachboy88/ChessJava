package Utils;

import Jeu.Coup;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueCoups {
    private List<Coup> coups;

    public HistoriqueCoups() {
        this.coups = new ArrayList<>();
    }

    /**
     * Ajoute un coup à l'historique
     * @param coup Le coup à ajouter
     */
    public void ajouterCoup(Coup coup) {
        coups.add(coup);
    }

    /**
     * Retourne tous les coups joués
     * @return La liste des coups
     */
    public List<Coup> getCoups() {
        return new ArrayList<>(coups); // Retourne une copie pour éviter la modification externe
    }

    /**
     * Retourne le dernier coup joué, ou null si aucun coup n'a été joué
     * @return Le dernier coup ou null
     */
    public Coup getDernierCoup() {
        if (coups.isEmpty()) {
            return null;
        }
        return coups.get(coups.size() - 1);
    }

    /**
     * Annule le dernier coup (le retire de l'historique)
     * @return Le coup annulé, ou null si l'historique est vide
     */
    public Coup annulerDernierCoup() {
        if (coups.isEmpty()) {
            return null;
        }
        return coups.remove(coups.size() - 1);
    }

    /**
     * Vide l'historique des coups
     */
    public void reinitialiser() {
        coups.clear();
    }

    /**
     * Retourne le nombre de coups joués
     * @return Le nombre de coups
     */
    public int getNombreCoups() {
        return coups.size();
    }

    /**
     * Convertit l'historique en notation algébrique standard
     * @return Une chaîne de caractères représentant l'historique en notation algébrique
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coups.size(); i++) {
            if (i % 2 == 0) {
                sb.append((i / 2) + 1).append(". ");
            }

            sb.append(coups.get(i).getNotationAlgebrique()).append(" ");

            if (i % 2 == 1) {
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}