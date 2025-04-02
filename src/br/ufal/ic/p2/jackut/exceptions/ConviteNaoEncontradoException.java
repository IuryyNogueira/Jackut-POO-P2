package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um convite espec�fico n�o pode ser encontrado no sistema.
 *
 * @author IuryNogueira
 */
public class ConviteNaoEncontradoException extends RuntimeException {
    /**
     * Constr�i uma nova exce��o com a mensagem padr�o de convite n�o encontrado.
     */
    public ConviteNaoEncontradoException() {
        super("Convite n�o encontrado.");
    }
}