package negocio;

import java.util.ArrayList;
import java.util.List;
import entidade.Instrumento;
import persistencia.InstrumentoRepository;
import persistencia.InstrumentoRepositoryImpl;
import persistencia.TransacaoRepository;
import util.InstrumentoException;

/**
 * Clasee que define as operações da camada de negocio de Instrumento
 * 
 * @author Rafael Marcelo
 *
 */
public class InstrumentoServiceImpl implements InstrumentoService {

	// Interface instrumentoRepository
	private InstrumentoRepository instrumentoRepository;

	// obtém a interface instrumentoRepository
	public InstrumentoRepository getInstrumentoRepository() {
		return instrumentoRepository;
	}

	// Insere na 'Persistencia' uma nova transação
	public void setInstrumentoRepository(InstrumentoRepository instrumentoRepository) {
		this.instrumentoRepository = instrumentoRepository;
	}

	/**
	 * Método para incluir e validar os dados da camada de Entidade
	 * 
	 * @param instrumento
	 * @return retorna instrumento, caso nao retorna nenhum erro na validação
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public Instrumento incluir(Instrumento instrumento) throws InstrumentoException {
		// Solicita a 'Persistencia' a criação de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conexão
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Valida os dados da entidade
		validarDadosEntidade(instrumento);

		// Inclui o 'Instrumento'
		getInstrumentoRepository().incluir(instrumento);

		// Solicita a 'persistencia' para fechar a conexão
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return instrumento;
	}

	/**
	 * Método para alterar e validar os dados da camada de Entidade
	 * 
	 * @param instrumento
	 * @return retorna instrumento, caso não retorna nenhum erro na validação
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public Instrumento alterar(Instrumento instrumento) throws InstrumentoException {
		// Solicita a 'Persistencia' a criação de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conexão
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Valida os dados da entidade
		validarDadosEntidade(instrumento);

		// Altera os valores dos campos 'Instrumento'
		getInstrumentoRepository().alterar(instrumento);

		// Solicita a 'persistencia' para fechar a conexão
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return instrumento;
	}

	/**
	 * Método para excluir e validar o 'ID' da camada de Entidade
	 * 
	 * @param id
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public void excluir(Integer id) throws InstrumentoException {
		// Solicita a 'Persistencia' a criação de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conexão
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Validar ID
		validarId(id);

		// Exclui um objeto do banco
		getInstrumentoRepository().excluir(id);

		// Solicita a 'persistencia' para fechar a conexão
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();
	}

	/**
	 * Método para consultar e validar o 'ID' da camada de Entidade
	 * 
	 * @param id
	 * @return retorna instrumento, caso não retorna nenhum erro na validação
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public Instrumento consultar(Integer id) throws InstrumentoException {
		Instrumento instrumento;
		
		// Solicita a 'Persistencia' a criação de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conexão
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(false);

		// Validar ID
		validarId(id);

		// consulta um objeto do banco
		instrumento = getInstrumentoRepository().consultar(id);

		// Solicita a 'persistencia' para fechar a conexão
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return instrumento;
	}

	/**
	 * Método para lsitar e validar se a lista está vázia
	 * 
	 * @return retorna lista de instrumento, caso a lista não esteja vázia
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@Override
	public List<Instrumento> listar() throws InstrumentoException {
		List<Instrumento> listaInstrumentos = new ArrayList<>();

		// Solicita a 'Persistencia' a criação de um objeto para acessar o banco.
		gerenciarConexaoRepository();

		// Solicita a 'Persistencia' para abrir uma conexão
		getInstrumentoRepository().getTransacaoRepository().abrirTransacao(true);

		// Obtem um lista e verifica se está vazia
		listaInstrumentos = verificarListaInstrumento(getInstrumentoRepository().listar());

		// Solicita a 'persistencia' para fechar a conexão
		getInstrumentoRepository().getTransacaoRepository().fecharTransacao();

		return listaInstrumentos;
	}

	/**
	 * Método que valida todos os campos da camanda da 'Entidade'
	 * 
	 * @param instrumento
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	private void validarDadosEntidade(Instrumento instrumento) throws InstrumentoException {
		String regex_email = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.[a-z]+(\\.[a-z]+)?$";
		String regex_nome = "[a-zA-Z\\s]+";

		// Validação do nome 'Instrumento'
		if (instrumento.getNome().isEmpty() || instrumento.getNome().length() > 50
				|| !(instrumento.getNome().matches(regex_nome))) {
			throw new InstrumentoException("Nome do instrumento inválido.");
		}

		// Validação de email 'Instrumento'
		if (!(instrumento.getEmail().matches(regex_email)) || instrumento.getEmail().length() > 50) {
			throw new InstrumentoException("E-mail inválido.");
		}

		// Validação de valor 'Instrumento'
		if (instrumento.getValor() <= 0.0) {
			throw new InstrumentoException("Valor do instrumento inválido.");
		}

		// Validação de quantidade 'Instrumento'
		if (instrumento.getQuantidadeCompra() <= 0) {
			throw new InstrumentoException("Quantidade de compra de instrumento inválido.");
		}
	}

	/**
	 * Método que valida o cmapo 'ID' da camanda da 'Entidade'
	 * 
	 * @param id
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	private void validarId(Integer id) throws InstrumentoException {
		if (id <= 0) {
			throw new InstrumentoException("ID inválido.");
		}
	}

	/**
	 * Método que valida se a lista está vázia
	 * 
	 * @param lista de objetos
	 * @throws InstrumentoException caso exista um erro, retorna uma
	 *                              InstrumentoException
	 */
	@SuppressWarnings("unchecked")
	private List<Instrumento> verificarListaInstrumento(List<?> list) throws InstrumentoException {
		if (list.isEmpty()) {
			throw new InstrumentoException("Lista está vázia");
		}
		return (List<Instrumento>) list;
	}

	/**
	 * Método que implementa um objeto da interface 'InstrumentoRepository'.
	 * Gerencia uma nova transação com o banco de dados.
	 */
	private void gerenciarConexaoRepository() {
		instrumentoRepository = new InstrumentoRepositoryImpl();
		getInstrumentoRepository().setTransacaoRepository(new TransacaoRepository());
	}
}
