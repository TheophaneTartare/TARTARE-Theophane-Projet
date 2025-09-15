import http from 'http';
import { Server as IOServer } from 'socket.io';
import RequestController from './controllers/requestController.js';

const server = http.createServer((req, res) => {
  const requestController = new RequestController(req, res);
  requestController.handleRequest();
});

const io = new IOServer(server);

let adminSocket = null;
let currentVote = ''; 
let votes = new Map(); 

let votantsConnectes = 0; 

io.on('connection', (socket) => {
  console.log(`Client connecté: ${socket.id}`);

  if (currentVote) {
    socket.emit('voteInProgress', currentVote, votes);
  }

  socket.emit('checkAdmin');

  socket.on('checkAdmin', () => {
    if (adminSocket) {
      socket.emit('adminExists');
    } else {
      adminSocket = socket;
      socket.emit('becomeAdmin');
    }
  });

  votantsConnectes++;
  io.emit("voterConnected", votantsConnectes);

  socket.on('disconnect', () => {
    if (socket === adminSocket) {
      adminSocket = null; 
    } else {
      votantsConnectes = Math.max(0, votantsConnectes - 1);
      io.emit("voterDisconnected", votantsConnectes); 
    }
    console.log(`Client ${socket.id} déconnecté`);
  });
  
  socket.on('startVote', (text) => {
    if (!adminSocket || socket !== adminSocket) return; 
  
    currentVote = text;
    votes = new Map();  
    io.emit('voteInProgress', currentVote, Object.fromEntries(votes));
  });
  

  socket.on('endVote', (finalVotes) => {
    if (!adminSocket || socket !== adminSocket) return;
  
    io.emit('voteResults', finalVotes); 
    currentVote = ''; 
    votes = {};  
  });

  socket.on('vote', (vote) => {
    if (!currentVote) return; 
  
    votes.set(socket.id, vote);
  
    const formattedVotes = Object.fromEntries(votes); 
    io.emit('updateVotes', formattedVotes); 
  });
  
});


server.listen(8080, () => {
  console.log('Server is listening on port 8080');
})
