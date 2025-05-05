package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar uma comunidade que n�o existe no sistema.
 *
 * <p>Esta � uma exce��o n�o verificada (unchecked) que estende {@link RuntimeException},
 * indicando que a comunidade solicitada n�o foi encontrada.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class ComunidadeNaoEncontradaException extends RuntimeException {

    /**
     * Constr�i uma nova ComunidadeNaoEncontradaException com a mensagem padr�o
     * indicando que a comunidade n�o existe.
     */
    public ComunidadeNaoEncontradaException() {
        super("Comunidade n�o existe.");
    }
}