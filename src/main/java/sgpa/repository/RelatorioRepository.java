package sgpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Projeto;
import sgpa.model.Relatorio;

@Repository
public interface RelatorioRepository extends JpaRepository<Relatorio, Long> {

	public Iterable<Relatorio> findByProjeto(Projeto projeto);

	public Relatorio findByIdAndProjetoId(Long idRelatorio, Long idProjeto);
}
