package sgpa.repository.helper.projeto;

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

import sgpa.model.Projeto;
import sgpa.repository.filter.ProjetoFilter;

public class ProjetoRepositoryImpl implements ProjetoRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;

	@SuppressWarnings("unchecked")
	@Override
	public Page<Projeto> filtrar(ProjetoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Projeto.class);
		criteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
		criteria.setMaxResults(pageable.getPageSize());
		criteria.addOrder(Order.asc("titulo"));
		adicionarFiltro(filtro, criteria);
		return new PageImpl<Projeto>(criteria.list(), pageable, obterTotal(filtro));
	}

	private Long obterTotal(ProjetoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Projeto.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(ProjetoFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getTitulo())) {
				criteria.add(Restrictions.ilike("titulo", filtro.getTitulo(), MatchMode.ANYWHERE));
			}

			if (!StringUtils.isEmpty(filtro.getStatus())) {
				criteria.add(Restrictions.eq("status", filtro.getStatus()));
			}

			if (!StringUtils.isEmpty(filtro.getCategoria())) {
				criteria.add(Restrictions.eq("categoria", filtro.getCategoria()));
			}

		}
	}

}
