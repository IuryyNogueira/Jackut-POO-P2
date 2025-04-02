package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um convite específico não pode ser encontrado no sistema.
 *
 * @author IuryNogueira
 */
public class ConviteNaoEncontradoException extends RuntimeException {
    /**
     * Constrói uma nova exceção com a mensagem padrão de convite não encontrado.
     */
    public ConviteNaoEncontradoException() {
        super("Convite não encontrado.");
    }
}