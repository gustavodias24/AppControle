package benicio.solucoes.appcontrole.model;

public class PessoaModel {
    String id, idFamilia ,nomeEleitor, dataNascimento, unidadeFederativa, municipio, zona, secao, numero, dataEmissao, idade;

    @Override
    public String toString() {
        return  "Nome do eleitor: " + nomeEleitor + '\n' +
                "Data de nascimento: " + dataNascimento + '\n' +
                "Unidade federativa: " + unidadeFederativa + '\n' +
                "Município: " + municipio + '\n' +
                "Zona: " + zona + '\n' +
                "Seção: " + secao + '\n' +
                "Número: " + numero + '\n' +
                "Data emissão: " + dataEmissao ;
    }

    public PessoaModel(String id, String idFamilia, String nomeEleitor, String dataNascimento, String unidadeFederativa, String municipio, String zona, String secao, String numero, String dataEmissao, String idade) {
        this.id = id;
        this.idFamilia = idFamilia;
        this.nomeEleitor = nomeEleitor;
        this.dataNascimento = dataNascimento;
        this.unidadeFederativa = unidadeFederativa;
        this.municipio = municipio;
        this.zona = zona;
        this.secao = secao;
        this.numero = numero;
        this.dataEmissao = dataEmissao;
        this.idade = idade;
    }

    public String getIdade() {
        return idade;
    }

    public void setIdade(String idade) {
        this.idade = idade;
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

    public String getNomeEleitor() {
        return nomeEleitor;
    }

    public void setNomeEleitor(String nomeEleitor) {
        this.nomeEleitor = nomeEleitor;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getUnidadeFederativa() {
        return unidadeFederativa;
    }

    public void setUnidadeFederativa(String unidadeFederativa) {
        this.unidadeFederativa = unidadeFederativa;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getSecao() {
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }
}
