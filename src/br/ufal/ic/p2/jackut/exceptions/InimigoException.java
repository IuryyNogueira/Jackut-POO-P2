package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação é inválida porque envolve um usuário
 * que está na lista de inimigos do usuário atual.
 *
 * <p>Esta é uma exceção não verificada (unchecked) que estende {@link RuntimeException},
 * indicando que a operação não pode ser realizada devido a uma relação de inimizade.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class InimigoException extends RuntimeException {

    /**
     * Constrói uma nova InimigoException com uma mensagem personalizada
     * indicando o nome do inimigo envolvido.
     *
     * @param nomeInimigo o nome/login do usuário que é considerado inimigo
     *                    e que causou a exceção
     */
    public InimigoException(String nomeInimigo) {
        super("Função inválida: " + nomeInimigo + " é seu inimigo.");
    }
}