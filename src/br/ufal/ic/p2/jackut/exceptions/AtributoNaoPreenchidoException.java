package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando um atributo obrigatório não está preenchido no perfil.
 *
 * @author IuryNogueira
 */
public class AtributoNaoPreenchidoException extends RuntimeException {
    /**
     * Constrói a exceção com mensagem padrão indicando atributo não preenchido
     */
    public AtributoNaoPreenchidoException() {
        super("Atributo não preenchido.");
    }
}