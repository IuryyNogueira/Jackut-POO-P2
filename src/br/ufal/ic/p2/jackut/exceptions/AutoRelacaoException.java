package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação tenta criar uma relação de um usuário consigo mesmo.
 *
 * <p>Esta exceção é uma subclasse de {@link RuntimeException}, portanto não precisa ser declarada
 * explicitamente em cláusulas throws.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @see RuntimeException
 */
public class AutoRelacaoException extends RuntimeException {
  /**
   * Constrói uma nova exceção com a mensagem de detalhe especificada.
   *
   * @param mensagem a mensagem de detalhe (que é salva para posterior recuperação
   *                pelo método {@link #getMessage()})
   */
  public AutoRelacaoException(String mensagem) {
    super(mensagem);
  }
}