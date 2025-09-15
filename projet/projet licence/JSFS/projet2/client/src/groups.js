const displayMessage = msg => document.getElementById('content').textContent = msg;
const listDiv = document.getElementById('list');
const g0 = document.getElementById('g0');
const g1 = document.getElementById('g1');
const g2 = document.getElementById('g2');
const g3 = document.getElementById('g3');
const g4 = document.getElementById('g4');
const g5 = document.getElementById('g5');
const g6 = document.getElementById('g6');



var curentGroup = 0 ;

const deleteStudent = async (studentId) => {
  try {
    const response = await fetch(`/task/groups/delgroup/${studentId}`, {
      method: 'DELETE'
    });

    if (!response.ok) {
      throw new Error('Erreur lors de la suppression');
    }

    console.log('Étudiant supprimé avec succès');
    fetchStudents(curentGroup);
  } catch (error) {
    console.error('Erreur:', error);
  }
};


const handleDelete = (id) => {
  deleteStudent(id);
};

const fetchStudents = async (group) => {
  try {
    let response; 
    console.log("Fetching students for group:", group);

    if (group === 0 ) {
      response = await fetch('/task/groups/', {
        headers: { 'Cache-Control': 'no-cache' }
      });
    } else {
      response = await fetch(`/task/groups/getgroup/${group}`, {
        headers: { 'Cache-Control': 'no-cache' }
      });
    }

    if (!response.ok) {
      throw new Error('Error fetching students');
    }

    const students = await response.json();
    console.log("Fetched students: ", students);  
    displayStudents(students);
  } catch (error) {
    console.error('Error fetching students:', error);
    displayMessage('Error fetching students.');
  }
};


const displayStudents = students => {
  listDiv.innerHTML = '';

  if (!Array.isArray(students) || students.length === 0) {
    displayMessage('Aucun étudiant trouvé.');
    return;
  }

  students.forEach(student => {
    const studentDiv = document.createElement('div');

    const studentData = student.studentId || student;
    const fullName = Array.isArray(studentData.firstName)
      ? studentData.firstName.join(', ')
      : studentData.firstName;

    if (curentGroup === 0) {
      studentDiv.innerHTML = `
        <span>${fullName} ${studentData.lastName} (${studentData.studentNumber})</span>
        ${[1, 2, 3, 4, 5, 6].map(num =>
          `<button class="addg" data-id="${studentData._id}" data-group="${num}">${num}</button>`
        ).join('')}
      `;
    } else {
      studentDiv.innerHTML = `
        <span>${fullName} ${studentData.lastName} (${studentData.studentNumber})</span>
        <button class="delete-btn" data-id="${studentData._id}">Supprimer</button>
      `;
    }

    listDiv.appendChild(studentDiv);
  });

  document.querySelectorAll('.delete-btn').forEach(button => {
    button.addEventListener('click', (event) => {
      const studentId = event.target.dataset.id;
      deleteStudent(studentId);
    });
  });

  document.querySelectorAll('.addg').forEach(button => {
    button.addEventListener('click', event => {
      const studentId = event.target.dataset.id;
      const groupNumber = event.target.dataset.group;
      changeGroup(studentId, groupNumber);
    });
  });
};


const changeGroup = async (studentId, groupNumber) => {
  try {
    const response = await fetch(`/task/groups/addgroup/${studentId}/${groupNumber}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' }
    });

    if (!response.ok) {
      throw new Error('Erreur lors du changement de groupe');
    }

    console.log(`Étudiant ${studentId} ajouté au groupe ${groupNumber}`);
    fetchStudents(curentGroup); 
  } catch (error) {
    console.error('Erreur:', error);
  }
};

const setup = () => {
  displayMessage('prêt');
  fetchStudents(curentGroup); 
};


const updateCurrentGroup = (groupId) => {
  curentGroup = groupId;
  console.log("Current group is now:", curentGroup);  
  fetchStudents(curentGroup);  
};

g0.addEventListener('click', () => updateCurrentGroup(0));
g1.addEventListener('click', () => updateCurrentGroup(1));
g2.addEventListener('click', () => updateCurrentGroup(2));
g3.addEventListener('click', () => updateCurrentGroup(3));
g4.addEventListener('click', () => updateCurrentGroup(4));
g5.addEventListener('click', () => updateCurrentGroup(5));
g6.addEventListener('click', () => updateCurrentGroup(6));

setup();