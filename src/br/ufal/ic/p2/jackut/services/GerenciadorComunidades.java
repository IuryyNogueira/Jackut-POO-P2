package br.ufal.ic.p2.jackut.services;
import br.ufal.ic.p2.jackut.services.GerenciadorUsuarios;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.Community;
import br.ufal.ic.p2.jackut.models.Usuario;
import java.io.Serializable;
import java.util.*;

public class GerenciadorComunidades implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Community> comunidades = new HashMap<>();
    private final GerenciadorUsuarios usuarios; // Referência ao gerenciador de usuários

    public void adicionarMembro(String nomeComunidade, String login) {
        Community comunidade = getComunidade(nomeComunidade);
        comunidade.addMember(login);
    }

    public void criarComunidade(String nome, String descricao, String dono) {
        if (comunidades.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }
        comunidades.put(nome, new Community(nome, descricao, dono));
    }


    public Community getComunidade(String nome) {
        Community c = comunidades.get(nome);
        if (c == null) throw new ComunidadeNaoEncontradaException();
        return c;
    }

    public String getDescricao(String nome) {
        return getComunidade(nome).getDescription();
    }

    public String getDono(String nome) {
        return getComunidade(nome).getOwner();
    }

    public String listarMembros(String nome) {
        Community c = getComunidade(nome);
        return "{" + String.join(",", c.getMembers()) + "}";
    }

    public void zerar() {
        comunidades.clear();
    }

    // Construtor modificado para receber a dependência
    public GerenciadorComunidades(GerenciadorUsuarios usuarios) {
        this.usuarios = usuarios;
    }

    // Método corrigido
    public void enviarMensagem(String nomeComunidade, String mensagem) {
        Community comunidade = getComunidade(nomeComunidade);
        for (String membro : comunidade.getMembers()) {
            Usuario usuario = usuarios.getUsuario(membro);
            usuario.receberMensagem(mensagem); // Mensagem pura, sem modificações
        }
    }
}