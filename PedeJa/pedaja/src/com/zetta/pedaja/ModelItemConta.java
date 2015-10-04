package com.zetta.pedaja;

import java.util.Date;

public class ModelItemConta {
	
	private int ItensContaId;
	private int ContaId;
	private Date DataPedido;
	private int TipoProdutoId;
	private int Sequencia;
	private double Valor;
	private String StatusPedido;
	private String DescricaoProduto;
	private int QuantidadeProduto;
	private String DescricaoTipoProduto;
	private Date DataBaixa;
	private int UsuarioId;
	private int GarcomId;
	private String Divisao;
	private String DescricaoCategoria;
	private String Tipo;
		
	
	public int getGarcomId() {
		return GarcomId;
	}
	public void setGarcomId(int garcomId) {
		GarcomId = garcomId;
	}
	public int getSequencia() {
		return Sequencia;
	}
	public void setSequencia(int sequencia) {
		Sequencia = sequencia;
	}
	public Date getDataBaixa() {
		return DataBaixa;
	}
	public void setDataBaixa(Date dataBaixa) {
		DataBaixa = dataBaixa;
	}
	public String getDescricaoTipoProduto() {
		return DescricaoTipoProduto;
	}
	public void setDescricaoTipoProduto(String descricaoTipoProduto) {
		DescricaoTipoProduto = descricaoTipoProduto;
	}
	public String getDescricaoProduto() {
		return DescricaoProduto;
	}
	public void setDescricaoProduto(String descricaoProduto) {
		DescricaoProduto = descricaoProduto;
	}
	public int getQuantidadeProduto() {
		return QuantidadeProduto;
	}
	public void setQuantidadeProduto(int quantidadeProduto) {
		QuantidadeProduto = quantidadeProduto;
	}
	public int getItensContaId() {
		return ItensContaId;
	}
	public void setItensContaId(int itensContaId) {
		ItensContaId = itensContaId;
	}
	public int getContaId() {
		return ContaId;
	}
	public void setContaId(int contaId) {
		ContaId = contaId;
	}
	public Date getDataPedido() {
		return DataPedido;
	}
	public void setDataPedido(Date dataPedido) {
		DataPedido = dataPedido;
	}
	public int getTipoProdutoId() {
		return TipoProdutoId;
	}
	public void setTipoProdutoId(int tipoProdutoId) {
		TipoProdutoId = tipoProdutoId;
	}
	public double getValor() {
		return Valor;
	}
	public void setValor(double valor) {
		Valor = valor;
	}
	public String getStatusPedido() {
		return StatusPedido;
	}
	public void setStatusPedido(String statusPedido) {
		StatusPedido = statusPedido;
	}
	public int getUsuarioId() {
		return UsuarioId;
	}
	public void setUsuarioId(int usuarioId) {
		UsuarioId = usuarioId;
	}
	public String getDivisao() {
		return Divisao;
	}
	public void setDivisao(String divisao) {
		Divisao = divisao;
	}
	public String getDescricaoCategoria() {
		return DescricaoCategoria;
	}
	public void setDescricaoCategoria(String descricaoCategoria) {
		DescricaoCategoria = descricaoCategoria;
	}
	public String getTipo() {
		return Tipo;
	}
	public void setTipo(String tipo) {
		Tipo = tipo;
	}
	
	
	 

}
