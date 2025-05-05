package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma tentativa de criar uma rela��o entre usu�rios falha
 * porque a rela��o espec�fica j� existe.
 *
 * <p>Esta � uma exce��o n�o verificada (unchecked) que estende {@link RuntimeException},
 * podendo ser utilizada para diversos tipos de rela��es existentes entre usu�rios.</p>
 *
 * <p>A mensagem espec�fica do erro pode ser personalizada conforme o tipo de rela��o.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class RelacaoExistenteException extends RuntimeException {

    /**
     * Constr�i uma nova RelacaoExistenteException com uma mensagem personalizada.
     *
     * @param mensagem a mensagem detalhada que descreve qual rela��o espec�fica j� existe.
     *                 A mensagem � salva para posterior recupera��o pelo m�todo {@link #getMessage()}.
     */
    public RelacaoExistenteException(String mensagem) {
        super(mensagem);
    }
}