package entidade;

import java.util.Date;

import util.Coluna;
import util.Tabela;

/**
 * Clasee que representa os dados na persistencia de 'Instrumento'
 * 
 * @author Rafael Marcelo
 * 
 */
@Tabela(nomeTabela = "Instrumento")
public class Instrumento {

	@Coluna(nomeColuna = "id")
	private Integer id;
	@Coluna(nomeColuna = "nome")
	private String nome;
	@Coluna(nomeColuna = "email")
	private String email;
	@Coluna(nomeColuna = "valor")
	private Float valor;
	@Coluna(nomeColuna = "dataCompra")
	private Date dataCompra;
	@Coluna(nomeColuna = "quantidadeCompra")
	private Integer quantidadeCompra;

	// Gerando os GET's e SET's
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Float getValor() {
		return valor;
	}

	public void setValor(Float valor) {
		this.valor = valor;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public Integer getQuantidadeCompra() {
		return quantidadeCompra;
	}

	public void setQuantidadeCompra(Integer quantidadeCompra) {
		this.quantidadeCompra = quantidadeCompra;
	}

	@Override
	public String toString() {
		return "Instrumento [id=" + id + ", nome=" + nome + ", email=" + email + ", valor=" + valor + ", dataCompra="
				+ dataCompra + ", quantidadeCompra=" + quantidadeCompra + "]";
	}
}
