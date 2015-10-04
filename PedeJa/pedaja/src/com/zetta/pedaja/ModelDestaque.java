package com.zetta.pedaja;

import java.util.Date;

public class ModelDestaque {
	
	private int DestaqueId;
	private Date InicioVigencia;
	private Date FimVigencia;
	private String TipoImagem;
	private int EmpresaId;
	private String Descricao;
	
	public String getNomeImagem() {
		return  DestaqueId + "." + TipoImagem;
	}
	
	public int getDestaqueId() {
		return DestaqueId;
	}
	public void setDestaqueId(int destaqueId) {
		DestaqueId = destaqueId;
	}
	public Date getInicioVigencia() {
		return InicioVigencia;
	}
	public void setInicioVigencia(Date inicioVigencia) {
		InicioVigencia = inicioVigencia;
	}
	public Date getFimVigencia() {
		return FimVigencia;
	}
	public void setFimVigencia(Date fimVigencia) {
		FimVigencia = fimVigencia;
	}
	public String getTipoImagem() {
		return TipoImagem;
	}
	public void setTipoImagem(String tipoImagem) {
		TipoImagem = tipoImagem;
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
	
	
	
}
