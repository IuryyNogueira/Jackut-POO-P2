package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação tenta acessar mensagens de um usuário,
 * mas não há mensagens disponíveis.
 *
 * <p>Esta é uma exceção não verificada (unchecked) que estende {@link RuntimeException},
 * indicando que não existem mensagens para serem recuperadas no momento.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class SemMensagensException extends RuntimeException {

  /**
   * Constrói uma nova SemMensagensException com a mensagem padrão
   * indicando que não há mensagens disponíveis.
   */
  public SemMensagensException() {
    super("Não há mensagens.");
  }
}