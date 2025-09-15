const WebSocket = require('ws');
const fs = require('fs');

function main() {
  const ws = new WebSocket('ws://localhost:3000');

  ws.on('open', () => {
    console.log('Connexion WebSocket ouverte');
    fs.readFile('envoi.txt', 'utf8', (err, data) => {
      if (err) {
        console.error("Erreur lors de la lecture du fichier :", err);
        return;
      }

      const lignes = data.split('\n');
      lignes.forEach(ligne => {
        ligne = ligne.trim();
        if (ligne) {
          ws.send(ligne);
          console.log(`Message envoyé : ${ligne}`);
        }
      });
    });
  });

  ws.on('message', (message) => {
    console.log(`Message reçu : ${message}`);
  });

  ws.on('error', (error) => {
    console.error(`Erreur : ${error}`);
  });

  ws.on('close', () => {
    console.log('Connexion WebSocket fermée');
  });
}

main();