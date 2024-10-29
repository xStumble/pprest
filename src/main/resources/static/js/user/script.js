window.addEventListener("load", async function () {
    const response = await fetch('http://localhost:8080/user/getcurrentuser')
    if (response.ok) {
        const user = await response.json();
        const userinfo = document.getElementById('user-info');
        const usernameEmail = document.createElement('b');
        usernameEmail.append(user['username'] + ' (' + user['email'] + ")");
        userinfo.append(usernameEmail);
        userinfo.append(' with roles: ' + rolesString(user['roles']));

        const tbody = document.querySelector('.tbody');

        const id = user['id'];
        const tdvalues = [
            user['username'], user['firstName'] , user['lastName'],
            user['age'], user['email']
        ];

        let tr = document.createElement('tr')

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

        tbody.append(tr);

    } else {
        alert('Ошибка HTTP: ' + response.status);
    }
});

function rolesString(rolesJSON) {
    let roles = '';
    for (let i = 0; i < rolesJSON.length; i++) {
        const rolesJSONCurrent = rolesJSON[i];
        roles = roles.concat(rolesJSONCurrent['authority'].split('_')[1], ' ');
    }
    return roles;
}