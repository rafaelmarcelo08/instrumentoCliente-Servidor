package controller;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import entidade.Instrumento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import negocio.InstrumentoServiceImpl;
import util.Caracter;
import util.InstrumentoException;
import util.MaskedTextField;

/**
 * Classe que controla as requisoções do cliente Cliente-Servidor
 * 
 * @author Rafael Marcelo
 *
 */
public class InstrumentoController implements Initializable, Runnable {

	// Declaração das atributos 'constantes'
	private static final int VALOR_NULO_CAMPO = 0;
	private static final int TAMANHO_DATA = 8;
	private static final int TAMANHO_MAXIMO_CAMPO_DATA = 10;

	// Declaração dos atibutos globais da classe
	ArrayList<Instrumento> todosInstrumentos = new ArrayList<>();
	InstrumentoServiceImpl service = new InstrumentoServiceImpl();
	Instrumento instrumento = new Instrumento();

	// Declaração das referencias dos icones da visão
	@FXML
	private TableView<Instrumento> tvLista;

	@FXML
	private TableColumn<Instrumento, Integer> idCol;

	@FXML
	private TableColumn<Instrumento, String> nomeCol;

	@FXML
	private TableColumn<Instrumento, String> emailCol;

	@FXML
	private TableColumn<Instrumento, Float> valorCol;

	@FXML
	private TableColumn<Instrumento, Date> dataCompraCol;

	@FXML
	private TableColumn<Instrumento, Integer> quantidadeCol;

	@FXML
	private Button btnIncluir;

	@FXML
	private Button btnAlterar;

	@FXML
	private Button btnConsultar;

	@FXML
	private Button btnExcluir;

	@FXML
	private Button btnBuscarObjeto;

	@FXML
	private TextField txtIdInclusao;

	@FXML
	private TextField txtIdConsulta;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private TextField txtValor;

	@FXML
	private TextField txtDataCompra;

	@FXML
	private TextField txtQuantidade;

	@FXML
	private Button btnCancelar;

