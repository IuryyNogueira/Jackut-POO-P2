package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta adicionar um amigo que j� est� na lista
 * de amigos ou possui um convite pendente.
 *
 * @author IuryNogueira
 */
public class AmigoJaAdicionadoException extends RuntimeException {
    /**
     * Constr�i a exce��o com uma mensagem espec�fica do contexto de erro.
     *
     * @param mensagem Descri��o detalhada do erro (exata dos requisitos de teste)
     */
    public AmigoJaAdicionadoException(String mensagem) {
        super(mensagem);
    }
}