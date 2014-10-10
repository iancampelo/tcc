package br.com.ucb.resource;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ucb.dao.UsuarioDao;
import br.com.ucb.modelo.Usuario;

import com.google.gson.Gson;

@Path("/usuario")
public class UsuarioResource {
	
	@GET
	@Path("/listarUsuarios")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Usuario> listarTodos() { 
		UsuarioDao udao = null;
		udao = udao.getInstancia();
		return udao.listarTodos();
	} 
	
	/**
	 * Método responsável por inserir um usuario
	 * @param Json String
	 * @throws Exception
	 */
	@POST
	@Path("/cadastrarUsuario")
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
	 * Método responsável por alterar um usuario
	 * @param Json String
	 * @throws Exception
	 */
	@POST
	@Path("/alterarUsuario")
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
	 * Método responsável por excluir um usuario
	 * @param Json String
	 * @throws Exception
	 */
	@POST
	@Path("/excluirUsuario")
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
	 * Método responsável por consultar um usuario
	 * @param Json String
	 * @throws Exception
	 */
	@POST
	@Path("/consultarUsuario")
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
