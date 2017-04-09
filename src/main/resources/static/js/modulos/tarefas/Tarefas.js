var Tarefas = {
	
	data: null,	
		
	settings: {
		btnAcaoExcluir: $('.btn-acao-excluir'),
		btnAcaoNovaTarefa: $('.btn-nova-tarefa'),
		modalNovaTarefa: $('#modal-nova-tarefa'),
		formAdicionarTarefa: $('#form-adicionar-tarefa'),
		blocoErro: $('#bloco-erro'),
		listaMilestones: $('#lista-milestones'),
		itensMilestone: $('.item-milestone'),
		tabelaTarefas: $('#tabela-tarefas tbody'),
		idProjeto: null,
		tarefasUrl: 'tarefa/obtermilestones'
	},
	
	init: function(idProjeto) {
		Tarefas.settings.idProjeto = idProjeto;
		Tarefas.bindUI();
		Tarefas.carregarTarefas();
	},
	
	bindUI: function() {
		Tarefas.settings.blocoErro.hide();
		$(document).on('submit', '#form-adicionar-tarefa', Tarefas.salvarTarefa);
		$(document).on('mouseenter', '#tabela-tarefas tbody tr', Tarefas.mostrarBotao);
		$(document).on('mouseleave', '#tabela-tarefas tbody tr', Tarefas.esconderBotao);
		$(document).on('click', 'input:checkbox', Tarefas.alterarStatus);
		$(document).on('click', '.item-milestone', Tarefas.trocarMilestone);
		$(document).on('click', '.btn-acao-excluir', Tarefas.excluir);
		$(document).on('click', '.btn-nova-tarefa', Tarefas.abrirModal);	
		
	},
	
	carregarTarefas: function() {
		$.ajax({
			method: "GET",
			url: Tarefas.settings.tarefasUrl
		})
		.done(function(data, textStatus, jqXHR){
			Tarefas.data = data;
			Tarefas.montarMenuMilestone();
		})
		.fail(function(jqXHR, textStatus, errorThrown){
			alert("Não foi possível carregar as tarefas.");
		})	
	},
	
	montarMenuMilestone: function() {
		Tarefas.settings.listaMilestones.empty();
		for (i = 0; i < Tarefas.data.length; i++) {
			milestoneObject = Tarefas.data[i];
			htmlMilestone =  '<li data-id="' + milestoneObject.id + '" data-position="' + i + '" class="list-group-item">' 
								+'<a class="item-milestone">' + milestoneObject.titulo + '</a>'
								+'<a class="btn btn-acao btn-nova-tarefa">+</a>'
							  +'</li>';
			Tarefas.settings.listaMilestones.append(htmlMilestone);
			
		}	
		Tarefas.selecionarPrimeiroMilestone();
	},
	
	selecionarPrimeiroMilestone: function() {
		itemMilestone = $('.item-milestone').parent().first();
		itemMilestone.addClass('active');
		Tarefas.carregarTarefasMilestone(itemMilestone);
	},
	
	trocarMilestone: function() {
		$('.item-milestone').parent().removeClass('active');
		itemMilestone = $(this).parent();
		itemMilestone.addClass('active');
		Tarefas.carregarTarefasMilestone(itemMilestone);
	},
	
	carregarTarefasMilestone: function(milestone) {
		Tarefas.settings.tabelaTarefas.empty();
		id = milestone.data('id');
		position = milestone.data('position');
		tarefas = Tarefas.data[position].tarefas;
		for (i = 0; i < tarefas.length; i++) {
			tarefaObject = tarefas[i];
			if(tarefaObject.concluida) {
				htmlTarefa =  '<tr class="tarefa-realizada" data-id="'+tarefaObject.id+'">' +
							  '<td><input type="checkbox" checked/></td>';
			} else {
				htmlTarefa =  '<tr data-id="'+tarefaObject.id+'">' +
				  			  '<td><input type="checkbox" /></td>';
			}
			
			var responsaveis = "";
			$.each(tarefaObject.responsaveis, function(k,v) {
		 		responsaveis += v.nome +", ";
		    });
			
			htmlTarefa +=	   '<td>'+tarefaObject.titulo+'</td>' +
							   '<td>'+responsaveis+'</td>' +
							   '<td>'+ (tarefaObject.dataLimite || "") +'</td>' +
							   '<td>'+ (tarefaObject.dataConclusao || "") +'</td>' +
							   '<td>' +
								  '<a class="btn btn-acao btn-acao-excluir" title="Excluir">' +
									 '<span class="glyphicon glyphicon-remove"></span>' +
								  '</a>' +
							   '</td>' +
						    '</tr>';
			Tarefas.settings.tabelaTarefas.append(htmlTarefa)
		}
		
	},
	
	excluir: function(event) {
		tarefa = $(this).parent().parent();
		if (confirm('Deseja excluir a tarefa?')) {
			Tarefas.excluirAjax(tarefa);
	    }
	},
	
	excluirAjax: function(tarefa) {
		id = tarefa.data('id');
		$.ajax({
			method: "GET",
			url: 'tarefa/' + id + '/excluir',
		})
		.done(function(data, textStatus, jqXHR){
			tarefa.remove();
			console.log(data);
		})
		.fail(function(jqXHR, textStatus, errorThrown){
			if(jqXHR.status == 403)
				alert("Você não tem permissão para excluir a tarefa.")
			else
				alert("Não foi possível excluir a tarefa.");
		})	
	},
	
	abrirModal: function(event) {
		Tarefas.settings.modalNovaTarefa.modal();
	},
	
	salvarTarefa: function(event) {
		event.preventDefault();
		$.ajax({
			method: "POST",
			url: 'tarefa/cadastrar',
			data: Tarefas.settings.formAdicionarTarefa.serialize()
		})
		.done(function(data, textStatus, jqXHR){
			console.log(data);
			Tarefas.settings.blocoErro.hide();
			Tarefas.settings.formAdicionarTarefa.trigger("reset");
			Tarefas.carregarTarefas();
			Tarefas.settings.modalNovaTarefa.modal('hide');
		})
		.fail(function(jqXHR, textStatus, errorThrown){
			Tarefas.settings.blocoErro.show();
			Tarefas.settings.blocoErro.find('ul').empty();
			var erros = jqXHR.responseJSON.fieldErrors;
			for (i = 0; i < erros.length; i++) {
				Tarefas.settings.blocoErro.find('ul').append('<li>'+erros[i].message+'</li>');
			}
		})	
	},
	
	alterarStatus: function(event){
		tarefa = $(this).parent().parent();
		Tarefas.alterarStatusAjax(tarefa);
	},
	
	alterarStatusAjax: function(tarefa) {
		id = tarefa.data('id');
		$.ajax({
			method: "GET",
			url: 'tarefa/' + id + '/alterarstatus',
		})
		.done(function(data, textStatus, jqXHR){
			var checkbox = tarefa.find('input:checkbox');
			if(data.concluida) {
				tarefa.addClass("tarefa-realizada");
				checkbox.prop("checked", true);
			}else{
				tarefa.removeClass("tarefa-realizada");
				checkbox.prop("disabled", false);
				checkbox.prop("checked", false);
			}
		})
		.fail(function(jqXHR, textStatus, errorThrown){
			if(jqXHR.status == 403)
				alert("Você não tem permissão para alterar a tarefa.")
			else
				alert("Não foi possível alterar o status da tarefa.");
		})	
	},
	
	mostrarBotao: function(event) {
		$(this).find('.btn-acao-excluir').css('visibility', 'visible');
		$(this).css('background-color', '#f8f8f8');
	},
	
	esconderBotao: function(event) {
		$(this).find('.btn-acao-excluir').css('visibility', 'hidden');
		$(this).css('background-color', '#ffffff');
	}		
		
};