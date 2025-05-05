package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma tentativa de adicionar um usuário a uma comunidade falha
 * porque o usuário já é membro da comunidade especificada.
 *
 * <p>Esta é uma exceção não verificada (unchecked) que estende {@link RuntimeException},
 * indicando uma violação da regra de negócio que impede usuários duplicados em comunidades.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class UsuarioJaMembroException extends RuntimeException {

  /**
   * Constrói uma nova UsuarioJaMembroException com a mensagem padrão
   * indicando que o usuário já pertence à comunidade.
   */
  public UsuarioJaMembroException() {
    super("Usuario já faz parte dessa comunidade.");
  }
}