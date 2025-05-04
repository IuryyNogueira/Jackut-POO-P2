package br.ufal.ic.p2.jackut.exceptions;

public class ComunidadeJaExisteException extends RuntimeException {
  public ComunidadeJaExisteException() {
    super("Comunidade com esse nome já existe.");
  }
}