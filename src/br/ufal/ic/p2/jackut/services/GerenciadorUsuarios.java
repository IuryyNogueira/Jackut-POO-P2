package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import java.io.Serializable;
import java.util.*;

/**
 * Gerencia todas as operações relacionadas a usuários, incluindo criação, armazenamento,
 * relacionamentos de amizade e persistência de dados.
 * <p>
 * Responsável por manter a integridade das relações de amizade e convites entre usuários.
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 * @see Usuario
 */
public class GerenciadorUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Usuario> usuarios = new HashMap<>();

    /**
     * Cria e armazena um novo usuário no sistema.
     *
     * @param login Identificador único do usuário
     * @param senha Senha de acesso
     * @param nome Nome completo do usuário
     * @throws UsuarioJaExisteException Se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (usuarios.containsKey(login)) {
            throw new UsuarioJaExisteException();
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Recupera um usuário pelo seu login.
     *
     * @param login Identificador do usuário
     * @return Instância do usuário correspondente
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    public Usuario getUsuario(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario;
    }

    /**
     * Estabelece relação de amizade entre dois usuários.
     * <p>
     * Fluxo de operação:
     * 1. Verifica auto-amizade
     * 2. Checa amizade existente
     * 3. Verifica convites pendentes
     * 4. Cria nova solicitação ou confirma amizade
     * </p>
     *
     * @param loginUsuario Login do usuário iniciador
     * @param loginAmigo Login do usuário alvo
     * @throws AutoAmizadeException Se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     * @throws AmigoJaAdicionadoException Se já forem amigos ou houver solicitação pendente
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo) {
        Usuario usuario = getUsuario(loginUsuario);
        Usuario amigo = getUsuario(loginAmigo);

        if (loginUsuario.equals(loginAmigo)) {
            throw new AutoAmizadeException();
        }

        if (usuario.getAmigos().contains(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usuário já está adicionado como amigo.");
        }

        if (usuario.temConvitePara(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }

        if (usuario.temConviteDe(loginAmigo)) {
            // Confirmação mútua de amizade
            usuario.adicionarAmigo(loginAmigo);
            amigo.adicionarAmigo(loginUsuario);
            usuario.removerConvite(loginAmigo);
            amigo.removerConvite(loginUsuario);
        } else {
            // Cria novo convite
            usuario.adicionarConvite(loginAmigo, Usuario.ConviteStatus.ENVIADO);
            amigo.adicionarConvite(loginUsuario, Usuario.ConviteStatus.RECEBIDO);
        }
    }

    /**
     * Verifica se dois usuários têm amizade mútua.
     *
     * @param login1 Primeiro usuário
     * @param login2 Segundo usuário
     * @return true se forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException Se algum usuário não existir
     */
    public boolean saoAmigos(String login1, String login2) {
        Usuario u1 = getUsuario(login1);
        Usuario u2 = getUsuario(login2);
        return u1.getAmigos().contains(login2) && u2.getAmigos().contains(login1);
    }

    /**
     * Gera lista formatada de amigos de um usuário.
     *
     * @param login Usuário alvo
     * @return String no formato "{amigo1,amigo2}" com amigos em ordem alfabética
     * @throws UsuarioNaoEncontradoException Se o usuário não existir
     */
    public String listarAmigos(String login) {
        Usuario usuario = getUsuario(login);
        return "{" + String.join(",", usuario.getAmigosOrdenados()) + "}";
    }

    /**
     * Reinicia o gerenciador removendo todos os usuários.
     */
    public void zerar() {
        usuarios.clear();
    }
}
