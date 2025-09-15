const express = require('express');
const router = express.Router();
const studentsController = require('../controllers/studentsControllers');
const groupsController = require('../controllers/groupsControllers') ;

// task pour students
router.get('/students', studentsController.getAllStudents);
router.get('/students/getAStudent/:studentId', studentsController.getAStudent);
router.post('/students', studentsController.createStudent);
router.delete('/students/:studentId', studentsController.deleteStudent);

// task pour groups 
router.get('/groups', groupsController.getAllStudentsNoGroups);
router.get('/groups/getgroup/:groupId', groupsController.getGroup);
router.post('/groups/addgroup/:studentId/:groupNumber', groupsController.addGroup);
router.delete('/groups/delgroup/:studentId', groupsController.deleteGroup);


module.exports = router;