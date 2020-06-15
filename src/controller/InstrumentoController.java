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
 * Classe que controla as requisições do cliente Cliente-Servidor
 * 
 * @author Rafael Marcelo
 *
 */
public class InstrumentoController implements Initializable, Runnable {

	/* Declaração das atributos 'constantes' */
	private static final int TAMANHO_DATA = 8;
	private static final int TAMANHO_MAXIMO_CAMPO_DATA = 10;

	/* Declaração das referencias dos icones da visão */
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
	private Button btnExcluir;
	
    @FXML
    private Button btnBuscarId;

    @FXML
    private TextField txtBuscarId;
    
    @FXML
    private TextField txtId;

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

	/* Declaração dos atibutos globais da classe */
	ArrayList<Instrumento> todosInstrumentos = new ArrayList<>();
	InstrumentoServiceImpl service = new InstrumentoServiceImpl();

	/**
	 * Declaração das referencias da tableView
	 */
	private void init() {
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomeCol.setCellValueFactory(new PropertyValueFactory<>("nome"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		valorCol.setCellValueFactory(new PropertyValueFactory<>("valor"));
		dataCompraCol.setCellValueFactory(new PropertyValueFactory<>("dataCompra"));
		quantidadeCol.setCellValueFactory(new PropertyValueFactory<>("quantidadeCompra"));
	}

	/**
	 * Método função lambda, inicialização da aplicação
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		/* Declaração das referencias da tableView */
		init();

		/* Formata a data para BR no table view */
		formatarDataTableView();

		/* Inicialização da listagem dos dados */
		run();

		/* Botoões de 'CRUD' da aplicação */

		/* Ação de quando o botão 'Incluir' e selecioado */
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

		/* Ação de quando o botão 'Excluir' e selecioado */
		btnExcluir.setOnAction(event -> {
			Instrumento instrumento = new Instrumento();

			try {
				instrumento = buscarLinhaSelecionada();

				if (instrumento == null) {
					throw new InstrumentoException("Instrumento não selecionado.");
				} else {
					/* Exclui o objeto selecionado do banco de dados */
					service.excluir(instrumento.getId());
				}
				run();
			} catch (InstrumentoException e) {
				alertarUsuario(e.getMsg());
			}
		});

		/* Ações que auxilia a area de texto e auxilia os botões. */

		/* Observador do campo de data, o mesmo formata a data no padrão BR */
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

		/* Eventos de click na tableview */
		tvLista.setOnMouseClicked(event -> {
			Instrumento instrumento = new Instrumento();
			instrumento = buscarLinhaSelecionada();
			preencherVisaoEntidade(instrumento);
		});

		
		// -----------------------------------------------------------------------------------------

		/* Ação de quando o botão 'Alterar' e selecioado */
		btnAlterar.setOnAction(event -> {
			Instrumento instrumento = new Instrumento();
			try {

				service.alterar(instrumento);

				limparCampos();
				run();
			} catch (InstrumentoException e) {
				alertarUsuario(e.getMsg());
			}
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

		if (txtNome.getText().isEmpty()) {
			throw new InstrumentoException("Nome não pode está vázio.");
		} else {
			instrumento.setNome(txtNome.getText());
		}

		if (txtEmail.getText().isEmpty()) {
			throw new InstrumentoException("E-mail não pode está vázio.");
		} else {
			instrumento.setEmail(txtEmail.getText());
		}

		if (txtValor.getText().isEmpty()) {
			throw new InstrumentoException("Valor não pode está vázio.");
		} else {
			try {
				valorInstrumento = Float.parseFloat(txtValor.getText());
				instrumento.setValor(valorInstrumento);
			} catch (Exception e) {
				throw new InstrumentoException("Erro ao converter valor.");
			}
		}

		if (txtDataCompra.getText().isEmpty()) {
			throw new InstrumentoException("Data não pode está vázio.");
		} else {
			instrumento.setDataCompra(formatarDataTipoDate(txtDataCompra.getText()));
		}

		if (txtQuantidade.getText().isEmpty()) {
			throw new InstrumentoException("Quantidade não pode está vázio.");

		} else {
			try {
				quantidadeInstrumento = Integer.parseInt(txtQuantidade.getText());
				instrumento.setQuantidadeCompra(quantidadeInstrumento);
			} catch (Exception e) {
				throw new InstrumentoException("Erro ao converter quantidade.");
			}
		}

		return instrumento;
	}

	/**
	 * Método que auxília formatar data do Campo 'Data Compra'
	 * 
	 * @param data
	 * @return Date
	 * @throws InstrumentoException, caso exista algum erro ao preencher instrumento
	 */
	private Date formatarDataTipoDate(String data) throws InstrumentoException {
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
	 * Metodo que auxilia na formatacao da data da table view
	 */
	private void formatarDataTableView() {
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

	/**
	 * Método que retorna instrumento selecionado na tableview
	 * 
	 * @return Retorna instrumento preenchido
	 */
	private Instrumento buscarLinhaSelecionada() {
		ObservableList<Instrumento> linhaSelecionada;
		Instrumento instrumento = new Instrumento();

		/* Devolve a linha selecionada */
		linhaSelecionada = tvLista.getSelectionModel().getSelectedItems();

		/* Insiro a linha selecionada no modelo */
		instrumento = linhaSelecionada.get(0);
		return instrumento;
	}

	/**
	 * Método que auxília em limpar os campos da visão
	 */
	private void limparCampos() {
		txtId.setText("");
		txtNome.setText("");
		txtEmail.setText("");
		txtValor.setText("");
		txtDataCompra.setText("");
		txtQuantidade.setText("");
	}

	/**
	 * Método que preenche a visão qunado e selecionado um item na tableview
	 * 
	 * @param instrumento
	 */
	private void preencherVisaoEntidade(Instrumento instrumento) {
		txtId.setText(instrumento.getId().toString());
		txtNome.setText(instrumento.getNome().toString());
		txtEmail.setText(instrumento.getEmail().toString());
		txtValor.setText(instrumento.getValor().toString());
		txtDataCompra.setText(instrumento.getDataCompra().toString());
		txtQuantidade.setText(instrumento.getQuantidadeCompra().toString());
	}
}