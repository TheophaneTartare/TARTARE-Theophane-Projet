import websocket
import random

class Des:
    """
    Représente un dé pour générer des tuiles aléatoires.
    Chaque face du dé correspond à une tuile prédéfinie.
    Il y a 4 dés : 3 avec des tuiles et 1 avec des tuiles spéciales
    """
    def __init__(self, facesRoute , facesConections):
        self.facesRoute = facesRoute  # Liste de tuiles sur le dé
        self.facesConections = facesConections
        self.tuiles_speciales_utilisees = 0  # Nombre de tuiles spéciales utilisées dans la partie
    
    def lancerDes(self):
        """
        Lance les dés et retourne les 4 tuiles
        Les dés : 3 dés avec les tuiles et 1 dé spécial avec tuile spécial
        """
        res = [
            random.choice(self.facesRoute),  # résultat dé 1
            random.choice(self.facesRoute),  # résultat dé 2
            random.choice(self.facesRoute),  # résultat dé 3
            random.choice(self.facesConections) # résultat dé conection  
        ]
        return res 
    
    def utiliser_tuile_speciale(self):
        """
        Le joueur choisit d'utiliser la tuile spéciale tirée ce tour.
        Retourne la tuile spéciale si elle peut être utilisée, sinon None.
        """
        if self.tuiles_speciales_utilisees < 3 and self.tuile_speciale_tiree is not None:
            self.tuiles_speciales_utilisees += 1
            tuile_speciale = self.tuile_speciale_tiree
            self.tuile_speciale_tiree = None  # Réinitialise la tuile spéciale pour le prochain tour
            return tuile_speciale
        return None
