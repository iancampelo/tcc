package assistente.br.ucb.tcc.assistentereflexivo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Created by ian.campelo on 10/14/14.
 */
public class Act extends User implements Serializable{

    @SerializedName("uid")
    private int userid;
    private String  nome;
    private Time tempoEstimado;
    private String  predicao;
    private String  estrategia;
    private String  recursos;
    private String  grauAtencao;
    private String  comprensao;
    private String  objetivo;
    private String  anotacoes;
    private float   kma;
    private Time tempoGasto;
    private float   kmb;

    public Act() {super();}

    public Act(String nome, Time tempoEstimado, String predicao, String estrategia,
               String recursos, String grauAtencao, String comprensao, String objetivo,
               String anotacoes, float kma, Time tempoGasto, float kmb, int uid) {
        super();
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

    public Time getTempoEstimado() {
        return tempoEstimado;
    }

    public void setTempoEstimado(Time tempoEstimado) {
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

    public Time getTempoGasto() {
        return tempoGasto;
    }

    public void setTempoGasto(Time tempoGasto) {
        this.tempoGasto = tempoGasto;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Act)) return false;

        Act act = (Act) o;

        if (Float.compare(act.kma, kma) != 0) return false;
        if (Float.compare(act.kmb, kmb) != 0) return false;
        if (userid != act.userid) return false;
        if (anotacoes != null ? !anotacoes.equals(act.anotacoes) : act.anotacoes != null)
            return false;
        if (comprensao != null ? !comprensao.equals(act.comprensao) : act.comprensao != null)
            return false;
        if (estrategia != null ? !estrategia.equals(act.estrategia) : act.estrategia != null)
            return false;
        if (grauAtencao != null ? !grauAtencao.equals(act.grauAtencao) : act.grauAtencao != null)
            return false;
        if (nome != null ? !nome.equals(act.nome) : act.nome != null) return false;
        if (objetivo != null ? !objetivo.equals(act.objetivo) : act.objetivo != null) return false;
        if (predicao != null ? !predicao.equals(act.predicao) : act.predicao != null) return false;
        if (recursos != null ? !recursos.equals(act.recursos) : act.recursos != null) return false;
        if (tempoEstimado != null ? !tempoEstimado.equals(act.tempoEstimado) : act.tempoEstimado != null)
            return false;
        if (tempoGasto != null ? !tempoGasto.equals(act.tempoGasto) : act.tempoGasto != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userid;
        result = 31 * result + (nome != null ? nome.hashCode() : 0);
        result = 31 * result + (tempoEstimado != null ? tempoEstimado.hashCode() : 0);
        result = 31 * result + (predicao != null ? predicao.hashCode() : 0);
        result = 31 * result + (estrategia != null ? estrategia.hashCode() : 0);
        result = 31 * result + (recursos != null ? recursos.hashCode() : 0);
        result = 31 * result + (grauAtencao != null ? grauAtencao.hashCode() : 0);
        result = 31 * result + (comprensao != null ? comprensao.hashCode() : 0);
        result = 31 * result + (objetivo != null ? objetivo.hashCode() : 0);
        result = 31 * result + (anotacoes != null ? anotacoes.hashCode() : 0);
        result = 31 * result + (kma != +0.0f ? Float.floatToIntBits(kma) : 0);
        result = 31 * result + (tempoGasto != null ? tempoGasto.hashCode() : 0);
        result = 31 * result + (kmb != +0.0f ? Float.floatToIntBits(kmb) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Act{" +
                "userid=" + userid +
                ", nome='" + nome + '\'' +
                ", tempoEstimado=" + tempoEstimado +
                ", predicao='" + predicao + '\'' +
                ", estrategia='" + estrategia + '\'' +
                ", recursos='" + recursos + '\'' +
                ", grauAtencao='" + grauAtencao + '\'' +
                ", comprensao='" + comprensao + '\'' +
                ", objetivo='" + objetivo + '\'' +
                ", anotacoes='" + anotacoes + '\'' +
                ", kma=" + kma +
                ", tempoGasto=" + tempoGasto +
                ", kmb=" + kmb +
                '}';
    }

    public String toJsonAct() {
        return "{" +
                "\"userid\":\"" + userid + '\"'+
                ", \"nome\":\"" + nome + '\"' +
                ", \"tempoEstimado\":\"" + tempoEstimado + '\"' +
                ", \"predicao\":\"" + predicao + '\"' +
                ", \"estrategia\":\"" + estrategia + '\"' +
                ", \"recursos\":\"" + recursos + '\"' +
                ", \"grauAtencao\":\"" + grauAtencao + '\"' +
                ", \"comprensao\":\"" + comprensao + '\"' +
                ", \"objetivo\":\"" + objetivo + '\"' +
                ", \"anotacoes\":\"" + anotacoes + '\"' +
                ", \"kma\":\"" + kma + '\"' +
                ", \"tempoGasto\":\"" + tempoGasto + '\"' +
                ", \"kmb\":\"" + kmb+ '\"'  +
                '}';
    }
}
