package br.com.touchhealth.treinamento.model;


import java.util.Date;


public class Pessoa {
    private Long id;
    private String nome;
    private Date dataNascimento;
    private Sexo sexo;
    private String cpf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pessoa id(Long id) {
        this.setId(id);
        return this;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Pessoa nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Pessoa dataNascimento(Date dataNascimento) {
        this.setDataNascimento(dataNascimento);
        return this;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Pessoa sexo(Sexo sexo) {
        this.setSexo(sexo);
        return this;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Pessoa cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

}
