var Usuarios = {

    usuarios: [],
    
    settings: {
        btnNovoUsuario : $('#btn-novo-usuario'),
        btnSalvarUsuario: $('#salvar-usuario'),
        novoUsuarioModal: $('#novo-usuario-modal')
    },
 
    init: function () {
        Usuarios.bindUI();
    },
 
    bindUI: function () {
        Usuarios.settings.btnNovoUsuario.on('click', Usuarios.novoUsuario);
        Usuarios.settings.btnSalvarUsuario.on('click', Usuarios.salvarUsuario);
    },
    
    novoUsuario: function (event) {
        Usuarios.settings.novoUsuarioModal.modal();
        console.log("Nova Usuario");
    },
    
    salvarUsuario: function (event) {
        console.log("Salvar Usuario");
        Usuarios.settings.novoUsuarioModal.modal('hide')
    }
};