import unittest
import sys
sys.path.append('../src')
from ReglesDujeu import *

class TestReglesDuJeu(unittest.TestCase):
    def setUp(self):
        self.regles = ReglesDujeu()
        self.regles.ajouter_joueur("Alice")
        self.regles.ajouter_joueur("Bob")

    def test_commencer_partie(self):
        self.regles.commencer_partie()
        self.assertEqual(self.regles.numero_round, 0)
        self.assertEqual(len(self.regles.joueurs), 2)
        for joueur in self.regles.joueurs:
            self.assertEqual(joueur["score"], 0)
            self.assertEqual(len(joueur["grille"]), 7)
            self.assertEqual(len(joueur["grille"][0]), 7)

    def test_commencer_round(self):
        self.regles.commencer_partie()
        self.regles.commencer_round()
        self.assertEqual(self.regles.numero_round, 1)

    def test_terminer_round(self):
        self.regles.commencer_partie()
        self.regles.commencer_round()
        self.regles.terminer_round()
        self.assertEqual(self.regles.numero_round, 1)

    def test_est_partie_terminee(self):
        self.regles.commencer_partie()
        for _ in range(7):
            self.regles.commencer_round()
        self.assertTrue(self.regles.est_partie_terminee())

if __name__ == "__main__":
    unittest.main()

