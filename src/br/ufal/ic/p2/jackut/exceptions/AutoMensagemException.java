package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um usuário tenta enviar uma mensagem para si mesmo.
 *
 * @author IuryNogueira
 */
public class AutoMensagemException extends RuntimeException {
    /**
     * Constrói a exceção com uma mensagem personalizada sobre auto-mensagem.
     *
     * @param mensagem Descrição detalhada do erro (ex: "Usuário não pode enviar recado para si mesmo")
     */
    public AutoMensagemException(String mensagem) {
        super(mensagem);
    }
}