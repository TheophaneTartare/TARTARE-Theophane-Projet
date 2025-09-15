const mongoose = require('mongoose');

const groupSchema = new mongoose.Schema({
    studentId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true,
        ref: 'Student' 
    },
    groupNumber: {
        type: Number,
        required: true,
        min: 1,
        max: 6
    }
});

const Group = mongoose.model('Group', groupSchema);

module.exports = Group;