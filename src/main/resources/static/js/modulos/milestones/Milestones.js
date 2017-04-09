var Milestones = {
	settings: {
		btnAdicionarMilestone: $('#btn-adicionar-milestone'),
		btnSalvarMilestone: $('#btn-salvar-milestone'),
		modalAdicionarMilestone: $('#modal-adicionar-milestone'),
		selectResponsaveis: $('#select-responsaveis')
	},

	init: function() {
		console.log("Milestones.init()");
		Milestones.bindUI();
	},
	
	bindUI: function() {
		console.log("Milestones.bindUI()");
		Milestones.settings.btnAdicionarMilestone.on('click', Milestones.novoMilestone);
		$(document).on('click', '#btn-salvar-milestone', Milestones.salvarMilestone);
		Milestones.settings.selectResponsaveis.multiselect();
		Milestones.abrirModal();
	},

	novoMilestone: function() {
		console.log("Milestones.novoMilestone()");
		Milestones.settings.modalAdicionarMilestone.modal();
	},
	
	salvarMilestone: function() {
		console.log("Milestones.salvarMilestone()");
	},
	
	abrirModal: function() {
		console.log("Milestones.abrirModal()");
		if(location.search != '')
			Milestones.settings.modalAdicionarMilestone.modal();
	}

};