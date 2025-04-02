package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um usu�rio tenta enviar uma mensagem para si mesmo.
 *
 * @author IuryNogueira
 */
public class AutoMensagemException extends RuntimeException {
    /**
     * Constr�i a exce��o com uma mensagem personalizada sobre auto-mensagem.
     *
     * @param mensagem Descri��o detalhada do erro (ex: "Usu�rio n�o pode enviar recado para si mesmo")
     */
    public AutoMensagemException(String mensagem) {
        super(mensagem);
    }
}