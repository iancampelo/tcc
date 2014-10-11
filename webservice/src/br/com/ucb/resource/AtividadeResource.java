package br.com.ucb.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ucb.dao.AtividadeDao;
import br.com.ucb.modelo.Atividade;
import br.com.ucb.modelo.Usuario;

import com.google.gson.Gson;

@Path("/atividade")
public class AtividadeResource {

	@GET
	@Path("/hello")
	@Produces(MediaType.APPLICATION_JSON)
	public String hello() { 
		return "Hello!!!";
	} 
	
	/**
	 * Metodo responsavel por inserir um atividade
	 * @param Json String
	 * @throws Exception
	 */
	@POST
	@Path("/cadastrarAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String cadastrarAtividade(String userStr) throws Exception{		
		AtividadeDao adao = null;
		Usuario usuario = new Usuario();
		Gson gson = new Gson();
		adao = adao.getInstancia();
		
		System.out.println(userStr);
		
		usuario = gson.fromJson(userStr, Usuario.class);
		
		return adao.inserir(usuario, usuario.getAtividades().get(0)) ? "S":"N";
	}
	
	/**
	 * Metodo responsavel por alterar uma atividade
	 * @param Json String
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
	 * Metodo responsavel por excluir uma atividade
	 * @param Json String
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
	 * Metodo responsavel por consultar uma atividade
	 * @param Json String
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
