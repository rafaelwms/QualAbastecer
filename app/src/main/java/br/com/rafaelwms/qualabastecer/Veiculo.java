package br.com.rafaelwms.qualabastecer;
import java.io.Serializable;



public class Veiculo implements Serializable{

	private Usuario usuario;
	private int id;
	private String nome;
	private int tipo;
	private int cor;
	private int combustivel;
	
	
	
	public Veiculo(){}
	
	public Veiculo(String nome, int tipo, int cor, int combustivel){
		this.setNome(nome);
		this.setTipo(tipo);
		this.setCor(cor);
		this.setCombustivel(combustivel);
	}
	
	public Veiculo(int id, String nome, int tipo, int cor, int combustivel){
		this.setId(id);
		this.setNome(nome);
		this.setTipo(tipo);
		this.setCor(cor);
		this.setCombustivel(combustivel);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getCor() {
		return cor;
	}

	public void setCor(int cor) {
		this.cor = cor;
	}

	public int getCombustivel() {
		return combustivel;
	}

	public void setCombustivel(int combustivel) {
		this.combustivel = combustivel;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String toString(){
		return this.getNome();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
