package br.com.ucb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.com.ucb.factory.ConnectionFactory;
import br.com.ucb.modelo.Atividade;
import br.com.ucb.modelo.Usuario;

/**
 * Classe responsavel por realizar as operacoes na base de dados pertinentes a atividade
 * @author Jean
 *
 */
public class AtividadeDao extends ConnectionFactory {

	private static AtividadeDao instancia = null;

	/**
	 * Metodo responsavel por criar uma instancia da classe seguindo o padrao singleton
	 * @return instancia
	 */
	public static AtividadeDao getInstancia() {
		if (instancia == null) {
			instancia = new AtividadeDao();
		}
		return instancia;
	}

	/**
	 * Metodo responsavel por inserir uma atividade no banco de dados
	 * 
	 * @param Atividade
	 * @return Boolean
	 */
	public Integer inserir(Atividade ativ) {

		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "insert into atividade(uid, nome, tempo_estimado, predicao, estrategia, recursos, grau_atencao, comprensao, objetivo, anotacoes, tempo_gasto, resultado) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);

			ps.setInt      	(1,  ativ.getUid());
			ps.setString   	(2,  ativ.getNome());
			ps.setTime		(3,  ativ.getTempoEstimado());
			ps.setInt      	(4,  ativ.getPredicao());
			ps.setString   	(5,  ativ.getEstrategia());
			ps.setString   	(6,  ativ.getRecursos());
			ps.setString   	(7,  ativ.getGrauAtencao());
			ps.setString   	(8,  ativ.getComprensao());
			ps.setString   	(9,  ativ.getObjetivo());
			ps.setString	(10, ativ.getAnotacoes());
			ps.setTime		(11, ativ.getTempoGasto());
			ps.setInt		(12, ativ.getResultado());

			boolean success = false;
			success = ps.executeUpdate() > 0; 
			ResultSet rs = null;	
			Integer ab = null;
			if(success){

				sql = "select max(id) id from atividade where uid = ?";
				fecharConexao(conn, ps, null);
				conn = criarConexao();
				ps = conn.prepareStatement(sql);
				ps.setInt(1, ativ.getUid());
				rs = ps.executeQuery();
				if (rs.next()) {
					ab = rs.getInt("id");
				}
			}
			else
				return null;

			return ab;


		} catch (Exception e) {
			System.out.println("Erro ao inserir atividade: " + e);
			e.printStackTrace();
			return null;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}

	/**
	 *  Metodo responsavel por excluir uma atividade do banco de dados
	 * 
	 * @param Atividade
	 * @return boolean
	 */
	public boolean excluir(Atividade ativ) {
		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "delete from atividade where id = ?";

		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);

			ps.setInt(1, ativ.getId());

