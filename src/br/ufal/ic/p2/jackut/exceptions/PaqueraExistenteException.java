package br.ufal.ic.p2.jackut.exceptions;

/**
 * Exce��o lan�ada quando uma tentativa de adicionar uma paquera falha
 * porque o usu�rio j� est� na lista de paqueras.
 *
 * <p>Esta � uma exce��o n�o verificada (unchecked) que estende {@link RuntimeException},
 * indicando que a rela��o de paquera j� existe entre os usu�rios.</p>
 *
 * @author [Seu Nome ou IuryNogueira]
 * @version 1.0
 * @since 1.0
 * @see RuntimeException
 */
public class PaqueraExistenteException extends RuntimeException {

    /**
     * Constr�i uma nova PaqueraExistenteException com a mensagem padr�o
     * indicando que o usu�rio j� est� na lista de paqueras.
     */
    public PaqueraExistenteException() {
        super("Usu�rio j� est� adicionado como paquera.");
    }
}