var express = require('express');
var router = express.Router();
var path = require('path');

// Middleware pour désactiver le cache pour cette route spécifique
router.use((req, res, next) => {
  res.setHeader('Cache-Control', 'no-store'); // Désactive complètement le cache
  next();
});

router.get('/', function(req, res, next) {
  res.sendFile(path.join(__dirname, '../public/students.html'));
});

module.exports = router;
