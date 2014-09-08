package br.com.comprefacil.dto;

public class FreteTO {
	private String codigoEmpresa;
	private String senha;
	private String codigoServico;
	private String cepOrigem;
	private String cepDestino;
	private String peso;
	private int codigoFormato;
	private double comprimento;
	private double altura;
	private double largura;
	private double diametro;
	private String maoPropria;
	private double valorDeclarado;
	private String avisoRecebimento;
	
	private double valor;
	private int prazo;
	
	public FreteTO() {
		this.codigoEmpresa = "0";
		this.senha = "0";
		this.cepOrigem = "13083970";
		this.maoPropria = "N";
		this.valorDeclarado = 0;
		this.avisoRecebimento = "N";
	}

	public String getCodigoEmpresa() {
		return codigoEmpresa;
	}

	public FreteTO setCodigoEmpresa(String codigoEmpresa) {
		this.codigoEmpresa = codigoEmpresa;
		return this;
	}

	public String getSenha() {
		return senha;
	}

	public FreteTO setSenha(String senha) {
		this.senha = senha;
		return this;
	}

	public String getCodigoServico() {
		return codigoServico;
	}

	public FreteTO setCodigoServico(String codigoServico) {
		this.codigoServico = codigoServico;
		return this;
	}

	public String getCepOrigem() {
		return cepOrigem;
	}

	public FreteTO setCepOrigem(String cepOrigem) {
		this.cepOrigem = cepOrigem;
		return this;
	}

	public String getCepDestino() {
		return cepDestino;
	}

	public FreteTO setCepDestino(String cepDestino) {
		this.cepDestino = cepDestino;
		return this;
	}

	public String getPeso() {
		return peso;
	}

	public FreteTO setPeso(String peso) {
		this.peso = peso;
		return this;
	}

	public int getCodigoFormato() {
		return codigoFormato;
	}

	public FreteTO setCodigoFormato(int codigoFormato) {
		this.codigoFormato = codigoFormato;
		return this;
	}

	public double getComprimento() {
		return comprimento;
	}

	public FreteTO setComprimento(double comprimento) {
		this.comprimento = comprimento;
		return this;
	}

	public double getAltura() {
		return altura;
	}

	public FreteTO setAltura(double altura) {
		this.altura = altura;
		return this;
	}

	public double getLargura() {
		return largura;
	}

	public FreteTO setLargura(double largura) {
		this.largura = largura;
		return this;
	}

	public double getDiametro() {
		return diametro;
	}

	public FreteTO setDiametro(double diametro) {
		this.diametro = diametro;
		return this;
	}

	public String getMaoPropria() {
		return maoPropria;
	}

	public FreteTO setMaoPropria(String maoPropria) {
		this.maoPropria = maoPropria;
		return this;
	}

	public double getValorDeclarado() {
		return valorDeclarado;
	}

	public FreteTO setValorDeclarado(double valorDeclarado) {
		this.valorDeclarado = valorDeclarado;
		return this;
	}

	public String getAvisoRecebimento() {
		return avisoRecebimento;
	}

	public FreteTO setAvisoRecebimento(String avisoRecebimento) {
		this.avisoRecebimento = avisoRecebimento;
		return this;
	}

	public double getValor() {
		return valor;
	}

	public FreteTO setValor(double valor) {
		this.valor = valor;
		return this;
	}

	public int getPrazo() {
		return prazo;
	}

	public FreteTO setPrazo(int prazo) {
		this.prazo = prazo;
		return this;
	}
	
}

