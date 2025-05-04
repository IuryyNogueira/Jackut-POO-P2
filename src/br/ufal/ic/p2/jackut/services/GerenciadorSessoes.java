package br.ufal.ic.p2.jackut.services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerencia as sessões de usuários no sistema, responsável pela criação,
 * armazenamento e validação de sessões ativas.
 * <p>
 * Mantém o mapeamento entre IDs de sessão únicos e logins de usuários autenticados.
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 * @see Serializable
 */
public class GerenciadorSessoes implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, String> sessoes = new HashMap<>();

    /**
     * Cria uma nova sessão para um usuário autenticado.
     *
     * @param login Login do usuário para o qual a sessão será criada
     * @return ID único da sessão gerada (formato UUID)
     * @throws IllegalArgumentException Se o login for nulo ou vazio
     */
    public String criarSessao(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login inválido para criação de sessão");
        }

        String id = UUID.randomUUID().toString();
        sessoes.put(id, login);
        return id;
    }

    /**
     * Recupera o login associado a uma sessão.
     *
     * @param idSessao ID da sessão a ser verificada
     * @return Login do usuário associado à sessão, ou null se a sessão não existir
     */
    public String getLogin(String idSessao) {
        return sessoes.get(idSessao);
    }

    /**
     * Encerra todas as sessões ativas, reiniciando o gerenciador.
     */
    public void zerar() {
        sessoes.clear();
    }


    public void removerSessoesDoUsuario(String login) {
        sessoes.values().removeIf(v -> v.equals(login));
    }
}