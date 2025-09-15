const mongoose = require('mongoose');
const student = require('../model/studentModel');

exports.getAllStudents = async (req, res) => { 
  try {
    const students = await student.find();  
    res.json(students);
  } catch (err) {
    console.error("Erreur de récupération :", err);
    res.status(500).json({ message: err.message });
  }
};

exports.getAStudent = async (req, res) => { 
  try {
    const { studentId } = req.params; 
    const aStudent = await student.findById(studentId); 
    if (!aStudent) {
      return res.status(404).json({ message: 'Étudiant non trouvé' });
    }
    res.json(aStudent);  
  } catch (err) {
    console.error("Erreur de récupération :", err);
    res.status(500).json({ message: err.message }); 
  }
};

exports.createStudent = async (req, res) => {
  try {
    const newStudent = new student(req.body);
    const savedStudent = await newStudent.save();
    res.status(201).json(savedStudent);
  } catch (err) {
    console.error("Error creating student:", err);
    res.status(400).json({ message: err.message });
  }
};

exports.deleteStudent = async (req, res) => {
  try {
    const { studentId } = req.params; 
    console.log("ID reçu sur le serveur :", studentId);

    if (!mongoose.Types.ObjectId.isValid(studentId)) {
      return res.status(400).json({ message: "ID invalide" });
    }

    const deletedStudent = await student.findByIdAndDelete(studentId);

    if (!deletedStudent) {
      return res.status(404).json({ message: "Étudiant introuvable" });
    }

    res.json({ message: "Étudiant supprimé avec succès", deletedStudent });
  } catch (err) {
    console.error("Erreur lors de la suppression :", err);
    res.status(500).json({ message: err.message });
  }
};

