package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma tentativa de criar uma comunidade falha
 * porque j� existe uma comunidade com o mesmo nome no sistema.
 *
 * <p>Esta exce��o � uma subclasse de {@link RuntimeException}, portanto n�o
 * requer declara��o expl�cita em cl�usulas throws.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @see RuntimeException
 */
public class ComunidadeJaExisteException extends RuntimeException {
  /**
   * Constr�i uma nova exce��o com a mensagem padr�o indicando que
   * j� existe uma comunidade com o nome especificado.
   */
  public ComunidadeJaExisteException() {
    super("Comunidade com esse nome j� existe.");
  }
}