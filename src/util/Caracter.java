package util;

/**
 * Classe que encapsula os tipos de caracteres para MaskedTextView
 * 
 * @author Rafael Marcelo
 *
 */
public class Caracter {

	private final String MASK_NUMBER = "0123456789";
	private final String MASK_HEXADECIMAL = "0123456789abcdefABCDEF";
	private final String MASK_UPPER_CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String MASK_LOWER_CHARACTER = "abcdefghijklmnopqrstuvwxyz";
	private final String MASK_LOWER_UPPER_CHARACTER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private final String MASK_ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public String getMASK_NUMBER() {
		return MASK_NUMBER;
	}

	public String getMASK_HEXADECIMAL() {
		return MASK_HEXADECIMAL;
	}

	public String getMASK_UPPER_CHARACTER() {
		return MASK_UPPER_CHARACTER;
	}

	public String getMASK_LOWER_CHARACTER() {
		return MASK_LOWER_CHARACTER;
	}

	public String getMASK_LOWER_UPPER_CHARACTER() {
		return MASK_LOWER_UPPER_CHARACTER;
	}

	public String getMASK_ALPHANUMERIC() {
		return MASK_ALPHANUMERIC;
	}
}
