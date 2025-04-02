package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um usu�rio tenta adicionar a si mesmo como amigo.
 *
 * @author IuryNogueira
 */
public class AutoAmizadeException extends RuntimeException {
    /**
     * Constr�i uma nova exce��o com a mensagem padr�o sobre autoamizade.
     */
    public AutoAmizadeException() {
        super("Usu�rio n�o pode adicionar a si mesmo como amigo.");
    }
}