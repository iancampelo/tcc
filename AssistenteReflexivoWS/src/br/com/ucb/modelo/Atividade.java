package br.com.ucb.modelo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Atividade{
	private int    id;
	private String nome;
	private float  tempoEstimado;
	private String predicao;
	private String estrategia;
	private String recursos;
	private String grauAtencao;
	private String comprensao;
	private String objetivo;
	private String anotacoes;
	private float  kma;
	private float  tempoGasto;
	
	private float kmb;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public float getTempoEstimado() {
		return tempoEstimado;
	}
	public void setTempoEstimado(float tempoEstimado) {
		this.tempoEstimado = tempoEstimado;
	}
	public float getTempoGasto() {
		return tempoGasto;
	}
	public void setTempoGasto(float tempoGasto) {
		this.tempoGasto = tempoGasto;
	}
	public float getKma() {
		return kma;
	}
	public void setKma(float kma) {
		this.kma = kma;
	}
	public float getKmb() {
		return kmb;
	}
	public void setKmb(float kmb) {
		this.kmb = kmb;
	}
	public String getPredicao() {
		return predicao;
	}
	public void setPredicao(String predicao) {
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
	
	@Override
	public String toString() {
		return "Atividade [id=" + id + ", nome=" + nome + ", tempoEstimado="
				+ tempoEstimado + ", predicao=" + predicao + ", estrategia="
				+ estrategia + ", recursos=" + recursos + ", grauAtencao="
				+ grauAtencao + ", comprensao=" + comprensao + ", objetivo="
				+ objetivo + ", anotacoes=" + anotacoes + ", kma=" + kma
				+ ", tempoGasto=" + tempoGasto + ", kmb=" + kmb + "]";
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
		result = prime * result
				+ ((predicao == null) ? 0 : predicao.hashCode());
		result = prime * result
				+ ((recursos == null) ? 0 : recursos.hashCode());
		result = prime * result + Float.floatToIntBits(tempoEstimado);
		result = prime * result + Float.floatToIntBits(tempoGasto);
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
		if (predicao == null) {
			if (other.predicao != null)
				return false;
		} else if (!predicao.equals(other.predicao))
			return false;
		if (recursos == null) {
			if (other.recursos != null)
				return false;
		} else if (!recursos.equals(other.recursos))
			return false;
		if (Float.floatToIntBits(tempoEstimado) != Float
				.floatToIntBits(other.tempoEstimado))
			return false;
		if (Float.floatToIntBits(tempoGasto) != Float
				.floatToIntBits(other.tempoGasto))
			return false;
		return true;
	}
	
}