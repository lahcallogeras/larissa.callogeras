package com.zetta.pedaja;

public class ModelItemDestaque {

	private int ItensDestaqueId;
	private int DestaqueId;
	private int EmpresaId;
	private int TipoProdutoId;
	private double ValorProduto;
	
	
	
	public double getValorProduto() {
		return ValorProduto;
	}
	public void setValorProduto(double valorProduto) {
		ValorProduto = valorProduto;
	}
	public int getItensDestaqueId() {
		return ItensDestaqueId;
	}
	public void setItensDestaqueId(int itensDestaqueId) {
		ItensDestaqueId = itensDestaqueId;
	}
	public int getDestaqueId() {
		return DestaqueId;
	}
	public void setDestaqueId(int destaqueId) {
		DestaqueId = destaqueId;
	}
	public int getEmpresaId() {
		return EmpresaId;
	}
	public void setEmpresaId(int empresaId) {
		EmpresaId = empresaId;
	}
	public int getTipoProdutoId() {
		return TipoProdutoId;
	}
	public void setTipoProdutoId(int tipoProdutoId) {
		TipoProdutoId = tipoProdutoId;
	}
	
	
}
