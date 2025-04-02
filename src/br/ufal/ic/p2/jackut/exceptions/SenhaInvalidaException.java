package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o recebe uma senha inv�lida.
 * <p>
 * Uma senha � considerada inv�lida se estiver em branco, vazia ou n�o atender
 * aos crit�rios de seguran�a do sistema.
 * </p>
 *
 * @author IuryNogueira
 */
public class SenhaInvalidaException extends RuntimeException {
    /**
     * Constr�i a exce��o com mensagem padr�o indicando problema na senha
     */
    public SenhaInvalidaException() {
        super("Senha inv�lida.");
    }
}