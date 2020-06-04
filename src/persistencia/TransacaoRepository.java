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

	// Objeto de conex�o
	private Connection conn = null;

	// obtem uma a conex�o
	protected Connection getConexao() {
		return conn;
	}

	// Dados de conex�o com o banco de dados
	private static final String url = "jdbc:mysql://localhost:3306/instrumentoBD";
	private static final String userName = "root";
	private static final String password = "";

	/**
	 * Abre uma conex�o com o banco de dados 'MYSQL'
	 * 
	 * @return os dados da conex�o com o banco de dados
	 * @throws SQLException 
	 * @throws Exception, se houver erros em abrir conex�o com o banco
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
	 * Abrir conex�o com o banco de dados
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
			throw new InstrumentoException("O banco n�o est� conectado.");
		}
	}

	// Fecha conex�o com o banco de dados
	public void fecharTransacao() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			// throw new SQLException("Erro ao fechar conex�o com o banco.");
		}
	}

	/**
	 * Main para testes de conex�o
	 */
	public static void main(String[] args) throws Exception {
		TransacaoRepository dados = new TransacaoRepository();
		Connection conn = null;

		dados.abrirTransacao(false);
		conn = dados.getConexao();
		if (conn != null) {
			System.out.println("O banco est� conectado.");
		} else {
			System.out.println("O banco n�o conectou.");
		}
	}
}