import { io } from 'socket.io-client';
import { Chart, LinearScale, BarElement, CategoryScale, BarController, Title } from 'chart.js';

Chart.register(LinearScale, BarElement, CategoryScale, BarController, Title);

const socket = io();
const zoneVote = document.getElementById("zoneVote");
let resultChart = null;

let currentVote = null; 

socket.emit("voterConnected", socket.id); 

socket.on("voteResults", (finalVotes) => {
  zoneVote.innerHTML = `<h3>Résultats du vote :</h3> <canvas id="resultChart"></canvas>`;
  setTimeout(() => displayResultsChart(finalVotes), 100); 
  currentVote = null; 
});



window.addEventListener("beforeunload", () => {
  socket.emit("voterDisconnected", socket.id); 
});


socket.emit("checkVote");

socket.on("voteInProgress", (voteText, votes) => {
  currentVote = voteText;
  zoneVote.innerHTML = `<p>Vote en cours : ${currentVote}</p>`;
  updateVoteDisplay(votes);
});

socket.on("updateVotes", (votes) => {
  updateVoteDisplay(votes);
});

socket.on("voteResults", (votes) => {
  zoneVote.innerHTML = `<h3>Résultats du vote :</h3> ${displayResults(votes)}`;
  currentVote = null; 
});

function voter(choix) {
  if (!currentVote) return; 
  socket.emit("vote", choix);
}

function displayResults(votes) {
  let res = "";
  const counts = {};
  for (let vote of Object.values(votes)) {
    counts[vote] = (counts[vote] || 0) + 1;
  }
  for (let [option, count] of Object.entries(counts)) {
    res += `<p>${option} : ${count} votes</p>`;
  }
  return res;
}

function updateVoteDisplay(votes) {
  zoneVote.innerHTML = `<h3>Vote en cours : ${currentVote}</h3>`;
  
  const buttons = [
    { label: 'Pour', vote: 'Pour' },
    { label: 'Contre', vote: 'Contre' },
    { label: 'NPPV', vote: 'NPPV' },
    { label: 'Abstention', vote: 'Abstention' },
  ];

  buttons.forEach(button => {
    const btn = document.createElement('button');
    btn.textContent = button.label;
    btn.addEventListener('click', () => voter(button.vote)); 
    zoneVote.appendChild(btn);
  });
}


socket.on("voteResults", (finalVotes) => {
  zoneVote.innerHTML = `<h3>Résultats du vote :</h3> <canvas id="resultChart"></canvas>`;

  requestAnimationFrame(() => {
    displayResultsChart(finalVotes);
  });

  currentVote = null; 
});


function displayResultsChart(voteData) {
  const resultChartElement = document.getElementById("resultChart");

  if (resultChart !== null) {
    resultChart.destroy();
  }

  resultChartElement.width = 900;
  resultChartElement.height = 600;

  resultChart = new Chart(resultChartElement, {
    type: 'bar',
    data: {
      labels: ['Pour', 'Contre', 'NPPV', 'Abstention'],
      datasets: [{
        label: 'Résultats finaux',
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
      responsive: true, 
      maintainAspectRatio: true, 
      aspectRatio: 1.5, 
      scales: {
        y: {
          beginAtZero: true,
          suggestedMax: 13,
          ticks: {
            stepSize: 1
          }
        }
      }
    }
  });
}





