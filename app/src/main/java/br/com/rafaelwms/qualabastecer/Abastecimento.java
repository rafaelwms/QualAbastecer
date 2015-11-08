package br.com.rafaelwms.qualabastecer;

import java.io.Serializable;


public class Abastecimento implements Serializable{
	
	private int id;
	private String data;
	private Veiculo carro;
	private Posto posto;
	private int combustivel;
	private double valorPago;
	private double litros;
	private double kilometragem;
	
	public Abastecimento(){}
	
	public Abastecimento(String data, Veiculo carro, Posto posto, int combustivel, double valorPago, double litros){
		this.setData(data);
		this.setCarro(carro);
		this.setPosto(posto);
		this.setCombustivel(combustivel);
		this.setValorPago(valorPago);
		this.setLitros(litros);
	}
	
	public Abastecimento(String data, Veiculo carro, Posto posto, int combustivel, double valorPago, double litros, double km){
		this.setData(data);
		this.setCarro(carro);
		this.setPosto(posto);
		this.setCombustivel(combustivel);
		this.setValorPago(valorPago);
		this.setLitros(litros);
		this.setKilometragem(km);
	}
	
	public Abastecimento(int id, String data, Veiculo carro, Posto posto, int combustivel, double valorPago, double litros, double km){
		this.setId(id);
		this.setData(data);
		this.setCarro(carro);
		this.setPosto(posto);
		this.setCombustivel(combustivel);
		this.setValorPago(valorPago);
		this.setLitros(litros);
		this.setKilometragem(km);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Veiculo getCarro() {
		return carro;
	}
	public void setCarro(Veiculo carro) {
		this.carro = carro;
	}
	public Posto getPosto() {
		return posto;
	}
	public void setPosto(Posto posto) {
		this.posto = posto;
	}
	public int getCombustivel() {
		return combustivel;
	}
	public void setCombustivel(int combustivel) {
		this.combustivel = combustivel;
	}
	public double getValorPago() {
		return valorPago;
	}
	public void setValorPago(double valorPago) {
		this.valorPago = valorPago;
	}
	public double getLitros() {
		return litros;
	}
	public void setLitros(double litros) {
		this.litros = litros;
	}

	public double getKilometragem() {
		return kilometragem;
	}

	public void setKilometragem(double kilometragem) {
		this.kilometragem = kilometragem;
	}

}
