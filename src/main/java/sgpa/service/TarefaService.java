package sgpa.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sgpa.model.Projeto;
import sgpa.model.Tarefa;
import sgpa.repository.TarefaRepository;

@Service
public class TarefaService {

	private TarefaRepository tarefaRepository;

	@Autowired
	public TarefaService(TarefaRepository tarefaRepository) {
		this.tarefaRepository = tarefaRepository;
	}

	public Iterable<Tarefa> listarTodosPorProjeto(Projeto projeto) {
		return tarefaRepository.findByProjeto(projeto);
	}

	public Tarefa obterPorIdPorProjetoId(Long idTarefa, Long idProjeto) {
		return tarefaRepository.findByIdAndProjetoId(idTarefa, idProjeto);
	}

	public Tarefa salvar(Tarefa tarefa) {
		return tarefaRepository.save(tarefa);
	}

	public Tarefa alterarStatus(Tarefa tarefa) {
		if (tarefa.getConcluida()) {
			tarefa.setConcluida(false);
			tarefa.setDataConclusao(null);
		} else {
			tarefa.setConcluida(true);
			tarefa.setDataConclusao(new Date());
		}
		return salvar(tarefa);
	}

	public void deletar(Tarefa tarefa) {
		tarefaRepository.delete(tarefa);
	}

}
