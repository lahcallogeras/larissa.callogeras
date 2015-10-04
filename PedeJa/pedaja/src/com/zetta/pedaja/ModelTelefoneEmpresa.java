package com.zetta.pedaja;

public class ModelTelefoneEmpresa {

	private int TelefoneEmpresaId;
	private int EmpresaId;
	private String DDD;
	private String Telefone;
	private String Descricao;
	
	public int getTelefoneEmpresaId() {
		return TelefoneEmpresaId;
	}
	public void setTelefoneEmpresaId(int telefoneEmpresaId) {
		TelefoneEmpresaId = telefoneEmpresaId;
	}
	public int getEmpresaId() {
		return EmpresaId;
	}
	public void setEmpresaId(int empresaId) {
		EmpresaId = empresaId;
	}
	public String getDDD() {
		return DDD;
	}
	public void setDDD(String dDD) {
		DDD = dDD;
	}
	public String getTelefone() {
		return Telefone;
	}
	public void setTelefone(String telefone) {
		Telefone = telefone;
	}
	public String getDescricao() {
		return Descricao;
	}
	public void setDescricao(String descricao) {
		Descricao = descricao;
	}
	
	 	
}
