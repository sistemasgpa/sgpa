package sgpa.controller.page;

public class Sugestao {

	private String value;
	private String data;

	public Sugestao(String value, String data) {
		this.value = value;
		this.data = data;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
