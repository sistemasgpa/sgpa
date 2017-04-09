package sgpa.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sgpa.model.CategoriaProjeto;
import sgpa.repository.CategoriaProjetoRepository;

@Service
public class CategoriaProjetoService {

	private CategoriaProjetoRepository categoriaProjetoRepository;

	@Autowired
	public CategoriaProjetoService(CategoriaProjetoRepository categoriaProjetoRepository) {
		this.categoriaProjetoRepository = categoriaProjetoRepository;
	}

	public List<CategoriaProjeto> listarTodos() {
		return categoriaProjetoRepository.findAllByOrderByNomeAsc();
	}

}
