package persistencia;

import java.io.Serializable;
import java.util.List;
import util.InstrumentoException;

/**
 * Interface que define as operacoes da camada de persistencia de Instrumento
 * 
 * @author Rafael Marcelo
 *
 */
public interface GenericoRepository<T, ID extends Serializable> {

	/**
	 * Incluir instrumento
	 * 
	 * @param instrumento
	 * @return
	 * @throws InstrumentoException
	 */
	public T incluir(T object) throws InstrumentoException;

	/**
	 * Altera algum campo dentro de um objto 'Intrumento'
	 * 
	 * @param instrumento
	 * @return
	 * @throws InstrumentoException
	 */
	public T alterar(T object) throws InstrumentoException;

	/**
	 * Excluir um objeto 'Instrumento' do banco de dados
	 * 
	 * @param id
	 * @throws InstrumentoException
	 */
	public void excluir(Integer id) throws InstrumentoException;

	/**
	 * Consultar um 'Instrumento' pelo identificador
	 * 
	 * @param id
	 * @return
	 * @throws InstrumentoException
	 */
	public T consultar(Integer id) throws InstrumentoException;

	/**
	 * Listar todos os 'Instrumentos' cadastrados
	 * 
	 * @return
	 * @throws InstrumentoException
	 */
	public List<?> listar() throws InstrumentoException;

	/*
	 * Implementa uma nova transação a aplicação do projeto
	 */
	public void setTransacaoRepository(TransacaoRepository object);

	/*
	 * obtem uma transação
	 */
	public TransacaoRepository getTransacaoRepository();
}
