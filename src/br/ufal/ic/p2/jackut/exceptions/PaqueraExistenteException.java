package br.ufal.ic.p2.jackut.exceptions;

public class PaqueraExistenteException extends RuntimeException {
    public PaqueraExistenteException() {
        super("Usuário já está adicionado como paquera.");
    }
}
