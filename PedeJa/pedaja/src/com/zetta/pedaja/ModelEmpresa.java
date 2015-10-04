package com.zetta.pedaja;

import java.util.Date;

public class ModelEmpresa {

	private int EmpresaId;
	private String RazaoSocial;
	private String NomeFantasia;
	private String CNPJ;
	private int CidadeId;
	private String Logradouro;
	private String Numero;
	private String Complemento;
	private String Bairro;
	private String Cep;
	private String Latitude;
	private String Longitude;
	private int EmpresaMatrizId;
	private Date DataCriacao;
	private String TipoImagem;
	private Date AtualizacaoCardapio;
	private Date UltimaAtu;
	private Date UltimaAtuCardapio;
	private int EmpresaIdLocal;
	private String ServidorLocal;
	private String MixProduto;
	
	public Date getUltimaAtuCardapio() {
		return UltimaAtuCardapio;
	}
	public void setUltimaAtuCardapio(Date ultimaAtuCardapio) {
		UltimaAtuCardapio = ultimaAtuCardapio;
	}
	public Date getUltimaAtu() {
		return UltimaAtu;
	}
	public void setUltimaAtu(Date ultimaAtu) {
		UltimaAtu = ultimaAtu;
	}
	public int getEmpresaId() {
		return EmpresaId;
	}
	public void setEmpresaId(int empresaId) {
		EmpresaId = empresaId;
	}
	public String getRazaoSocial() {
		return RazaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		RazaoSocial = razaoSocial;
	}
	public String getNomeFantasia() {
		return NomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		NomeFantasia = nomeFantasia;
	}
	public String getCNPJ() {
		return CNPJ;
	}
	public void setCNPJ(String cNPJ) {
		CNPJ = cNPJ;
	}
	public int getCidadeId() {
		return CidadeId;
	}
	public void setCidadeId(int cidadeId) {
		CidadeId = cidadeId;
	}
	public String getLogradouro() {
		return Logradouro;
	}
	public void setLogradouro(String logradouro) {
		Logradouro = logradouro;
	}
	public String getNumero() {
		return Numero;
	}
	public void setNumero(String numero) {
		Numero = numero;
	}
	public String getComplemento() {
		return Complemento;
	}
	public void setComplemento(String complemento) {
		Complemento = complemento;
	}
	public String getBairro() {
		return Bairro;
	}
	public void setBairro(String bairro) {
		Bairro = bairro;
	}
	public String getCep() {
		return Cep;
	}
	public void setCep(String cep) {
		Cep = cep;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public int getEmpresaMatrizId() {
		return EmpresaMatrizId;
	}
	public void setEmpresaMatrizId(int empresaMatrizId) {
		EmpresaMatrizId = empresaMatrizId;
	}
	public Date getDataCriacao() {
		return DataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		DataCriacao = dataCriacao;
	}
	public String getNomeImagem() {
		return  EmpresaId + "." + TipoImagem;
	}

	public String getCaminhoImagem() {
		return "/.PedeJa/Logos/";
	}
	
	public String getTipoImagem() {
		return TipoImagem;
	}
	public void setTipoImagem(String tipoImagem) {
		TipoImagem = tipoImagem;
	}
	public Date getAtualizacaoCardapio() {
		return AtualizacaoCardapio;
	}
	public void setAtualizacaoCardapio(Date atualizacaoCardapio) {
		AtualizacaoCardapio = atualizacaoCardapio;
	}	

	public String getCaminhoDestaqueImagem() {
		return "/.PedeJa/Destaque/" + EmpresaId + "/";
	}
	public int getEmpresaIdLocal() {
		return EmpresaIdLocal;
	}
	public void setEmpresaIdLocal(int empresaIdLocal) {
		EmpresaIdLocal = empresaIdLocal;
	}
	public String getServidorLocal() {
		return ServidorLocal;
	}
	public void setServidorLocal(String servidorLocal) {
		ServidorLocal = servidorLocal;
	}
	public String getMixProduto() {
		return MixProduto;
	}
	public void setMixProduto(String mixProduto) {
		MixProduto = mixProduto;
	}
	
	
	
	
}
