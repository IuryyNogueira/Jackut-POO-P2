package br.ufal.ic.p2.jackut.services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerencia as sess�es de usu�rios no sistema, incluindo cria��o, consulta e encerramento.
 * <p>
 * Mant�m o mapeamento entre identificadores de sess�o (UUID) e logins de usu�rios
 * autenticados, permitindo valida��o de opera��es restritas.
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
     * Cria uma nova sess�o para um usu�rio autenticado.
     *
     * @param login login do usu�rio para o qual gerar a sess�o
     * @return ID �nico da sess�o (UUID)
     * @throws IllegalArgumentException se o login for nulo ou vazio
     */
    public String criarSessao(String login) {
        if (login == null || login.isBlank()) {
            throw new IllegalArgumentException("Login inv�lido para cria��o de sess�o");
        }
        String id = UUID.randomUUID().toString();
        sessoes.put(id, login);
        return id;
    }

    /**
     * Recupera o login associado a uma sess�o.
     *
     * @param idSessao ID da sess�o
     * @return login do usu�rio ou null se a sess�o n�o existir
     */
    public String getLogin(String idSessao) {
        return sessoes.get(idSessao);
    }

    /**
     * Encerra todas as sess�es ativas, removendo todos os registros.
     */
    public void zerar() {
        sessoes.clear();
    }

    /**
     * Remove todas as sess�es relacionadas a um determinado usu�rio.
     *
     * @param login login do usu�rio cujas sess�es devem ser removidas
     */
    public void removerSessoesDoUsuario(String login) {
        sessoes.values().removeIf(v -> v.equals(login));
    }
}
