package br.ufal.ic.p2.jackut.exceptions;

public class InimigoException extends RuntimeException {
    public InimigoException(String nome) {
        super(nome + " é seu inimigo.");
    }
}