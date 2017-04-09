package sgpa.config;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import sgpa.model.Participacao;
import sgpa.service.UsuarioService;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UsuarioService usuarioService;

	@SuppressWarnings("unchecked")
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		if (request.isUserInRole("ROLE_ADMIN"))
			return true;

		final String ID_PROJETO = "idProjeto";
		Map<String, String> pathVariables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (pathVariables.containsKey(ID_PROJETO)) {
			Long idProjeto = Long.parseLong(pathVariables.get(ID_PROJETO));
			Principal principal = request.getUserPrincipal();
			Optional<Participacao> optionalParticipacao = usuarioService.obterParticipacaoProjeto(principal.getName(),
					idProjeto);

			if (!optionalParticipacao.isPresent()) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mv)
			throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex)
			throws Exception {
	}

}
