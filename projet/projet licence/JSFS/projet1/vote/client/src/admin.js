import { io } from 'socket.io-client';
import { Chart, LinearScale, BarElement, CategoryScale, BarController, Title } from 'chart.js';

Chart.register(LinearScale, BarElement, CategoryScale, BarController, Title);

const socket = io();

const adminMessage = document.getElementById('adminMessage');
const voteTextInput = document.getElementById('voteText');
const openVoteBtn = document.getElementById('openVoteBtn');
const closeVoteBtn = document.getElementById('closeVoteBtn');
const votConect = document.getElementById('votantconect');
const votRecus = document.getElementById('votereçu');

let votantsConnectes = 0;
let votesRecus = 0;
let myChart = null;

let voteData = {
  Pour: 0,
  Contre: 0,
  NPPV: 0,
  Abstention: 0
};

socket.emit('checkAdmin');

socket.on('becomeAdmin', () => {
  adminMessage.textContent = "Vous êtes l'administrateur des votes.";
  voteTextInput.disabled = false;
  openVoteBtn.disabled = false;
});

socket.on('adminExists', () => {
  adminMessage.textContent = "Un administrateur est déjà connecté.";
});

socket.on("voteResults", (finalVotes) => {
  voteData = finalVotes; 
  if (myChart) {
    myChart.data.datasets[0].data = Object.values(voteData);
    myChart.update();
  }
});


openVoteBtn.addEventListener('click', () => {
  const voteText = voteTextInput.value;
  socket.emit('startVote', voteText);
  openVoteBtn.disabled = true;
  closeVoteBtn.disabled = false;

  const votingResultsDiv = document.getElementById('votingResults');
  votingResultsDiv.style.display = "block";

  voteData = { Pour: 0, Contre: 0, NPPV: 0, Abstention: 0 };
  votesRecus = 0;
  votRecus.textContent = votesRecus;

  const myChartElement = document.getElementById('myChart');
  if (myChartElement) {
    if (myChart) {
      myChart.destroy(); 
    }

    myChart = new Chart(myChartElement, {
      type: 'bar',
      data: {
        labels: ['Pour', 'Contre', 'NPPV', 'Abstention'],
        datasets: [{
          label: 'Votes',
          data: Object.values(voteData),
          backgroundColor: [
            'rgba(128,255,128,0.5)',
            'rgba(255,99,132,0.5)',
            'rgba(255,255,0,0.5)',
            'rgba(0,0,255,0.5)'
          ],
          borderColor: ['rgba(0, 0, 0, 1)'],
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          y: {
            min: 0,
            max: 13
          }
        }
      }
    });
  } else {
    console.error("L'élément myChart est introuvable !");
  }
});


closeVoteBtn.addEventListener('click', () => {
  socket.emit('endVote', voteData); 
  closeVoteBtn.disabled = true;
  openVoteBtn.disabled = false; 
  voteTextInput.value = "";

});



socket.on("voterConnected", (voter) => {
  console.log(`${voter} a rejoint le vote`);
  votantsConnectes++;
  votConect.textContent = votantsConnectes;
});

socket.on("voterDisconnected", (voter) => {
  console.log(`${voter} a quitté le vote`);
  votantsConnectes = Math.max(0, votantsConnectes - 2);
  votConect.textContent = votantsConnectes;
});

socket.on("updateVotes", (votes) => {
  voteData = { Pour: 0, Contre: 0, NPPV: 0, Abstention: 0 };
  Object.values(votes).forEach(vote => {
    if (voteData[vote] !== undefined) {
      voteData[vote]++;
    }
  });

  if (myChart) {
    myChart.data.datasets[0].data = Object.values(voteData);
    myChart.update();
  } else {
    console.error("Le graphique n'est pas encore initialisé !");
  }

  votesRecus = Object.keys(votes).length;
  votRecus.textContent = votesRecus;
});
