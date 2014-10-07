package br.com.ucb.teste;

import br.com.ucb.dao.AtividadeDao;
import br.com.ucb.dao.UsuarioDao;
import br.com.ucb.modelo.Atividade;
import br.com.ucb.modelo.Usuario;

public class TesteDao {

	public static void main(String[] args) {
		
		Atividade ativ = new Atividade();
		Usuario user = new Usuario();
		
		AtividadeDao ativDao = new AtividadeDao();
		UsuarioDao userDao = new UsuarioDao();
		
		userDao.listarTodos();
		
		user.setNome("aaaa");
		user.setUsuario("aaaa");
		user.setSenha("12345");
		user.setNascimento("01/06/1991");
		user.setFuncao("Programador");
		
		userDao.inserir(user);
		
		user = userDao.consultar("aaaa");
		
		System.out.println(user.toString());
		
		user.setNome("Jean");
		user.setUsuario("ab");
		user.setSenha("12345");
		user.setNascimento("01/06/1991");
		user.setFuncao("Programador");
		
		System.out.println("Atualizou: " + userDao.atualizar(user));
		
		System.out.println("Excluiu: " + userDao.excluir(user));
		
		ativ.setId(1);
		ativ.setNome("TCC");
		ativ.setTempoEstimado(60);
		ativ.setPredicao("aaa");
		ativ.setEstrategia("aaaa");
		ativ.setRecursos("Livros, internet");
		
		user = userDao.consultar("jeansdp");
		
		System.out.println("Inseriu: "+ativDao.inserir(user, ativ));
		
		System.out.println("Atualizaou: "+ativDao.atualizar(ativ));
		
		System.out.println("Consultou: "+ ativDao.consultar(ativ));
		
		System.out.println("Excluiu: "+ativDao.excluir(ativ));
	}
	
}
