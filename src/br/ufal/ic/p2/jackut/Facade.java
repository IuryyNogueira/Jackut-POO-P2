package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import java.io.*;

/**
 * Fachada principal do sistema Jackut que fornece uma interface simplificada para as opera��es do sistema.
 *
 * @author IuryNogueira
 */
public class Facade {
    private Jackute sistema;
    private static final String ARQUIVO_DADOS = "dados_jackut.dat";

    /**
     * Constr�i uma nova fachada e carrega os dados persistidos do sistema.
     */
    public Facade() {
        carregarDados();
    }

    /**
     * Cria um novo usu�rio no sistema.
     *
     * @param login Identificador �nico do usu�rio
     * @param senha Senha de acesso do usu�rio
     * @param nome Nome completo do usu�rio
     * @throws LoginInvalidoException Se o login for inv�lido
     * @throws SenhaInvalidaException Se a senha for inv�lida
     * @throws UsuarioJaExisteException Se o login j� estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        sistema.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica um usu�rio e inicia uma nova sess�o.
     *
     * @param login Login do usu�rio
     * @param senha Senha do usu�rio
     * @return ID da sess�o criada
     * @throws LoginOuSenhaInvalidosException Se as credenciais forem inv�lidas
     */
    public String abrirSessao(String login, String senha) {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Obt�m o valor de um atributo do perfil de um usu�rio.
     *
     * @param login Login do usu�rio
     * @param atributo Nome do atributo
     * @return Valor do atributo solicitado
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     * @throws AtributoNaoPreenchidoException Se o atributo n�o estiver definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Edita um atributo do perfil do usu�rio logado.
     *
     * @param idSessao ID da sess�o ativa
     * @param atributo Nome do atributo a ser modificado
     * @param valor Novo valor para o atributo
     * @throws UsuarioNaoEncontradoException Se a sess�o for inv�lida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        sistema.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Adiciona um usu�rio como amigo, enviando ou confirmando um convite.
     *
     * @param idSessao ID da sess�o do usu�rio solicitante
     * @param amigoLogin Login do usu�rio a ser adicionado como amigo
     * @throws AutoAmizadeException Se o usu�rio tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException Se o amigo n�o existir
     * @throws AmigoJaAdicionadoException Se j� existir amizade ou convite pendente
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        sistema.adicionarAmigo(idSessao, amigoLogin);
    }

    /**
     * Verifica se dois usu�rios s�o amigos m�tuos.
     *
     * @param login1 Login do primeiro usu�rio
     * @param login2 Login do segundo usu�rio
     * @return true se os usu�rios forem amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException Se algum usu�rio n�o existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return sistema.ehAmigo(login1, login2);
    }

    /**
     * Lista os amigos de um usu�rio.
     *
     * @param login Login do usu�rio
     * @return String formatada com a lista de amigos no formato "{amigo1,amigo2}"
     * @throws UsuarioNaoEncontradoException Se o usu�rio n�o existir
     */
    public String getAmigos(String login) {
        return sistema.getAmigos(login);
    }

    /**
     * Envia um recado para outro usu�rio.
     *
     * @param idSessao ID da sess�o do remetente
     * @param destinatario Login do usu�rio destinat�rio
     * @param mensagem Conte�do do recado
     * @throws AutoMensagemException Se o remetente for o mesmo que o destinat�rio
     * @throws UsuarioNaoEncontradoException Se o destinat�rio n�o existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        sistema.enviarRecado(idSessao, destinatario, mensagem);
    }

    /**
     * L� o pr�ximo recado n�o lido do usu�rio logado.
     *
     * @param idSessao ID da sess�o ativa
     * @return Conte�do do recado
     * @throws SemRecadosException Se n�o houver recados dispon�veis
     * @throws UsuarioNaoEncontradoException Se a sess�o for inv�lida
     */
    public String lerRecado(String idSessao) {
        return sistema.lerRecado(idSessao);
    }

    /**
     * Reinicia o sistema, removendo todos os usu�rios e sess�es.
     */
    public void zerarSistema() {
        sistema.zerar();
    }

    /**
     * Encerra o sistema, salvando os dados persistentes.
     */
    public void encerrarSistema() {
        salvarDados();
    }

    // M�todos auxiliares de persist�ncia
    private void salvarDados() {
        try {
            sistema.salvarEstado(ARQUIVO_DADOS);
        } catch (IOException e) {
            throw new PersistenciaException();
        }
    }

    private void carregarDados() {
        try {
            sistema = Jackute.carregarEstado(ARQUIVO_DADOS);
        } catch (FileNotFoundException e) {
            sistema = new Jackute();
        } catch (IOException | ClassNotFoundException e) {
            throw new PersistenciaException();
        }
    }
}