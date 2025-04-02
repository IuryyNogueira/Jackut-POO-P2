/**
 * Pacote principal do sistema Jackut, respons�vel por coordenar opera��es de usu�rios,
 * sess�es, amizades e recados.
 *
 * O sistema foi estruturado utilizando o padr�o Facade, dividindo as responsabilidades em duas
 * classes principais:
 * - A classe {@code Jackut} � o n�cleo do sistema, onde toda a l�gica de neg�cio e o estado do sistema
 *   s�o implementados.
 * - A classe {@code Facade} fornece uma interface simplificada para a intera��o com o sistema, delegando
 *   as opera��es para o n�cleo.
 *
 * Essa organiza��o facilita a manuten��o, os testes e a extens�o do sistema.
 *
 * @author Iury
 * @version 1.0
 */
package br.ufal.ic.p2.jackut;
