package negocio;

import java.util.ArrayList;
import java.util.List;
import entidade.Instrumento;
import persistencia.InstrumentoRepository;
import persistencia.InstrumentoRepositoryImpl;
import persistencia.TransacaoRepository;
import util.InstrumentoException;

/**
 * Clasee que define as opera��es da camada de negocio de Instrumento
 * 
 * @author Rafael Marcelo
 *
 */
public class InstrumentoServiceImpl implements InstrumentoService {

	// Interface instrumentoRepository
	private InstrumentoRepository instrumentoRepository;

	// obt�m a interface instrumentoRepository
	public InstrumentoRepository getInstrumentoRepository() {
		return instrumentoRepository;
	}

	// Insere na 'Persistencia' uma nova transa��o
	public void setInstrumentoRepository(InstrumentoRepository instrumentoRepository) {
		this.instrumentoRepository = instrumentoRepository;
	}

	/**
	 * M�todo para incluir e validar os dados da camada de Entidade
	 * 
	 * @param instrumento
	 * @return retorna instrumento, caso nao retorna nenhum erro na valida��o
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public Instrumento incluir(Instrumento instrumento) throws InstrumentoException {
		// Solicita a 'Persistencia' a cria��o de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conex�o
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Valida os dados da entidade
		validarDadosEntidade(instrumento);

		// Inclui o 'Instrumento'
		getInstrumentoRepository().incluir(instrumento);

		// Solicita a 'persistencia' para fechar a conex�o
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return instrumento;
	}

	/**
	 * M�todo para alterar e validar os dados da camada de Entidade
	 * 
	 * @param instrumento
	 * @return retorna instrumento, caso n�o retorna nenhum erro na valida��o
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public Instrumento alterar(Instrumento instrumento) throws InstrumentoException {
		// Solicita a 'Persistencia' a cria��o de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conex�o
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Valida os dados da entidade
		validarDadosEntidade(instrumento);

		// Altera os valores dos campos 'Instrumento'
		getInstrumentoRepository().alterar(instrumento);

		// Solicita a 'persistencia' para fechar a conex�o
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return instrumento;
	}

	/**
	 * M�todo para excluir e validar o 'ID' da camada de Entidade
	 * 
	 * @param id
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public void excluir(Integer id) throws InstrumentoException {
		// Solicita a 'Persistencia' a cria��o de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conex�o
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Validar ID
		validarId(id);

		// Exclui um objeto do banco
		getInstrumentoRepository().excluir(id);

		// Solicita a 'persistencia' para fechar a conex�o
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();
	}

	/**
	 * M�todo para consultar e validar o 'ID' da camada de Entidade
	 * 
	 * @param id
	 * @return retorna instrumento, caso n�o retorna nenhum erro na valida��o
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public Instrumento consultar(Integer id) throws InstrumentoException {
		Instrumento instrumento;
		
		// Solicita a 'Persistencia' a cria��o de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conex�o
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Validar ID
		validarId(id);

		// consulta um objeto do banco
		instrumento = getInstrumentoRepository().consultar(id);

		// Solicita a 'persistencia' para fechar a conex�o
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return instrumento;
	}

	/**
	 * M�todo para lsitar e validar se a lista est� v�zia
	 * 
	 * @return retorna lista de instrumento, caso a lista n�o esteja v�zia
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public List<Instrumento> listar() throws InstrumentoException {
		List<Instrumento> listaInstrumentos = new ArrayList<>();

		// Solicita a 'Persistencia' a cria��o de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conex�o
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(true);

		// Obtem um lista e verifica se est� vazia
		listaInstrumentos = verificarListaInstrumento(getInstrumentoRepository().listar());

		// Solicita a 'persistencia' para fechar a conex�o
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return listaInstrumentos;
	}

	/**
	 * M�todo que valida todos os campos da camanda da 'Entidade'
	 * 
	 * @param instrumento
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	private void validarDadosEntidade(Instrumento instrumento) throws InstrumentoException {
		String regex_email = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-z]+(\\.[a-z]+)?$";
		String regex_nome = "[a-zA-Z\\s]+";

		// Valida��o do nome 'Instrumento'
		if (instrumento.getNome().isEmpty() || instrumento.getNome().length() > 50
				|| !(instrumento.getNome().matches(regex_nome))) {
			throw new InstrumentoException("Nome do instrumento inv�lido.");
		}

		// Valida��o de email 'Instrumento'
		if (!(instrumento.getEmail().matches(regex_email)) || instrumento.getEmail().length() > 50) {
			throw new InstrumentoException("E-mail inv�lido.");
		}

		// Valida��o de valor 'Instrumento'
		if (instrumento.getValor() <= 0.0) {
			throw new InstrumentoException("Valor do instrumento inv�lido.");
		}

		// Valida��o de quantidade 'Instrumento'
		if (instrumento.getQuantidadeCompra() <= 0) {
			throw new InstrumentoException("Quantidade de compra de instrumento inv�lido.");
		}
	}

	/**
	 * M�todo que valida o cmapo 'ID' da camanda da 'Entidade'
	 * 
	 * @param id
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	private void validarId(Integer id) throws InstrumentoException {
		if (id <= 0) {
			throw new InstrumentoException("ID inv�lido.");
		}
	}

	/**
	 * M�todo que valida se a lista est� v�zia
	 * 
	 * @param lista de objetos
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@SuppressWarnings("unchecked")
	private List<Instrumento> verificarListaInstrumento(List<?> list) throws InstrumentoException {
		if (list.isEmpty()) {
			throw new InstrumentoException("Lista est� v�zia");
		}
		return (List<Instrumento>) list;
	}

	/**
	 * M�todo que implementa um objeto da interface 'InstrumentoRepository'.
	 * Gerencia uma nova transa��o com o banco de dados.
	 */
	private void gerenciarConexaoRepository() {
		instrumentoRepository = new InstrumentoRepositoryImpl();
		getInstrumentoRepository().setTransacaoRepository(new TransacaoRepository());
	}
}
