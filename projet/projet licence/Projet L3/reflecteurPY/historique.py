import websocket


def on_message(ws, message):
    print(f"Message reçu : {message}")
    with open("reçu.txt", "a") as fichier:
        fichier.write(message + "\n")

def on_error(ws, error):
    print(f"Erreur : {error}")

def on_close(ws, close_status_code, close_msg):

    print("Connexion fermée")
    with open(HISTORIQUE_FICHIER, "w") as fichier:
        fichier.write("")

def on_open(ws):
    print("Connecté au réflecteur.")

if __name__ == "__main__":
    ws_url = "ws://localhost:3000"
    
    ws = websocket.WebSocketApp(
        ws_url,
        on_message=on_message,
        on_error=on_error,
        on_close=on_close
    )
    ws.on_open = on_open
    ws.run_forever()
