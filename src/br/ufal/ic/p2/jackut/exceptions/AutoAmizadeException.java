package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um usuário tenta adicionar a si mesmo como amigo.
 *
 * @author IuryNogueira
 */
public class AutoAmizadeException extends RuntimeException {
    /**
     * Constrói uma nova exceção com a mensagem padrão sobre autoamizade.
     */
    public AutoAmizadeException() {
        super("Usuário não pode adicionar a si mesmo como amigo.");
    }
}