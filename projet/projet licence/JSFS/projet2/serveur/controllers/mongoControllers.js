const mongoose = require('mongoose');

const dbHost = '127.0.0.1';
const dbPort = 27017;
const dbName = 'student';
const dbURI = `mongodb://${dbHost}:${dbPort}/${dbName}`;

mongoose.connect(dbURI, {
  useNewUrlParser: true,
  useUnifiedTopology: true
});


mongoose.connection.on('connected', () => {
  console.log(`db.controller.js : connected to ${dbURI}`);
});

mongoose.connection.on('error', (err) => {
  console.error(`db.controller.js : connection error ${err}`);
});

mongoose.connection.on('disconnected', () => {
  console.log(`db.controller.js : disconnected from ${dbURI}`);
});

// Fermeture propre en cas d'arrêt du serveur
const shutdown = async (msg) => {
  await mongoose.connection.close();
  console.log(`Mongoose shutdown : ${msg}`);
  process.exit(0);
};

process.on('SIGINT', () => shutdown('application ends'));
process.on('SIGTERM', () => shutdown('SIGTERM received'));

module.exports = mongoose;