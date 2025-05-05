package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o � inv�lida porque envolve um usu�rio
 * que est� na lista de inimigos do usu�rio atual.
 *
 * <p>Esta � uma exce��o n�o verificada (unchecked) que estende {@link RuntimeException},
 * indicando que a opera��o n�o pode ser realizada devido a uma rela��o de inimizade.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class InimigoException extends RuntimeException {

    /**
     * Constr�i uma nova InimigoException com uma mensagem personalizada
     * indicando o nome do inimigo envolvido.
     *
     * @param nomeInimigo o nome/login do usu�rio que � considerado inimigo
     *                    e que causou a exce��o
     */
    public InimigoException(String nomeInimigo) {
        super("Fun��o inv�lida: " + nomeInimigo + " � seu inimigo.");
    }
}