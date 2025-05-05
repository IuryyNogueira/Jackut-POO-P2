package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma tentativa de criar uma comunidade falha
 * porque já existe uma comunidade com o mesmo nome no sistema.
 *
 * <p>Esta exceção é uma subclasse de {@link RuntimeException}, portanto não
 * requer declaração explícita em cláusulas throws.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @see RuntimeException
 */
public class ComunidadeJaExisteException extends RuntimeException {
  /**
   * Constrói uma nova exceção com a mensagem padrão indicando que
   * já existe uma comunidade com o nome especificado.
   */
  public ComunidadeJaExisteException() {
    super("Comunidade com esse nome já existe.");
  }
}