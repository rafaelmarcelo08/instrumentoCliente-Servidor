package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ValidacaoDataController {
	
	public void validarData(String dataTexto) throws InstrumentoException{
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

		try {
			formato.setLenient(false);
			formato.parse(dataTexto);
		} catch (ParseException e) {
			throw new InstrumentoException("Data invalida.");
		}
	}
}
