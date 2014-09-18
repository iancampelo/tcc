package br.com.ucb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.com.ucb.factory.ConnectionFactory;
import br.com.ucb.modelo.Projeto;
import br.com.ucb.modelo.Usuario;

public class ProjetoDao extends ConnectionFactory{

	private static ProjetoDao instancia = null;
	
	/**
	 * M�todo respons�vel por criar uma estancia da classe seguindo o padr�o singleton
	 * @return ProjetoDao
	 */
	public static ProjetoDao getInstancia(){
		if (instancia == null){
			instancia = new ProjetoDao();
		}
		return instancia;
	}
	
	/**
	 * M�todo respons�vel por inserir um projeto no banco de dados
	 * @param Usuario
	 * @param Projeto
	 * @return boolean
	 */
	public boolean inserir (Usuario user, Projeto proj){
		
		Connection        conn = null;
		PreparedStatement ps   = null;
		
		String sql = "insert into projeto(uid,nome,tempo_estimado,kma) "+
					 "values (?,?,?,?,?,?)";
		try{
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			
			ps.setInt   (1, user.getId           ());
			ps.setString(2, proj.getNome         ());
			ps.setFloat (3, proj.getTempoEstimado());
			ps.setFloat (4, proj.getKma          ());
			
			return ps.execute();
			
		}catch(Exception e){
			System.out.println("Erro ao inserir projeto: "+e);
			e.printStackTrace();
			return false;
		}finally{
			fecharConexao(conn, ps, null);
		}
	}
	
	/**
	 * M�todo respons�vel por excluir um registro do banco de dados
	 * @param Projeto
	 * @return boolean
	 */
	public boolean excluir (Projeto proj){
		Connection        conn = null;
		PreparedStatement ps   = null;
		
		String sql = "delete from projeto where id = ?";
		
		try{
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, proj.getId());
			
			return ps.execute();
			
		}catch(Exception e){
			System.out.println("Erro ao excluir projeto: "+e);
			e.printStackTrace();
			return false;
		}finally{
			fecharConexao(conn, ps, null);
		}
	}
	
	/**
	 * M�todo respons�vel por consultar um registro no banco de dados
	 * @param Projeto
	 * @return Projeto
	 */
	public Projeto consultar(Projeto proj){
		Connection        conn = null;
		PreparedStatement ps   = null;
		ResultSet 		  rs   = null;
		
		String sql = "select * from projeto where id = ?";
		
		try{
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			
			ps.setInt(1, proj.getId());
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				proj.setId           (rs.getInt   ("id"));
				proj.setNome         (rs.getString("nome"));
				proj.setTempoEstimado(!Float.isNaN(rs.getFloat("tempo_estimado"))? rs.getFloat("tempo_estimado") : 0); // se o resultado for nulo coloca 0
				proj.setTempoGasto   (!Float.isNaN(rs.getFloat("tempo_gasto"   ))? rs.getFloat("tempo_gasto")    : 0); // se o resultado for nulo coloca 0
				proj.setKma          (!Float.isNaN(rs.getFloat("kma"))? rs.getFloat("kma") : 0);
				proj.setKmb          (!Float.isNaN(rs.getFloat("kmb"))? rs.getFloat("kmb") : 0);
				
				return proj;
			}else{
				return null;
			}
		}catch(Exception e){
			System.out.println("Erro ao excluir projeto: "+e);
			e.printStackTrace();
			return null;
		}finally{
			fecharConexao(conn, ps, rs);
		}
	}

	/**
	 * M�todo respons�vel por listar todos os projetos do usu�rio
	 * @param Usuario
	 * @return ArrayList<Projeto>
	 */
	public ArrayList<Projeto> listar(Usuario user){
		Connection 		   conn 	= null;
		PreparedStatement  ps 		= null;
		ResultSet 		   rs 		= null;
		ArrayList<Projeto> projetos = null;
		
		String sql = "select * from projeto where uid = ?";
		
		try{
			conn = criarConexao();
			ps = conn.prepareStatement(sql);	
			ps.setInt(1, user.getId());
			rs = ps.executeQuery();
			
			projetos = new ArrayList<Projeto>();
			
			while(rs.next()){
				Projeto proj = new Projeto();
				proj.setId           (rs.getInt   ("id"));
				proj.setNome         (rs.getString("nome"));
				proj.setTempoEstimado(!Float.isNaN(rs.getFloat("tempo_estimado"))? rs.getFloat("tempo_estimado") : 0); // se o resultado for nulo coloca 0
				proj.setTempoGasto   (!Float.isNaN(rs.getFloat("tempo_gasto"   ))? rs.getFloat("tempo_gasto")    : 0); // se o resultado for nulo coloca 0
				proj.setKma          (!Float.isNaN(rs.getFloat("kma"))? rs.getFloat("kma") : 0);
				proj.setKmb          (!Float.isNaN(rs.getFloat("kmb"))? rs.getFloat("kmb") : 0);
				
				projetos.add(proj);
			}
			
			return projetos;
			
		}catch(Exception e){
			System.out.println("Erro ao excluir projeto: "+e);
			e.printStackTrace();
			return projetos;
		}finally{
			fecharConexao(conn, ps, rs);
		}
	}
	
	/**
	 * M�todo respons�vel por atualizar um projeto
	 * @param Usuario
	 * @param Projeto
	 * @return boolean
	 */
	public boolean atualizar (Usuario user, Projeto proj){
		
		Connection        conn = null;
		PreparedStatement ps   = null;
		
		String sql = "update projeto set "+
				     "nome = ?, tempo_estimado = ?, tempo_gasto = ?, kma = ?, kmb = ? "+
					 "where id = ?";
		try{
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			
			ps.setString(1, proj.getNome         ());
			ps.setFloat (2, proj.getTempoEstimado());
			ps.setFloat (3, proj.getTempoGasto   ());
			ps.setFloat (4, proj.getKma          ());
			ps.setFloat (5, proj.getKma          ());
			ps.setInt   (6, user.getId           ());
			
			return ps.executeUpdate() > 0 ? true : false;
			
		}catch(Exception e){
			System.out.println("Erro ao inserir projeto: "+e);
			e.printStackTrace();
			return false;
		}finally{
			fecharConexao(conn, ps, null);
		}
	}	
}




