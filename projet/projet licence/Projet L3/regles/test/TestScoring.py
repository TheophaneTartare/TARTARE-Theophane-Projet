import unittest
import sys
sys.path.append('../src')
from Scoring import *

class TestScoring(unittest.TestCase):
    def setUp(self):
        self.grille = [
            [None, None, {"sortie_connectee": True}, None, None, None, None],
            [None, None, None, None, None, None, None],
            [None, None, None, None, None, None, None],
            [None, None, None, None, None, None, None],
            [None, None, None, None, None, None, None],
            [None, None, None, None, None, None, None],
            [None, None, None, None, None, None, None],
        ]

    def test_calcul_reseaux_connectes(self):
        score = Scoring.calculer_reseaux_connectes(self.grille)
        self.assertEqual(score, 1) 

    def test_calcul_plus_long_chemin(self):
        grille = [
            [{"type": "railway", "length": 3}, None, None],
            [{"type": "railway", "length": 5}, None, None],
            [None, None, None],
        ]
        score = Scoring.calculer_chemin_le_plus_long(grille, "railway")
        self.assertEqual(score, 5)

    def test_calcul_cases_centrales(self):
        grille = [[None for _ in range(7)] for _ in range(7)]
        grille[3][3] = {"type": "railway"}
        grille[4][4] = {"type": "highway"}
        score = Scoring.calculer_cases_centrales(grille)
        self.assertEqual(score, 2)

    def test_calcul_penalites(self):
        grille = [[None for _ in range(7)] for _ in range(7)]
        grille[0][0] = {"est_incomplete": True}
        grille[1][1] = {"est_incomplete": True}
        score = Scoring.calculer_penalites(grille)
        self.assertEqual(score, 2) 

if __name__ == "__main__":
    unittest.main()

