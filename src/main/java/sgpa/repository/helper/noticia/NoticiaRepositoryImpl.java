package sgpa.repository.helper.noticia;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import sgpa.model.Noticia;
import sgpa.repository.filter.NoticiaFilter;

public class NoticiaRepositoryImpl implements NoticiaRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public Page<Noticia> filtrar(NoticiaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Noticia.class);
		criteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		criteria.setMaxResults(pageable.getPageSize());
		criteria.addOrder(Order.desc("data"));
		adicionarFiltro(filtro, criteria);

		return new PageImpl<Noticia>(criteria.list(), pageable, obterTotal(filtro));
	}

	private Long obterTotal(NoticiaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Noticia.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(NoticiaFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getTitulo())) {
				criteria.add(Restrictions.ilike("titulo", filtro.getTitulo(), MatchMode.ANYWHERE));
			}

			if (!StringUtils.isEmpty(filtro.getCategoria())) {
				criteria.add(Restrictions.eq("categoria", filtro.getCategoria()));
			}

		}
	}
}
