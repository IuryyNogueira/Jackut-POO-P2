package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta criar uma rela��o de um usu�rio consigo mesmo.
 *
 * <p>Esta exce��o � uma subclasse de {@link RuntimeException}, portanto n�o precisa ser declarada
 * explicitamente em cl�usulas throws.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @see RuntimeException
 */
public class AutoRelacaoException extends RuntimeException {
  /**
   * Constr�i uma nova exce��o com a mensagem de detalhe especificada.
   *
   * @param mensagem a mensagem de detalhe (que � salva para posterior recupera��o
   *                pelo m�todo {@link #getMessage()})
   */
  public AutoRelacaoException(String mensagem) {
    super(mensagem);
  }
}