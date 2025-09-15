# import websocket
#ws = websocket.WebSocket()
#ws.connect("ws://localhost:3000")
# fichier
#f = open("fic.txt", 'r')
#contenu = f.read()
#for s in contenu:
  #  ws.send(s)
 #   print(s)
#ws.send("1 ENTERS")
#ws.send("Client Num 1 PLACES R")
#print(ws.recv())
#ws.close()
import websocket

def on_message(wsapp, message): 
    print(f"Message reçu : {message}")

def on_error(wsapp, error):
    print(f"Erreur : {error}")

def on_close(wsapp, close_status_code, close_msg):
    print("Connexion fermée")

def on_open(wsapp):
   # ouverture de la connexion WebSocket.
  #  Lire le fichier ligne par ligne et envoyer son contenu au réflecteur.
    try:
        with open("envoi.txt", "r") as fichier:
            for ligne in fichier:
                ligne = ligne.strip()  # Supprime les espaces ou retours à la ligne inutiles
                if ligne:  # Vérifie que la ligne n'est pas vide
                    wsapp.send(ligne)
                    print(f"Message envoyé : {ligne}")
    except FileNotFoundError:
        print("Erreur : Le fichier 'texte.txt' est introuvable.")
    except Exception as e:
        print(f"Erreur inattendue lors de la lecture du fichier : {e}")

if __name__ == "__main__":
    # Active les traces pour faciliter le débogage
    websocket.enableTrace(True)

    # Initialise la connexion WebSocket
    wsapp = websocket.WebSocketApp(
        "ws://localhost:3000",
        on_open=on_open,
        on_message=on_message,
        on_error=on_error,
        on_close=on_close
    )

    # Exécute la connexion WebSocket en continu
    wsapp.run_forever()
