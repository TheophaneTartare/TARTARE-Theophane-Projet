from Tuile import *
from Des import *

class Plateau: 


    def __init__(self, size):
        """methode pour initaliser le plateau du jeu 
        en passant en paramètre la taille de la grille du plateau,
        initialiser les listes contenant les faces speciales et 
        les tuiles à placer, et crée un objet Des"""
        self.size = size
        self.grille = [[None for _ in range(size)] for _ in range(size)]
        self.faces = []
        self.facesSpeciales = []
        self.des = Des(self.faces, self.facesSpeciales)
        self.tuiles_a_placer = [] # recuperer les tuiles à placer
        self.sortie={
        (0, 2): Tuile('E','E','E','T'), (0, 4): Tuile('E','R','E','E'),(0, 6): Tuile('E','T','E','E'),
        (1, 7): Tuile('E','E','R','E'), (3, 7): Tuile('E','E','T','E'), (5, 7): Tuile('E','E','R','E'),
        (6, 6): Tuile('E','T','E','E'), (6, 4): Tuile('E','E','E','R'), (6, 2): Tuile('E','T','E','E'),
        (1, 1): Tuile('R','E','E','E'), (3, 1): Tuile('T','E','E','E'), (5, 1): Tuile('R','E','E','E'),
        }



    def est_valide(self, x, y,sortie):
        """methode qui prend les cordonnees d'une case 
        et vérifie si elle est valide en renvoyant true, sinon false"""
        if(sortie):
            return 0 <= x <= self.size and 0 <= y <= self.size
        return 0 <= x < self.size and 0 <= y <= self.size

