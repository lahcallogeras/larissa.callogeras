package com.zetta.pedaja;

public class ModelCidade {
	

	private int CidadeId;
	private int EstadoId;
	private String Nome;
	
	public void setCidadeId(int cidadeId) {
		CidadeId = cidadeId;
	}
	public void setEstadoId(int estadoId) {
		EstadoId = estadoId;
	}
	public void setNome(String nome) {
		Nome = nome;
	}
	public int getCidadeId() {
		return CidadeId;
	}
	public int getEstadoId() {
		return EstadoId;
	}
	public String getNome() {
		return Nome;
	}

}