	/**
	 * Método função lambda, inicialização da aplicação
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Declaração das referencias da tableView
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		valorCol.setCellValueFactory(new PropertyValueFactory<>("valor"));
		dataCompraCol.setCellValueFactory(new PropertyValueFactory<>("dataCompra"));
		quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidadeCompra"));

		/**
		 * Formata a data para BR no table view
		 */
		dataCompraCol.setCellFactory(cell -> {
			return new TableCell<Instrumento, Date>() {
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);

					if (!empty) {
						setText(format.format(item));
					} else {
						setText("");
						setGraphic(null);
					}
				}
			};
		});

		// Inicialização da listagem dos dados
		run();

		/**
		 * Botoões de 'CRUD' da aplicação
		 */

		// Ação de quando o botão 'Incluir' e selecioado
		btnIncluir.setOnAction(event -> {

			try {
				service.incluir(preencherInstrumento());
				limparCampos();
				run();
				alertarUsuario("Instrumento incluído.");
			} catch (InstrumentoException e) {
				alertarUsuario(e.getMsg());
			}
		});

		// Ação de quando o botão 'Alterar' e selecioado
		btnAlterar.setOnAction(event -> {
			Instrumento instrumentoAlterado = new Instrumento();
			try {
				instrumentoAlterado = preencherInstrumento();
				instrumentoAlterado.setId(converterId(txtIdInclusao.getText()));
				service.alterar(instrumentoAlterado);
				limparCampos();
				limparConsulta();
				run();
				alertarUsuario("Instrumento alterado.");

				gerenciarBotoes();

				btnConsultar.setDisable(false);
			} catch (InstrumentoException e) {
				alertarUsuario(e.getMsg());
			}
		});

		// Ação de quando o botão 'Consultar' e selecioado
		btnConsultar.setOnAction(event -> {
			instrumento = null;

			try {
				instrumento = consultarInstrumento();

				if (instrumento != null) {
					btnIncluir.setDisable(true);
					btnBuscarObjeto.setDisable(false);
					btnConsultar.setDisable(true);
				} else {
					btnBuscarObjeto.setDisable(true);
				}
			} catch (InstrumentoException e) {
				alertarUsuario(e.getMsg());
			}
		});

		// Ação de quando o botão 'Excluir' e selecioado
		btnExcluir.setOnAction(event -> {
			Integer id;

			try {

				id = converterId(txtIdInclusao.getText());

				service.excluir(id);

				limparCampos();
				limparConsulta();
				run();
				alertarUsuario("Instrumento excluído.");
				gerenciarBotoes();

				btnConsultar.setDisable(false);
			} catch (InstrumentoException e) {
				alertarUsuario(e.getMsg());
			}
		});

		// Ações que auxilia a area de texto e auxilia os botões.

		/**
		 * Está ação busca o objeto em memoria para inserir na visão
		 */
		btnBuscarObjeto.setOnAction(event -> {
			btnIncluir.setDisable(true);
			btnAlterar.setDisable(false);
			btnExcluir.setDisable(false);

			try {
				preencherAVisao();
				btnBuscarObjeto.setDisable(true);
			} catch (InstrumentoException e) {
				alertarUsuario(e.getMsg());
			}
		});

		// Observador do campo de data, o mesmo formata a data no padrão BR
		txtDataCompra.setOnKeyReleased(event -> {
			String dataCampoString;

			dataCampoString = txtDataCompra.getText().replaceAll("/", "");

			if (dataCampoString.length() == TAMANHO_DATA) {
				Caracter caracter = new Caracter();
				MaskedTextField mask = new MaskedTextField();
				mask.setMask("##/##/####");
				mask.setCaracteresValidos(caracter.getMASK_NUMBER());
				mask.setTf(txtDataCompra);
				mask.setFormatter();
				mask.limitarTamanhoCampo(TAMANHO_MAXIMO_CAMPO_DATA);

			}
		});

		// Limpa todos os campos, habilita/desabilita os campos da visão
		btnCancelar.setOnAction(event -> {
			btnIncluir.setDisable(false);
			btnAlterar.setDisable(true);
			btnExcluir.setDisable(true);
			btnBuscarObjeto.setDisable(true);
			btnConsultar.setDisable(false);

			limparCampos();
			limparConsulta();
			run();
		});
	}

	/**
	 * Método que auxília preencher a 'entidade Instrumento' com os dados da visão
	 * 
	 * @return Instrumento
	 * @throws InstrumentoException, caso exista algum erro ao preencher instrumento
	 */
	private Instrumento preencherInstrumento() throws InstrumentoException {
		Instrumento instrumento = new Instrumento();
		Float valorInstrumento;
		Integer quantidadeInstrumento;

		instrumento.setNome(txtNome.getText());
		instrumento.setEmail(txtEmail.getText());

		if (!(txtValor.getText().isEmpty())) {
			valorInstrumento = Float.parseFloat(txtValor.getText());
			instrumento.setValor(valorInstrumento);

		} else {
			instrumento.setValor((float) VALOR_NULO_CAMPO);
		}

		if (!(txtQuantidade.getText().isEmpty())) {
			quantidadeInstrumento = Integer.parseInt(txtQuantidade.getText());
			instrumento.setQuantidadeCompra(quantidadeInstrumento);
		} else {
			instrumento.setQuantidadeCompra(VALOR_NULO_CAMPO);
		}

		instrumento.setDataCompra(formatarData(txtDataCompra.getText()));

		return instrumento;
	}

	/**
	 * Método que auxília formatar data do Campo 'Data Compra'
	 * 
	 * @param data
	 * @return Date
	 * @throws InstrumentoException, caso exista algum erro ao preencher instrumento
	 */
	private Date formatarData(String data) throws InstrumentoException {
		DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		Date dataFormatada;

		try {
			formato.setLenient(false);
			dataFormatada = formato.parse(data);
			return dataFormatada;
		} catch (ParseException e) {
			throw new InstrumentoException("Erro ao converter data");
		}
	}

	/**
	 * Método que auxília preencher a visão com os dados do banco de dados
	 * 
	 * @throws InstrumentoException, caso exista algum erro ao preencher instrumento
	 */
	private void preencherAVisao() throws InstrumentoException {
		StringBuilder idInclusaoString = new StringBuilder();
		StringBuilder valorstring = new StringBuilder();
		StringBuilder dataString = new StringBuilder();
		StringBuilder quantidadeString = new StringBuilder();

		try {
			idInclusaoString.append(String.valueOf(instrumento.getId()));
			valorstring.append(String.valueOf(instrumento.getValor()));
			quantidadeString.append(String.valueOf(instrumento.getQuantidadeCompra()));
			dataString.append(formatarDataVisao());

			txtIdInclusao.setText(idInclusaoString.toString());
			txtNome.setText(instrumento.getNome());
			txtEmail.setText(instrumento.getEmail());
			txtValor.setText(valorstring.toString());
			txtQuantidade.setText(quantidadeString.toString());
			txtDataCompra.setText(dataString.toString());
		} catch (Exception e) {
			throw new InstrumentoException("Erro ao preencher a visão.");
		}
	}

	/**
	 * Método que auxília limpar o campo consulta
	 */
	private void limparConsulta() {
		txtIdConsulta.setText("");
	}

	/**
	 * Método que auxília formatar a data na visão
	 * 
	 * @return retorna uma data formatada
	 */
	public String formatarDataVisao() {
		StringBuilder dataFormatada = new StringBuilder();

		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		dataFormatada.append(formato.format(instrumento.getDataCompra()));
		return dataFormatada.toString();
	}

	/**
	 * Método que auxília validar ID na camada de visão
	 * 
	 * @param idString
	 * @return id
	 * @throws InstrumentoException, caso exista algum erro ao preencher instrumento
	 */
	public Integer converterId(String idString) throws InstrumentoException {
		try {
			return Integer.parseInt(idString);
		} catch (Exception e) {
			throw new InstrumentoException("Erro ao converter id.");
		}
	}

	/**
	 * Método que auxília em limpar os campos da visão
	 */
	private void limparCampos() {
		txtIdInclusao.setText("");
		txtNome.setText("");
		txtEmail.setText("");
		txtValor.setText("");
		txtDataCompra.setText("");
		txtQuantidade.setText("");
	}

	/**
	 * Método que auxília no gerenciamento dos botões
	 */
	private void gerenciarBotoes() {
		btnBuscarObjeto.setDisable(true);
		btnAlterar.setDisable(true);
		btnExcluir.setDisable(true);
		btnIncluir.setDisable(false);
	}

	/**
	 * Método que auxília na consulta de um objeto
	 * 
	 * @return Instrumento
	 * @throws InstrumentoException, caso exista algum erro ao preencher instrumento
	 */
	private Instrumento consultarInstrumento() throws InstrumentoException {
		Instrumento instrumentoConsulta = new Instrumento();
		Integer id;

		try {
			id = Integer.parseInt(txtIdConsulta.getText());
		} catch (Exception e) {
			throw new InstrumentoException("Erro ao converter valor");
		}

		try {
			instrumentoConsulta = service.consultar(id);
			todosInstrumentos.clear();
			todosInstrumentos.add(instrumentoConsulta);
			tvLista.setItems(listaInstrumentos());
			return instrumentoConsulta;
		} catch (InstrumentoException e) {
			throw new InstrumentoException(e.getMsg());
		}
	}

	/**
	 * Método que auxília nas mensgens de alerta para o usuário
	 * 
	 * @param mensagem
	 */
	private void alertarUsuario(String mensagem) {
		Alert alert;
		alert = new Alert(AlertType.INFORMATION, mensagem, ButtonType.OK);
		alert.setTitle("Atenção");
		alert.setHeaderText("informação");
		alert.show();
	}

	/**
	 * Método que auxília na observação da lista da table view
	 * 
	 * @return lista de objetos para setar na table view
	 */
	private ObservableList<Instrumento> listaInstrumentos() {
		return FXCollections.observableArrayList(todosInstrumentos);
	}

	/**
	 * Método que auxília na execução de uma Thread auxiliar
	 */
	@Override
	public void run() {
		try {
			todosInstrumentos = (ArrayList<Instrumento>) service.listar();

			tvLista.setItems(listaInstrumentos());
		} catch (InstrumentoException e) {
			alertarUsuario(e.getMsg());
		}
	}
}