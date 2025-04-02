package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando ocorre um erro durante opera��es de persist�ncia de dados.
 * <p>
 * Pode ser causada por falhas de I/O ou problemas de serializa��o/desserializa��o.
 * </p>
 *
 * @author IuryNogueira
 */
public class PersistenciaException extends RuntimeException {
    /**
     * Constr�i a exce��o com mensagem padr�o sobre falha na persist�ncia
     */
    public PersistenciaException() {
        super("Erro na persist�ncia de dados.");
    }
}