
package br.com.ubsconecta.dto;

import java.time.LocalDate;

public class PerfilRequest {

    private Integer idUsuario;
    private String tipoUsuario;

    private String nome;
    private String cpf;
    private String rg;
    private LocalDate dataNascimento;
    private String telefone;
    private String email;

    private String cargo;
    private String conselhoProfissional;
    private String registroProfissional;
    private String especialidade;
    private Integer idUbs;

    public PerfilRequest() {
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    
    public String getTipoUsuario() {
    	return tipoUsuario;
    }
    
    public void setTipoUsuario(String tipoUsuario) {
    	this.tipoUsuario = tipoUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getConselhoProfissional() {
        return conselhoProfissional;
    }

    public void setConselhoProfissional(String conselhoProfissional) {
        this.conselhoProfissional = conselhoProfissional;
    }

    public String getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Integer getIdUbs() {
        return idUbs;
    }

    public void setIdUbs(Integer idUbs) {
        this.idUbs = idUbs;
    }
}
