package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exceção lançada quando uma operação tenta adicionar um amigo que já está na lista
 * de amigos ou possui um convite pendente.
 *
 * @author IuryNogueira
 */
public class AmigoJaAdicionadoException extends RuntimeException {
    /**
     * Constrói a exceção com uma mensagem específica do contexto de erro.
     *
     * @param mensagem Descrição detalhada do erro (exata dos requisitos de teste)
     */
    public AmigoJaAdicionadoException(String mensagem) {
        super(mensagem);
    }
}