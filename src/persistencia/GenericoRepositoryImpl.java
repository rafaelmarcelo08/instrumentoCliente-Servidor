package persistencia;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import util.Coluna;
import util.InstrumentoException;
import util.Tabela;

/**
 * Classe que define as operações da camada de persistencia generica
 * 
 * @author Rafael Marcelo
 */
public class GenericoRepositoryImpl<T, ID extends Serializable> implements GenericoRepository<T, ID> {

	// Obtém o nome da Entidade a ser persistida
	private Class<T> nomeEntidade;

	// Construtor que inicializa a classe com o nome da Entidade a ser persistida no
	// atributo
	@SuppressWarnings("unchecked")
	public GenericoRepositoryImpl() {
		this.nomeEntidade = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	/*
	 * Metodo para retorna o nome da tabela da Entidade
	 */
	public String getNomeTabela() {
		return nomeEntidade.getAnnotation(Tabela.class).nomeTabela();
	}

	// Interface da persistencia
	protected TransacaoRepository transacaoRepository;
	
	/*
	 * Implementa uma nova transação a aplicação do projeto
	 */
	@Override
	public void setTransacaoRepository(TransacaoRepository object) {
		transacaoRepository = object;
	}

	/*
	 * obtem uma transação
	 */
	@Override
	public TransacaoRepository getTransacaoRepository() {
		return transacaoRepository;
	}

	/**
	 * Método que inclui um objeto na persistencia
	 */
	@Override
	public T incluir(T object) throws InstrumentoException {
		Statement stmt = null;

		try {
			StringBuilder sql = new StringBuilder();

			// Desenvolvendo sql de inserção de objetos no banco
			sql.append("INSERT INTO ");
			sql.append(getNomeTabela().toUpperCase());
			sql.append(montarInstrucaoInsert(object));

			// Mostra instrução SQL no console
			System.out.println(sql.toString());

			// Executando comando SQL para inserção no banco
			stmt = getTransacaoRepository().getConexao().createStatement();
			stmt.execute(sql.toString());

			return object;
		} catch (Exception e) {
			// Lança uma exceção, caso ocorra algum erro na inclusão
			throw new InstrumentoException("Erro ao incluir objeto no banco.\n" + e);
		} finally {
			// Fechando conexão Statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					/*
					 * Lança uma exceção, caso ocorra algum erro no fechamento do Statement
					 */
					throw new InstrumentoException("Erro ao fechar conexão com o banco.\n" + e);
				}
			}
		}
	}

	/**
	 * Método que altera um objeto na persistencia
	 */
	@Override
	public T alterar(T object) throws InstrumentoException {
		Statement stmt = null;

		try {
			StringBuilder sql = new StringBuilder();

			// Desenvolvendo sql de inserção de objetos no banco
			sql.append("UPDATE ");
			sql.append(getNomeTabela().toUpperCase());
			sql.append(" SET ");
			sql.append(montarInstrucaoUpdate(object));

			// Mostra instrução SQL no console
			System.out.println(sql.toString());

			// Executando comando sql para inserção no banco
			stmt = getTransacaoRepository().getConexao().createStatement();
			stmt.execute(sql.toString());

			return object;
		} catch (Exception e) {
			// Lança uma exceção, caso ocorra algum erro na alteração
			throw new InstrumentoException("Objeto não existe no banco.");
		} finally {
			// Fechando conexão Statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (Exception e) {
					/*
					 * Lança uma exceção, caso ocorra algum erro no fechamento do Statement
					 */
					throw new InstrumentoException("Erro ao fechar conexão com o banco.\n" + e);
				}
			}
		}
	}

	/**
	 * Método que exclui um objeto na persistencia
	 */
	@Override
	public void excluir(Integer id) throws InstrumentoException {
		Statement stmt = null;

		try {
			StringBuilder sql = new StringBuilder();

			// Desenvolvendo sql de inserção de objetos no banco
			sql.append("DELETE FROM ");
			sql.append(getNomeTabela().toUpperCase());
			sql.append(montarInstrucaoDelete(id));
			
			// Mostra instrução SQL no console
			System.out.println(sql.toString());

			// Executando comando sql para inserção no banco
			stmt = getTransacaoRepository().getConexao().createStatement();
			stmt.execute(sql.toString());
		} catch (Exception e) {
			// Lança uma exceção, caso ocorra algum erro na exclusão
			throw new InstrumentoException("Erro ao excluir objeto no banco.\n" + e);
		} finally {
			// Fechando conexão Statement
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					/*
					 * Lança uma exceção, caso ocorra algum erro no fechamento do Statement
					 */
					throw new InstrumentoException("Erro ao fechar conexão com o banco.\n" + e);
				}
			}
		}
	}

	/**
	 * Método que consulta um objeto na persistencia
	 */
	@Override
	public T consultar(Integer id) throws InstrumentoException {
		Statement stmt = null;
		ResultSet rs = null;
		Field[] camposEntidade = nomeEntidade.getDeclaredFields();
		T objeto;

		try {
			StringBuilder sql = new StringBuilder();

			// Desenvolvendo sql de listagem dos objetos no banco
			sql.append("SELECT ");
			sql.append(montarInstrucaoSelect());
			sql.append(" FROM ");
			sql.append(getNomeTabela().toUpperCase());
			sql.append(" WHERE ");

			for (Field field : camposEntidade) {
				field.setAccessible(true);

				Coluna coluna = field.getAnnotation(Coluna.class);

				if ("id".equalsIgnoreCase(coluna.nomeColuna())) {
					sql.append(coluna.nomeColuna());
					break;
				}
			}
			sql.append(" = ");
			sql.append(id);

			// Mostra instrução SQL no console
			System.out.println(sql.toString());

			// Executando comando SQL para inserção no banco
			stmt = getTransacaoRepository().getConexao().createStatement();
			rs = stmt.executeQuery(sql.toString());

			if (rs.next()) {
				objeto = criarObjeto(rs);
				return objeto;
			} else {
				throw new Exception("Objeto não existe no banco");
			}
		} catch (Exception e) {
			// Lança uma exceção, caso ocorra algum erro na inclusão
			throw new InstrumentoException(e);
		} finally {
			// Fechando conexão Statement
			if (stmt != null) {
				try {
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					/*
					 * Lança uma exceção, caso ocorra algum erro no fechamento do Statement
					 */
					throw new InstrumentoException("Erro ao fechar conexão com o banco.\n" + e);
				}
			}
		}
	}

	/**
	 * Método que lista todos os na persistencia
	 */
	@Override
	public List<T> listar() throws InstrumentoException {
		Statement stmt = null;
		ResultSet rs = null;
		T objeto;

		try {
			StringBuilder sql = new StringBuilder();
			ArrayList<T> listaObjeto = new ArrayList<T>();

			// Desenvolvendo sql de listagem dos objetos no banco
			sql.append("SELECT ");
			sql.append(montarInstrucaoSelect());
			sql.append(" FROM ");
			sql.append(getNomeTabela().toUpperCase());

			// Mostra instrução SQL no console
			System.out.println(sql.toString());

			// Executando comando SQL para inserção no banco
			stmt = getTransacaoRepository().getConexao().createStatement();

			rs = stmt.executeQuery(sql.toString());

			while (rs.next()) {
				objeto = this.criarObjeto(rs);
				listaObjeto.add(objeto);
			}

			return listaObjeto;
		} catch (Exception e) {
			// Lança uma exceção, caso ocorra algum erro na inclusão
			throw new InstrumentoException(e);
		} finally {
			// Fechando conexão Statement
			if (stmt != null) {
				try {
					stmt.close();
					rs.close();
				} catch (SQLException e) {
					/*
					 * Lança uma exceção, caso ocorra algum erro no fechamento do Statement ou do
					 * ResultSet
					 */
					throw new InstrumentoException("Erro ao fechar conexão com o banco.\n" + e);
				}
			}
		}
	}

	/**
	 * Método para montar instrução INSERT, para incluir no banco de dados
	 * 
	 * @param objeto
	 * @return retorna o SQL pronto para ser executado
	 * @throws Exception caso exista um erro, retorna uma Exception
	 */
	private String montarInstrucaoInsert(T objeto) throws Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder campos = new StringBuilder();
		StringBuilder values = new StringBuilder();
		Field[] camposEntidade = nomeEntidade.getDeclaredFields();

		try {
			campos.append("(");
			values.append("(");

			/*
			 * Percorre os campos da entidade e monta a instrução INSERT
			 */
			for (Field field : camposEntidade) {
				field.setAccessible(true);

				Coluna coluna = field.getAnnotation(Coluna.class);

				if (!("id".equalsIgnoreCase(coluna.nomeColuna()))) {

					if (campos.length() > 1) {
						campos.append(", ");
					}

					if (values.length() > 1) {
						values.append(", ");
					}

					campos.append(coluna.nomeColuna());

					if (isUsarAspas(field.getType())) {
						values.append("'");

						// CONSTRUCAO DO DATE
						if (verificarTipoData(field.getType())) {
							StringBuilder dataFormatada = new StringBuilder();
							Object value = field.get(objeto);

							dataFormatada.append(formatarData(value));
							values.append(dataFormatada);
						} else {
							values.append(field.get(objeto));
						}

						values.append("'");
					} else {
						values.append(field.get(objeto));
					}
				}
			}

			campos.append(")");
			values.append(")\n");

			sql.append(campos);
			sql.append(" VALUES \n\n\t");
			sql.append(values);

			return sql.toString();
		} catch (Exception e) {
			throw new Exception("Erro ao montar sql 'INSERT'.");
		}
	}

	/**
	 * Método para montar instrução UPDATE, para atualizar no banco de dados
	 * 
	 * @param objeto
	 * @return retorna o SQL pronto para ser executado
	 * @throws Exception caso exista um erro, retorna uma Exception
	 */
	private String montarInstrucaoUpdate(T objeto) throws Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder campoValor = new StringBuilder();
		StringBuilder id = new StringBuilder();
		Field[] camposEntidade = objeto.getClass().getDeclaredFields();
		Integer contador = 1;

		try {

			/*
			 * Percorre os campos da entidade e monta a instrução UPDATE
			 */
			for (Field field : camposEntidade) {
				field.setAccessible(true);

				Coluna coluna = field.getAnnotation(Coluna.class);

				if (!("id".equalsIgnoreCase(coluna.nomeColuna()))) {
					campoValor.append("\n\t");
					campoValor.append(field.getName());
					campoValor.append(" = ");

					if (isUsarAspas(field.getType())) {
						campoValor.append("'");

						// CONSTRUCAO DO DATE
						if (verificarTipoData(field.getType())) {
							StringBuilder dataFormatada = new StringBuilder();
							Object valorData = field.get(objeto);

							dataFormatada.append(formatarData(valorData));
							campoValor.append(dataFormatada);
						} else {
							campoValor.append(field.get(objeto));
						}

						campoValor.append("'");
					} else {
						campoValor.append(field.get(objeto));
					}

					// PODE MELHORAR ?
					if (contador < (camposEntidade.length - 1)) {
						campoValor.append(", ");
						contador++;
					}
				} else {
					id.append("\n");
					id.append("WHERE ");
					id.append(field.getName());
					id.append(" = ");
					id.append(field.get(objeto));
				}
			}

			sql.append(campoValor);
			sql.append(id);

			return sql.toString();
		} catch (Exception e) {
			throw new Exception("Erro ao montar sql de 'UPDATE'.");
		}
	}

	/**
	 * Método para montar instrução DELETE, para deletar objeto no banco de dados
	 * 
	 * @param object
	 * @return retorna o SQL pronto para ser executado
	 * @throws Exception caso exista um erro, retorna uma Exception
	 */
	private String montarInstrucaoDelete(Integer id) throws Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder campoValor = new StringBuilder();
		Field[] camposEntidade = nomeEntidade.getDeclaredFields();

		try {

			/*
			 * Percorre os campos da entidade e monta a instrução Delete
			 */
			for (Field field : camposEntidade) {
				field.setAccessible(true);

				Coluna coluna = field.getAnnotation(Coluna.class);

				if ("id".equalsIgnoreCase(coluna.nomeColuna())) {
					campoValor.append(coluna.nomeColuna());
					break;
				}
			}

			campoValor.append(" = ");
			campoValor.append(id);
			sql.append(" WHERE ");
			sql.append(campoValor);

			return sql.toString();
		} catch (Exception e) {
			throw new Exception("Erro ao montar sql de 'DELETE'.");
		}
	}

	/**
	 * Método para montar instrução select, para listar objedos do banco de dados
	 * 
	 * @param object
	 * @return retorna o SQL pronto para ser executado
	 * @throws Exception caso exista um erro, retorna uma Exception
	 */
	private String montarInstrucaoSelect() throws Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder campos = new StringBuilder();
		Field[] camposEntidade = nomeEntidade.getDeclaredFields();

		try {
			/*
			 * Percorre os campos da entidade e monta a instrução SELECT
			 */
			for (Field field : camposEntidade) {
				field.setAccessible(true);

				Coluna coluna = field.getAnnotation(Coluna.class);

				if (campos.length() > 1) {
					campos.append(", ");
				}
				campos.append(coluna.nomeColuna());
			}
			sql.append(campos);
			return sql.toString();
		} catch (Exception e) {
			throw new Exception("Erro ao montar sql de 'SELECT'.");
		}
	}

	/**
	 * Método para criar o relacionamento entre o objeto e a tabela do banco de
	 * dados
	 * 
	 * @param object
	 * @return retorna o objeto pronto
	 * @throws Exception caso exista um erro, retorna uma Exception
	 */
	private T criarObjeto(ResultSet rs) throws Exception {
		T objeto = null;

		Field[] camposEntidade = nomeEntidade.getDeclaredFields();

		try {
			// Instancia o objeto dinamicamente
			objeto = (T) nomeEntidade.newInstance();

			// mapeia os valores do resultset no objeto da entidade
			for (Field field : camposEntidade) {
				field.setAccessible(true);

				Coluna coluna = field.getAnnotation(Coluna.class);

				Object value = rs.getObject(coluna.nomeColuna());

				field.set(objeto, value);
			}
			return objeto;
		} catch (InstantiationException | IllegalAccessException | SQLException e) {
			throw new Exception("Erro ao criar o relacionamento entre objeto e a tabela.");
		}
	}

	/*
	 * Verifica os tipos dos campos da Entidade e retona as aspas no comando SQL
	 */
	private Boolean isUsarAspas(Class<?> tipo) {
		if (tipo == int.class || tipo == long.class || tipo == double.class || tipo == float.class
				|| tipo == Integer.class || tipo == Long.class || tipo == Double.class || tipo == Float.class) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Verifica se o tipo do campo da Entidade e Date
	 */
	private Boolean verificarTipoData(Class<?> tipo) {
		if (tipo == Date.class) {
			return true;
		}
		return false;
	}

	/*
	 * Monta a data para inserção/alteração no banco de dados
	 */
	private String formatarData(Object valor) {
		StringBuilder dataFormatada = new StringBuilder();
		Date data = (Date) valor;
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

		dataFormatada.append(formato.format(data));

		return dataFormatada.toString();
	}
}