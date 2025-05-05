package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.services.GerenciadorUsuarios;
import br.ufal.ic.p2.jackut.models.Usuario;
import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.Community;
import java.io.Serializable;
import java.util.*;

/**
 * Controla a cria��o e gerenciamento de comunidades no sistema,
 * incluindo membros, descri��o e envio de mensagens.
 * Mant�m refer�ncias a comunidades armazenadas em mem�ria e
 * interage com GerenciadorUsuarios para notifica��es.
 *
 * @author Iury
 * @version 1.0
 * @since 2025-05-04
 */
public class GerenciadorComunidades implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Community> comunidades = new HashMap<>();
    private final GerenciadorUsuarios usuarios; // Refer�ncia ao gerenciador de usu�rios

    /**
     * Constr�i o gerenciador de comunidades usando o gerenciador de usu�rios fornecido.
     *
     * @param usuarios inst�ncia de GerenciadorUsuarios para notifica��es e valida��es
     */
    public GerenciadorComunidades(GerenciadorUsuarios usuarios) {
        this.usuarios = usuarios;
    }

    /**
     * Cria uma nova comunidade.
     *
     * @param nome      nome �nico da comunidade
     * @param descricao descri��o da comunidade
     * @param dono      login do usu�rio a ser definido como propriet�rio
     * @throws ComunidadeJaExisteException se j� existir comunidade com mesmo nome
     */
    public void criarComunidade(String nome, String descricao, String dono) {
        if (comunidades.containsKey(nome)) {
            throw new ComunidadeJaExisteException();
        }
        comunidades.put(nome, new Community(nome, descricao, dono));
    }

    /**
     * Adiciona um membro a uma comunidade existente.
     *
     * @param nomeComunidade nome da comunidade
     * @param login          login do usu�rio a ser adicionado
     * @throws ComunidadeNaoEncontradaException se a comunidade n�o existir
     */
    public void adicionarMembro(String nomeComunidade, String login) {
        Community comunidade = getComunidade(nomeComunidade);
        comunidade.addMember(login);
    }

    /**
     * Retorna a inst�ncia de Community pelo nome.
     *
     * @param nome nome da comunidade
     * @return inst�ncia da comunidade
     * @throws ComunidadeNaoEncontradaException se a comunidade n�o existir
     */
    public Community getComunidade(String nome) {
        Community c = comunidades.get(nome);
        if (c == null) throw new ComunidadeNaoEncontradaException();
        return c;
    }

    /**
     * Obt�m a descri��o de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return descri��o cadastrada
     * @throws ComunidadeNaoEncontradaException se a comunidade n�o existir
     */
    public String getDescricao(String nome) {
        return getComunidade(nome).getDescription();
    }

    /**
     * Obt�m o login do propriet�rio de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return login do dono
     * @throws ComunidadeNaoEncontradaException se a comunidade n�o existir
     */
    public String getDono(String nome) {
        return getComunidade(nome).getOwner();
    }

    /**
     * Lista membros de uma comunidade.
     *
     * @param nome nome da comunidade
     * @return string no formato "{membro1,membro2,...}"
     * @throws ComunidadeNaoEncontradaException se a comunidade n�o existir
     */
    public String listarMembros(String nome) {
        Community c = getComunidade(nome);
        return "{" + String.join(",", c.getMembers()) + "}";
    }

    /**
     * Envia uma mensagem textual a todos os membros de uma comunidade.
     *
     * @param nomeComunidade nome da comunidade
     * @param mensagem       texto da mensagem a ser entregue
     * @throws ComunidadeNaoEncontradaException se a comunidade n�o existir
     */
    public void enviarMensagem(String nomeComunidade, String mensagem) {
        Community comunidade = getComunidade(nomeComunidade);
        for (String membro : comunidade.getMembers()) {
            Usuario usuario = usuarios.getUsuario(membro);
            usuario.receberMensagem(mensagem);
        }
    }

    /**
     * Remove todas as comunidades pertencentes a um usu�rio e limpa sua participa��o nas demais.
     *
     * @param login login do usu�rio cujas comunidades devem ser removidas ou limpas
     */
    public void removerUsuarioDeTodasComunidades(String login) {
        List<String> comunidadesParaDeletar = new ArrayList<>();
        for (Map.Entry<String, Community> entry : comunidades.entrySet()) {
            if (entry.getValue().getOwner().equals(login)) {
                comunidadesParaDeletar.add(entry.getKey());
            }
        }
        for (String nomeComunidade : comunidadesParaDeletar) {
            comunidades.remove(nomeComunidade);
        }
        for (Community c : comunidades.values()) {
            c.getMembers().remove(login);
        }
        usuarios.removerComunidadeDeTodosUsuarios(comunidadesParaDeletar);
    }

    /**
     * Remove todas as comunidades armazenadas, resetando o gerenciador.
     */
    public void zerar() {
        comunidades.clear();
    }
}
