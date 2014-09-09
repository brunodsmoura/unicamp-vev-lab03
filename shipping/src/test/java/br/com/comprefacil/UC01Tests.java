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

import com.github.tomakehurst.wiremock.http.Fault;
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
	public void testSemErros() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0.5")
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"0\", \"MsgErro\": \"Processamento com sucesso\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(String.valueOf(frete.getValor()).equals("10.5"));
		assertTrue(String.valueOf(frete.getPrazo()).equals("1"));
		assertTrue(frete.getErro() == 0);
	}
	
	@Test
	public void testCEPInvalido() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0")
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"0\", \"PrazoEntrega\": \"0\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-3\", \"MsgErro\": \"CEP de destino invalido\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -3);
	}
	
	@Test
	public void testCodigoServicoInvalido() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("0")
			.setCepDestino("04538132")
			.setPeso("0.5")
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"0\", \"PrazoEntrega\": \"0\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-1\", \"MsgErro\": \"Código de servico invalido\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -1);
	}
	
	@Test
	public void testPesoExcedido() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("999999999")
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-4\", \"MsgErro\": \"Peso excedido\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -4);
	}
	
	@Test
	public void testLarguraInvalida() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0.5")
			.setCodigoFormato(1)
			.setComprimento(10)
			.setAltura(10)
			.setLargura(0)
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-13\", \"MsgErro\": \"Largura invalida\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -13);
	}
	
	@Test
	public void testAlturaInvalida() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0.5")
			.setCodigoFormato(1)
			.setComprimento(10)
			.setAltura(0)
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-14\", \"MsgErro\": \"Altura invalida\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -14);
	}
	
	@Test
	public void testComprimentoInvalido() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0.5")
			.setCodigoFormato(1)
			.setComprimento(0)
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-24\", \"MsgErro\": \"Comprimento invalido\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -24);
	}
	
	@Test
	public void testDiametroInvalido() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0.5")
			.setCodigoFormato(1)
			.setComprimento(10)
			.setAltura(10)
			.setLargura(10)
			.setDiametro(0)
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-25\", \"MsgErro\": \"Diametro invalido\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -25);
	}
	
	@Test
	public void testServicoIndisponivel() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0.5")
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"-33\", \"MsgErro\": \"Sistema temporariamente fora do ar\" }")));

		frete = freteService.calcPrecoPrazo(frete);
		
		assertNotNull(frete);
		assertTrue(frete.getErro() == -33);
	}
	
	@Test(expected=org.apache.http.MalformedChunkCodingException.class)
	public void testMalformedChunkCoding() throws Exception {
		FreteTO frete = new FreteTO();
		frete.setCodigoServico("40010")
			.setCepDestino("04538132")
			.setPeso("0.5")
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
	                .withBody("{ \"Codigo\": \"0\", \"Valor\": \"10.50\", \"PrazoEntrega\": \"1\", \"ValorMaoPropria\": \"0.00\", \"ValorAvisoRecebimento\": \"0.00\", \"ValorValorDeclarado\": \"0.00\", \"EntregaDomiciliar\": \"S\", \"EntregaSabado\": \"S\", \"Erro\": \"0\", \"MsgErro\": \"\" }")
					.withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

		frete = freteService.calcPrecoPrazo(frete);
	}
}