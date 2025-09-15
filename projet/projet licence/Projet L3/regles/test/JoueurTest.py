import unittest

import sys
sys.path.append('../src')
from Joueur import *

class TestJoueur(unittest.TestCase):
    def setUp(self):
        self.joueur = Joueur("Alice")
    
    def test_ajout_tuiles_main(self):
        tuiles = ["Tuile1", "Tuile2"]
        self.joueur.ajoutTuilesMain(tuiles)
        self.assertEqual(self.joueur.tuiles_en_main, tuiles)
    
    def test_delete_tuile(self):
        self.joueur.ajoutTuilesMain(["Tuile1", "Tuile2"])
        self.joueur.deleteTuile("Tuile1")
        self.assertEqual(self.joueur.tuiles_en_main, ["Tuile2"])
        
    def test_peut_utiliser_tuile_speciale(self):
        self.assertTrue(self.joueur.peut_utiliser_tuile_speciale())
        self.joueur.tuiles_speciales_utilisees = 3
        self.assertFalse(self.joueur.peut_utiliser_tuile_speciale())
    
if __name__ == "__main__":
    unittest.main()