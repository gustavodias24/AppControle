package benicio.solucoes.appcontrole.model;

public class DoacaoModel {
    //tipo = exame ou cesta basica
    String id, idFamilia, idLocalidade, data, obs, tipo, nomeFamilia ;
    int quantidade = 1;

    int status = 0; // em andamento, cancelado, concluido

    @Override
    public String toString() {
        return  "Data: " + data + '\n' +
                "Tipo: " + tipo + '\n' +
                "Para família: " + nomeFamilia + '\n' +
                "Quantidade: " + quantidade + '\n' +
                "obs: " + obs ;
    }


    public DoacaoModel(String id, String idFamilia, String idLocalidade, String data, String obs, String tipo, String nomeFamilia, int quantidade) {
        this.id = id;
        this.idFamilia = idFamilia;
        this.idLocalidade = idLocalidade;
        this.data = data;
        this.obs = obs;
        this.tipo = tipo;
        this.nomeFamilia = nomeFamilia;
        this.quantidade = quantidade;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getIdLocalidade() {
        return idLocalidade;
    }

    public void setIdLocalidade(String idLocalidade) {
        this.idLocalidade = idLocalidade;
    }

    public String getNomeFamilia() {
        return nomeFamilia;
    }

    public void setNomeFamilia(String nomeFamilia) {
        this.nomeFamilia = nomeFamilia;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdFamilia() {
        return idFamilia;
    }

    public void setIdFamilia(String idFamilia) {
        this.idFamilia = idFamilia;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
