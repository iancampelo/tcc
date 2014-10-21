package br.com.ucb.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ucb.dao.AtividadeDao;
import br.com.ucb.modelo.Atividade;

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
	 * @throws Exception
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
	 * @throws Exception
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
	 * @throws Exception
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
	 * @throws Exception
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
	
}
