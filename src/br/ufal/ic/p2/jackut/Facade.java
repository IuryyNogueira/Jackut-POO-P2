package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import java.io.*;

/**
 * Fachada do sistema Jackut, oferecendo interface de alto n�vel para opera��es
 * de gerenciamento de usu�rios, sess�es, relacionamentos, recados e comunidades.
 * Cuida tamb�m da persist�ncia autom�tica de estado em disco.
 *
 * @author Iury
 * @version 1.0
 * @since 2025-05-04
 */
public class Facade {
    private Jackute sistema;
    private static final String ARQUIVO_DADOS = "dados_jackut.dat";

    /**
     * Inicializa a fachada e tenta carregar dados persistidos;
     * se n�o existirem, cria novo sistema.
     */
    public Facade() {
        carregarDados();
    }

    /**
     * Cria um novo usu�rio.
     *
     * @param login login �nico
     * @param senha senha de acesso
     * @param nome  nome completo
     * @throws LoginInvalidoException   se login inv�lido
     * @throws SenhaInvalidaException   se senha inv�lida
     * @throws UsuarioJaExisteException se login j� em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        sistema.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica e abre sess�o para um usu�rio.
     *
     * @param login login do usu�rio
     * @param senha senha do usu�rio
     * @return ID da sess�o criada
     * @throws LoginOuSenhaInvalidosException se credenciais inv�lidas
     */
    public String abrirSessao(String login, String senha) {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Obt�m valor de atributo de perfil.
     *
     * @param login    login do usu�rio
     * @param atributo nome do atributo
     * @return valor do atributo
     * @throws UsuarioNaoEncontradoException  se usu�rio n�o existir
     * @throws AtributoNaoPreenchidoException se atributo n�o definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Modifica um atributo do perfil do usu�rio logado.
     *
     * @param idSessao ID da sess�o ativa
     * @param atributo nome do atributo
     * @param valor    novo valor
     * @throws UsuarioNaoEncontradoException se sess�o inv�lida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        sistema.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Inicia ou confirma amizade entre usu�rios.
     *
     * @param idSessao    ID da sess�o solicitante
     * @param amigoLogin  login do usu�rio a adicionar
     * @throws AutoAmizadeException        se adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException se amigo n�o existir
     * @throws AmigoJaAdicionadoException  se j� houver amizade
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        sistema.adicionarAmigo(idSessao, amigoLogin);
    }

    /**
     * Verifica amizade m�tua entre dois usu�rios.
     *
     * @param login1 login do primeiro usu�rio
     * @param login2 login do segundo usu�rio
     * @return true se amigos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException se algum usu�rio n�o existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return sistema.ehAmigo(login1, login2);
    }

    /**
     * Lista amigos de um usu�rio.
     *
     * @param login login do usu�rio
     * @return string "{amigo1,amigo2,...}"
     */
    public String getAmigos(String login) {
        return sistema.getAmigos(login);
    }

    /**
     * Envia recado a outro usu�rio.
     *
     * @param idSessao    ID da sess�o do remetente
     * @param destinatario login do destinat�rio
     * @param mensagem     conte�do do recado
     * @throws AutoMensagemException       se enviar para si mesmo
     * @throws UsuarioNaoEncontradoException se destinat�rio n�o existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        sistema.enviarRecado(idSessao, destinatario, mensagem);
    }

    /**
     * L� pr�ximo recado dispon�vel.
     *
     * @param idSessao ID da sess�o do usu�rio
     * @return conte�do do recado
     * @throws SemRecadosException         se n�o houver recados
     * @throws UsuarioNaoEncontradoException se sess�o inv�lida
     */
    public String lerRecado(String idSessao) {
        return sistema.lerRecado(idSessao);
    }

    /**
     * Cria comunidade e registra criador como membro inicial.
     *
     * @param sessao    ID da sess�o do usu�rio
     * @param nome      nome �nico da comunidade
     * @param descricao descri��o funcional
     * @throws ComunidadeJaExisteException    se existir nome duplicado
     * @throws UsuarioNaoEncontradoException  se sess�o inv�lida
     */
    public void criarComunidade(String sessao, String nome, String descricao) {
        sistema.criarComunidade(sessao, nome, descricao);
    }

    /**
     * Adiciona usu�rio a comunidade existente.
     *
     * @param sessao ID da sess�o do usu�rio
     * @param nome   nome da comunidade
     * @throws ComunidadeNaoEncontradaException se comunidade n�o existir
     * @throws UsuarioJaMembroException        se j� membro
     */
    public void adicionarComunidade(String sessao, String nome) {
        sistema.adicionarComunidade(sessao, nome);
    }

