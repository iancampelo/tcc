package br.com.ucb.resource;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.ucb.dao.UsuarioDao;
import br.com.ucb.modelo.Usuario;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Path("/usuario")
public class UsuarioResource {
	
	@GET 
	@Path("/helloworld")
	@Produces(MediaType.APPLICATION_JSON) 
	public String showHelloWorld() { 
		return "Ola Mundo"; 
	} 
	
	@GET
	@Path("/listarUsuarios")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Usuario> listarTodos() { 
		UsuarioDao udao = null;
		udao = udao.getInstancia();
		return udao.listarTodos();
	} 
	
	@PUT
	@Path("/cadastrarUsuario")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean cadastrarUsuario(JsonObject user){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();

		udao = udao.getInstancia();
		
		usuario.setUsuario   (user.get("usuario"   ).getAsString());
		usuario.setSenha     (user.get("senha"     ).getAsString());
		usuario.setNome      (user.get("nome"      ).getAsString());
		usuario.setNascimento(user.get("nascimento").getAsString());
		usuario.setFuncao    (user.get("funcao"    ).getAsString());
		
		return udao.inserir(usuario);
	}
	
	@PUT
	@Path("/alterarUsuario")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean alterarUsuario(JsonObject user){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();

		udao = udao.getInstancia();
		
		usuario.setId        (user.get("id"        ).getAsInt   ());
		usuario.setUsuario   (user.get("usuario"   ).getAsString());
		usuario.setSenha     (user.get("senha"     ).getAsString());
		usuario.setNome      (user.get("nome"      ).getAsString());
		usuario.setNascimento(user.get("nascimento").getAsString());
		usuario.setFuncao    (user.get("funcao"    ).getAsString());
		
		return udao.atualizar(usuario);
	}
	
	@PUT
	@Path("/excluirUsuario")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean excluirUsuario(JsonElement id){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();

		udao = udao.getInstancia();
		
		usuario.setId(id.getAsInt());
		
		return udao.excluir(usuario);
	}
	
	@PUT
	@Path("/consultarUsuario")
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario consultarUsuario(JsonObject user){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();

		udao = udao.getInstancia();
		usuario.setUsuario(user.get("usuario").getAsString());
		
		usuario = udao.consultar(usuario.getUsuario());
		
		return usuario;
	}
}
