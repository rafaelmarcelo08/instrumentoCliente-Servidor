package negocio;

import java.util.List;
import entidade.Instrumento;
import util.InstrumentoException;

/**
 * Interface que define as opera��es da camada de negocio de Instrumento
 * 
 * @author Rafael Marcelo
 *
 */
public interface InstrumentoService {

	/**
	 * M�todo para incluir e validar os dados da camada de Entidade
	 * 
	 * @param instrumento
	 * @return retorna instrumento, caso nao retorna nenhum erro na valida��o
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	public Instrumento incluir(Instrumento instrumento) throws InstrumentoException;

	/**
	 * M�todo para alterar e validar os dados da camada de Entidade
	 * 
	 * @param instrumento
	 * @return retorna instrumento, caso n�o retorna nenhum erro na valida��o
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	public Instrumento alterar(Instrumento instrumento) throws InstrumentoException;

	/**
	 * M�todo para excluir e validar os dados da camada de Entidade
	 * 
	 * @param id
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	public void excluir(Integer id) throws InstrumentoException;

	/**
	 * M�todo para consultar e validar os dados da camada de Entidade
	 * 
	 * @param id
	 * @return retorna instrumento, caso nao retorna nenhum erro na valida��o
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	public Instrumento consultar(Integer id) throws InstrumentoException;

	/**
	 * M�todo para listar e validar se o objeto est� v�zio
	 * 
	 * @return retorna lista de instrumento
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	public List<Instrumento> listar() throws InstrumentoException;
}
