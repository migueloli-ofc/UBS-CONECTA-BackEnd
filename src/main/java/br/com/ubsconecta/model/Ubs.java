package br.com.ubsconecta.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ubs")
public class Ubs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    
    @Column (name = "nome_ubs")
    private String nomeUbs;
    
    @Column (name = "endereco_ubs")
    private String enderecoUbs;
    
    @Column (name = "imagem_ubs")
    private String imagemUbs;
    
    @Column (name = "id_cidade")
    private Integer idCidade;
    
    @Column(name = "codigo_cnes", unique = true, length = 20)
    private String codigoCnes;

    public Ubs() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeUbs() {
        return nomeUbs;
    }

    public void setNomeUbs(String nomeUbs) {
        this.nomeUbs = nomeUbs;
    }

    public String getEnderecoUbs() {
        return enderecoUbs;
    }

    public void setEnderecoUbs(String enderecoUbs) {
        this.enderecoUbs = enderecoUbs;
    }

    public String getImagemUbs() {
        return imagemUbs;
    }

    public void setImagemUbs(String imagemUbs) {
        this.imagemUbs = imagemUbs;
    }

    public Integer getIdCidade() {
        return idCidade;
    }

    public void setIdCidade(Integer idCidade) {
        this.idCidade = idCidade;
    }
    
    public String getCodigoCnes() {
    	return codigoCnes;
    }
    
    public void setCodigoCnes(String codigoCnes) {
    	this.codigoCnes = codigoCnes;
    }
}