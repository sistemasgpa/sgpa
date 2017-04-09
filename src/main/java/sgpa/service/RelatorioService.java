package sgpa.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sgpa.model.Projeto;
import sgpa.model.Relatorio;
import sgpa.repository.RelatorioRepository;

@Service
public class RelatorioService {

	private RelatorioRepository relatorioRepository;

	@Autowired
	public RelatorioService(RelatorioRepository relatorioRepository) {
		this.relatorioRepository = relatorioRepository;
	}

	public Iterable<Relatorio> listarTodosPorProjeto(Projeto projeto) {
		return relatorioRepository.findByProjeto(projeto);
	}

	public Relatorio obterPorIdPorProjetoId(Long idRelatorio, Long idProjeto) {
		return relatorioRepository.findByIdAndProjetoId(idRelatorio, idProjeto);
	}

	public Relatorio salvar(Relatorio relatorio) {
		return relatorioRepository.save(relatorio);
	}

	public Relatorio enviar(Relatorio relatorio, String texto) {
		relatorio.setTexto(texto);
		relatorio.setDataEnvio(new Date());
		relatorio.setEnviado(true);
		return salvar(relatorio);
	}

	public void deletar(Relatorio relatorio) {
		relatorioRepository.delete(relatorio);
	}

}
