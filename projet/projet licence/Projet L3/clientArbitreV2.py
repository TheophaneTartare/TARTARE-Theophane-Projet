# version 1 de l arbitre : lancer l interface, lancer les des et les afficher  
import websocket
import sys
from PyQt5.QtCore import QThread, pyqtSignal, QTimer
from PyQt5.QtWidgets import QApplication, QMainWindow
from PyQt5.QtGui import QPixmap
from interfaceGraphique import interGraphique as ig
from interfaceGraphique.Dictionnaire import *
import os   

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "regles/src")))

from Des import Des
from Arbitre import Arbitre
from Joueur import Joueur

#from interfaceGraphique.interGraphique import *

# pour les blames ont a la centaine qui est egale a la nature du blames est les dizaine et uniter qui est l'id du joueur blamer 
# 100 + id = joueur placement incorecte de tuile 
# 200 + id = joueur a fait un YIELD invalide 


class WebSocketThread(QThread):
    """cette classe hérite de Qthread et est utilisée pour gérer 
    la connexion Websocket"""
    message_recu = pyqtSignal(list)
    wsapp_ready = pyqtSignal(object)

    def run(self):
        """initialise la connexion en déclanchant la connexion
        et maintient la connexion ouverte"""
        websocket.enableTrace(False)
        self.wsapp = websocket.WebSocketApp(
            "ws://localhost:3000",
            on_message=self.on_message,
            on_error=self.on_error,
            on_close=self.on_close
        )
        self.wsapp.on_open = self.on_open
        self.wsapp.run_forever()

    def on_message(self, wsapp, message):
        """traiter les messages reçus en les séparant avec un espace """
        print(f"Message reçu : {message}")
        self.message_recu.emit(message.split(" "))


    def on_error(self, wsapp, error):
        """ capture et affiche toute erreur survenue"""
        print(f"Erreur : {error}")

    def on_close(self, wsapp, close_status_code, close_msg):
        """annoncer que la connexion websocket est fermée"""
        print("Connexion WebSocket fermée")

    def on_open(self, wsapp):
        """envoie le message ENTERS pour indiquer qu'un joueur
        a integré la partie"""
        print("Connexion WebSocket ouverte")
        self.wsapp_ready.emit(wsapp)
        wsapp.send(f"0 ENTERS")

class MainWindow(QMainWindow):
    """cette classe hérite de QMainWindow, elle représente
    la fenêtre principale"""
    def __init__(self):
        """ initialise la fenêtre, crée un arbitre et lance le webSocketThread
        pour gérer la communication """
        super().__init__()
        self.wsapp = None

        self.arbitre = Arbitre(0)

        self.ws_thread = WebSocketThread()
        self.ws_thread.message_recu.connect(self.commande)
        self.ws_thread.wsapp_ready.connect(self.on_wsapp_ready)
        self.ws_thread.start()

        self.resize(2000, 2000)
        self.show()

    def commande(self, message):
        """ gère les commandes reçues via Websocket
        et analyse le message reçu et exécute des actions en fonction 
        du contenu du message"""
        parts = message

        if len(parts) == 2 and parts[1] == "ENTERS":
            nom_joueur = parts[0]  
            if nom_joueur == "0" : 
                self.lancer_des()
            else :
                self.arbitre.create_joueur(nom_joueur)


        elif parts[1] == "PLACES" :
            nom_joueur = parts[0]
            tuile = parts[2]
            orientation = parts[3]
            coordonner = list(parts[4])
            x = coordonner[0]
            y = int(coordonner[1])


            if not self.arbitre.isSpecial(parts[2]):
                self.deleteTuile(nom_joueur, parts[2])

            if not self.arbitre.arbitre_place_tuile(nom_joueur, tuile, orientation, x, y):
                message = f"{0} BLAMES {100 + int(nom_joueur)}"
                if self.wsapp and self.wsapp.sock and self.wsapp.sock.connected:
                    self.wsapp.send(message)
                    self.arbitre.retourEtapePrecedante(str(nom_joueur))


            

            print(f"Tous les joueurs ont joué : {self.arbitre.toutLesJoueurJouer()}")
        
        elif parts[1] == "YIELDS" :  
            nom_joueur = parts[0] 
            if not self.arbitre.joueurFiniTour(nom_joueur) :
                message = f"{0} BLAMES {200+int(nom_joueur)}"
                if self.wsapp and self.wsapp.sock and self.wsapp.sock.connected:
                    self.wsapp.send(message)
                
            if self.arbitre.toutLesJoueurJouer() and self.arbitre.JoeurFiniToursavoirSiTout() :
                self.lancer_des()  

        elif parts[1] == "BLAMES" :
            if int(parts[2]) - 200 > 0: 
                nom_joueur = int(parts[2]) - 200
               # self.arbitre.retourEtapePrecedante(str(nom_joueur))
            


        
        

    def deleteTuile(self,nom,tuile) :
        """supprime la tuile du jeu"""
        self.arbitre.deleteTuile(nom,tuile)

    def lancer_des(self) :
        """gère le lancement de dès et l'ajout des tuiles lancées aux joueurs"""
        arbitre = self.arbitre
        listeDee = self.arbitre.tiragedee()
        arbitre.ajoutTuilesMain(listeDee)   
        message = f"{0} THROWS {' '.join(listeDee)}"
        if self.wsapp and self.wsapp.sock and self.wsapp.sock.connected:
            self.wsapp.send(message)
            print(f"envoie de {listeDee}")
        else:
            print("Erreur, la connexion websocket est fermée !")
    


    def on_wsapp_ready(self, wsapp):
        """ méthode appelée lorsque websocket est pret
        et assigne l'instance websocket et initialise l'affichage
        de la fenetre principale"""
        self.wsapp = wsapp  # Assigne l'instance active
       # self.dessin = ig.Dessin(7, self)
       # self.setCentralWidget(self.dessin)


if __name__ == "__main__":
    app = QApplication(sys.argv)
    mainWindow = MainWindow()
    sys.exit(app.exec_())