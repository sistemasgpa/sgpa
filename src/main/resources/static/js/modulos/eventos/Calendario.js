var Calendario = {

	settings: {
		calendario: $('#calendario'),
		modalEvento: $('#modal-evento'),
		urlEventos: 'evento/buscarTodos',
		urlEditarEvento: 'evento/buscarTodos',
		blocoErro: $('#bloco-erro'),
		formAdicionarEvento: $('#form-adicionar-evento')
	},

	init: function(event) {
		Calendario.bindUI();
		Calendario.configurarCalendario();
		$(".form_datetime").datetimepicker({format: 'dd/mm/yyyy hh:ii'});
		Calendario.settings.calendario.fullCalendar('gotoDate', '2016-05-03 13:30:00')
	},
	
	configurarCalendario: function() {
		Calendario.settings.calendario.fullCalendar({
			header: {
				left: 'prev,next',
				center: 'title',
				right: 'today'
			},
			locale: 'pt-br',
			buttonIcons: false,
			editable: false,
			events: Calendario.settings.urlEventos,
			dayClick: Calendario.clicouNoDia,
			eventClick: Calendario.clicouNoEvento
        })
	},
	
	bindUI: function(event) {
		Calendario.settings.blocoErro.hide();
		$(document).on('submit', '#form-adicionar-evento', Calendario.salvarEvento);
		Calendario.settings.modalEvento.on('hidden.bs.modal', Calendario.limparModalEvento);
	},
	
	limparModalEvento: function() {
		 $("#titulo").val('');
	     $("#dataInicio").val('');
	     $("#dataFim").val('');
	     $("#id").remove();
	},

	clicouNoDia: function(date, jsEvent, view) {
        Calendario.settings.modalEvento.modal();
	}, 
	
	clicouNoEvento: function(calEvent, jsEvent, view) {
		var dataInicio = calEvent.start.format('DD/MM/YYYY HH:mm');
        var dataFim = calEvent.end.format('DD/MM/YYYY HH:mm');
        $("#titulo").val(calEvent.title);
        $("#dataInicio").val(dataInicio);
        $("#dataFim").val(dataFim);
        Calendario.settings.formAdicionarEvento.append('<input type="hidden" name="id" id="id" value="'+ calEvent.id +'" />');
    	Calendario.settings.modalEvento.modal();
	},
	
	salvarEvento: function(event) {
		event.preventDefault();
		$.ajax({
			method: "POST",
			url: 'evento/cadastrar',
			data: Calendario.settings.formAdicionarEvento.serialize()
		})
		.done(function(data, textStatus, jqXHR){
			console.log(data);
			
			Calendario.settings.blocoErro.hide();
			Calendario.settings.modalEvento.trigger('reset');
			Calendario.settings.calendario.fullCalendar('refetchEvents')
			Calendario.settings.modalEvento.modal('hide');
			
		})
		.fail(function(jqXHR, textStatus, errorThrown){
			Calendario.settings.blocoErro.show();
			Calendario.settings.blocoErro.find('ul').empty();
			var erros = jqXHR.responseJSON.fieldErrors;
			for (i = 0; i < erros.length; i++) {
				Calendario.settings.blocoErro.find('ul').append('<li>'+erros[i].message+'</li>');
			}
			
		})	
	}

};