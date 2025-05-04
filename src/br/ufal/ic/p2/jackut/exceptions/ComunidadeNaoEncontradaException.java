package br.ufal.ic.p2.jackut.exceptions;

public class ComunidadeNaoEncontradaException extends RuntimeException {
    public ComunidadeNaoEncontradaException() {
        super("Comunidade não existe.");
    }
}