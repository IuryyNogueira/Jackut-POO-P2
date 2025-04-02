package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um login inv�lido � fornecido.
 * <p>
 * Um login � considerado inv�lido se estiver em branco, vazio ou n�o atender
 * aos crit�rios de formata��o do sistema.
 * </p>
 *
 * @author IuryNogueira
 */
public class LoginInvalidoException extends RuntimeException {
    /**
     * Constr�i a exce��o com mensagem padr�o indicando problema no login
     */
    public LoginInvalidoException() {
        super("Login inv�lido.");
    }
}