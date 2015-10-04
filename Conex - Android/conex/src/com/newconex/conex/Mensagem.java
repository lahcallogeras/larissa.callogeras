package com.newconex.conex;

import java.util.Date;

public class Mensagem {
	private int codigoMsg;
	private Contato usuOrig;
	private Contato usuDestino;
	private Date dataMsg;
	private String textoMsg;
	private String statusLida;
	
	
	public int getCodigoMsg() {
		return codigoMsg;
	}
	public void setCodigoMsg(int codigoMsg) {
		this.codigoMsg = codigoMsg;
	}
	public Contato getUsuOrig() {
		return usuOrig;
	}
	public void setUsuOrig(Contato usuOrig) {
		this.usuOrig = usuOrig;
	}
	public Contato getUsuDestino() {
		return usuDestino;
	}
	public void setUsuDestino(Contato usuDestino) {
		this.usuDestino = usuDestino;
	}
	public Date getDataMsg() {
		return dataMsg;
	}
	public void setDataMsg(Date dataMsg) {
		this.dataMsg = dataMsg;
	}
	public String getTextoMsg() {
		return textoMsg;
	}
	public void setTextoMsg(String textoMsg) {
		this.textoMsg = textoMsg;
	}
	public String getStatusLida() {
		return statusLida;
	}
	public void setStatusLida(String statusLida) {
		this.statusLida = statusLida;
	}
	
}
