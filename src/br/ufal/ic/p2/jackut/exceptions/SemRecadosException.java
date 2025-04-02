package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando não há recados disponíveis para leitura.
 *
 * @author IuryNogueira
 */
public class SemRecadosException extends RuntimeException {
    /**
     * Constrói a exceção com a mensagem padrão indicando ausência de recados
     */
    public SemRecadosException() {
        super("Não há recados.");
    }
}