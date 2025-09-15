import unittest
from enum import Enum

import sys
sys.path.append('../src')
import random
from Tuile import *

from Des import *


class TestDes(unittest.TestCase):
    def setUp(self):
        
        self.faces = [
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
        self.facesSpeciales = [
            Tuile('R', 'T', 'R', 'T'),
            Tuile('R', 'R', 'R', 'T'),
            Tuile('T', 'T', 'T', 'R'),
            Tuile('R', 'R', 'R', 'R'),
            Tuile('T', 'T', 'T', 'T'),
            Tuile('R', 'R', 'T', 'T')
        ]
        self.des = Des(self.faces, self.facesSpeciales)



    def test_lancer_des(self):
        """ Vérifie le lancer de des"""
        res = self.des.lancerDes()
        self.assertEqual(len(res), 4)


    def test_utiliser_tuile_speciale(self):
        """ Vérifie que l'utilisation des tuiles spéciales respecte la limite de 3 par partie """
        self.des.tuile_speciale_tiree = random.choice(self.facesSpeciales)

        # 3 utilisations possibles
        self.assertNotEqual(self.des.utiliser_tuile_speciale(), None) # pas egal a none 
        self.des.tuile_speciale_tiree = random.choice(self.facesSpeciales)
        self.assertNotEqual(self.des.utiliser_tuile_speciale(), None)
        self.des.tuile_speciale_tiree = random.choice(self.facesSpeciales)
        self.assertNotEqual(self.des.utiliser_tuile_speciale(), None)

        # echec
        self.des.tuile_speciale_tiree = random.choice(self.facesSpeciales)
        self.assertEqual(self.des.utiliser_tuile_speciale(), None)



        
if __name__ == "__main__":
    unittest.main()