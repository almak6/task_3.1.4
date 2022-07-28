async function getUserTable() {
    let table = $('#users-table tbody')
    await fetch('/admin/users')
        .then(response => response.json())
        .then(users => {
            users.forEach(user => {
                table.append(`
                    <tr id="tableRow-${user.id}">
                        <td name="editId">${user.id}</td>
                        <td name="editName">${user.name}</td>
                        <td name="editLastName">${user.lastName}</td>
                        <td name="editAge">${user.age}</td>
                        <td name="editEmail">${user.email}</td>
                        <td name="editRoles"></td>
                    </tr> 
                `)
                user.roles.forEach(role => {
                    $('#users-tableContent td:last').append(`<span>${role.name.substring(5) + ' '}</span>`)
                })
                $('#users-tableContent #tableRow-' + user.id).append(`
                <td>
                    <button id="edit-button-${user.id}" name="editButton" type="button" class="btn btn-info" data-toggle="modal" data-target="#modalEdit">Edit</button>
                </td>
                <td>
                    <button id="delete-button-${user.id}" name="deleteButton" type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalDelete">Delete</button>
                </td>
                `)
            })
        }).then(function() {
            buttonEvent();
        });

    $("#buttonEditConfirm").on("click", async f => {
        f.preventDefault();
        let user = {
            id: $('#modalEdit #editId').val(),
            name: $('#modalEdit #editName').val(),
            lastName: $('#modalEdit #editLastName').val(),
            age: $('#modalEdit #editAge').val(),
            email: $('#modalEdit #editEmail').val(),
            password: $('#modalEdit #editPassword').val(),
            roles: $('#modalEdit select').val()
        };

        // $('#modalEdit #editPassword').val('');

        let response = await fetch('/admin/users/' + user.id, {
            method: 'PATCH',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(user),
        });

        $('table #tableRow-' + user.id + " td[name = 'editName']").html(user.name);
        $('table #tableRow-' + user.id + " td[name = 'editLastName']").html(user.lastName);
        $('table #tableRow-' + user.id + " td[name = 'editAge']").html(user.age);
        $('table #tableRow-' + user.id + " td[name = 'editEmail']").html(user.email);
        $('table #tableRow-' + user.id + " td[name = 'editRoles']").empty();

        user.roles.forEach(role => {
            $('#users-tableContent #tableRow-' + user.id + " td[name = 'editRoles']").append(`
                <span>${role.substring(5)}</span>
            `)
        });

        $('#modalEdit').modal('hide');
    });

    $('#buttonDeleteConfirm').on('click', async f => {
        f.preventDefault();
        let id = $('#modalDelete #deleteId').val();

        await fetch('/admin/users/' + id, {
            method: 'DELETE'
        });

        $('table #tableRow-' + id).remove();

        $('#modalDelete').modal('hide');

    });
    $('#newUser #buttonNewUserConfirm').on('click', async f => {
        f.preventDefault();
        let user = {
            name: $('#newUser #name').val(),
            lastName: $('#newUser #lastname').val(),
            age: $('#newUser #age').val(),
            email: $('#newUser #email').val(),
            password: $('#newUser #password').val(),
            roles: $('#newUser #rolesSelector').val()
        };

        let response = await fetch('admin/users', {
            method: 'POST',
            headers: {
                'content-type': 'application/json'
            },
            body: JSON.stringify(user)
        });

        let savedUser = await response.json();

        $('#users-tableContent tbody').append(`
            <tr id="tableRow-${savedUser.id}">
                <td>${savedUser.id}</td>
                <td name="editName">${savedUser.name}</td>
                <td name="editLastName">${savedUser.lastName}</td>
                <td name="editAge">${savedUser.age}</td>
                <td name="editEmail">${savedUser.email}</td>
                <td name="editRoles"></td>
            </tr>
        `);

        savedUser.roles.forEach(role => {
            $('#users-tableContent #tableRow-' + savedUser.id + ' td:last').append(`
                <span>${role.name.substring(5)}</span>
            `);
        });
        $('#users-tableContent #tableRow-' + savedUser.id).append(`
                <td>
                    <button id="edit-button-${savedUser.id}" name="editButton" type="button" class="btn btn-info" data-toggle="modal" data-target="#modalEdit">Edit</button>
                </td>
                <td>
                    <button id="delete-button-${savedUser.id}" name="deleteButton" type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalDelete">Delete</button>
                </td>
        `);

        $('#name').val('');
        $('#lastname').val('');
        $('#age').val('');
        $('#email').val('');
        $('#password').val('');
        $('#rolesSelector').prop('selected', false);

        buttonEvent();
    });

    $('#v-pills-tab #v-pills-user-tab').on('click', async f => {
        fetch('user/');
    });
}
getUserTable();

function buttonEvent() {
    $("button[name = 'editButton']").on("click", async f => {
        let button = f.target;
        let id = button.getAttribute("id").substring(12);
        fetch('/admin/users/' + id)
            .then(response => response.json())
            .then(user => {
                $('#modalEdit #editId').val(user.id);//.val(значение) - изменяет значение элемента в обьекте JQuery
                $('#modalEdit #editName').val(user.name);
                $('#modalEdit #editLastName').val(user.lastName);
                $('#modalEdit #editAge').val(user.age);
                $('#modalEdit #editEmail').val(user.email);
                $('#modalEdit #editPassword').val();//.val() - возвращает значение элемента в обьекте JQuery
                $('#modalEdit option').prop('selected', false);
                $('#modalEdit #rolesSelector').empty();
                $('#modalEdit #rolesSelector').append(`<option id="roleAdmin" value="ROLE_ADMIN" requared>ADMIN</option>`)
                $('#modalEdit #rolesSelector').append(`<option id="roleUser" value="ROLE_USER" requared>USER</option>`)

                roleAdmin = $('#roleAdmin').val();
                roleUser = $('#roleUser').val();

                for (const role of user.roles) {
                    if (role.name === roleAdmin) {
                        $('#modalEdit #roleAdmin').prop('selected', true);
                    }
                    if (role.name === roleUser) {
                        $('#modalEdit #roleUser').prop('selected', true);
                    }
                }
            });
    });
    $("button[name = 'deleteButton']").on('click', async f => {
        let button = f.target;
        let id = button.getAttribute('id').substring(14);
        console.log(id);
        fetch('/admin/users/' + id)
            .then(response => response.json())
            .then(user => {
                $('#modalDelete #deleteId').val(user.id);
                $('#modalDelete #deleteName').val(user.name);
                $('#modalDelete #deleteLastName').val(user.lastName);
                $('#modalDelete #deleteAge').val(user.age);
                $('#modalDelete #deleteEmail').val(user.email);
                $('#modalDelete #deletePassword').val();
                $('#modalDelete option').prop('selected', false);
                $('#modalDelete #deleteRolesSelector').empty();
                $('#modalDelete #deleteRolesSelector').append(`<option id="roleAdmin" value="ROLE_ADMIN" requared>ADMIN</option>`)
                $('#modalDelete #deleteRolesSelector').append(`<option id="roleUser" value="ROLE_USER" requared>USER</option>`)

                roleAdmin = $('#roleAdmin').val();
                roleUser = $('#roleUser').val();

                for (const role of user.roles) {
                    if (role.name === roleAdmin) {
                        $('#modalDelete #roleAdmin').prop('selected', true);
                    }
                    if (role.name === roleUser) {
                        $('#modalDelete #roleUser').prop('selected', true);
                    }
                }
            });
    });
}