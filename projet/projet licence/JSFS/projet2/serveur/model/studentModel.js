const mongoose = require('mongoose');

const studentModel = new mongoose.Schema({
  studentNumber: {
    type: String,
    required: true,
    unique: true,
  },
  lastName: {
    type: String,
    required: true,
    uppercase: true,
    trim: true,
  },
  firstName: {
    type: [String],
    required: true,
    validate: {
      validator: function (names) {
        return names.every((name) => name.charAt(0) === name.charAt(0).toUpperCase() && name.slice(1) === name.slice(1).toLowerCase());
      },
      message: 'Les prénoms doivent être capitalisés.',
    },
  },
});

studentModel.virtual('fullName').get(function () {
  return this.firstName.join(', ');
});

const Student = mongoose.model('Student', studentModel, 'etudiants');

module.exports = Student;