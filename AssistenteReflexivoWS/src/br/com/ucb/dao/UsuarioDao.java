package br.com.ucb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import br.com.ucb.factory.ConnectionFactory;
import br.com.ucb.modelo.Usuario;

public class UsuarioDao extends ConnectionFactory{

	private static UsuarioDao instancia = null;

	/**
	 * M�todo respons�vel por criar uma estancia da classe seguindo o padr�o
	 * singleton
	 * 
	 * @return AtividadeDao
	 */
	public static UsuarioDao getInstancia() {
		if (instancia == null) {
			instancia = new UsuarioDao();
		}
		return instancia;
	}
	
	/**
	 * M�todo respons�vel por inserir um usuario no banco de dados
	 * 
	 * @param Usuario
	 * @param Usuario
	 * @return boolean
	 */
	public boolean inserir(Usuario user) {

		Connection conn = null;
		PreparedStatement ps = null;
		
		String sql = "insert into usuario(usuario, senha, nome, nascimento,funcao) "
				+ "values (?,?,?,?,?)";
		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			ps.setString(1, user.getUsuario());
			ps.setString(2, user.getSenha());
			ps.setString(3, user.getNome());
			ps.setDate  (4,  new java.sql.Date(format.parse(user.getNascimento()).getTime()));
			ps.setString(5, user.getFuncao());

			return ps.execute();

		} catch (Exception e) {
			System.out.println("Erro ao inserir usuario: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}

	/**
	 * M�todo respons�vel por excluir uma usuario do banco de dados
	 * 
	 * @param Usuario
	 * @return boolean
	 */
	public boolean excluir(Usuario user) {
		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "delete from usuario where id = ?";

		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);

			ps.setInt(1, user.getId());

			return ps.execute();

		} catch (Exception e) {
			System.out.println("Erro ao excluir usuario: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}

	/**
	 * M�todo respons�vel por consultar um usuario no banco de dados
	 * 
	 * @param ID do usuario
	 * @return Usuario
	 */
	public Usuario consultar(String usuario) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Usuario user = null;

		String sql = "select * from usuario where usuario = ?";

		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			ps.setString(1, usuario);

			rs = ps.executeQuery();

			if (rs.next()) {
				user = new Usuario();
				
				user.setId        (rs.getInt   ("id"));
				user.setUsuario   (rs.getString("usuario"));
				user.setSenha     (rs.getString("senha"));
				user.setNome      (rs.getString("nome"));
				user.setNascimento(rs.getDate  ("nascimento").toString());
				user.setFuncao    (rs.getString("funcao"));
	
				return user;
			} else {
				return user;
			}
		} catch (Exception e) {
			System.out.println("Erro ao consultar Usuario: " + e);
			e.printStackTrace();
			return null;
		} finally {
			fecharConexao(conn, ps, rs);
		}
	}

	/**
	 * M�todo respons�vel por atualizar um usuario
	 * 
	 * @param Usuario
	 * @return boolean
	 */
	public boolean atualizar(Usuario user) {

		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "update usuario set "+
				     "senha = ?, nome = ?, nascimento = ? funcao = ?"+
				     "where id = ?";
		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

			ps.setString(1, user.getSenha());
			ps.setString(2, user.getNome());
			ps.setDate  (3,  new java.sql.Date(format.parse(user.getNascimento()).getTime()));
			ps.setString(4, user.getFuncao()); 
			
			return ps.executeUpdate() > 0 ? true : false;

		} catch (Exception e) {
			System.out.println("Erro ao atualizar usuario: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}
	
}
