import unittest


import sys
sys.path.append('../src')
from Tour import *
from Joueur import *
from Des import *
from Tuile import *
from Plateau import *
from Message import *

class TestTour(unittest.TestCase):

    def setUp(self):
        """Initialisation des objets pour les tests"""
        self.joueur1 = Joueur("Joueur1")
        self.joueur2 = Joueur("Joueur2")
        self.joueur3 = Joueur("Joueur3")

        # Création des plateaux pour chaque joueur
        self.joueur1.plateau = Plateau(7)
        self.joueur2.plateau = Plateau(7)
        self.joueur3.plateau = Plateau(7)

        # Définition de dés communs pour tous les joueurs
        faces = [
            Tuile('R', 'R', 'E', 'E'),
            Tuile('R', 'R', 'R', 'E'),
            Tuile('T', 'T', 'E', 'E'),
            Tuile('R', 'E', 'R', 'E'),
            Tuile('T', 'T', 'T', 'E'),
            Tuile('T', 'E', 'T', 'E'),
            Tuile('R', 'T', 'R', 'T'),
            Tuile('R', 'T', 'E', 'E'),
            Tuile('R', 'E', 'E', 'T'),
            Tuile('R', 'E', 'T', 'E')


        ]
        facesSpeciales = [
            Tuile('R', 'T', 'R', 'T'),
            Tuile('R', 'R', 'R', 'T'),
            Tuile('T', 'T', 'T', 'R'),
            Tuile('R', 'R', 'R', 'R'),
            Tuile('T', 'T', 'T', 'T'),
            Tuile('R', 'R', 'T', 'T')
        ]
        self.des = Des(faces,facesSpeciales)


        self.joueur1.des = self.des
        self.joueur2.des = self.des
        self.joueur3.des = self.des

        self.message_service = Message("ws://localhost:3000")
        
        self.message_service.connect_websocket()

        self.tour = Tour([self.joueur1, self.joueur2, self.joueur3], self.des, "ws://localhost:3000")
        self.tour.message_service = self.message_service


    def test_placer_tuiles(self):
        joueur = self.joueur1
        tuiles = self.des.lancerDes()

        for tuile in tuiles:
            x, y = 3, 3
            placement_reussi = joueur.plateau.placer_tuile(tuile, x, y)
            self.assertTrue(placement_reussi)

    def test_executer_tour(self):
        self.tour.executer_tour()
        for joueur in self.tour.joueurs:
            self.assertTrue(len(joueur.plateau.grille) > 0, "Le plateau du joueur devrait contenir au moins une tuile")

if __name__ == '__main__':
    unittest.main()
