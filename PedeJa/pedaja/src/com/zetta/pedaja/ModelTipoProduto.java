package com.zetta.pedaja;

public class ModelTipoProduto {
	
	private int TipoProdutoId;
	private int EmpresaId;
	private int ProdutoId;
	private String Descricao;
	private double Valor;
	private double Porcentagem;
	private String Tipo;
	private String Tamanho;
	private int Visivel;
	private int Contabil;
	private Boolean Selecionado;
	
	
	
	public int getContabil() {
		return Contabil;
	}
	public void setContabil(int contabil) {
		Contabil = contabil;
	}
	public Boolean getSelecionado() {
		return Selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		Selecionado = selecionado;
	}
	public int getTipoProdutoId() {
		return TipoProdutoId;
	}
	public void setTipoProdutoId(int tipoProdutoId) {
		TipoProdutoId = tipoProdutoId;
	}
	public int getEmpresaId() {
		return EmpresaId;
	}
	public void setEmpresaId(int empresaId) {
		EmpresaId = empresaId;
	}
	public int getProdutoId() {
		return ProdutoId;
	}
	public void setProdutoId(int produtoId) {
		ProdutoId = produtoId;
	}
	public String getDescricao() {
		return Descricao;
	}
	public void setDescricao(String descricao) {
		Descricao = descricao;
	}
	public double getValor() {
		return Valor;
	}
	public void setValor(double valor) {
		Valor = valor;
	}
	public double getPorcentagem() {
		return Porcentagem;
	}
	public void setPorcentagem(double porcentagem) {
		Porcentagem = porcentagem;
	}
	public String getTipo() {
		return Tipo;
	}
	public void setTipo(String tipo) {
		Tipo = tipo;
	}
	public int getVisivel() {
		return Visivel;
	}
	public void setVisivel(int visivel) {
		Visivel = visivel;
	}
	public String getTamanho() {
		return Tamanho;
	}
	public void setTamanho(String tamanho) {
		Tamanho = tamanho;
	}
	
	
	

}
