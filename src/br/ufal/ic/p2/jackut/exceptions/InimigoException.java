package br.ufal.ic.p2.jackut.exceptions;

public class InimigoException extends RuntimeException {
    public InimigoException(String nomeInimigo) {
        super("Função inválida: " + nomeInimigo + " é seu inimigo.");
    }
}