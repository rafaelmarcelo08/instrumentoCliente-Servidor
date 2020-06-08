package util;

/**
 * Classe que encapsula as exceções da aplicação do 'banco de dados'
 * 
 * @author Rafael Marcelo
 *
 */
public class BancoDadosException extends Exception {

	private static final long serialVersionUID = -4835568410468390974L;
	private Exception ex;
	private String msg;

	public BancoDadosException(Exception e) {
		ex = e;
		msg = e.getMessage();
	}

	public BancoDadosException(String e) {
		msg = e;
	}

	public BancoDadosException(Exception e, String mensagem) {
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
