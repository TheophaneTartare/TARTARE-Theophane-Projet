const displayMessage = msg => document.getElementById('content').textContent = msg;
const listDiv = document.getElementById('list');

let uptatestudentId ;

const deleteStudent = async (studentId) => {
  try {
    const response = await fetch(`/task/students/${studentId}`, {
      method: 'DELETE'
    });

    if (!response.ok) {
      throw new Error('Erreur lors de la suppression');
    }

    console.log('Étudiant supprimé avec succès');
    fetchStudents();
  } catch (error) {
    console.error('Erreur:', error);
  }
};


const handleDelete = (id) => {
  deleteStudent(id);
};
const fetchStudents = async () => {
  try {
    const response = await fetch('/task/students', {
      headers: { 'Cache-Control': 'no-cache' }
    });
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
  students.forEach(student => {
    const studentDiv = document.createElement('div');
    studentDiv.innerHTML = `
      <span>${student.firstName.join(', ')} ${student.lastName} (${student.studentNumber})</span>
      <button class="delete-btn" data-id="${student._id}">Supprimer</button>
      <button class="uptdate-btn" data-id="${student._id}">modifier</button>
    `;
    listDiv.appendChild(studentDiv);
  });

  document.querySelectorAll('.delete-btn').forEach(button => {
    button.addEventListener('click', (event) => {
      const studentId = event.target.dataset.id;
      deleteStudent(studentId);
    });
  });

  document.querySelectorAll('.uptdate-btn').forEach(button => {
    button.addEventListener('click', (event) => {
      const studentId = event.target.dataset.id;
      updateStudent(studentId);
    });
  });
  
};


const updateStudent = async (id) => {
  uptatestudentId = id; // On sauvegarde l'ID de l'étudiant à mettre à jour

  // Récupérer les informations de l'étudiant
  try {
    const response = await fetch(`/task/students/getAStudent/${id}`);
    const student = await response.json();
    
    // Remplir les champs du formulaire avec les informations de l'étudiant
    document.getElementById('firstName').value = student.firstName.join(', ');
    document.getElementById('name').value = student.lastName;
    document.getElementById('studentNumber').value = student.studentNumber;
    
    displayMessage('Mise à jour d\'étudiant en cours...');
  } catch (error) {
    console.error('Erreur lors de la récupération de l\'étudiant:', error);
    displayMessage('Erreur lors de la récupération des informations de l\'étudiant.');
  }
};




const setup = () => {
  displayMessage('prêt');
  fetchStudents(); 
};


setup();

const createStudent = async () => {
  const firstName = document.getElementById('firstName').value.split(',');
  const lastName = document.getElementById('name').value;
  const studentNumber = document.getElementById('studentNumber').value;

  try {
    if (uptatestudentId != null ) {
      deleteStudent(uptatestudentId) ;
    }
    const response = await fetch('/task/students', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ firstName, lastName, studentNumber }),
    });

    const result = await response.json();
    if (response.ok) {
      displayMessage(`${lastName} ajouter`);
      fetchStudents();
    } else {
      displayMessage(result.message || 'Erreur lors de la création.');
    }
    uptatestudentId = null ;
  } catch (error) {
    console.error('Error creating student:', error);
    displayMessage('Error creating student.');
  }
};

const emptyRow = () => {
  const firstName = document.getElementById('firstName');
  const lastName = document.getElementById('name');
  const studentNumber = document.getElementById('studentNumber');

  firstName.value = '' ; 
  lastName.value = '' ;
  studentNumber.value = ''; 
  displayMessage('vider')

}

document.getElementById('empty').addEventListener('click', emptyRow);

document.getElementById('create').addEventListener('click', createStudent);
