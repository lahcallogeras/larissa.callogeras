package com.zetta.pedaja;

public class ModelMetade {
	
	private String Descricao;
	private String Detalhe;
	private double Valor;
	private double ValorParcial;
	private int TipoProdutoId;
	private int ProdutoId;
	private String Divisao;
	
	public String getDescricao() {
		return Descricao;
	}
	public void setDescricao(String descricao) {
		Descricao = descricao;
	}	
	public String getDetalhe() {
		return Detalhe;
	}
	public void setDetalhe(String detalhe) {
		Detalhe = detalhe;
	}
	public double getValor() {
		return Valor;
	}
	public void setValor(double valor) {
		Valor = valor;
	}
	public int getTipoProdutoId() {
		return TipoProdutoId;
	}
	public void setTipoProdutoId(int tipoProdutoId) {
		TipoProdutoId = tipoProdutoId;
	}
	public int getProdutoId() {
		return ProdutoId;
	}
	public void setProdutoId(int produtoId) {
		ProdutoId = produtoId;
	}
	public String getDivisao() {
		return Divisao;
	}
	public void setDivisao(String divisao) {
		Divisao = divisao;
	}
	public double getValorParcial() {
		return ValorParcial;
	}
	public void setValorParcial(double valorParcial) {
		ValorParcial = valorParcial;
	}
	
	

}
