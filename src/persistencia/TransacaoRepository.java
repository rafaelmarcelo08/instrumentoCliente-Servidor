package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.BancoDadosException;
import util.InstrumentoException;

/**
 * Classe que conecta com o banco de dados.
 * 
 * @author Rafael Marcelo
 */
public class TransacaoRepository {

	// JDBC Driver Utilizado
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	// Objeto de conexão
	private Connection conn = null;

	// obtem uma a conexão
	protected Connection getConexao() {
		return conn;
	}

	// Dados de conexão com o banco de dados
	private static final String url = "jdbc:mysql://localhost:3306/instrumentoBD";
	private static final String userName = "root";
	private static final String password = "";

	/**
	 * Abre uma conexão com o banco de dados 'MYSQL'
	 * 
	 * @return os dados da conexão com o banco de dados
	 * @throws SQLException 
	 * @throws Exception, se houver erros em abrir conexão com o banco
	 */
	private void abrirConexao() throws BancoDadosException {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException | SQLException ex) {
			 throw new BancoDadosException(ex);
		}
	}

	/**
	 * Abrir conexão com o banco de dados
	 * 
	 * @param leitura, verifica se e leitura ou escrita no banco.
	 * @return os dados conectado com o banco de dados
	 * @throws BancoDadosException 
	 * @throws Exception, se houver ao conectar com o banco
	 */
	public void abrirTransacao(Boolean leitura) throws InstrumentoException {

		try {
			if (conn == null) {
				abrirConexao();
				conn.setReadOnly(leitura);
			}
		} catch (Exception e) {
			throw new InstrumentoException("O banco não está conectado.");
		}
	}

	// Fecha conexão com o banco de dados
	public void fecharTransacao() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// throw new SQLException("Erro ao fechar conexão com o banco.");
		}
	}

	/**
	 * Main para testes de conexão
	 */
	public static void main(String[] args) throws Exception {
		TransacaoRepository dados = new TransacaoRepository();
		Connection conn = null;

		dados.abrirTransacao(false);
		conn = dados.getConexao();
		if (conn != null) {
			System.out.println("O banco está conectado.");
		} else {
			System.out.println("O banco não conectou.");
		}
	}
}