package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando as credenciais de autenticação são inválidas.
 * <p>
 * Pode ocorrer quando o login não existe ou a senha não corresponde ao usuário.
 * </p>
 *
 * @author IuryNogueira
 */
public class LoginOuSenhaInvalidosException extends RuntimeException {
    /**
     * Constrói a exceção com mensagem padrão de credenciais inválidas
     */
    public LoginOuSenhaInvalidosException() {
        super("Login ou senha inválidos.");
    }
}