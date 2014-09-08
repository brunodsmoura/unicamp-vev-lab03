package br.com.comprefacil.service.impl;

import org.json.JSONObject;

import br.com.comprefacil.call.HttpFetcher;
import br.com.comprefacil.dto.FreteTO;
import br.com.comprefacil.exception.CorreiosException;
import br.com.comprefacil.service.CorreiosService;

public class StubCorreiosServiceImpl implements CorreiosService {
	
	private HttpFetcher instance = null;

	public StubCorreiosServiceImpl(){ instance = new HttpFetcher(); }
	
	public JSONObject recuperarEndereco(String cep) throws CorreiosException {
		JSONObject enderecoJSON = null;

		try {
			String endereco = instance.fetchAsString("http://localhost:8090/correios/enderecos?cep=" + cep);
			enderecoJSON = new JSONObject(endereco);
		}catch(Exception cause){
			cause.printStackTrace();
		}

		if(enderecoJSON != null && enderecoJSON.has("erro")){
			Integer errorCode = -1;
			
			try{
				errorCode = enderecoJSON.getInt("erro");
			}catch(Exception cause){
				cause.printStackTrace();
			}
			
			throw new CorreiosException(String.format("Codigo de Erro dos Correios: %d", errorCode), errorCode);
		}

		return enderecoJSON;
	}

	public JSONObject calcPrecoPrazo(FreteTO frete) throws CorreiosException {
		JSONObject freteJSON = null;

		try {
			String fretePrazo = instance.fetchAsString("http://localhost:8090/correios/frete?nCdEmpresa=" + frete.getCodigoEmpresa()
					+ "&sDsSenha=" + frete.getSenha()
					+ "&nCdServico=" + frete.getCodigoServico()
					+ "&sCepOrigem=" + frete.getCepOrigem()
					+ "&sCepDestino=" + frete.getCepDestino()
					+ "&nVlPeso=" + frete.getPeso()
					+ "&nCdFormato=" + frete.getCodigoFormato()
					+ "&nVlComprimento=" + frete.getComprimento()
					+ "&nVlAltura=" + frete.getAltura()
					+ "&nVlLargura=" + frete.getLargura()
					+ "&nVlDiametro=" + frete.getDiametro()
					+ "&sCdMaoPropria=" + frete.getMaoPropria()
					+ "&nVlValorDeclarado=" + frete.getValorDeclarado()
					+ "&sCdAvisoRecebimento=" + frete.getAvisoRecebimento()
					);
			freteJSON = new JSONObject(fretePrazo);
		}catch(Exception cause){
			cause.printStackTrace();
		}

		if(freteJSON != null && freteJSON.has("erro")){
			Integer errorCode = -1;
			
			try{
				errorCode = freteJSON.getInt("erro");
			}catch(Exception cause){
				cause.printStackTrace();
			}
			
			throw new CorreiosException(String.format("Codigo de Erro dos Correios: %d", errorCode), errorCode);
		}

		return freteJSON;
	}
}