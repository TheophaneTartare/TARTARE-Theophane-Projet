class ReglesDujeu:
    def __init__(self):
        self.numero_round = 0
        self.max_rounds = 7
        self.joueurs = []

    def ajouter_joueur(self, nom_joueur):
        self.joueurs.append({
            "nom": nom_joueur,
            "grille": [[None for _ in range(7)] for _ in range(7)],
            "score": 0,
            "tuiles_speciales_utilisees": 0  # Nombre de tuiles spéciales utilisées

        })

    def commencer_round(self):
        if self.numero_round >= self.max_rounds:
            raise Exception("La partie est terminée.")
        self.numero_round += 1
        print(f"Début du round {self.numero_round}")

    def terminer_round(self):
        print(f"Fin du round {self.numero_round}")
        for joueur in self.joueurs:
            print(f"Joueur {joueur['nom']} : vérifiez votre grille.")

    def est_partie_terminee(self):
        return self.numero_round >= self.max_rounds

    def commencer_partie(self):
        print("Nouvelle partie !")
        self.numero_round = 0
        for joueur in self.joueurs:
            joueur["grille"] = [[None for _ in range(7)] for _ in range(7)]
            joueur["score"] = 0
            joueur["tuiles_speciales_utilisees"] = 0

    def utiliser_tuile_speciale(self, nom_joueur, des):
        """
        Le joueur choisit d'utiliser la tuile spéciale tirée ce tour.
        """
        for joueur in self.joueurs:
            if joueur["nom"] == nom_joueur:
                if joueur["tuiles_speciales_utilisees"] < 3:
                    tuile_speciale = des.utiliser_tuile_speciale()
                    if tuile_speciale:
                        joueur["tuiles_speciales_utilisees"] += 1
                        return tuile_speciale
        return None
