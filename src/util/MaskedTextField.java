package util;

import java.text.ParseException;
import javax.swing.text.MaskFormatter;
import javafx.scene.control.TextField;

/**
 *  Classe que insere mascara no campo selecionado
 *  
 * @author Rafael Marcelo
 * @version 1.1
 */
public class MaskedTextField {

	private final MaskFormatter mf;
	private TextField tf;
	private String CaracteresValidos;
	private String mask;
	
	public MaskedTextField() {
		mf = new MaskFormatter();
	}
	
	public void setFormatter() {
		formatter(this.tf, this.CaracteresValidos, this.mask);
	}

	private void formatter(TextField tf, String CaracteresValidos, String mask) {
		
		try {
			mf.setMask(mask);
		} catch (ParseException ex) {
			System.out.println(ex.getMessage());
		}
		mf.setValidCharacters(CaracteresValidos);
		mf.setValueContainsLiteralCharacters(false);

		String text = tf.getText().replaceAll("\\W", "");

		
		Boolean repetir = true;
		while (repetir) {

            char ultimoCaractere;

            if (text.equals("")) {
                break;
            } else {
                ultimoCaractere = text.charAt(text.length() - 1);
            }

            try {
                text = mf.valueToString(text);
                repetir = false;
            } catch (ParseException ex) {
                text = text.replace(ultimoCaractere + "", "");
                repetir = true;
            }

        }
		tf.setText(text);

		if (!text.equals("")) {
			tf.positionCaret(tf.getLength());
		}
	}

	public void limitarTamanhoCampo(Integer tamanho) {
		tf.textProperty().addListener((observable, oldValue, newValue) ->{
			if(newValue.length() > tamanho) {
				tf.setText(oldValue);
			}
		});
	}
	
	public void limitarTamanhoCampo() {
		tf.textProperty().addListener((observable, oldValue, newValue) ->{
			if(newValue.equals("")) {
				tf.setText(oldValue);
			}
		});
	}
	
	/*
	 * Metódos de acesso
	 */
	public TextField getTf() {
		return tf;
	}

	public void setTf(TextField tf) {
		this.tf = tf;
	}

	public String getCaracteresValidos() {
		return CaracteresValidos;
	}

	public void setCaracteresValidos(String caracteresValidos) {
		CaracteresValidos = caracteresValidos;
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public MaskFormatter getMf() {
		return mf;
	}
}