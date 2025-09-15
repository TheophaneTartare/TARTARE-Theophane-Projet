from enum import Enum

import random
from Tuile import *

from Des import *

class Joueur:
    """
    Représente un joueur dans le jeu.
    Gère les voies spéciales et les choix des tuiles à chaque round.
    """
    def __init__(self, nom, plateau=None):
        """
        Initialise un joueur avec un nom, un plateau.
        :param nom: Nom du joueur
        :param plateau: Plateau associé au joueur (optionnel)
        """
        self.nom = nom  
        self.tuileSpeciales_restantes = 0  # Nombre de voies spéciales disponibles pendant une partie (7 rounds)
        self.tuiles_speciales_utilisees=0 # Nombre de tuile speciales déja utilisées
        self.tuiles_en_main = []   # Liste des tuiles que le joueur a en main pour le tour actuel
        self.tuilesPlacer = [] 
        self.score = 0  # Score actuel du joueur
        self.deeSpecial=['SH', 'SR', 'SS','HH','RR',"SHR"]
    
    def ajoutTuilesMain(self, tuiles):
        """
        Ajoute des tuiles à la main du joueur.
        """
        self.tuiles_en_main.extend(tuiles)

    def retourArriereJoueur(self) :
        print(f" avant : Derniere Tuile Placer = {self.tuilesPlacer[-1] } et self.tuileplacer = {self.tuilesPlacer} et self.tuileAPlacer{self.tuiles_en_main}  ")
        self.tuiles_en_main.append(self.tuilesPlacer[-1])
        del self.tuilesPlacer[-1]
        print(f" apres : Derniere Tuile Placer = {self.tuilesPlacer[-1] } et self.tuileplacer = {self.tuilesPlacer} et self.tuileAPlacer{self.tuiles_en_main}  ")

    def deleteTuile(self,tuile) :
        """
        Supprime une tuile de la main du joueur
        """
        tuiles = self.tuiles_en_main
        print(f"deletuile de  {tuile} " )
        tuiles.remove(tuile) 
        self.tuilesPlacer.append(tuile)
        print(f"apres le deletuile ont a self.tuileEnMain  {self.tuiles_en_main} et self.tuilePlace ={self.tuilesPlacer}" )

    def mainVide(self):
        print(f"pour main vide Joueur.tuiles_en_main = {self.tuiles_en_main}")
        return len(self.tuiles_en_main) == 0
    
    
    def peut_utiliser_tuile_speciale(self):
        """
        Vérifie si le joueur peut encore utiliser une tuile spéciale.
        """
        return self.tuiles_speciales_utilisees < 3

    def utiliser_tuile_speciale(self, des):
        """
        Le joueur choisit d'utiliser la tuile spéciale tirée ce tour.
        Retourne la tuile spéciale si elle peut être utilisée, sinon None.
        """
        if self.peut_utiliser_tuile_speciale():
            tuile_speciale = des.utiliser_tuile_speciale()
            if tuile_speciale:
                self.tuiles_speciales_utilisees += 1
                self.tuiles_speciales_restantes -= 1
                return tuile_speciale
        return None
    
    def isSpecial(self,tuile) :
        if tuile in self.deeSpecial :
            return True
        else:
            return False 
    
