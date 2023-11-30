package benicio.solucoes.appcontrole.model;

public class LocalidadeModel {

    String id, tipo, nome;

    public LocalidadeModel() {
    }

    @Override
    public String toString() {
        return "Nome: " + nome + '\n' +   "Tipo: " + tipo;
    }

    public LocalidadeModel(String id, String tipo, String nome) {
        this.id = id;
        this.tipo = tipo;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
