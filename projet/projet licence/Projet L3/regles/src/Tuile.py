from enum import Enum

import random

class TuileType(Enum):
    """
    Enumération représentant les types de tuiles 
    Chaque côté d'une tuile peut être :
    - VIDE : pas de connexion
    - ROUTE : une connexion de type route
    - RAIL : une connexion de type rail
    """
    VIDE = 'E'
    ROUTE = 'R'
    RAIL = 'T'

class Tuile:
    """
    Représentation une tuile 
    Une tuile peut être une route, un rail ou les deux
    Chaque coté de la tuile est donnée
    """
    def __init__(self, haut,gauche,bas,droit):
        self.haut = TuileType(haut)  # coté supérieur
        self.droit = TuileType(droit)  # coté droit
        self.bas = TuileType(bas)  # coté inférieur
        self.gauche = TuileType(gauche)  # coté gauche

    def __repr__(self):
        """Retourne une représentation d'une tuile avec ses 4 cotés"""
        return f"Tuile({self.haut.value}, {self.droit.value}, {self.bas.value}, {self.gauche.value})"

    def pivoter(self):
        """
        Rotation
        """
        self.haut, self.droit, self.bas, self.gauche = self.gauche, self.haut, self.droit, self.bas

    def symetrie(self):
        """
        Symetrie
        """
        self.droit, self.gauche = self.gauche, self.droit

    # def verification_tuile(self, tuile2, cote):
    #     """
    #     Vérifie si deux tuiles correspondent sur un côté donné.
    #     :param tuile2: Deuxième tuile
    #     :param cote: Côté à vérifier ('haut', 'droit', 'bas', 'gauche')
    #     :return: True si les tuiles correspondent, sinon False
    #     """
    #     for _ in range(2): 
    #         for _ in range(4):  
    #             if cote == 'haut':
    #                 if self.haut == tuile2.bas:
    #                     return True
    #             elif cote == 'droit':
    #                 if self.droit == tuile2.gauche:
    #                     return True
    #             elif cote == 'bas':
    #                 if self.bas == tuile2.haut:
    #                     return True
    #             elif cote == 'gauche':
    #                 if self.gauche == tuile2.droit:
    #                     return True
    #             else:
    #                 raise ValueError(f"{cote} n'est pas une direction valide !!!")
                
    #             tuile2.pivoter()  
            
    #         tuile2.symetrie()  
    #     return False  
    def verification_tuile(self, tuile2, cote):
        """
        Vérifie si deux tuiles correspondent sur un côté donné.
        :param tuile2: Deuxième tuile
        :param cote: Côté à vérifier ('haut', 'droit', 'bas', 'gauche')
        :return: True si les tuiles correspondent, sinon False
        
        Note: Les côtés VIDE ("E") sont toujours compatibles entre eux.
        """
        # Correspondance entre les côtés opposés
        opposés = {
        "haut": "bas",
        "droit": "gauche",
        "bas": "haut",
        "gauche": "droit"
        }

        cote_oppose = opposés[cote]

        if cote == "haut":
            return (self.haut == tuile2.bas) or (self.haut == TuileType.VIDE and tuile2.bas == TuileType.VIDE)
        elif cote == "droit":
            return (self.droit == tuile2.gauche) or (self.droit == TuileType.VIDE and tuile2.gauche == TuileType.VIDE)
        elif cote == "bas":
            return (self.bas == tuile2.haut) or (self.bas == TuileType.VIDE and tuile2.haut == TuileType.VIDE)
        elif cote == "gauche":
            return (self.gauche == tuile2.droit) or (self.gauche == TuileType.VIDE and tuile2.droit == TuileType.VIDE)
        else:
            raise ValueError(f"{cote} n'est pas une direction valide !")



        
    
     

