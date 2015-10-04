package com.zetta.pedaja;

public class ModelProduto {
	
	private int ProdutoId;
	private int EmpresaId;
	private int CategoriaId;
	private String Descricao;
	private String Detalhe;
	
	
	
	public String getDetalhe() {
		return Detalhe;
	}
	public void setDetalhe(String detalhe) {
		Detalhe = detalhe;
	}
	public int getProdutoId() {
		return ProdutoId;
	}
	public void setProdutoId(int produtoId) {
		ProdutoId = produtoId;
	}
	public int getEmpresaId() {
		return EmpresaId;
	}
	public void setEmpresaId(int empresaId) {
		EmpresaId = empresaId;
	}
	public int getCategoriaId() {
		return CategoriaId;
	}
	public void setCategoriaId(int categoriaId) {
		CategoriaId = categoriaId;
	}
	public String getDescricao() {
		return Descricao;
	}
	public void setDescricao(String descricao) {
		Descricao = descricao;
	}
	
	
}




