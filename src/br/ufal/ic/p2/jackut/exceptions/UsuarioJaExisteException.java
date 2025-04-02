package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação tenta criar um usuário com login já existente.
 *
 * @author IuryNogueira
 */
public class UsuarioJaExisteException extends RuntimeException {
    /**
     * Constrói uma nova exceção com a mensagem padrão de usuário já existente.
     */
    public UsuarioJaExisteException() {
        super("Conta com esse nome já existe.");
    }
}