package br.com.ucb.resource;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;

import br.com.ucb.dao.AtividadeDao;
import br.com.ucb.dao.UsuarioDao;
import br.com.ucb.modelo.Atividade;
import br.com.ucb.modelo.Usuario;

/**
 * Classe do Web Service que recebera as informcoes pertinentes a atividades e realizara as operacoes
 * desejadas na base de dados
 * @author Jean Silvestre
 *
 */
@Path("/atividade")
public class AtividadeResource{
	@GET
	@Path("/hello")
	@Produces(MediaType.APPLICATION_JSON)
	public String hello(){		
		System.out.println(System.currentTimeMillis());
		return "hello";
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e inserir esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 */
	@POST
	@Path("/cadastrarAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String cadastrarAtividade(String ativStr) throws Exception{		
		AtividadeDao adao = null;

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
		adao = adao.getInstancia();

		System.out.println(ativStr);
		
		Time tm = Time.valueOf("00:23:43");
		System.out.println(tm.toString());
		
		Atividade ativ = gson.fromJson(ativStr, Atividade.class);
		
		ativ = getTimes(ativ,ativStr);
		
		return adao.inserir(ativ) ? "S":"N";
	}

	public Atividade getTimes(Atividade ativ, String ativStr) throws JSONException{
		JSONObject jsonObj = new JSONObject(ativStr);
		String tempoEst = jsonObj.getString("tempoEstimado");
		if(tempoEst != null){
			if(!tempoEst.isEmpty()){
				ativ.setTempoEstimado(Time.valueOf(tempoEst));
			}
		}
		String tmpG = jsonObj.getString("tempoGasto");
		if(tmpG != null){
			if(!tmpG.isEmpty()){
				ativ.setTempoGasto(Time.valueOf(tmpG));
			}
		}
		else
			ativ.setTempoGasto(Time.valueOf("00:00:00"));
		
		return ativ;
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e alterar esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 * @throws Exception 
	 */
	@POST
	@Path("/alterarAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String alterarAtividade(String ativStr) throws Exception{		
		AtividadeDao adao = null;
		Atividade atividade = new Atividade();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();
		adao = adao.getInstancia();

		atividade = gson.fromJson(ativStr, Atividade.class);
		
		atividade = getTimes(atividade, ativStr);

		return adao.atualizar(atividade)? "S" : "N";
	}

	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e excluir esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 * @throws Exception 
	 */
	@POST
	@Path("/excluirAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String excluirAtividade(String ativStr) throws Exception{		
		AtividadeDao adao = null;
		Atividade atividade = new Atividade();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

		adao = adao.getInstancia();

		atividade = gson.fromJson(ativStr, Atividade.class);
		
		atividade = getTimes(atividade, ativStr);

		return adao.excluir(atividade)?"S":"N";
	}

	/**
	 * Metodo responsavel por traduzir uma String Json em uma atividade e consultar esta atividade atraves do DAO
	 * @param Json String (Atividade)
	 * @return atividade
	 * @throws Exception 
	 */
	@POST
	@Path("/consultarAtividade")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Atividade consultarAtividade(String ativStr) throws Exception{		
		AtividadeDao adao = null;
		Atividade atividade = new Atividade();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

		adao = adao.getInstancia();

		atividade = gson.fromJson(ativStr, Atividade.class);

		atividade = getTimes(atividade, ativStr);
		
		atividade = adao.consultar(atividade);

		return atividade;
	}
	
	/**
	 * Metodo responsavel por traduzir uma String Json em todas as atividades do usuario
	 * @param Json String (Usuario)
	 * @return atividades
	 * @throws Exception 
	 */
	@POST
	@Path("/listarAtividades")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Atividade> listarAtividades(String usuario) throws Exception{		
		AtividadeDao adao = null;
		ArrayList<Atividade> atividades = new ArrayList<Atividade>();
		
		UsuarioDao udao = null;
		Usuario user = new Usuario();
		Gson gsonU = new Gson();

		udao = udao.getInstancia();
		user = gsonU.fromJson(usuario, Usuario.class);
		user = udao.consultar(user.getUsuario());

		adao = adao.getInstancia();
		atividades = adao.listar(user);
		return atividades;
	}
	

	/**
	 * Metodo responsavel por definir o indice KMA para uma atividade
	 * @param Json String (Atividade)
	 * @return Atividade kma
	 * @throws Exception 
	 */
	@POST
	@Path("/getKma")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Atividade getKma(String ativStr) throws Exception{
		AtividadeDao adao = null;
		Atividade atividade = null;
		Usuario usuario = new Usuario();
		ArrayList<Atividade> atividades = new ArrayList<Atividade>();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

		float paramA = 0; // numero de vezes que disse que teria sucesso e teve
		float paramB = 0; // numero de vezes que disse que nao teria sucesso e teve
		float paramC = 0; // numero de vezes que disse que teria sucesso e n�o teve
		float paramD = 0; // numero de vezes que disse que nao teria sucesso e nao teve

		adao = adao.getInstancia();

		atividade = gson.fromJson(ativStr, Atividade.class);
		usuario.setId(atividade.getUid());
		atividade = getTimes(atividade, ativStr);
		atividades = adao.listar(usuario);

		for (Atividade ativ : atividades) {
			if(ativ.getPredicao() == 1 && ativ.getResultado() == 1){
				paramA++;
			}
			else if(ativ.getPredicao() == 0 && ativ.getResultado() == 1){
				paramB++;
			}
			else if(ativ.getPredicao() == 1 && ativ.getResultado() == 0){
				paramC++;
			}
			else if(ativ.getPredicao() == 0 && ativ.getResultado() == 0){
				paramD++;
			}
		}

		atividade.setKma(calcularKma(paramA, paramB, paramC, paramD));

		return atividade;
	}

	/**
	 * Metodo responsavel por realizar o calculo do kma
	 * @param paramA
	 * @param paramB
	 * @param paramC
	 * @param paramD
	 * @return kma
	 */
	private float calcularKma(float paramA, float paramB, float paramC, float paramD){
		return ((paramA + paramD) - (paramB + paramC)) / (paramA + paramB + paramC + paramD);
	}

	/**
	 * Metodo responsavel por realizar o calculo do KMB de uma atividade
	 * @throws Exception 
	 */
	@POST
	@Path("/getKmb")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	private Atividade getKmb (String ativStr) throws Exception{
		Atividade ativ = null;
		AtividadeDao adao = null;
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();

		ativ = gson.fromJson(ativStr, Atividade.class);

		ativ = getTimes(ativ, ativStr);
		
		calcularKmb(ativ);

		adao = adao.getInstancia();

		adao.atualizar(ativ);

		return ativ;
	}

	/**
	 * Metodo responsavel por retornar o valor kmb da atividade
	 * @param atividade
	 * @return kmb
	 */
	private void calcularKmb (Atividade ativ){
		if(ativ.getPredicao() == -1 && ativ.getResultado() == -1){
			ativ.setKmb(0);
		}
		else if(ativ.getPredicao() == -1 && ativ.getResultado() == 0){ 
			ativ.setKmb(0.5f);
		}
		else if(ativ.getPredicao() == -1 && ativ.getResultado() == 1){ 
			ativ.setKmb(1);
		}
		else if(ativ.getPredicao() == 0 && ativ.getResultado() == -1){ 
			ativ.setKmb(-0.5f);
		}
		else if(ativ.getPredicao() == 0 && ativ.getResultado() == 0){ 
			ativ.setKmb(0);
		}
		else if(ativ.getPredicao() == 0 && ativ.getResultado() == 1){ 
			ativ.setKmb(0.5f);
		}
		else if(ativ.getPredicao() == 1 && ativ.getResultado() == -1){ 
			ativ.setKmb(-1);
		}
		else if(ativ.getPredicao() == 1 && ativ.getResultado() == 0){ 
			ativ.setKmb(-0.5f);
		}
		else if(ativ.getPredicao() == 1 && ativ.getResultado() == 1){ 
			ativ.setKmb(0);
		}
	}

	/**
	 * Metodo responsavel por consultar o nível do usuário
	 * @param Json String (Usuario)
	 * @return Float kma
	 * @throws Exception
	 */
	@POST
	@Path("/getKmbMedio")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Float getKmbMedio(String usuarioStr){
		Usuario usuario = null;
		AtividadeDao adao = null;
		Gson gson = new Gson();

		usuario = gson.fromJson(usuarioStr, Usuario.class);
		
		adao = adao.getInstancia();

		return adao.consultarKmbMedio(usuario);
	}
	
	/**
	 * Metodo responsavel por consultar o nivel do usuario
	 * @param Json String (Usuario)
	 * @return Float kma
	 * @throws Exception
	 */
	@POST
	@Path("/getNivel")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Float getNivel(String usuarioStr){
		/*
		 	List ativs = getAtividades(usuarioStr);
			if(getKmbMedio(usuarioStr)==1){
				switch ativs.size(){
					case 2:
						return 1;
						break;
					case 4:
						return 2;
						break;
					default:
						return 0;
						break;
				}
			}
			else
				//make something cool!;

		 */
		
		Usuario usuario = null;
		AtividadeDao adao = null;
		Gson gson = new Gson();

		usuario = gson.fromJson(usuarioStr, Usuario.class);

		adao = adao.getInstancia();

		return adao.consultarKmbMedio(usuario);
	}
}
