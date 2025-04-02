/**
 * Pacote principal do sistema Jackut, responsável por coordenar operações de usuários,
 * sessões, amizades e recados.
 *
 * O sistema foi estruturado utilizando o padrão Facade, dividindo as responsabilidades em duas
 * classes principais:
 * - A classe {@code Jackut} é o núcleo do sistema, onde toda a lógica de negócio e o estado do sistema
 *   são implementados.
 * - A classe {@code Facade} fornece uma interface simplificada para a interação com o sistema, delegando
 *   as operações para o núcleo.
 *
 * Essa organização facilita a manutenção, os testes e a extensão do sistema.
 *
 * @author Iury
 * @version 1.0
 */
package br.ufal.ic.p2.jackut;
