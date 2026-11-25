package br.com.ubsconecta.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_paciente")
    private Integer idPaciente;

    @Column(name = "id_profissional")
    private Integer idProfissional;

    @Column(name = "id_ubs")
    private Integer idUbs;

    @Column(name = "id_tipo_consulta")
    private Integer idTipoConsulta;

    @Column(name = "id_horarios")
    private Integer idHorarios;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_atendimento")
    private LocalDateTime dataAtendimento;

    private String motivo;

    private String status;
    
    @Transient
    private String nomePaciente;
    
    @Transient
    private String nomeUbs;

    public Consulta() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Integer getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(Integer idProfissional) {
        this.idProfissional = idProfissional;
    }

    public Integer getIdUbs() {
        return idUbs;
    }

    public void setIdUbs(Integer idUbs) {
        this.idUbs = idUbs;
    }

    public Integer getIdTipoConsulta() {
        return idTipoConsulta;
    }

    public void setIdTipoConsulta(Integer idTipoConsulta) {
        this.idTipoConsulta = idTipoConsulta;
    }

    public Integer getIdHorarios() {
        return idHorarios;
    }

    public void setIdHorarios(Integer idHorarios) {
        this.idHorarios = idHorarios;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public void setDataSolicitacao(LocalDateTime dataSolicitacao) {
        this.dataSolicitacao = dataSolicitacao;
    }

    public LocalDateTime getDataAtendimento() {
        return dataAtendimento;
    }

    public void setDataAtendimento(LocalDateTime dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getNomePaciente() {
    	return nomePaciente;
    }
    
    public void setNomePaciente(String nomePaciente) {
    	this.nomePaciente = nomePaciente;
    }
    
    public String getNomeUbs() {
    	return nomeUbs;
    }
    
    public void setNomeUbs(String nomeUbs) {
    	this.nomeUbs = nomeUbs;
    }
}