class Scoring:
    @staticmethod
    def calculer_reseaux_connectes(grille_joueur):
     compteur = 0
     for ligne in grille_joueur:
        for case in ligne:
            if case and isinstance(case, dict) and case.get("sortie_connectee"):
                compteur += 1
     return compteur


    @staticmethod
    def calculer_chemin_le_plus_long(grille_joueur, type_chemin):
     chemin_le_plus_long = 0
     for ligne in grille_joueur:
        for chemin in ligne:
            if isinstance(chemin, dict):
                if chemin.get("type") == type_chemin:
                  chemin_le_plus_long = max(chemin_le_plus_long, chemin.get("length", 0))
     return chemin_le_plus_long


    @staticmethod
    def calculer_cases_centrales(grille_joueur):
        points_centraux = 0
        coordonnees_centrales = [(3, 3), (3, 4), (3, 5), (4, 3), (4, 4), (4, 5), (5, 3), (5, 4), (5, 5)]
        for x, y in coordonnees_centrales:
            if grille_joueur[x][y] is not None:
                points_centraux += 1
        return points_centraux

    @staticmethod
    def calculer_penalites(grille_joueur):
        penalites = 0
        for ligne in grille_joueur:
            for cellule in ligne:
                if cellule is not None and cellule.get("est_incomplete", False):
                    penalites += 1
        return penalites

    @staticmethod
    def calculer_score_total(grille_joueur):
        score = 0
        score += Scoring.calculer_reseaux_connectes(grille_joueur)
        score += Scoring.calculer_chemin_le_plus_long(grille_joueur, "voie_ferree")
        score += Scoring.calculer_chemin_le_plus_long(grille_joueur, "autoroute")
        score += Scoring.calculer_cases_centrales(grille_joueur)
        score -= Scoring.calculer_penalites(grille_joueur)
        return score

    @staticmethod
    def _trouver_sorties_connectees(grille_joueur):
        sorties_connectees = []
        for ligne in grille_joueur:
            for cellule in ligne:
                if cellule and cellule.get("sortie_connectee", False):
                    sorties_connectees.append(cellule)
        return sorties_connectees

