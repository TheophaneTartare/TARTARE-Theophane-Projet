import websocket
import sys
from PyQt5.QtCore import QThread, pyqtSignal
from PyQt5.QtWidgets import QApplication, QMainWindow
import interfaceGraphique.interGraphique as ig


idJoueur="2"
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
        print(f"Message reçu : {message}")
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

        self.ws_thread = WebSocketThread()
        self.ws_thread.message_recu.connect(self.commande)
        self.ws_thread.wsapp_ready.connect(self.on_wsapp_ready)
        self.ws_thread.start()

    
        self.resize(2000, 2000)
        self.show()

    def commande(self,message):
        if self.dessin:
            if(message[1]=="THROWS"):
                print(f"Changement d'image : {message[2]}")
                self.dessin.setNomImgGrandeTuile(message[2])
            elif(message[1]=="PLACES"):
                self.dessin.placeTuile(message[2],message[3],message[4])

    def on_wsapp_ready(self, wsapp):
        self.wsapp = wsapp
        self.dessin = ig.Dessin(7,idJoueur,self,self.wsapp)
        self.setCentralWidget(self.dessin)
    

if __name__ == "__main__":
    app = QApplication(sys.argv)
    mainWindow = MainWindow()
    sys.exit(app.exec_())

