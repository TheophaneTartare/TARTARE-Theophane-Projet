import sys
import websocket
from PyQt5.QtCore import QThread, pyqtSignal
from PyQt5.QtWidgets import QApplication, QMainWindow
from PyQt5.QtGui import QPixmap
import interfaceGraphique.interGraphique as ig
from regles.src.Des import *
from regles.src.ReglesDujeu import *
from regles.src.Plateau import *
from regles.src.Tuile import *

class WebSocketThread(QThread):
    message_recu = pyqtSignal(list)
    wsapp_ready = pyqtSignal(object)
    
    def __init__(self):
        """
        Initialisation 
        """
        super().__init__()
        self.joueurs = {}
        self.arbitreId = "0"
        self.regles = ReglesDujeu()

    def run(self):
        """
        Lance la connexion WebSocket et commence à écouter les messages du serveur
        """
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
        """
        Traite les messages reçus du serveur"""

        print(f"Message reçu : {message}")
        self.message_recu.emit(message.split(" "))
        messageDecouper = message.split(" ")
        if len(messageDecouper) > 1:
            idJoueur = messageDecouper[0]
            commande = messageDecouper[1]

            if commande == "ENTERS" and idJoueur != self.arbitreId:
                self.joueurs[idJoueur] = True
                print(f"dictionnaire joueur : {self.joueurs}")
                self.regles.ajouter_joueur(idJoueur)

            elif commande == "LEAVES":
                del self.joueurs[idJoueur]
                print(f"dictionnaire joueur : {self.joueurs}")

    def on_error(self, wsapp, error):
        """Gère les erreurs de la connexion WebSocket"""
        print(f"Erreur : {error}")

    def on_close(self, wsapp, close_status_code, close_msg):
        """Gère la fermeture de la connexion WebSocket."""
        print("Connexion WebSocket fermée")

    def on_open(self, wsapp):
        """Envoie un message au serveur pour indiquer quun joueur entre dans la partie"""
        self.wsapp_ready.emit(self.wsapp)
        message = f"{self.arbitreId} ENTERS"
        self.wsapp.send(message)

class MainWindow(QMainWindow):
    def __init__(self):
        """Initialise la fenêtre principale"""
        super().__init__()
        self.wsapp = None
        self.dessin = None
        self.arbitreId = "0"
        self.plateau = Plateau(7)

        self.ws_thread = WebSocketThread()
        self.ws_thread.message_recu.connect(self.commande)
        self.ws_thread.wsapp_ready.connect(self.on_wsapp_ready)
        self.ws_thread.start()

        self.resize(2000, 2000)
        self.show()

    def commande(self, message):
        """ Gère les commandes envoyées via WebSocket, telles que le placement des tuiles ou le changement d'image."""
        if self.dessin:
            if message[1] == "THROWS":
                print(f"Changement d'image : {message[2]}")
            elif message[1] == "PLACES":
                self.dessin.placeTuile(message[2], message[3], message[4])
            elif message[1] == "BLAMES":
                print(f"BLAMES: {message[2]}")

    def on_wsapp_ready(self, wsapp):
        """ Initialise l'interface graphique lorsque WebSocket est prêt"""
        self.wsapp = wsapp
        self.dessin = ig.Dessin(7, self.arbitreId, self, self.wsapp, self.plateau)
        self.setCentralWidget(self.dessin)
        self.lancer_des()

    def lancer_des(self):
        """Lance les dés et envoie les résultats au serveur WebSocket."""
        print("Lancement des dés...")
        faces = ['Hc','Hj','H','RR', 'Rc','Rj','R','Sc', 'FSc', 'S']
        faces_speciales = ['HH','SR','SS','HR', 'SH', 'SHR']
        des = Des(faces, faces_speciales)
        resultats = des.lancerDes()
        print(f"Résultats des dés : {resultats}")
        message = f"{self.arbitreId} THROWS {' '.join(resultats)}"
        print(f"message: {message}")
        self.wsapp.send(message)
        self.afficher_des(resultats)

    def afficher_des(self, resultats):
        """Affiche les résultats des dés"""
        for resultat in resultats:
            self.dessin.ajoutTuileReserve(resultat, special=False)

if __name__ == "__main__":
    app = QApplication(sys.argv)
    mainWindow = MainWindow()
    sys.exit(app.exec_())