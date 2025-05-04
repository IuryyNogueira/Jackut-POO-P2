package br.ufal.ic.p2.jackut.services;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Gerencia as sess�es de usu�rios no sistema, respons�vel pela cria��o,
 * armazenamento e valida��o de sess�es ativas.
 * <p>
 * Mant�m o mapeamento entre IDs de sess�o �nicos e logins de usu�rios autenticados.
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
     * Cria uma nova sess�o para um usu�rio autenticado.
     *
     * @param login Login do usu�rio para o qual a sess�o ser� criada
     * @return ID �nico da sess�o gerada (formato UUID)
     * @throws IllegalArgumentException Se o login for nulo ou vazio
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
     * @param idSessao ID da sess�o a ser verificada
     * @return Login do usu�rio associado � sess�o, ou null se a sess�o n�o existir
     */
    public String getLogin(String idSessao) {
        return sessoes.get(idSessao);
    }

    /**
     * Encerra todas as sess�es ativas, reiniciando o gerenciador.
     */
    public void zerar() {
        sessoes.clear();
    }


    public void removerSessoesDoUsuario(String login) {
        sessoes.values().removeIf(v -> v.equals(login));
    }
}