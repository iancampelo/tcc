package br.com.ucb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import br.com.ucb.factory.ConnectionFactory;
import br.com.ucb.modelo.Atividade;
import br.com.ucb.modelo.Usuario;

public class AtividadeDao extends ConnectionFactory {

	private static AtividadeDao instancia = null;

	/**
	 * Método responsável por criar uma instancia da classe seguindo o padrão
	 * singleton'
	 * 
	 * @return AtividadeDao
	 */
	public static AtividadeDao getInstancia() {
		if (instancia == null) {
			instancia = new AtividadeDao();
		}
		return instancia;
	}

	/**
	 * Método responsável por inserir uma atividade no banco de dados
	 * 
	 * @param Usuario
	 * @param Atividade
	 * @return boolean
	 */
	public boolean inserir(Usuario user, Atividade ativ) {

		Connection conn = null;
		PreparedStatement ps = null;

		String sql = "insert into atividade(uid,nome,tempo_estimado,predicao,estrategia,recursos,grau_atencao,comprensao,objetivo,kma) "
				+ "values (?,?,?,?,?,?,?,?,?,?)";
		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);

			ps.setInt   (1,  user.getId());
			ps.setString(2,  ativ.getNome());
			ps.setFloat (3,  ativ.getTempoEstimado());
			ps.setString(4,  ativ.getPredicao());
			ps.setString(5,  ativ.getEstrategia());
			ps.setString(6,  ativ.getRecursos());
			ps.setString(7,  ativ.getGrauAtencao());
			ps.setString(8,  ativ.getComprensao());
			ps.setString(9,  ativ.getObjetivo());
			ps.setFloat (10, ativ.getKma());

			return ps.execute();

		} catch (Exception e) {
			System.out.println("Erro ao inserir atividade: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}

	/**
	 * Método responsável por excluir uma atividade do banco de dados
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

			return ps.execute();

		} catch (Exception e) {
			System.out.println("Erro ao excluir Atividade: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}

	/**
	 * Método responsável por consultar uma atividade no banco de dados
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
				ativ.setId			 (rs.getInt("id"));
				ativ.setNome	     (rs.getString("nome"));
				ativ.setTempoEstimado(!Float.isNaN(rs.getFloat("tempo_estimado")) ? 
						              rs.getFloat("tempo_estimado") : 0); // se o resultado for nulo coloca 0
				ativ.setPredicao	 (rs.getString("predicao"));
				ativ.setEstrategia	 (rs.getString("estrategia"));
				ativ.setRecursos	 (rs.getString("recursos"));
				ativ.setGrauAtencao	 (rs.getString("grau_atencao"));
				ativ.setComprensao	 (rs.getString("comprensao"));
				ativ.setObjetivo	 (rs.getString("objetivo"));
				ativ.setAnotações	 (rs.getString("anotacoes"));
				ativ.setTempoGasto	 (!Float.isNaN(rs.getFloat("tempo_gasto")) ? 
						              rs.getFloat("tempo_gasto") : 0); // se o resultado for nulo coloca 0
				ativ.setKma			 (!Float.isNaN(rs.getFloat("kma")) ? rs.getFloat("kma") : 0);
				ativ.setKmb			 (!Float.isNaN(rs.getFloat("kmb")) ? rs.getFloat("kmb") : 0);

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
	 * Método responsável por listar todos as atividades do usuário
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
				ativ.setId			 (rs.getInt("id"));
				ativ.setNome	     (rs.getString("nome"));
				ativ.setTempoEstimado(!Float.isNaN(rs.getFloat("tempo_estimado")) ? 
						              rs.getFloat("tempo_estimado") : 0); // se o resultado for nulo coloca 0
				ativ.setPredicao	 (rs.getString("predicao"));
				ativ.setEstrategia	 (rs.getString("estrategia"));
				ativ.setRecursos	 (rs.getString("recursos"));
				ativ.setGrauAtencao	 (rs.getString("grau_atencao"));
				ativ.setComprensao	 (rs.getString("comprensao"));
				ativ.setObjetivo	 (rs.getString("objetivo"));
				ativ.setAnotações	 (rs.getString("anotacoes"));
				ativ.setTempoGasto	 (!Float.isNaN(rs.getFloat("tempo_gasto")) ? 
						              rs.getFloat("tempo_gasto") : 0); // se o resultado for nulo coloca 0
				ativ.setKma			 (!Float.isNaN(rs.getFloat("kma")) ? rs.getFloat("kma") : 0);
				ativ.setKmb			 (!Float.isNaN(rs.getFloat("kmb")) ? rs.getFloat("kmb") : 0);

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
	 * Método responsável por atualizar um Atividade
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
				     "comprensao = ?, objetivo = ?, anotacoes = ?, tempo_gasto = ?, kma = ?, kmb = ? "+
				     "where id = ?";
		try {
			conn = criarConexao();
			ps = conn.prepareStatement(sql);

			ps.setString(1,  ativ.getNome());
			ps.setFloat (2,  ativ.getTempoEstimado());
			ps.setString(3,  ativ.getPredicao());
			ps.setString(4,  ativ.getEstrategia());
			ps.setString(5,  ativ.getRecursos());
			ps.setString(6,  ativ.getGrauAtencao());
			ps.setString(7,  ativ.getComprensao());
			ps.setString(8,  ativ.getObjetivo());
			ps.setString(9,  ativ.getAnotações());
			ps.setFloat (10, ativ.getTempoGasto());
			ps.setFloat (11, ativ.getKma());
			ps.setFloat (12, ativ.getKmb());
			ps.setInt	(13, ativ.getId());

			return ps.executeUpdate() > 0 ? true : false;

		} catch (Exception e) {
			System.out.println("Erro ao atualizar atividade: " + e);
			e.printStackTrace();
			return false;
		} finally {
			fecharConexao(conn, ps, null);
		}
	}
}
