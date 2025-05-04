package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Representa um recado enviado entre usuários do sistema.
 * <p>
 * Armazena o conteúdo da mensagem e o identificador do remetente,
 * sendo serializável para permitir persistência.
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 * @see Serializable
 */
public class Recado implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String remetente;
    private final String mensagem;

    /**
     * Constrói um novo recado com remetente e mensagem.
     *
     * @param remetente Login do usuário que enviou o recado
     * @param mensagem Conteúdo textual do recado
     */
    public Recado(String remetente, String mensagem) {
        this.remetente = remetente;
        this.mensagem = mensagem;
    }

    // Novo método adicionado
    public String getRemetente() {
        return remetente;
    }

    /**
     * Retorna a representação textual do recado (apenas o conteúdo da mensagem).
     *
     * @return String contendo a mensagem do recado
     */
    @Override
    public String toString() {
        return mensagem;
    }
}