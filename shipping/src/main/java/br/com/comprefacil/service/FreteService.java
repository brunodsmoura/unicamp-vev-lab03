package br.com.comprefacil.service;

import org.apache.http.MalformedChunkCodingException;

import br.com.comprefacil.dto.FreteTO;
import br.com.comprefacil.exception.CorreiosException;

public interface FreteService {
	public FreteTO calcPrecoPrazo(FreteTO frete) throws CorreiosException, MalformedChunkCodingException;
}