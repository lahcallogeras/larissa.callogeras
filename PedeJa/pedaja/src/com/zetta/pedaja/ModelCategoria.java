package com.zetta.pedaja;

import java.util.ArrayList;
import java.util.List;

public class ModelCategoria {
	
	private int CategoriaId;
	private int EmpresaId;
	private String Descricao;
	private int MixProduto;
	
	private ArrayList<ModelProduto> lstProduto;

	

	public List<ModelProduto> getLstProduto() {
		return lstProduto;
	}
	public void setLstProduto(ArrayList<ModelProduto> lstProduto) {
		this.lstProduto = lstProduto;
	}
	public int getCategoriaId() {
		return CategoriaId;
	}
	public void setCategoriaId(int categoriaId) {
		CategoriaId = categoriaId;
	}
	public int getEmpresaId() {
		return EmpresaId;
	}
	public void setEmpresaId(int empresaId) {
		EmpresaId = empresaId;
	}
	public String getDescricao() {
		return Descricao;
	}
	public void setDescricao(String descricao) {
		Descricao = descricao;
	}
	public int getMixProduto() {
		return MixProduto;
	}
	public void setMixProduto(int mixProduto) {
		MixProduto = mixProduto;
	}
	
	
	
	 
}
