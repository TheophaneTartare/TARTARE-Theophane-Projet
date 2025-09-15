from Plateau import Plateau
from Des import Des
from Joueur import Joueur
from ReglesDujeu import ReglesDujeu
from Tuile import Tuile 
from Tour import Tour


def main():
    # Initialisation du jeu
    faces = [
        Tuile('T', 'E', 'E', 'T'),  
        Tuile('T', 'T', 'E', 'T'),  
        Tuile('T', 'E', 'T', 'E'),  
        Tuile('R', 'E', 'E', 'R'),
        Tuile('R', 'R', 'E', 'R'),  
        Tuile('R', 'E', 'R', 'E'),
        Tuile('R', 'T', 'R', 'T'),  
        Tuile('T', 'E', 'R', 'E'),  
        Tuile('T', 'E', 'E', 'R') 
    ]
    
    # Créer des tuiles spéciales 
    faces_speciales = [
        Tuile('R', 'T', 'R', 'T'),  
        Tuile('R', 'R', 'T', 'R'),  
        Tuile('R', 'T', 'T', 'T'), 
        Tuile('R', 'R', 'R', 'R'),  
        Tuile('T', 'T', 'T', 'T'),  
        Tuile('R', 'T', 'T', 'R') 
    ]

    plateau = Plateau(size=7)
    des = Des(faces, faces_speciales)
    regles = ReglesDujeu()

    # Création des joueurs
    joueur1 = Joueur(nom=1, plateau=plateau)
    joueur2 = Joueur(nom=2, plateau=plateau)
    
    # Ajouter les joueurs aux règles
    regles.ajouter_joueur(joueur1.nom)
    regles.ajouter_joueur(joueur2.nom)
    
    # Début de la partie
    regles.commencer_partie()
    
    

    while not regles.est_partie_terminee():
        # Exécuter le tour pour tous les joueurs
        tour.executer_tour()
        
        # Fin du round
        regles.terminer_round()


    print("La partie est terminée !")

if __name__ == "__main__":
    main()
