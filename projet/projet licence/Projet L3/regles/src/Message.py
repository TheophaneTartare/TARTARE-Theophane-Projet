import re
import websocket
import threading

class Message:
    def __init__(self, websocket_url):
        """Initialise la classe en définissant les modèles de validation de messages et
        en se connectant au serveur WebSocket spécifié par l'URL"""
        self.valid_patterns = {
            'ENTERS': r'^$',
            'LEAVES': r'^$',
            'THROWS': r'^(\w+)( \w+)*$',  # tuile+
            'PLACES': r'^\w+ \w+$',  # tuile cellule
            'YIELDS': r'^$',  # Aucun paramètre
            'SCORES': r'^\d+ \d+$',
            'BLAMES': r'^\d+$',
            'GRANTS': r'^\w+ \w+$',  # id2 message
            'AGREES': r'^.+$',  # règle+
            'ELECTS': r'^\w+( \w+)*$',  # joueur+
        }
        self.websocket_url = websocket_url
        self.websocket = None
        self.connected = False
        self.connection_event = threading.Event()
        self.connect_websocket()
        
    

    def connect_websocket(self):
        """Connecte le client WebSocket au serveur avec reconnexion automatique."""
        def run():
            while True:
                try:
                    print("Tentative de connexion WebSocket...")
                    self.ws = websocket.WebSocketApp(self.websocket_url,
                                                     on_message=self.on_message,
                                                     on_error=self.on_error,
                                                     on_close=self.on_close)
                    self.ws.on_open = self.on_open
                    self.ws.run_forever()
                except Exception as e:
                    print(f"Erreur WebSocket : {e}")

        self.ws_thread = threading.Thread(target=run)
        self.ws_thread.daemon = True
        self.ws_thread.start()

    def on_message(self, ws, message):
        """Gère les messages reçus du serveur WebSocket."""
        print(f"Message reçu via WebSocket : {message}")
        self.receive(message)

    def on_error(self, ws, error):
        """Gère les erreurs de connexion WebSocket."""
        print(f"Erreur WebSocket : {error}")
        self.connected = False

    def on_close(self, ws, close_status_code, close_msg):
        """Gère la fermeture de la connexion WebSocket."""
        print("Connexion WebSocket fermée")
        self.connected = False

    def on_open(self, ws):
        """Gère l'ouverture de la connexion WebSocket."""
        print("Connexion WebSocket établie avec succès.")
        self.connected = True
        self.connection_event.set()
        
    def start(self):
        """Démarre le service de gestion des messages"""
        print("Message service started!")

    def receive(self, message):
        """Traite un message reçu, valide et affiche un message valide ou erreur """
        if self.validate(message):
            print(f"Message reçu : {message}")
        else:
            print(f"Message invalide : {message}")

    def validate(self, message):
        """Vérifie la validité du message."""
        parts = message.split(maxsplit=2)
        if len(parts) < 2:
            return False  # Doit avoir au moins id et mot-clé

        sender_id = parts[0]
        keyword = parts[1]
        params = parts[2] if len(parts) > 2 else ""

        if keyword not in self.valid_patterns:
            return False

        pattern = self.valid_patterns[keyword]
        return bool(re.fullmatch(pattern, params))

    def send(self, sender_id, keyword, *params):
        """Envoie un message en s'assurant que la WebSocket est connectée."""
        message = f"{sender_id} {keyword} {' '.join(params)}"

        if self.ws and self.ws.sock and self.ws.sock.connected:
            self.ws.send(message)
            print(f"Message envoyé via WebSocket : {message}")
        else:
            print("WebSocket non connectée. Impossible d'envoyer le message.")


if __name__ == "__main__":
    websocket_url = "ws://localhost:3000"
    message = Message(websocket_url)
    message.start()

    # Attendre que la connexion WebSocket soit établie
    message.connection_event.wait()
    print("Connexion WebSocket établie. Début des tests.")


    # Tests de messages valides
    message.send("id1", "ENTERS")
    message.send("id2", "ENTERS")

    message.send("id2", "THROWS", "tuile1", "tuile2")
    message.send("id2", "PLACES", "tuile2", "C3")
    message.send("id2", "SCORES", "1", "10")
    message.send("id2", "YIELDS")
    message.send("id2", "GRANTS", "id2", "SpecialRule")
    message.send("id2", "ELECTS", "playerA", "playerB")

    # Tests de messages invalides
    message.send("id9", "THROWS")  # Manque de paramètres
    message.send("id1", "ENTERS", "player1")
    message.send("id5", "PLACES", "tile1", "A1", "vertical", "extra")  # Trop d'arguments
    message.send("id6", "SCORES", "1", "50", "extra")  # Trop d'arguments
    message.send("id7", "GRANTS", "player2", "access", "extra")
