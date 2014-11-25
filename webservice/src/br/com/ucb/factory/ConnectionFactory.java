package br.com.ucb.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Classe responsavel por abrir e fechar as conexoes com a base de dados
 * @author Jean Silvestre
 *
 */
public class ConnectionFactory {
	
	private static final String DRIVER  = "com.mysql.jdbc.Driver";
	private static final String URL     = "jdbc:mysql://localhost/assistentereflexivo";
	private static final String USUARIO = "root";
	private static final String SENHA   = "root";
	
	/** 
	 * M�todo responsavel por criar a conexao com o banco de dados
	 * @return Connection
	 */
	public static Connection criarConexao() {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			return DriverManager.getConnection(URL, USUARIO, SENHA);
		}
		catch (Exception e) {
			System.out.println("Erro ao criar conexão com o banco: "+URL);
		}
		
		return conn;
	}
	
	/**
	 * Metodo responsavel por fechar as conexoes com o banco de dados
	 * @param Connection
	 * @param PreparedStatment
	 * @param ResultSet
	 */
	public void fecharConexao(Connection conn, PreparedStatement ps, ResultSet rs){
		try{
			if(conn != null){
				conn.close();
			}
			if(ps != null){
				ps.close();
			}
			if(ps != null){
				ps.close();
			}
		}catch(Exception e){
			System.out.println("Erro ao fechar conex�o com o banco: "+URL);
		}
	}
	
}
