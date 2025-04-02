package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação recebe uma senha inválida.
 * <p>
 * Uma senha é considerada inválida se estiver em branco, vazia ou não atender
 * aos critérios de segurança do sistema.
 * </p>
 *
 * @author IuryNogueira
 */
public class SenhaInvalidaException extends RuntimeException {
    /**
     * Constrói a exceção com mensagem padrão indicando problema na senha
     */
    public SenhaInvalidaException() {
        super("Senha inválida.");
    }
}