# recuperer les emplacements libres
    def emplacements_libres(self):
        """methode qui renvoie les emplacements vides sur le plateau"""
        emplacements_libres = []
        for i, ligne in enumerate(self.grille):
            for j, case in enumerate(ligne):
                if case is None:  # si la case est libre
                    emplacements_libres.append((i, j))
        return emplacements_libres

    def voisins(self, x, y):
        """Renvoie la liste des voisins d'une case aux coordonnées (x, y), qu'ils soient dans self.sortie ou non."""
        voisins = []
        directions = [(0, 1, "haut"), (0, -1, "bas"), (-1, 0, "gauche"), (1, 0, "droit")]
        
        for dx, dy, cote in directions:
            nx, ny = x + dx, y + dy  # coordonnées du voisin
            
            # Si la grille a une taille limite, on s'assure que (nx, ny) reste dans les bornes
            if 0 <= nx <= self.size and 0 <= ny <= self.size:
                voisins.append(((nx, ny), cote))
        
        return voisins

    def recuperer_tuile(self, x, y):
        """recupère la tuile située dans la case dont les cordonnees
         sont passés en parametre"""
        if self.est_valide(x,y):
            return self.grille[x][y]
        return None
        
 
    # def placer_tuile(self, tuile, x, y):
    #     """place la tuile dans la case correspondant aux cordonnees x et y
    #     en utilisant les fonctions precedentes pour verifier si l emplacement 
    #     est libre et compatible"""
    #     print(f"({x}, {y}).")
    #     if not self.est_valide(x,y):
    #         print(f"Impossible de placer la tuile à ({x}, {y}) : place invalide")
    #         return False
    #     liste_voisins = self.voisins(x, y)
    #     print(f"({liste_voisins},listevoisin.")
    #     #directions = ['haut', 'droit', 'bas', 'gauche']

    #     # Vérifier si la tuile est compatible avec au moins un voisin
    #     for (nx, ny), cote in liste_voisins:
    #         voisin = self.grille[nx][ny]
    #         print(f"voisin: {voisin}, type: {type(voisin)}")
    #         if voisin is not None and not tuile.verification_tuile(voisin, cote):
    #             # Placer la tuile et afficher un message
    #            print(f"Impossible de placer la tuile à ({x}, {y}) : aucun voisin compatible.")
    #            return False
    #     self.grille[x][y] = tuile
    #     print(f"La tuile a été placée à l'emplacement ({x}, {y}).")
    #     return True
        
    #     # Vérifier si la tuile est compatible avec au moins un voisin
    #     for (nx, ny), cote in liste_voisins:
    #         voisin = self.grille[nx][ny]
    #         if voisin is not None and tuile.verification_tuile(voisin, cote):
    #             # Placer la tuile et afficher un message
    #             self.grille[x][y] = tuile
    #             print(f"La tuile a été placée à l'emplacement ({x}, {y}).")
    #             return True

    #     print(f"Impossible de placer la tuile à ({x}, {y}) : aucun voisin compatible.")
    #     return False
    def placer_tuile(self, tuile, x, y):
        """place la tuile dans la case correspondant aux cordonnees x et y
        en utilisant les fonctions precedentes pour verifier si l emplacement 
        est libre et compatible"""
        
        
        sortie=(x,y)in self.sortie

        if not self.est_valide(x,y,sortie):
            print(f"Impossible de placer la tuile à ({x}, {y}) : place invalide")
            return False

        if (x, y) in self.sortie:
            if x==0:
                if not tuile.verification_tuile(self.sortie[(x, y)], "gauche"):
                    print(f"Impossible de placer la tuile à ({x}, {y}) : incompatible avec la sortie")
                    return False
            if x==6:
                if not tuile.verification_tuile(self.sortie[(x, y)], "droit"):
                    print(f"Impossible de placer la tuile à ({x}, {y}) : incompatible avec la sortie")
                    return False
            if y==7:
                if not tuile.verification_tuile(self.sortie[(x, y)], "haut"):
                    print(f"Impossible de placer la tuile à ({x}, {y}) : incompatible avec la sortie")
                    return False
            if y==1:
                if not tuile.verification_tuile(self.sortie[(x, y)], "bas"):
                    print(f"Impossible de placer la tuile à ({x}, {y}) : incompatible avec la sortie")
                    return False

        liste_voisins = self.voisins(x, y)
        #directions = ['haut', 'droit', 'bas', 'gauche']

        voisin_valide = False
        for (nx, ny), cote in liste_voisins:
            voisin = self.grille[nx][ny-1]
            
            if voisin is not None:
                if not tuile.verification_tuile(voisin, cote):
                    print(f"Impossible de placer la tuile à ({x}, {y}) : voisin ({nx}, {ny}) incompatible.")
                    return False
                else:
                    voisin_valide = True
            if(x,y) in self.sortie:
                voisin_valide = True

            
        if not voisin_valide:
            return False
        # Si tous les voisins sont compatibles, on place la tuile
        self.grille[x][y-1] = tuile
        print(f"La tuile a été placée à l'emplacement ({x}, {y}).")

        return True
 
    def est_tuile_placee(self, tuile):
        """verifie si la tuile est bien placée"""
        for ligne in self.grille:
            for tuile2 in ligne:
                if tuile is tuile2:
                    return True
        return False


    def verifier_placement_4des(self, des):
        """verifie si les 4 des lancés ont été tous placés"""
        for tuile in self.tuiles_a_placer:
            if not self.est_tuile_placee(tuile): # la tuile n est pas encore placée
                print("La tuile {tuile} n'a pas été placée !")
                return False 
        return True


    def barrer_tuile_speciale(self, tuile):
        """une fois une tuile spéciale utiliséé, il faut la barrer du plateau"""
        if any(tuile is tuile2 for tuile2 in self.des.facesSpeciales) and self.est_tuile_placee(tuile):
            self.des.facesSpeciales.remove(tuile)

    @staticmethod
    def main():
        print("=== Démonstration de la classe Plateau ===")
        plateau = Plateau(7)

        # tuiles de test
        tuile1 = Tuile('R', 'E', 'R', 'E')
        tuile2 = Tuile('R', 'E', 'E', 'R')
        tuile3 = Tuile('R', 'T', 'R', 'T')
        tuile4 = Tuile('T', 'E', 'R', 'E')

        print("\n--- Test 1: Placer la première tuile ---")
        plateau.placer_tuile(tuile1, 2, 2)

        print("\n--- Test 2: Tuile compatible ---")
        plateau.placer_tuile(tuile2, 2, 3)  


        print("\n--- Test 3: Tuile incompatible ---")
        plateau.placer_tuile(tuile3, 2, 1)  

        print("\n--- Test 4: Position invalide ---")
        plateau.placer_tuile(tuile4, 7, 7)  

        print("\n--- Test 5: Emplacements libres ---")
        print("Emplacements libres:", plateau.emplacements_libres())


        print("\n--- Test 6: Vérifier si les tuiles ont été placées ---")
        print("Tuile1 est placée ?", plateau.est_tuile_placee(tuile1))
        print("Tuile3 est placée ?", plateau.est_tuile_placee(tuile3))


if __name__ == "__main__":
    Plateau.main()
