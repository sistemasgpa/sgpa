package sgpa.controller.page;

import java.util.List;

public class RespostaSugestao {

	private List<Sugestao> suggestions;

	public RespostaSugestao(List<Sugestao> suggestions) {
		this.suggestions = suggestions;
	}

	public List<Sugestao> getSuggestions() {
		return suggestions;
	}

	public void setSuggestions(List<Sugestao> suggestions) {
		this.suggestions = suggestions;
	}

}
