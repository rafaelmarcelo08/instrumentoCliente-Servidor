package util;

/**
 * Classe que encapsula as exceções da aplicação 'Instrumento'
 * 
 * @author Rafael Marcelo
 *
 */

public class InstrumentoException extends Exception {

	/**
	 * COMENTARIO
	 */
	private static final long serialVersionUID = -8915071648787268431L;

	private Exception ex;
	private String msg;

	public InstrumentoException(Exception e) {
		ex = e;
		msg = e.getMessage();
	}

	public InstrumentoException(String e) {
		msg = e;
	}

	public InstrumentoException(Exception e, String mensagem) {
		e.printStackTrace();
		ex = e;
		msg = mensagem;
	}

	public Exception getEx() {
		return ex;
	}

	public String getMsg() {
		return msg;
	}

}