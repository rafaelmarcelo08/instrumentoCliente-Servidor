package util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)

/**
 * Interface que obtem os nomes das colunas da entidade
 * 
 * @author Rafael Marcelo
 *
 */
public @interface Coluna {

	public String nomeColuna();

}
