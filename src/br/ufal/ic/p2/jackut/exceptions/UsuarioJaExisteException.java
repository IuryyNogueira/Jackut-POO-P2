package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta criar um usu�rio com login j� existente.
 *
 * @author IuryNogueira
 */
public class UsuarioJaExisteException extends RuntimeException {
    /**
     * Constr�i uma nova exce��o com a mensagem padr�o de usu�rio j� existente.
     */
    public UsuarioJaExisteException() {
        super("Conta com esse nome j� existe.");
    }
}