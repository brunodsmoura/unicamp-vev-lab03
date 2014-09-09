package br.com.comprefacil.service.impl;

import java.util.regex.Pattern;

import org.apache.http.MalformedChunkCodingException;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import br.com.comprefacil.dto.FreteTO;
import br.com.comprefacil.exception.CorreiosException;
import br.com.comprefacil.factory.EnderecoFactory;
import br.com.comprefacil.service.FreteService;
import br.com.comprefacil.service.CorreiosService;

/**
 * Default implementation for the EnderecoService.
 * @author espsoft06
 */
public class FreteServiceImpl implements FreteService {
	
	private static Logger logger = Logger.getLogger(FreteServiceImpl.class);

	public FreteTO calcPrecoPrazo(FreteTO frete)
		throws CorreiosException, MalformedChunkCodingException {

		CorreiosService correiosService = EnderecoFactory.getCorreiosInstance();
		JSONObject retornoWsCorreios = correiosService.calcPrecoPrazo(frete);

		try{
			 frete.setValor(Double.parseDouble(retornoWsCorreios.getString("Valor")));
			 frete.setPrazo(Integer.parseInt(retornoWsCorreios.getString("PrazoEntrega")));
			 frete.setErro(Integer.parseInt(retornoWsCorreios.getString("Erro")));
			 frete.setMensagemErro(retornoWsCorreios.getString("MsgErro"));
			 
			 logger.debug("Preço e prazo calculado: " + frete);
		}catch(Exception cause){
			cause.printStackTrace();
		}
		
		return frete;
	}

	
}