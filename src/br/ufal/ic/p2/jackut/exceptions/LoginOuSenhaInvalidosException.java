package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando as credenciais de autentica��o s�o inv�lidas.
 * <p>
 * Pode ocorrer quando o login n�o existe ou a senha n�o corresponde ao usu�rio.
 * </p>
 *
 * @author IuryNogueira
 */
public class LoginOuSenhaInvalidosException extends RuntimeException {
    /**
     * Constr�i a exce��o com mensagem padr�o de credenciais inv�lidas
     */
    public LoginOuSenhaInvalidosException() {
        super("Login ou senha inv�lidos.");
    }
}