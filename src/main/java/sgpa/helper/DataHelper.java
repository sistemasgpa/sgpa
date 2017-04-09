package sgpa.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataHelper {

	public static String formatarData(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(data);
	}

	public static String formatarDataSimples(Date data) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(data);
	}

}
