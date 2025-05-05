package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação tenta acessar uma comunidade que não existe no sistema.
 *
 * <p>Esta é uma exceção não verificada (unchecked) que estende {@link RuntimeException},
 * indicando que a comunidade solicitada não foi encontrada.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class ComunidadeNaoEncontradaException extends RuntimeException {

    /**
     * Constrói uma nova ComunidadeNaoEncontradaException com a mensagem padrão
     * indicando que a comunidade não existe.
     */
    public ComunidadeNaoEncontradaException() {
        super("Comunidade não existe.");
    }
}