package com.zetta.pedaja;

import java.util.Date;

public class ModelConta {
	
	private int ContaId;
	private int EmpresaId;
	private int MesaId;
	private int UsuarioId;
	private int GarcomId;
	private Date DataAbertura;
	private Date DataEncerramento;
	private Date DataBaixa;
	private int ModoPagamentoId;
	private String CodigoAbertura;
	
	public void setCodigoAbertura(String codigoAbertura) {
		CodigoAbertura = codigoAbertura;
	}
	
	public int getGarcomId() {
		return GarcomId;
	}
	public String getCodigoAbertura() {
		return CodigoAbertura;
	}
	public int getContaId() {
		return ContaId;
	}
	public void setContaId(int contaId) {
		ContaId = contaId;
	}
	public int getEmpresaId() {
		return EmpresaId;
	}
	public void setEmpresaId(int empresaId) {
		EmpresaId = empresaId;
	}
	public int getMesaId() {
		return MesaId;
	}
	public void setMesaId(int mesaId) {
		MesaId = mesaId;
	}
	public int getUsuarioId() {
		return UsuarioId;
	}
	public void setUsuarioId(int usuarioId) {
		UsuarioId = usuarioId;
	}
	public Date getDataAbertura() {
		return DataAbertura;
	}
	public void setDataAbertura(Date dataAbertura) {
		DataAbertura = dataAbertura;
	}
	public Date getDataEncerramento() {
		return DataEncerramento;
	}
	public void setDataEncerramento(Date dataEncerramento) {
		DataEncerramento = dataEncerramento;
	}
	public Date getDataBaixa() {
		return DataBaixa;
	}
	public void setDataBaixa(Date dataBaixa) {
		DataBaixa = dataBaixa;
	}
	public int getModoPagamentoId() {
		return ModoPagamentoId;
	}
	public void setModoPagamentoId(int modoPagamentoId) {
		ModoPagamentoId = modoPagamentoId;
	}
	
	
	

}
