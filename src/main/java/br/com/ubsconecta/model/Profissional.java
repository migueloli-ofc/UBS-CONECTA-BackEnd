package br.com.ubsconecta.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "profissional")
public class Profissional {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    private String nome;

    private String cpf;
    
    private String cargo;
    
    @Column (name = "conselho_profissional")
    private String conselhoProfissional;
    
    @Column (name = "registro_profissional")
    private String registroProfissional;
    
    private String especialidade;
    
    private String telefone;
    
    private String email;
    
    @Column (name = "id_usuario")
    private Integer idUsuario;
    
    @Column (name = "id_ubs")
    private Integer idUbs;

    
    public Profissional() {
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
    
    public Integer getIdUbs() {
    	return idUbs;
    }
    
    public void setIdUbs(Integer idUbs) {
    	this.idUbs = idUbs;
    }
}
