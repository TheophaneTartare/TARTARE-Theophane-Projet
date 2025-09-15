from Joueur import Joueur 
from Des import Des
from Plateau import Plateau
from Tuile import Tuile
from DictionnaireTuile import tuiles,lettreEnChiffre
import random


class Arbitre:
    def __init__(self, nom):
        self.nom = nom  
        self.joueurs = {} 
        self.des = Des(['Hc','Hj','H','Rc','Rj','R'],['HR','Sc','S'])
        self.deeSpecial = ['SH', 'SR', 'SS','HH','HR','RR']
        self.tuiles = tuiles
        self.listeDee=[]

    def create_joueur(self, nom):
        print(f"Création du joueur {nom}")
        self.joueurs[nom] = {"nom":nom ,"tuile" :self.listeDee , "yield" : False ,"plateau" : Plateau(7) , "tuilePlacer" :[] }  

    def getJoueur(self,nom) :
        return self.joueurs[nom]

    def ajoutTuilesMain(self, listdees):
        for joueur in self.joueurs.values():
            joueur["tuile"]=listdees

    def joueurFiniTour(self , nomJoueur ) :
        joueur = self.joueurs[nomJoueur] 
        print(f"tuile à la fin du tour {joueur['tuile']} ")
        if len(joueur["tuile"]) == 0 :
            joueur["yield"] = True
            return True 
        else : 
            return False  
    
    def JoeurFiniToursavoirSiTout(self) :
        res = True 
        for joueur in self.joueurs.values() :
            if joueur["yield"] == False  : 
                res = False 
        return res 
        

    def resetJoueurTour(self) :
        for joueur in self.joueurs.values() :
            joueur["yield"] = False  


    def tiragedee(self) :
        tuiles = self.des.lancerDes()
        self.tuiles_en_main = tuiles
        self.listeDee=tuiles
        return tuiles

    def arbitre_place_tuile(self,nomJoueur,tuile,orientation,x,y) :
        joueur = self.joueurs[nomJoueur] 
        plateau = joueur["plateau"]
        x = lettreEnChiffre[x] 
        
        tuilePlacerCoter = self.tuiles[orientation][tuile]  
        tuileAPlacer = Tuile(tuilePlacerCoter[0] ,tuilePlacerCoter[1] ,tuilePlacerCoter[2] ,tuilePlacerCoter[3])
        isCorect = plateau.placer_tuile(tuileAPlacer,x,y)
        return isCorect 

    def deleteTuile(self, nom, tuile):
        print(f"supression de {tuile}")
        tuiles = self.joueurs[nom]["tuile"]
        if tuile in tuiles:
            tuiles.remove(tuile)
            self.joueurs[nom]["tuilePlacer"].append(tuile)
            print(f"les tuile restante de {nom} sont {tuiles}")
        else:
            print(f"La tuile {tuile} n'est pas dans la liste.")

    def retourEtapePrecedante(self, nom):
        print("retour Etape")
        tuiles = self.joueurs[nom]["tuile"]
        
        if self.joueurs[nom]["tuilePlacer"]:
            tuiles.append(self.joueurs[nom]["tuilePlacer"][-1] )
            del self.joueurs[nom]["tuilePlacer"][-1]

        print(f"tuilePlacer = {self.joueurs[nom]['tuilePlacer'][-1]} tuille A placer = {self.joueurs[nom]['tuile']} ")



    def isSpecial(self,tuile) :
        if tuile in self.deeSpecial :
            return True
        else :
            return False 

    def toutLesJoueurJouer(self) :
       res = True
       for joueur in self.joueurs.values():
           if len(joueur["tuile"]) != 0 :
               res = False
       return res




    