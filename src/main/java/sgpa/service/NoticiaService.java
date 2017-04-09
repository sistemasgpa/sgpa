package sgpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import sgpa.model.CategoriaNoticia;
import sgpa.model.Noticia;
import sgpa.repository.CategoriaNoticiaRepository;
import sgpa.repository.NoticiaRepository;
import sgpa.repository.filter.NoticiaFilter;

@Service
public class NoticiaService {

	private CategoriaNoticiaRepository categoriaNoticiaRepository;
	private NoticiaRepository noticiaRepository;
	
	@Autowired
	public NoticiaService(CategoriaNoticiaRepository categoriaNoticiaRepository, NoticiaRepository noticiaRepository) {
		this.categoriaNoticiaRepository = categoriaNoticiaRepository;
		this.noticiaRepository = noticiaRepository;
	}

	public Noticia obterPorId(Long idNoticia) {
		return noticiaRepository.findOne(idNoticia);
	}
	
	public Page<Noticia> listarTodas(Pageable pageable) {
		return noticiaRepository.findAll(pageable);
	}

	public Noticia salvar(Noticia noticia) {
		return noticiaRepository.save(noticia);
	}
	
	public Iterable<CategoriaNoticia> listarTodasCategorias() {
		return categoriaNoticiaRepository.findAll();
	}
	
	public Page<Noticia> filtrarPorIdCategoria(Long idCategoria, Pageable pageable) {
		if(idCategoria==0){
			return noticiaRepository.filtrar(new NoticiaFilter("", null), pageable);
		}
		CategoriaNoticia cn = new CategoriaNoticia();
		cn.setId(idCategoria);
		return noticiaRepository.filtrar(new NoticiaFilter("",cn), pageable);
	}
	
	public Page<Noticia> filtrar(NoticiaFilter filtro, Pageable pageable) {
		return noticiaRepository.filtrar(filtro, pageable);
	}
	
}
