package br.com.ucb.modelo;

import java.io.Serializable;
import java.sql.Time;

import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

/**
 *  Classe que contem os dados de uma atividade
 *  @author Jean Silvestre
 */
@XmlRootElement
public class Atividade implements Serializable{
	
	
	private static final long serialVersionUID = -8433919224984350110L;
	@Expose private int        	id;
	@Expose private int        	uid;
	@Expose private String     	nome;
	private Time 				tempoEstimado;
	@Expose private int        	predicao;
	@Expose private String 	   	estrategia;
	@Expose private String     	recursos;
	@Expose private String     	grauAtencao;
	@Expose private String     	comprensao;
	@Expose private String     	objetivo;
	@Expose private String     	anotacoes;
	@Expose private float      	kma;
	private Time  				tempoGasto;
	@Expose private int        	resultado;
	@Expose private float 	   	kmb;
	
	public Atividade() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Time getTempoEstimado() {
		return tempoEstimado;
	}

	public void setTempoEstimado(Time tempoEstimado) {
		this.tempoEstimado = tempoEstimado;
	}

	public int getPredicao() {
		return predicao;
	}

	public void setPredicao(int predicao) {
		this.predicao = predicao;
	}

	public String getEstrategia() {
		return estrategia;
	}

	public void setEstrategia(String estrategia) {
		this.estrategia = estrategia;
	}

	public String getRecursos() {
		return recursos;
	}

	public void setRecursos(String recursos) {
		this.recursos = recursos;
	}

	public String getGrauAtencao() {
		return grauAtencao;
	}

	public void setGrauAtencao(String grauAtencao) {
		this.grauAtencao = grauAtencao;
	}

	public String getComprensao() {
		return comprensao;
	}

	public void setComprensao(String comprensao) {
		this.comprensao = comprensao;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}

	public String getAnotacoes() {
		return anotacoes;
	}

	public void setAnotacoes(String anotacoes) {
		this.anotacoes = anotacoes;
	}

	public float getKma() {
		return kma;
	}

	public void setKma(float kma) {
		this.kma = kma;
	}

	public Time getTempoGasto() {
		return tempoGasto;
	}

	public void setTempoGasto(Time tempoGasto) {
		this.tempoGasto = tempoGasto;
	}

	public int getResultado() {
		return resultado;
	}

	public void setResultado(int resultado) {
		this.resultado = resultado;
	}

	public float getKmb() {
		return kmb;
	}

	public void setKmb(float kmb) {
		this.kmb = kmb;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((anotacoes == null) ? 0 : anotacoes.hashCode());
		result = prime * result
				+ ((comprensao == null) ? 0 : comprensao.hashCode());
		result = prime * result
				+ ((estrategia == null) ? 0 : estrategia.hashCode());
		result = prime * result
				+ ((grauAtencao == null) ? 0 : grauAtencao.hashCode());
		result = prime * result + id;
		result = prime * result + Float.floatToIntBits(kma);
		result = prime * result + Float.floatToIntBits(kmb);
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((objetivo == null) ? 0 : objetivo.hashCode());
		result = prime * result + predicao;
		result = prime * result
				+ ((recursos == null) ? 0 : recursos.hashCode());
		result = prime * result + resultado;
		result = prime * result
				+ ((tempoEstimado == null) ? 0 : tempoEstimado.hashCode());
		result = prime * result
				+ ((tempoGasto == null) ? 0 : tempoGasto.hashCode());
		result = prime * result + uid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atividade other = (Atividade) obj;
		if (anotacoes == null) {
			if (other.anotacoes != null)
				return false;
		} else if (!anotacoes.equals(other.anotacoes))
			return false;
		if (comprensao == null) {
			if (other.comprensao != null)
				return false;
		} else if (!comprensao.equals(other.comprensao))
			return false;
		if (estrategia == null) {
			if (other.estrategia != null)
				return false;
		} else if (!estrategia.equals(other.estrategia))
			return false;
		if (grauAtencao == null) {
			if (other.grauAtencao != null)
				return false;
		} else if (!grauAtencao.equals(other.grauAtencao))
			return false;
		if (id != other.id)
			return false;
		if (Float.floatToIntBits(kma) != Float.floatToIntBits(other.kma))
			return false;
		if (Float.floatToIntBits(kmb) != Float.floatToIntBits(other.kmb))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (objetivo == null) {
			if (other.objetivo != null)
				return false;
		} else if (!objetivo.equals(other.objetivo))
			return false;
		if (predicao != other.predicao)
			return false;
		if (recursos == null) {
			if (other.recursos != null)
				return false;
		} else if (!recursos.equals(other.recursos))
			return false;
		if (resultado != other.resultado)
			return false;
		if (tempoEstimado == null) {
			if (other.tempoEstimado != null)
				return false;
		} else if (!tempoEstimado.equals(other.tempoEstimado))
			return false;
		if (tempoGasto == null) {
			if (other.tempoGasto != null)
				return false;
		} else if (!tempoGasto.equals(other.tempoGasto))
			return false;
		if (uid != other.uid)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Atividade [id=" + id + ", uid=" + uid + ", nome=" + nome
				+ ", tempoEstimado=" + tempoEstimado + ", predicao=" + predicao
				+ ", estrategia=" + estrategia + ", recursos=" + recursos
				+ ", grauAtencao=" + grauAtencao + ", comprensao=" + comprensao
				+ ", objetivo=" + objetivo + ", anotacoes=" + anotacoes
				+ ", kma=" + kma + ", tempoGasto=" + tempoGasto
				+ ", resultado=" + resultado + ", kmb=" + kmb + "]";
	}

}