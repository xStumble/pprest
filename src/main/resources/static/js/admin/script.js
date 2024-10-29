window.addEventListener("load", async function () {
    const response = await fetch('http://localhost:8080/admin/getallusers');
    if (response.ok) {
        const json = await response.json();
        for (let i = 0; i < json.length; i++) {
            tableAppend(json[i]);
        }

        const roles = await getRoles();
        const select = document.getElementById('role');
        select.size = roles.length;
        for (const obj of roles) {
            const option = document.createElement('option');
            option.value = obj['id'];
            option.append(obj['authority'].split('_')[1]);
            select.append(option);
        }

        await loadUser();

    } else {
        alert('Ошибка HTTP: ' + response.status);
    }

});


const forms = [
    document.getElementById('addForm'),
    document.getElementById('editModalForm'),
    document.getElementById('deleteModalForm')
];
for (const forml of forms) {
    forml.addEventListener("submit", async function (e) {
        e.preventDefault();

        const form = e.currentTarget;

        const alerts = form.querySelectorAll('.alert');
        for (const alert of alerts) {
            if (!alert.classList.contains('d-none')) {
                alert.classList.add('d-none');
                alert.innerText = '';
            }
        }

        let prefix = '';
        switch (form.id) {
            case 'editModalForm':
                prefix = 'edit_';
                break;
            case 'deleteModalForm':
                prefix = 'delete_';
                break;
        }

        if (form.id === 'editModalForm') {
            prefix = 'edit_';
        } else if (form.id === 'deleteModalForm') {
            prefix = 'delete_';
        }

        let user = {
            username: document.getElementById(prefix + 'username').value,
            firstName: document.getElementById(prefix + 'firstname').value,
            lastName: document.getElementById(prefix + 'lastname').value,
            age: parseInt(document.getElementById(prefix + 'age').value),
            email: document.getElementById(prefix + 'email').value,
            roles: []
        };

        if (form.id !== 'deleteModalForm') {
            user['password'] = document.getElementById(prefix + 'password').value;
        }

        if (form.id === 'editModalForm' || form.id === 'deleteModalForm') {
            user['id'] = parseInt(document.getElementById(prefix + 'id').value);
        }

        const selectedRoles = document.getElementById(prefix + 'role').selectedOptions;
        const dbRoles = await getRoles();
        for (const dbRole of dbRoles) {
            for (const selectedRole of selectedRoles) {
                if (dbRole['id'] === parseInt(selectedRole.value)) {
                    user['roles'].push(dbRole);
                    break;
                }
            }
        }

        let response = await fetch(form.action, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user)
        });
        let result = await response.json();

        if (response.ok) {
            if (form.id === 'editModalForm') {
                const modal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
                modal.hide();
                tableUpdate(user);
            } else if (form.id === 'deleteModalForm') {
                const modal = bootstrap.Modal.getInstance(document.getElementById('deleteModal'));
                modal.hide();
                tableDelete(user['id']);
            } else {
                const inputs = form.getElementsByTagName('input');
                for (const input of inputs) {
                    input.value = '';
                }

                const options = form.getElementsByTagName('option');
                for (const option of options) {
                    option.selected = false;
                }

                document.getElementById('home').classList.toggle('show');
                document.getElementById('home').classList.toggle('active');
                document.getElementById('home-tab').classList.toggle('active');

                document.getElementById('profile').classList.toggle('show');
                document.getElementById('profile').classList.toggle('active');
                document.getElementById('profile-tab').classList.toggle('active');

                user['id'] = parseInt(result);
                tableAppend(user);
            }
        } else {
            for (const field in result) {
                const errorField = form.querySelector('[data-field=\"' + field + '\"]').querySelector('.alert');
                for (const str of result[field]) {
                    errorField.append(str);
                    errorField.insertAdjacentHTML('beforeend', '<br>');
                    errorField.classList.remove('d-none');
                }
            }
        }


    });
}


