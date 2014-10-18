package assistente.br.ucb.tcc.assistentereflexivo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ian.campelo on 10/14/14.
 */
public class Act extends User implements Serializable{

    @SerializedName("uid")
    private int userid;
    private String  nome;
    private float   tempoEstimado;
    private String  predicao;
    private String  estrategia;
    private String  recursos;
    private String  grauAtencao;
    private String  comprensao;
    private String  objetivo;
    private String  anotacoes;
    private float   kma;
    private float   tempoGasto;
    private float   kmb;

    public Act() {}

    public Act(String nome, float tempoEstimado, String predicao, String estrategia,
               String recursos, String grauAtencao, String comprensao, String objetivo,
               String anotacoes, float kma, float tempoGasto, float kmb, int uid) {

        setNome(nome);
        setTempoEstimado(tempoEstimado);
        setPredicao(predicao);
        setEstrategia(estrategia);
        setRecursos(recursos);
        setGrauAtencao(grauAtencao);
        setComprensao(comprensao);
        setObjetivo(objetivo);
        setAnotacoes(anotacoes);
        setKma(kma);
        setTempoGasto(tempoGasto);
        setKmb(kmb);
        setUserid(uid);
    }

    public float getKmb() {
        return kmb;
    }

    public void setKmb(float kmb) {
        this.kmb = kmb;
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

    public float getKma() {
        return kma;
    }

    public void setKma(float kma) {
        this.kma = kma;
    }

    public float getTempoGasto() {
        return tempoGasto;
    }

    public void setTempoGasto(float tempoGasto) {
        this.tempoGasto = tempoGasto;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
