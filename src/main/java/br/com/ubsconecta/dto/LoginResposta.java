
package br.com.ubsconecta.dto;

public class LoginResposta {

    public String mensagem;
    public String tipoUsuario;
    public Integer idUsuario;

    public LoginResposta() {
    }

    public LoginResposta(String mensagem, String tipoUsuario) {
        this.mensagem = mensagem;
        this.tipoUsuario = tipoUsuario;
    }

    public LoginResposta(String mensagem, String tipoUsuario, Integer idUsuario) {
        this.mensagem = mensagem;
        this.tipoUsuario = tipoUsuario;
        this.idUsuario = idUsuario;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}
