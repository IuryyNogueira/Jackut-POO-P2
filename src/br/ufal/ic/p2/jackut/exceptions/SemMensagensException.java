package br.ufal.ic.p2.jackut.exceptions;

public class SemMensagensException extends RuntimeException {
  public SemMensagensException() {
    super("N�o h� mensagens.");
  }
}