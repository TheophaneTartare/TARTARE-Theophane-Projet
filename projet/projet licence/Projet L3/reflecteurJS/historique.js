const WebSocket = require('ws');
const fs = require('fs');

function main() {
  const wsUrl = 'ws://localhost:3000';
  const ws = new WebSocket(wsUrl);
  const historiqueFichier = "reçu.txt";

  ws.on('open', () => {
    console.log('Connecté au réflecteur.');
  });

  ws.on('message', (message) => {
    console.log(`Message reçu : ${message}`);
    fs.appendFile(historiqueFichier, message + '\n', (err) => {
      if (err) {
        console.error('Erreur lors de l\'écriture dans le fichier :', err);
      }
    });
  });

  ws.on('error', (error) => {
    console.error(`Erreur : ${error}`);
  });

  ws.on('close', () => {
    console.log('Connexion fermée');
    fs.writeFile(historiqueFichier, "", (err) => {
      if (err){
        console.error("Erreur lors de l'effacement du fichier :", err);
      }
    });
  });
}

main();

