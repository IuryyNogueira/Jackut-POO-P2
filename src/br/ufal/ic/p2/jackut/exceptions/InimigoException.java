package br.ufal.ic.p2.jackut.exceptions;

public class InimigoException extends RuntimeException {
    public InimigoException(String nomeInimigo) {
        super("Fun��o inv�lida: " + nomeInimigo + " � seu inimigo.");
    }
}