			return ps.executeUpdate() > 0;

		} catch (Exception e) {
			System.out.println("Erro ao excluir Atividade: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}

	/**
	 *  Metodo responsavel por consultar uma atividade no banco de dados
	 * 
	 * @param Atividade
	 * @return Atividade
	 */
	public Atividade consultar(Atividade ativ) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select * from atividade where id = ?";

		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);

			ps.setInt(1, ativ.getId());

			rs = ps.executeQuery();

			if (rs.next()) {
				ativ.setId			 (rs.getInt		 ("id"));
				ativ.setUid          (rs.getInt		 ("uid"));
				ativ.setNome	     (rs.getString	 ("nome"));
				ativ.setTempoEstimado(rs.getTime	 ("tempo_estimado"));
				ativ.setPredicao	 (rs.getInt   	 ("predicao"));
				ativ.setEstrategia	 (rs.getString	 ("estrategia"));
				ativ.setRecursos	 (rs.getString	 ("recursos"));
				ativ.setGrauAtencao	 (rs.getString	 ("grau_atencao"));
				ativ.setComprensao	 (rs.getString	 ("comprensao"));
				ativ.setObjetivo	 (rs.getString	 ("objetivo"));
				ativ.setAnotacoes	 (rs.getString	 ("anotacoes"));
				ativ.setTempoGasto	 (rs.getTime	 ("tempo_gasto"));
				ativ.setKma			 (rs.getFloat    ("kma"));
				ativ.setResultado	 (rs.getInt      ("resultado"));
				ativ.setKmb			 (rs.getFloat	 ("kmb"));

				return ativ;
			} else {
				return null;
			}
		} catch (Exception e) {
			System.out.println("Erro ao consultar atividade: " + e);
			e.printStackTrace();
			return null;
		} finally {
			fecharConexao(conn, ps, rs);
		}
	}

	/**
	 *  Metodo responsavel por listar todos as atividades do usuario
	 * 
	 * @param Usuario
	 * @return ArrayList<Atividade>
	 */
	public ArrayList<Atividade> listar(Usuario user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ArrayList<Atividade> Atividades = null;

		String sql = "select * from atividade where uid = ?";

		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getId());
			rs = ps.executeQuery();

			Atividades = new ArrayList<Atividade>();

			while (rs.next()) {
				Atividade ativ = new Atividade();
				ativ.setId			 (rs.getInt		 ("id"));
				ativ.setUid			 (rs.getInt		 ("uid"));
				ativ.setNome	     (rs.getString	 ("nome"));
				ativ.setTempoEstimado(rs.getTime	 ("tempo_estimado"));
				ativ.setPredicao	 (rs.getInt      ("predicao"));
				ativ.setEstrategia	 (rs.getString   ("estrategia"));
				ativ.setRecursos	 (rs.getString   ("recursos"));
				ativ.setGrauAtencao	 (rs.getString   ("grau_atencao"));
				ativ.setComprensao	 (rs.getString   ("comprensao"));
				ativ.setObjetivo	 (rs.getString   ("objetivo"));
				ativ.setAnotacoes	 (rs.getString   ("anotacoes"));
				ativ.setTempoGasto	 (rs.getTime	 ("tempo_gasto")); 
				ativ.setKma			 (rs.getFloat    ("kma"));
				ativ.setResultado	 (rs.getInt  	 ("resultado"));
				ativ.setKmb          (rs.getFloat	 ("kmb"));

				Atividades.add(ativ);
			}

			return Atividades;

		} catch (Exception e) {
			System.out.println("Erro ao excluir Atividade: " + e);
			e.printStackTrace();
			return Atividades;
		} finally {
			fecharConexao(conn, ps, rs);
		}
	}

	/**
	 *  Metodo responsavel por atualizar uma Atividade
	 * 
	 * @param Usuario
	 * @param Atividade
	 * @return boolean
	 */
	public boolean atualizar(Atividade ativ) {

		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "update atividade set "+
				"nome = ?, tempo_estimado = ?, predicao = ?, estrategia = ?, recursos = ?, grau_atencao = ?, "+
				"comprensao = ?, objetivo = ?, anotacoes = ?, tempo_gasto = ?, kma = ?, resultado = ? , kmb = ?"+
				"where id = ?";
		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);

			ps.setString    (1,  ativ.getNome());
			ps.setTime 		(2,  ativ.getTempoEstimado());
			ps.setInt       (3,  ativ.getPredicao());
			ps.setString	(4,  ativ.getEstrategia());
			ps.setString	(5,  ativ.getRecursos());
			ps.setString	(6,  ativ.getGrauAtencao());
			ps.setString	(7,  ativ.getComprensao());
			ps.setString	(8,  ativ.getObjetivo());
			ps.setString	(9,  ativ.getAnotacoes());
			ps.setTime 		(10, ativ.getTempoGasto());
			ps.setFloat 	(11, ativ.getKma());
			ps.setInt   	(12, ativ.getResultado());
			ps.setFloat 	(13, ativ.getKmb());
			ps.setInt		(14, ativ.getId());

			return ps.executeUpdate() > 0 ? true : false;

		} catch (Exception e) {
			System.out.println("Erro ao atualizar atividade: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}

	/**
	 *  Metodo responsavel por realizar a consulta do KMB medio nas atividades para o usuario
	 * @param user
	 * @return valor
	 */
	public String consultarKmbMedio(Usuario user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select avg(kmb) qtd from atividade where uid = ?";

		String a = null;
		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getId());
			rs = ps.executeQuery();


			while (rs.next()) {
				a = rs.getString("qtd");
			}

		} catch (Exception e) {
			System.out.println("Erro ao consultar kmb medio: " + e);
			e.printStackTrace();
			return null;
		} finally {
			fecharConexao(conn, ps, rs);
		}
		return a;
	}

	/**
	 *  Metodo responsavel por realizar a consulta do KMA medio nas atividades para o usuario
	 * @param user
	 * @return valor
	 */
	public String consultarKmaMedio(Usuario user) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		String sql = "select avg(kma) qtd from atividade where uid = ?";

		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);
			ps.setInt(1, user.getId());
			rs = ps.executeQuery();
			String a = null;
			while (rs.next()) {
				int i = rs.findColumn("qtd");
				a = rs.getString(i);
			}
			if(a!=null){
				return a;
			}
			return null;

		} catch (Exception e) {
			System.out.println("Erro ao consultar kma medio: " + e.getCause());
			e.printStackTrace();
			return null;
		} finally {
			fecharConexao(conn, ps, rs);
		}
	}

}
