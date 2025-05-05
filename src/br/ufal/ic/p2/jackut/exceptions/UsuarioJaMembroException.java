package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma tentativa de adicionar um usu�rio a uma comunidade falha
 * porque o usu�rio j� � membro da comunidade especificada.
 *
 * <p>Esta � uma exce��o n�o verificada (unchecked) que estende {@link RuntimeException},
 * indicando uma viola��o da regra de neg�cio que impede usu�rios duplicados em comunidades.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class UsuarioJaMembroException extends RuntimeException {

  /**
   * Constr�i uma nova UsuarioJaMembroException com a mensagem padr�o
   * indicando que o usu�rio j� pertence � comunidade.
   */
  public UsuarioJaMembroException() {
    super("Usuario j� faz parte dessa comunidade.");
  }
}