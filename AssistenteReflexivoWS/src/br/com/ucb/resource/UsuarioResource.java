package br.com.ucb.resource;

import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.ws.rs.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import br.com.ucb.dao.UsuarioDao;
import br.com.ucb.modelo.Usuario;

@Path("/usuario")
public class UsuarioResource {

	@PUT
	@GET
	@Path("/cadastrarUsuario")
	@Produces("application/json")
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
	@GET
	@Path("/alterarUsuario")
	@Produces("application/json")
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
	@GET
	@Path("/excluirUsuario")
	@Produces("application/json")
	public boolean excluirUsuario(JsonElement id){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();

		udao = udao.getInstancia();
		
		usuario.setId(id.getAsInt());
		
		return udao.excluir(usuario);
	}
	
	@PUT
	@GET
	@Path("/consultarUsuario")
	@Produces("application/json")
	public Usuario consultarUsuario(JsonElement user){		
		UsuarioDao udao = null;
		Usuario usuario = new Usuario();

		udao = udao.getInstancia();
		usuario.setUsuario(user.getAsString());
		
		usuario = udao.consultar(usuario.getUsuario());
		
		return usuario;
	}
}
