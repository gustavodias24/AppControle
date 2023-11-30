package benicio.solucoes.appcontrole.model;

public class FamiliaModel {
    String id, idLocalidade, nome, nomeLocalidade;

    public FamiliaModel(String id, String idLocalidade, String nome, String nomeLocalidade) {
        this.id = id;
        this.idLocalidade = idLocalidade;
        this.nome = nome;
        this.nomeLocalidade = nomeLocalidade;
    }

    @Override
    public String toString() {
        return "Família: " + nome ;
    }



    public String getNomeLocalidade() {
        return nomeLocalidade;
    }

    public void setNomeLocalidade(String nomeLocalidade) {
        this.nomeLocalidade = nomeLocalidade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdLocalidade() {
        return idLocalidade;
    }

    public void setIdLocalidade(String idLocalidade) {
        this.idLocalidade = idLocalidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
