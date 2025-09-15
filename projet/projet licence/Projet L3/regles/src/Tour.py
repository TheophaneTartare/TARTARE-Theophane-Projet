
from Joueur import Joueur
from Des import Des
from Message import Message 

class Tour:

    def __init__(self, joueurs, des, websocket_url):
        self.joueurs = joueurs
        self.des = des
        self.websocket_url = websocket_url
        self.message = Message(websocket_url)
        self.tuiles_placees = set() # un ensemble vide ou sont stockées les positions des tuiles placées
  
    

    def placer_tuiles(self, joueur, tuiles):
        """
        Permet à un joueur de placer les tuiles sur son plateau
        
        """
        for tuile in tuiles:
            print(f"{joueur.nom}, vous devez placer la tuile : {tuile}")
            while True:
                try:
                    x = int(input(f"Entrez la coordonnée x (0-{joueur.plateau.size - 1}) : "))
                    y = int(input(f"Entrez la coordonnée y (0-{joueur.plateau.size - 1}) : "))
                    
                    # Vérifier si l'emplacement est déjà occupé
                    if joueur.plateau.grille[x][y] is not None:
                        print("Emplacement déjà occupé. Veuillez choisir un autre emplacement.")    
                        continue
                    #placer tuile
                    if joueur.plateau.placer_tuile(tuile, x, y):
                        
                        self.message.send(joueur.nom, "PLACES", str(tuile),  f"x={x}, y={y}")
                        break
                    else:
                        print("Placement invalide. Veuillez choisir un autre emplacement.")
                except ValueError:
                    print("Veuillez entrer des nombres entiers valides.")



    def executer_tour(self):
        """
        Exécute un tour complet pour tous les joueurs.
        """
        
        tuiles_tirees = self.des.lancerDes()
        print(f"Tuiles tirées pour tous : {tuiles_tirees}")

        for joueur in self.joueurs:
            self.placer_tuiles(joueur, tuiles_tirees)
            tuiles_tirees_str = [str(tuile) for tuile in tuiles_tirees]
            self.message.send(joueur.nom, "THROWS", *tuiles_tirees_str)

        for joueur in self.joueurs:
            self.message.send(joueur.nom, "YIELDS")



  



