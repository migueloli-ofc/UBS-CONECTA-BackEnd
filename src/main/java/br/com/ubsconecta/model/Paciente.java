package br.com.ubsconecta.model;


import java.time.LocalDate;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "paciente")
public class Paciente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    private String nome;

    private String cpf;
    
    private String rg;
    
    @Column (name = "data_nascimento")
    private LocalDate dataNascimento;
    
    private String telefone;
    
    private String email;
    
    @Column (name = "id_usuario")
    private Integer idUsuario;
    

    public Paciente() {
    }

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
    
    public Integer getIdUsuario() {
    	return idUsuario;
    }
    
    public void setIdUsuario(Integer idUsuario) {
    	this.idUsuario = idUsuario;
    }
}
