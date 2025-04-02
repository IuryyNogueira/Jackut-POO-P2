package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando n�o h� recados dispon�veis para leitura.
 *
 * @author IuryNogueira
 */
public class SemRecadosException extends RuntimeException {
    /**
     * Constr�i a exce��o com a mensagem padr�o indicando aus�ncia de recados
     */
    public SemRecadosException() {
        super("N�o h� recados.");
    }
}