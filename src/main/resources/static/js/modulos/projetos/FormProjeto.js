var FormProjeto = {

	participacoes: [],
	participacaoAtual: null,

	settings: {
		btnAdicionarParticipante: $('#btn-adicionar-participante'),
		selectTipoParticipacao: $('#tipoParticipacao'),
		inputUsuario: $('#input-usuario'),
		secaoParticipacoes: $('#secao-participacoes'),
		conteudoParticipacoes: $('#conteudo-participacoes'),
		itemParticipante: $('.item-participante'),
		autoCompleteServiceUrl: '/usuario/listarpornome',
		autoCompleteParamName: 'nome'
	},

	init: function(event) {
		FormProjeto.carregarParticipantesNoArray();
		FormProjeto.iniciarAutoComplete();
		FormProjeto.desabilitarAdicionarParticipante();
		FormProjeto.bindUI();
	},
	
	bindUI: function(event) {
		$(document).on('click', '#btn-adicionar-participante', FormProjeto.adicionarParticipante);
		$(document).on('click', '.btn-remover-participante', FormProjeto.removerParticipante);
	},

	carregarParticipantesNoArray : function(event) {
		FormProjeto.settings.itemParticipante.each(function( index ) {
			id = $(this).data("id");
			idUsuario = $(this).data("id-usuario");
			nome = $(this).data("nome");
			tipo = $(this).data("tipo");
			if(FormProjeto.verificarSeParticipacaoExiste(id) == -1)
				FormProjeto.participacoes.push(new Participacao(id, idUsuario, nome, tipo));
	    });
		FormProjeto.mostrarEsconderSecaoParticipacoes();	
	},
	
	verificarSeParticipacaoExiste: function(id) {
		for (i = 0; i < FormProjeto.participacoes.length; i++) {
			if(FormProjeto.participacoes[i].idUsuario == id) return i;
		}
		return -1;
	},
	
	iniciarAutoComplete: function(event) {
		FormProjeto.settings.inputUsuario.autocomplete({
			serviceUrl: FormProjeto.settings.autoCompleteServiceUrl,
			paramName: FormProjeto.settings.autoCompleteParamName,
			onSelect: function(suggestion) {
				FormProjeto.participacaoAtual = new Participacao('', suggestion.data, suggestion.value,'');
				FormProjeto.habilitarAdicionarParticipante();
			}
		});
	},
	
	mostrarEsconderSecaoParticipacoes: function() {
		if(FormProjeto.participacoes.length == 0) {
			FormProjeto.settings.secaoParticipacoes.hide();
		}
	},

	habilitarAdicionarParticipante: function(event) {
		FormProjeto.settings.btnAdicionarParticipante.prop("disabled", false);
		FormProjeto.settings.selectTipoParticipacao.prop("disabled", false);
	},

	desabilitarAdicionarParticipante: function(event) {
		FormProjeto.settings.btnAdicionarParticipante.prop("disabled", true);
		FormProjeto.settings.selectTipoParticipacao.prop("disabled", true);
		FormProjeto.settings.inputUsuario.val("");
	},

	adicionarParticipante: function(event) {
		if(FormProjeto.verificarSeParticipacaoExiste(FormProjeto.participacaoAtual.idUsuario) != -1){
			FormProjeto.desabilitarAdicionarParticipante();
			alert("Esse usuário já foi adicionado como participante do projeto.");
			return false;
		}
		FormProjeto.participacaoAtual.tipoParticipacao = FormProjeto.settings.selectTipoParticipacao.val();
		FormProjeto.participacoes.push(FormProjeto.participacaoAtual);
		FormProjeto.recarregarTabelaParticipantes();
		FormProjeto.settings.secaoParticipacoes.show();
		FormProjeto.desabilitarAdicionarParticipante();
	}, 
	
	removerParticipante: function(event) {
		target = $( event.target );
		if(FormProjeto.verificarSeParticipacaoExiste(target.data("id")) != -1){
			FormProjeto.participacoes.splice(i, 1);
			target.parent().parent().remove();
			FormProjeto.mostrarEsconderSecaoParticipacoes();
			FormProjeto.recarregarTabelaParticipantes();
		}
	},
	
	recarregarTabelaParticipantes: function(event) {
		FormProjeto.settings.conteudoParticipacoes.empty();
		for (i = 0; i < FormProjeto.participacoes.length; i++) {
			participante = FormProjeto.participacoes[i];
			FormProjeto.adicionarParticipanteNaTabela(participante, i);
		}
	},
	
	adicionarParticipanteNaTabela: function(participante, index) {
		htmlCode = 
			'<tr class="item-participante">' +
			'<td>' + participante.nomeUsuario + '</td>' +
			'<td>' + participante.tipoParticipacao + '</td>' +
			'<td><a class="btn btn-default btn-remover-participante" data-id="'+participante.idUsuario+'">Remover</a></td>' +
			'<input type="hidden" id="equipe[' + index + '].id" name="equipe[' + index + '].id" value="' + participante.id + '" />' +
			'<input type="hidden" id="equipe[' + index + '].usuario.id" name="equipe[' + index + '].usuario.id" value="' + participante.idUsuario + '" />' +
			'<input type="hidden" id="equipe[' + index + '].usuario.nome" name="equipe[' + index + '].usuario.nome" value="' + participante.nomeUsuario + '" />' +
			'<input type="hidden" id="equipe[' + index + '].tipoParticipacao" name="equipe[' + index + '].tipoParticipacao" value="' + participante.tipoParticipacao + '" />' +
			'</tr>';
		
		FormProjeto.settings.conteudoParticipacoes.append(htmlCode);
	}

};