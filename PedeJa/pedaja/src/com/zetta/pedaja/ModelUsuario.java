package com.zetta.pedaja;

import java.util.Date;


public class ModelUsuario {

	private int UsuarioId;
	private int CidadeId;	
	private String Nome;
	private String Email;
	private Date DataNascimento;
	private String CPF;
	private Date   DataCriacao;
	private Date UltimaAtu;
	private String Usuario;
	private String Senha;
	private Date UltimoAcesso;
	
	public Date getUltimoAcesso() {
		return UltimoAcesso;
	}
	public void setUltimoAcesso(Date ultimoAcesso) {
		UltimoAcesso = ultimoAcesso;
	}
	public int getUsuarioId() {
		return UsuarioId;
	}
	public void setUsuarioId(int usuarioId) {
		UsuarioId = usuarioId;
	}
	public int getCidadeId() {
		return CidadeId;
	}
	public void setCidadeId(int cidadeId) {
		CidadeId = cidadeId;
	}
	public String getNome() {
		return Nome;
	}
	public void setNome(String nome) {
		Nome = nome;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public Date getDataNascimento() {
		return DataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		DataNascimento = dataNascimento;
	}
	public String getCPF() {
		return CPF;
	}
	public void setCPF(String cPF) {
		CPF = cPF;
	}
	public Date getDataCriacao() {
		return DataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		DataCriacao = dataCriacao;
	}
	public Date getUltimaAtu() {
		return UltimaAtu;
	}
	public void setUltimaAtu(Date ultimaAtu) {
		UltimaAtu = ultimaAtu;
	}
	public String getUsuario() {
		return Usuario;
	}
	public void setUsuario(String usuario) {
		Usuario = usuario;
	}
	public String getSenha() {
		return Senha;
	}
	public void setSenha(String senha) {
		Senha = senha;
	}
	
	
	
	
}
