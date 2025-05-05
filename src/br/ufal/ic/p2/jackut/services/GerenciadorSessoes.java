package br.ufal.ic.p2.jackut.services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerencia as sessões de usuários no sistema, incluindo criação, consulta e encerramento.
 * <p>
 * Mantém o mapeamento entre identificadores de sessão (UUID) e logins de usuários
 * autenticados, permitindo validação de operações restritas.
 * </p>
 *
 * @author Iury
 * @version 1.0
 * @since 2025-05-04
 */
public class GerenciadorSessoes implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, String> sessoes = new HashMap<>();

    /**
     * Cria uma nova sessão para um usuário autenticado.
     *
     * @param login login do usuário para o qual gerar a sessão
     * @return ID único da sessão (UUID)
     * @throws IllegalArgumentException se o login for nulo ou vazio
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
     * @param idSessao ID da sessão
     * @return login do usuário ou null se a sessão não existir
     */
    public String getLogin(String idSessao) {
        return sessoes.get(idSessao);
    }

    /**
     * Encerra todas as sessões ativas, removendo todos os registros.
     */
    public void zerar() {
        sessoes.clear();
    }

    /**
     * Remove todas as sessões relacionadas a um determinado usuário.
     *
     * @param login login do usuário cujas sessões devem ser removidas
     */
    public void removerSessoesDoUsuario(String login) {
        sessoes.values().removeIf(v -> v.equals(login));
    }
}
