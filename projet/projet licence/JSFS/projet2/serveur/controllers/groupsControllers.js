const mongoose = require('mongoose');
const student = require('../model/studentModel');
const group = require('../model/groupsModel'); 

var curentGroup = 0 ;

exports.getAllStudentsNoGroups = async (req, res) => { 
  try {
    const groupedStudents = await group.distinct('studentId');

    const students = await student.find({ _id: { $nin: groupedStudents } });

    res.json(students);
  } catch (err) {
    console.error("Erreur de récupération :", err);
    res.status(500).json({ message: err.message });
  }
};


exports.getGroup = async (req, res) => {
  const { groupId } = req.params;

  if (!groupId || isNaN(groupId) || groupId < 1) {
    return res.status(400).json({ message: "ID de groupe invalide" });
  }

  curentGroup = parseInt(groupId, 10); 

  try {
    const groupStudents = await group.find({ groupNumber: curentGroup }).populate('studentId');
    console.log("Étudiants dans le groupe", curentGroup, ":", groupStudents);
    res.json(groupStudents);
  } catch (err) {
    console.error("Erreur lors de la récupération du groupe :", err);
    res.status(500).json({ message: err.message });
  }
};


exports.addGroup = async (req, res) => {
  const { studentId, groupNumber } = req.params;

  const groupNum = parseInt(groupNumber, 10);


  if (isNaN(groupNum) || groupNum < 1) {
    return res.status(400).json({ message: "Numéro de groupe invalide" });
  }

  try {
    const studentToAdd = await student.findById(studentId);

    if (!studentToAdd) {
      return res.status(404).json({ message: "Étudiant non trouvé" });
    }

    const existingGroup = await group.findOne({ studentId });

    if (existingGroup) {
      if (existingGroup.groupNumber !== groupNum) { 
        existingGroup.groupNumber = groupNum;
        await existingGroup.save();
        return res.json({ message: "Étudiant déplacé dans un nouveau groupe" });
      } else {
        return res.status(400).json({ message: "L'étudiant est déjà dans ce groupe" });
      }
    }

    const newGroup = new group({
      studentId: studentId,
      groupNumber: groupNumber
    });

    await newGroup.save();
    res.json({ message: "Étudiant ajouté au groupe avec succès" });

  } catch (err) {
    console.error("Erreur lors de l'ajout au groupe :", err);
    res.status(500).json({ message: err.message });
  }
};


exports.deleteGroup = async (req, res) => {
  const { studentId } = req.params;

  try {
    await group.deleteMany({ studentId: studentId });

    res.json({ message: "Étudiant retiré de tous les groupes avec succès" });
  } catch (err) {
    console.error("Erreur lors de la suppression :", err);
    res.status(500).json({ message: err.message });
  }
};

