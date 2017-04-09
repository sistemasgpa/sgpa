package sgpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sgpa.model.Arquivo;
import sgpa.model.Projeto;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Long> {

	public Iterable<Arquivo> findByProjetoOrderByIdDesc(Projeto projeto);

	public Arquivo findByIdAndProjetoId(Long idArquivo, Long idProjeto);

}
