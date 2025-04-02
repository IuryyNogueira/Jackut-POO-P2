package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um login inválido é fornecido.
 * <p>
 * Um login é considerado inválido se estiver em branco, vazio ou não atender
 * aos critérios de formatação do sistema.
 * </p>
 *
 * @author IuryNogueira
 */
public class LoginInvalidoException extends RuntimeException {
    /**
     * Constrói a exceção com mensagem padrão indicando problema no login
     */
    public LoginInvalidoException() {
        super("Login inválido.");
    }
}