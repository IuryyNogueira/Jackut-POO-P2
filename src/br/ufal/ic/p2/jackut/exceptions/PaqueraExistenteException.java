package br.ufal.ic.p2.jackut.exceptions;

public class PaqueraExistenteException extends RuntimeException {
    public PaqueraExistenteException() {
        super("Usu�rio j� est� adicionado como paquera.");
    }
}
