package br.com.ucb.resource;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ucb.dao.AtividadeDao;
import br.com.ucb.modelo.Atividade;
import br.com.ucb.modelo.Usuario;

import com.google.gson.Gson;

/**
 * Classe do Web Service que recebera as informcoes pertinentes a atividades e realizara as operacoes
 * desejadas na base de dados
 * @author Jean Silvestre
 *
 */
@Path("/atividade")
public class AtividadeResource {
	
	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e inserir esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 */
	@POST
	@Path("/cadastrarAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String cadastrarAtividade(String ativStr) throws Exception{		
		AtividadeDao adao = null;
		Gson gson = new Gson();
		adao = adao.getInstancia();
		
		System.out.println(ativStr);
		
		Atividade ativ = gson.fromJson(ativStr, Atividade.class);
		
		return adao.inserir(ativ) ? "S":"N";
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e alterar esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 */
	@POST
	@Path("/alterarAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String alterarAtividade(String ativStr){		
		AtividadeDao adao = null;
		Atividade atividade = new Atividade();
		Gson gson = new Gson();
		adao = adao.getInstancia();
		
		atividade = gson.fromJson(ativStr, Atividade.class);
		
		return adao.atualizar(atividade)? "S" : "N";
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e excluir esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 */
	@POST
	@Path("/excluirAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String excluirAtividade(String ativStr){		
		AtividadeDao adao = null;
		Atividade atividade = new Atividade();
		Gson gson = new Gson();

		adao = adao.getInstancia();
		
		atividade = gson.fromJson(ativStr, Atividade.class);
		
		return adao.excluir(atividade)?"S":"N";
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e consultar esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 * @return atividade
	 */
	@POST
	@Path("/consultarAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Atividade consultarAtividade(String ativStr){		
		AtividadeDao adao = null;
		Atividade atividade = new Atividade();
		Gson gson = new Gson();

		adao = adao.getInstancia();
		
		atividade = gson.fromJson(ativStr, Atividade.class);
		
		atividade = adao.consultar(atividade);
		
		return atividade;
	}
	
	/**
	 * Metodo responsavel por definir o indice KMA para uma atividade
	 * @param Json String (Atividade)
	 * @return Atividade kma
	 */
	@POST
	@Path("/getKma")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Atividade getKma(String ativStr){
		AtividadeDao adao = null;
		Atividade atividade = null;
		Usuario usuario = new Usuario();
		ArrayList<Atividade> atividades = new ArrayList<Atividade>();
		Gson gson = new Gson();
		
		float paramA = 0; // numero de vezes que disse que teria sucesso e teve
		float paramB = 0; // numero de vezes que disse que nao teria sucesso e teve
		float paramC = 0; // numero de vezes que disse que teria sucesso e não teve
		float paramD = 0; // numero de vezes que disse que nao teria sucesso e nao teve
		
		adao = adao.getInstancia();
		
		atividade = gson.fromJson(ativStr, Atividade.class);
		usuario.setId(atividade.getUid());
		atividades = adao.listar(usuario);
		
		for (Atividade ativ : atividades) {
			if(ativ.getPredicao() == 1 && ativ.getResultado() == 1){
				paramA++;
			}
			else if(ativ.getPredicao() == 0 && ativ.getResultado() == 1){
				paramB++;
			}
			else if(ativ.getPredicao() == 1 && ativ.getResultado() == 0){
				paramC++;
			}
			else if(ativ.getPredicao() == 0 && ativ.getResultado() == 0){
				paramD++;
			}
		}
		
		atividade.setKma(calcularKma(paramA, paramB, paramC, paramD));
		
		return atividade;
	}
	
	/**
	 * Metodo responsavel por realizar o calculo do kma
	 * @param paramA
	 * @param paramB
	 * @param paramC
	 * @param paramD
	 * @return kma
	 */
	private float calcularKma(float paramA, float paramB, float paramC, float paramD){
		return ((paramA + paramD) - (paramB + paramC)) / (paramA + paramB + paramC + paramD);
	}
	
	/**
	 * Metodo responsavel por realizar o calculo do KMB de uma atividade
	 */
	@POST
	@Path("/getKmb")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	private Atividade getKmb (String ativStr){
		Atividade ativ = null;
		AtividadeDao adao = null;
		Gson gson = new Gson();
		
		ativ = gson.fromJson(ativStr, Atividade.class);
		
		calcularKmb(ativ);
		
		adao = adao.getInstancia();
		
		adao.atualizar(ativ);
		
		return ativ;
	}
	
	/**
	 * Metodo responsavel por retornar o valor kmb da atividade
	 * @param atividade
	 * @return kmb
	 */
	private void calcularKmb (Atividade ativ){
		if(ativ.getPredicao() == -1 && ativ.getResultado() == -1){
			ativ.setKmb(0);
		}
		else if(ativ.getPredicao() == -1 && ativ.getResultado() == 0){ 
			ativ.setKmb(0.5f);
		}
		else if(ativ.getPredicao() == -1 && ativ.getResultado() == 1){ 
			ativ.setKmb(1);
		}
		else if(ativ.getPredicao() == 0 && ativ.getResultado() == -1){ 
			ativ.setKmb(-0.5f);
		}
		else if(ativ.getPredicao() == 0 && ativ.getResultado() == 0){ 
			ativ.setKmb(0);
		}
		else if(ativ.getPredicao() == 0 && ativ.getResultado() == 1){ 
			ativ.setKmb(0.5f);
		}
		else if(ativ.getPredicao() == 1 && ativ.getResultado() == -1){ 
			ativ.setKmb(-1);
		}
		else if(ativ.getPredicao() == 1 && ativ.getResultado() == 0){ 
			ativ.setKmb(-0.5f);
		}
		else if(ativ.getPredicao() == 1 && ativ.getResultado() == 1){ 
			ativ.setKmb(0);
		}
	}
	
	/**
	 * Metodo responsavel por consultar o KMB medio das atividades para o usuario
	 * @param Json String (Usuario)
	 * @return Float kma
	 * @throws Exception
	 */
	@POST
	@Path("/getKmbMedio")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Float getKmbMedio(String usuarioStr){
		Usuario usuario = null;
		AtividadeDao adao = null;
		Gson gson = new Gson();
		
		usuario = gson.fromJson(usuarioStr, Usuario.class);
		
		adao = adao.getInstancia();
		
		return adao.consultarKmbMedio(usuario);
	}
}
