package util;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)

/**
 * Interface que obtem os nome da tabela da entidade
 * 
 * @author Rafael Marcelo
 *
 */
public @interface Tabela {

	public String nomeTabela();

}
