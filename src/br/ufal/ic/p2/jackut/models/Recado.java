package br.ufal.ic.p2.jackut.models;

import java.io.Serializable;

/**
 * Representa um recado enviado entre usu�rios do sistema.
 * <p>
 * Armazena o conte�do da mensagem e o identificador do remetente,
 * sendo serializ�vel para permitir persist�ncia.
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
     * Constr�i um novo recado com remetente e mensagem.
     *
     * @param remetente Login do usu�rio que enviou o recado
     * @param mensagem Conte�do textual do recado
     */
    public Recado(String remetente, String mensagem) {
        this.remetente = remetente;
        this.mensagem = mensagem;
    }

    // Novo m�todo adicionado
    public String getRemetente() {
        return remetente;
    }

    /**
     * Retorna a representa��o textual do recado (apenas o conte�do da mensagem).
     *
     * @return String contendo a mensagem do recado
     */
    @Override
    public String toString() {
        return mensagem;
    }
}