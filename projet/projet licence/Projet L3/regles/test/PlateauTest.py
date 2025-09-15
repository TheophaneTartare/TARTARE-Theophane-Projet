import unittest
import sys
sys.path.append('../src')
from Plateau import *
from Tuile import *
from Des import *

class PlateauTest(unittest.TestCase):
    plateau = Plateau(3)
    def test_initialisation(self):
        plateau = Plateau(3)
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
        plateau.des = Des(faces,facesSpeciales)

        plateau.tuiles_a_placer = plateau.des.lancerDes()
        assert plateau.size == 3, "Erreur : La taille du plateau n'est pas correcte."
        assert len(plateau.grille) == 3 and len(plateau.grille[0]) == 3, "Erreur : La grille n'a pas la taille attendue."
        
    def test_est_valide(self):
        plateau = Plateau(3)
        assert plateau.est_valide(0, 0) == True, "Erreur : (0, 0) devrait être valide."
        assert plateau.est_valide(3, 3) == False, "Erreur : (3, 3) devrait être invalide."
        assert plateau.est_valide(-1, -1) == False, "Erreur : (-1, -1) devrait être invalide."

    def test_emplacements_libres(self):
        plateau = Plateau(3)
        plateau.grille[1][1] = Tuile('R', 'T', 'E', 'E')  # Placer une tuile à (1, 1)
        emplacements = plateau.emplacements_libres()
        assert (1, 1) not in emplacements, "Erreur : (1, 1) ne devrait pas être libre."
        assert (0, 0) in emplacements, "Erreur : (0, 0) devrait être libre."

    def test_voisins(self):
        plateau = Plateau(3)
        voisins = plateau.voisins(1, 1)
        assert ((0, 1), "haut") in voisins, "Erreur : (0, 1) devrait être un voisin de (1, 1)."
        assert ((2, 1), "bas") in voisins, "Erreur : (1, 0) devrait être un voisin de (1, 1)."
        assert ((1, 0), "gauche") in voisins, "Erreur : (1, 2) devrait être un voisin de (1, 1)."
        assert ((1, 2), "droit") in voisins, "Erreur : (2, 1) devrait être un voisin de (1, 1)." 

    def test_recuperer_tuile(self):
        plateau = Plateau(3)
        tuile = Tuile('R', 'T', 'E', 'E')
        plateau.placer_tuile(tuile, 0, 0)
        recupered_tuile = plateau.recuperer_tuile(0, 0)
        assert recupered_tuile == tuile, "Erreur : La tuile récupérée n'est pas la bonne."

    def test_placer_tuile(self):
        
        tuileA = Tuile('R', 'E', 'T', 'E')
        tuileB = Tuile('E', 'T', 'E', 'R')

        self.assertTrue(self.plateau.placer_tuile(tuileA, 2, 2))
        self.assertEqual(self.plateau.grille[2][2], tuileA)
        self.assertFalse(self.plateau.placer_tuile(tuileB, 5, 5))  # Hors limites

        # Essayer de placer une tuile dans un emplacement invalide
        x = self.plateau.placer_tuile(tuileA, 3, 3)
        self.assertEqual(x, False)

    def test_verifier_placement_4des(self):
        self.plateau.tuiles_a_placer = ['A', 'B', 'C', 'D']
        self.plateau.est_tuile_placee = lambda tuile: tuile in ['A', 'B', 'C', 'D']
        self.assertTrue(self.plateau.verifier_placement_4des([]))

    def test_barrer_tuile_speciale(self):
        self.plateau.des.facesSpeciales = ['X', 'Y', 'Z']
        self.plateau.est_tuile_placee = lambda tuile: tuile == 'Y'
        self.plateau.barrer_tuile_speciale('Y')
        self.assertNotIn('Y', self.plateau.des.facesSpeciales)

# Pour exécuter les tests
if __name__ == '__main__':
    unittest.main()
print("Tous les tests ont réussi !")
