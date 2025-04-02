package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma opera��o tenta acessar um usu�rio que n�o existe no sistema.
 *
 * @author IuryNogueira
 */
public class UsuarioNaoEncontradoException extends RuntimeException {
    /**
     * Constr�i uma nova exce��o com a mensagem padr�o de usu�rio n�o encontrado.
     */
    public UsuarioNaoEncontradoException() {
        super("Usu�rio n�o cadastrado.");
    }
}