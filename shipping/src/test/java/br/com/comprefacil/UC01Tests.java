package br.com.comprefacil;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.comprefacil.dto.EnderecoTO;
import br.com.comprefacil.dto.FreteTO;
import br.com.comprefacil.exception.CepInvalidoException;
import br.com.comprefacil.exception.CorreiosException;
import br.com.comprefacil.factory.EnderecoFactory;
import br.com.comprefacil.service.EnderecoService;
import br.com.comprefacil.service.FreteService;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

public class UC01Tests {

	@Rule 
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(8090);

	private FreteService freteService = null;

	@Before
	public void setUp(){
		freteService = EnderecoFactory.getFreteInstance();
	}

	@Test
	public void testBasico() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0,5")
			.setCodigoFormato(1)
			.setComprimento(10)
			.setAltura(10)
			.setLargura(10)
			.setDiametro(17.32)
			.setMaoPropria("N")
			.setValorDeclarado(0)
			.setAvisoRecebimento("N");
	    
		stubFor(get(urlEqualTo("/correios/frete?nCdEmpresa=" + frete.getCodigoEmpresa()
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
				+ "&sCdAvisoRecebimento=" + frete.getAvisoRecebimento()))
	            .willReturn(aResponse()
	                .withStatus(200)
	                .withHeader("Content-Type", "application/json")
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10,50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0,00\", \"ValorAvisoRecebimento\": \"0,00\", \"ValorValorDeclarado\": \"0,00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"0\", \"MsgErro\": \"\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(String.valueOf(frete.getValor()).equals("10,50"));
		assertTrue(String.valueOf(frete.getPrazo()).equals("1"));
	}
}