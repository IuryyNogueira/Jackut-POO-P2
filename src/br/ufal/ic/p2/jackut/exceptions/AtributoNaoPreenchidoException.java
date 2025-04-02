package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando um atributo obrigat�rio n�o est� preenchido no perfil.
 *
 * @author IuryNogueira
 */
public class AtributoNaoPreenchidoException extends RuntimeException {
    /**
     * Constr�i a exce��o com mensagem padr�o indicando atributo n�o preenchido
     */
    public AtributoNaoPreenchidoException() {
        super("Atributo n�o preenchido.");
    }
}