import websocket
import sys
from PyQt5.QtCore import QThread, pyqtSignal, QTimer
from PyQt5.QtWidgets import QApplication, QMainWindow
from interfaceGraphique import interGraphique as ig
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), "regles/src")))
from Des import Des
from Arbitre import Arbitre
from Joueur import Joueur



idJoueur=input("Entrez votre ID de joueur : ")
class WebSocketThread(QThread):
    message_recu = pyqtSignal(list)
    wsapp_ready = pyqtSignal(object)


    def run(self):
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
        print(f"Message reçu joueur : {message}")
        self.message_recu.emit(message.split(" "))


    def on_error(self, wsapp, error):
        print(f"Erreur : {error}")

    def on_close(self, wsapp, close_status_code, close_msg):
        print("Connexion WebSocket fermée")

    def on_open(self,wsapp):
        self.wsapp_ready.emit(self.wsapp)
        wsapp.send(f"{idJoueur}  ENTERS")



class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()
        self.wsapp=None
        self.dessin = None
       # self.joueur = Joueur(idJoueur)

        self.ws_thread = WebSocketThread()
        self.ws_thread.message_recu.connect(self.commande)
        self.ws_thread.wsapp_ready.connect(self.on_wsapp_ready)
        self.ws_thread.start()

    
        self.resize(2000, 2000)
        self.show()

    def commande(self,message):
        parts = message
        if self.dessin:
            if parts[1] == 'ENTERS' and idJoueur == parts[0] :
                print(f"joueur {idJoueur}")

            elif(parts[1]=="THROWS"):
                self.dessin.ajoutTuileReserve(parts[2])
                self.dessin.ajoutTuileReserve(parts[3])
                self.dessin.ajoutTuileReserve(parts[4])
                self.dessin.ajoutTuileReserve(parts[5])
                tuiles=[parts[2],parts[3],parts[4],parts[5]]

                

            elif parts[1] == 'BLAMES' :
                if int(parts[2]) - int(idJoueur) == 100  : 
                    self.dessin.retourArriereGrahpique() 
                

                elif int(parts[2]) - int(idJoueur) == 200 : 
                    print("erreur de yield") 
                   # self.dessin.retourArriereGrahpique() 
                    
                    


    def on_wsapp_ready(self, wsapp):
        self.wsapp = wsapp
        self.dessin = ig.Dessin(7,idJoueur,self,self.wsapp)
        self.setCentralWidget(self.dessin)

    def deleteTuile(self,tuile) :
        self.dessin.deleteTuile(tuile)
    

if __name__ == "__main__":
    app = QApplication(sys.argv)
    mainWindow = MainWindow()
    sys.exit(app.exec_())