

import websocket

def on_message(wsapp, message):
    print(f"Message reçu : {message}")

def on_error(wsapp, error):
    print("Erreur : {error}")

def on_close(wsapp, close_status_code, close_msg):
    print("Connexion fermée")

def on_open(wsapp):
    fichier = open("envoi.txt","r")

    with open("texte.txt") as fichier:
        for ligne in fichier:
            wsapp.send(ligne)
            print(ligne) 
    
if __name__ == "__main__":
    websocket.enableTrace(True)
    wsapp = websocket.WebSocketApp("ws://localhost:3000",
                              on_open=on_open,
                              on_message=on_message,
                              on_error=on_error,
                              on_close=on_close)
wsapp.on_open = on_open
wsapp.run_forever()
