package br.com.ucb.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ucb.dao.UsuarioDao;
import br.com.ucb.modelo.Usuario;

import com.google.gson.Gson;

/**
 * Classe do Web Service que recebera as informcoes pertinentes a usuarios e realizara as operacoes
 * desejadas na base de dados
 * @author Jean Silvestre
 *
 */
@Path("usuario")
public class UsuarioResource {
	
	/**
	 * Metodo responsavel por traduzir uma String Json em um usuario e inserir este usuario atraves do DAO
	 * @param Json String (Usuario)
	 * @throws Exception
	 */
	@POST
	@Path("cadastrarUsuario")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String cadastrarUsuario(String userStr) throws Exception{		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();
		Gson gson = new Gson();
		udao = udao.getInstancia();
		
		System.out.println(userStr);
		
		usuario = gson.fromJson(userStr, Usuario.class);
		
		return udao.inserir(usuario)?"S":"N";
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em um Usuario e alterar este usuario atraves do DAO
	 * @param Json String
	 * @throws Exception
	 */
	@POST
	@Path("alterarUsuario")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String alterarUsuario(String userStr){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();
		Gson gson = new Gson();
		udao = udao.getInstancia();
		
		usuario = gson.fromJson(userStr, Usuario.class);
		
		return udao.atualizar(usuario)?"S":"N";
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em um Usuario e excluir este  usuario atraves do DAO
	 * @param Json String (Usuario)
	 * @throws Exception
	 */
	@POST
	@Path("excluirUsuario")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String excluirUsuario(String userStr){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();
		Gson gson = new Gson();

		udao = udao.getInstancia();
		
		usuario = gson.fromJson(userStr, Usuario.class);
		
		return udao.excluir(usuario)?"S":"N";
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em um Usuario e consultar este usuario atraves do DAO
	 * @param Json String (Usuario)
	 * @throws Exception 
	 */
	@POST
	@Path("consultarUsuario")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario consultarUsuario(String userStr){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();
		Gson gson = new Gson();

		udao = udao.getInstancia();
		
		usuario = gson.fromJson(userStr, Usuario.class);
		
		usuario = udao.consultar(usuario.getUsuario());
		
		return usuario;
	}
	
}
