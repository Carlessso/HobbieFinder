package com.hobbiefinder.ui;

public class Hobbie {
    public Integer id;
    public String nome;
    public String detalhes;
    public Integer vagas;
    public Integer ref_categoria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public Integer getVagas() {
        return vagas;
    }

    public void setVagas(Integer vagas) {
        this.vagas = vagas;
    }

    public Integer getRef_categoria() {
        return ref_categoria;
    }

    public void setRef_categoria(Integer ref_categoria) {
        this.ref_categoria = ref_categoria;
    }
}
