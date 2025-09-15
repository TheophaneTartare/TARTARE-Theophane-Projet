import unittest
from enum import Enum
import sys
sys.path.append('../src')
from Tuile import *

class TestTuile(unittest.TestCase):
    
    def test_tuile_creation(self):
        """
        Teste la création d'une tuile avec différents types de côtés
        """
        tuile = Tuile('R', 'T', 'E', 'R')
        self.assertEqual(tuile.haut, TuileType.ROUTE)
        self.assertEqual(tuile.droit, TuileType.RAIL)
        self.assertEqual(tuile.bas, TuileType.VIDE)
        self.assertEqual(tuile.gauche, TuileType.ROUTE)
    

    def test_tuile_rotation(self):
        """Test de la rotation d'une tuile"""
        tuile = Tuile('R', 'T', 'E', 'R')
        tuile.pivoter()
        self.assertEqual(tuile.haut, TuileType.ROUTE)
        self.assertEqual(tuile.droit, TuileType.ROUTE)
        self.assertEqual(tuile.bas, TuileType.RAIL)
        self.assertEqual(tuile.gauche, TuileType.VIDE)

    def test_tuile_symetrie(self):
        """Test de la symétrie d'une tuile"""
        tuile = Tuile('R', 'T', 'E', 'R')
        tuile.symetrie()
        self.assertEqual(tuile.haut, TuileType.ROUTE)
        self.assertEqual(tuile.droit, TuileType.ROUTE)
        self.assertEqual(tuile.bas, TuileType.VIDE)
        self.assertEqual(tuile.gauche, TuileType.RAIL)

    def test_verification_tuile_valide(self):
        """Test du placement d'une tuile compatible avec un autre côté"""
        tuile1 = Tuile('R', 'T', 'E', 'R')
        tuile2 = Tuile('E', 'R', 'R', 'T')  
        self.assertTrue(tuile1.verification_tuile(tuile2, 'bas'))

    def test_verification_tuile_invalide(self):
        """Test du placement d'une tuile qui ne correspond pas"""
        tuile1 = Tuile('R', 'R', 'R', 'R')
        tuile2 = Tuile('E', 'T', 'E', 'T')
        self.assertFalse(tuile1.verification_tuile(tuile2, 'droit'))

    def test_verification_tuile_avec_rotation(self):
        """Test du placement d'une tuile qui doit être pivotée pour correspondre"""
        tuile1 = Tuile('R', 'T', 'E', 'R')
        tuile2 = Tuile('T', 'E', 'R', 'R')  
        self.assertTrue(tuile1.verification_tuile(tuile2, 'droit'))

    def test_verification_tuile_avec_symetrie(self):
        """Test du placement d'une tuile qui doit être symétrisée pour correspondre"""
        tuile1 = Tuile('R', 'T', 'E', 'R')
        tuile2 = Tuile('R', 'E', 'R', 'T')  
        self.assertTrue(tuile1.verification_tuile(tuile2, 'gauche'))


if __name__ == "__main__":
    unittest.main()