    /**
     * Lista comunidades de um usu�rio.
     *
     * @param login login do usu�rio
     * @return string "{comun1,comun2,...}"
     */
    public String getComunidades(String login) {
        return sistema.getComunidades(login);
    }

    /**
     * Lista membros de comunidade.
     *
     * @param nome nome da comunidade
     * @return string "{membro1,membro2,...}"
     */
    public String getMembrosComunidade(String nome) {
        return sistema.getMembrosComunidade(nome);
    }

    /**
     * Obt�m descri��o de comunidade.
     *
     * @param nome nome da comunidade
     * @return texto descritivo
     */
    public String getDescricaoComunidade(String nome) {
        return sistema.getDescricaoComunidade(nome);
    }

    /**
     * Obt�m login do dono da comunidade.
     *
     * @param nome nome da comunidade
     * @return login do propriet�rio
     */
    public String getDonoComunidade(String nome) {
        return sistema.getDonoComunidade(nome);
    }

    /**
     * Envia mensagem a todos os membros de comunidade.
     *
     * @param id       ID da sess�o do remetente
     * @param comunidade nome da comunidade
     * @param mensagem    texto da mensagem
     */
    public void enviarMensagem(String id, String comunidade, String mensagem) {
        sistema.enviarMensagem(id, comunidade, mensagem);
    }

    /**
     * L� pr�xima mensagem de comunidade.
     *
     * @param id ID da sess�o do usu�rio
     * @return texto da mensagem
     */
    public String lerMensagem(String id) {
        return sistema.lerMensagem(id);
    }

    /**
     * Adiciona �dolo (f�) ao usu�rio.
     *
     * @param sessao ID da sess�o
     * @param idolo  login do �dolo
     */
    public void adicionarIdolo(String sessao, String idolo) {
        sistema.adicionarIdolo(sessao, idolo);
    }

    /**
     * Adiciona paquera (privado) ao usu�rio.
     *
     * @param sessao  ID da sess�o
     * @param paquera login da paquera
     */
    public void adicionarPaquera(String sessao, String paquera) {
        sistema.adicionarPaquera(sessao, paquera);
    }

    /**
     * Verifica rela��o f�-�dolo.
     *
     * @param login login do f�
     * @param idolo login do �dolo
     * @return true se for f�
     */
    public boolean ehFa(String login, String idolo) {
        return sistema.ehFa(login, idolo);
    }

    /**
     * Retorna lista de f�s.
     *
     * @param login login do usu�rio
     * @return string "{fa1,fa2,...}"
     */
    public String getFas(String login) {
        return sistema.getFas(login);
    }

    /**
     * Verifica rela��o de paquera.
     *
     * @param sessao  ID da sess�o
     * @param paquera login da paquera
     * @return true se existir rela��o
     */
    public boolean ehPaquera(String sessao, String paquera) {
        return sistema.ehPaquera(sessao, paquera);
    }

    /**
     * Retorna lista de paqueras.
     *
     * @param sessao ID da sess�o
     * @return string "{p1,p2,...}"
     */
    public String getPaqueras(String sessao) {
        return sistema.getPaqueras(sessao);
    }

    /**
     * Marca usu�rio como inimigo.
     *
     * @param sessao  ID da sess�o
     * @param inimigo login do inimigo
     */
    public void adicionarInimigo(String sessao, String inimigo) {
        sistema.adicionarInimigo(sessao, inimigo);
    }

    /**
     * Remove usu�rio do sistema, limpando todas refer�ncias.
     *
     * @param idSessao ID da sess�o do usu�rio a remover
     */
    public void removerUsuario(String idSessao) {
        sistema.removerUsuario(idSessao);
    }

    /**
     * Reinicia todos os dados do sistema.
     */
    public void zerarSistema() {
        sistema.zerar();
    }

    /**
     * Encerra o sistema, salvando o estado atual em disco.
     */
    public void encerrarSistema() {
        salvarDados();
    }

    /**
     * Persist�ncia: salva estado em arquivo.
     */
    private void salvarDados() {
        try {
            sistema.salvarEstado(ARQUIVO_DADOS);
        } catch (IOException e) {
            throw new PersistenciaException();
        }
    }

    /**
     * Persist�ncia: carrega estado de arquivo, ou cria sistema novo.
     */
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