function tableAppend(user) {
    const tbody = document.querySelector('.tbody');

    const id = user['id'];
    const tdvalues = [
        user['username'], user['firstName'] , user['lastName'],
        user['age'], user['email']
    ];

    let tr = document.createElement('tr');
    let th = document.createElement('th');
    th.setAttribute('scope', 'row');
    th.append(id);
    tr.append(th);

    for (let i = 0; i < tdvalues.length; i++) {
        let td = document.createElement('td');
        td.append(tdvalues[i]);
        tr.append(td);
    }

    let tdRoles = document.createElement('td');
    tdRoles.append(rolesString(user['roles']));
    tr.append(tdRoles);

    let tdEdit = document.createElement('td');
    let buttonEdit = document.createElement('button');
    buttonEdit.setAttribute('type', 'button');
    buttonEdit.classList.add('btn', 'btn-info', 'text-white', 'editbutton');
    buttonEdit.setAttribute('data-bs-toggle', 'modal');
    buttonEdit.setAttribute('data-bs-target', '#editModal');
    buttonEdit.append('Edit');
    tdEdit.append(buttonEdit);
    tr.append(tdEdit);

    let tdDelete = document.createElement('td');
    let buttonDelete = document.createElement('button');
    buttonDelete.setAttribute('type', 'button');
    buttonDelete.classList.add('btn', 'btn-danger', 'deletebutton');
    buttonDelete.setAttribute('data-bs-toggle', 'modal');
    buttonDelete.setAttribute('data-bs-target', '#deleteModal');
    buttonDelete.append('Delete');
    tdDelete.append(buttonDelete);
    tr.append(tdDelete);

    tbody.append(tr);
}

const modals = [
    document.getElementById('editModal'),
    document.getElementById('deleteModal')
];
modals.forEach(function (el) {
    el.addEventListener('show.bs.modal', function (e) {
        const button = e.relatedTarget;
        const modal = e.currentTarget;

        const tr = button.closest('tr');
        const id = tr.children[0].innerText;

        fillModal(modal, id);
    });
});

async function fillModal(modal, id) {
    const response = await fetch('http://localhost:8080/admin/getuser?id=' + id);
    if (response.ok) {
        const json = await response.json();

        const neededKeys = ['id', 'username', 'firstName', 'lastName', 'age', 'email'];
        const prefix = modal.id === 'editModal' ? 'edit_' : 'delete_';

        for (const key of neededKeys) {
            const input = document.getElementById(prefix + key.toLocaleLowerCase());
            input.value = json[key];
        }

        const roles = await getRoles();
        const select = modal.getElementsByTagName('select')[0];
        select.size = roles.length;
        select.innerHTML = '';
        for (const obj of roles) {
            const option = document.createElement('option');
            option.value = obj['id'];
            option.append(obj['authority'].split('_')[1]);

            const userRoles = json['roles'];
            if (userRoles.some(item => item['authority'] === obj['authority'])) {
                option.selected = true;
            }
            select.append(option);
        }

    } else {
        alert('Ошибка HTTP: ' + response.status);
    }
}

function tableUpdate(updatedUser) {
    const tbody = document.querySelector('.table').getElementsByTagName('tbody')[0];
    for (const tr of tbody.children) {
        if (parseInt(tr.children[0].innerText) === updatedUser['id']/* id user['id']*/) {
            const tds = tr.getElementsByTagName('td');
            tds[0].innerText = updatedUser['username'];
            tds[1].innerText = updatedUser['firstName'];
            tds[2].innerText = updatedUser['lastName'];
            tds[3].innerText = updatedUser['age'];
            tds[4].innerText = updatedUser['email'];
            tds[5].innerText = rolesString(updatedUser['roles']);
            break;
        }
    }
}

function tableDelete(id) {
    const tbody = document.querySelector('.table').getElementsByTagName('tbody')[0];
    for (const tr of tbody.children) {
        if (parseInt(tr.children[0].innerText) === id) {
            tr.remove();
        }
    }
}

const editModal = document.getElementById('editModal');
editModal.addEventListener('hidden.bs.modal', function () {
   const alerts = editModal.querySelectorAll('.alert');
   for (const alert of alerts) {
       if (!alert.classList.contains('d-none')) {
           alert.classList.add('d-none');
           alert.innerText = '';
       }
   }
});


async function getRoles() {
    const response = await fetch('http://localhost:8080/admin/roles');
    if (response.ok) {
        return await response.json();
    } else {
        alert('Ошибка HTTP: ' + response.status);
    }
}

function rolesString(rolesJSON) {
    let roles = '';
    for (let i = 0; i < rolesJSON.length; i++) {
        const rolesJSONCurrent = rolesJSON[i];
        roles = roles.concat(rolesJSONCurrent['authority'].split('_')[1], ' ');
    }
    return roles;
}

async function loadUser() {
    const response = await fetch('http://localhost:8080/user/getcurrentuser')
    if (response.ok) {
        const user = await response.json();
        const userinfo = document.getElementById('user-info');
        const usernameEmail = document.createElement('b');
        usernameEmail.append(user['username'] + ' (' + user['email'] + ")");
        userinfo.append(usernameEmail);
        userinfo.append(' with roles: ' + rolesString(user['roles']));
    } else {
        alert('Ошибка HTTP: ' + response.status);
    }
}