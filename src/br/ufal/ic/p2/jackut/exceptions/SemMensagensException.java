package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar mensagens de um usu�rio,
 * mas n�o h� mensagens dispon�veis.
 *
 * <p>Esta � uma exce��o n�o verificada (unchecked) que estende {@link RuntimeException},
 * indicando que n�o existem mensagens para serem recuperadas no momento.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class SemMensagensException extends RuntimeException {

  /**
   * Constr�i uma nova SemMensagensException com a mensagem padr�o
   * indicando que n�o h� mensagens dispon�veis.
   */
  public SemMensagensException() {
    super("N�o h� mensagens.");
  }
}