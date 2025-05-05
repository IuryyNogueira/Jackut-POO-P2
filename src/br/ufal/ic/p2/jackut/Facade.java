package br.ufal.ic.p2.jackut;

import br.ufal.ic.p2.jackut.exceptions.*;
import java.io.*;

/**
 * Fachada do sistema Jackut, oferecendo interface de alto nível para operações
 * de gerenciamento de usuários, sessões, relacionamentos, recados e comunidades.
 * Cuida também da persistência automática de estado em disco.
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
     * se não existirem, cria novo sistema.
     */
    public Facade() {
        carregarDados();
    }

    /**
     * Cria um novo usuário.
     *
     * @param login login único
     * @param senha senha de acesso
     * @param nome  nome completo
     * @throws LoginInvalidoException   se login inválido
     * @throws SenhaInvalidaException   se senha inválida
     * @throws UsuarioJaExisteException se login já em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        sistema.criarUsuario(login, senha, nome);
    }

    /**
     * Autentica e abre sessão para um usuário.
     *
     * @param login login do usuário
     * @param senha senha do usuário
     * @return ID da sessão criada
     * @throws LoginOuSenhaInvalidosException se credenciais inválidas
     */
    public String abrirSessao(String login, String senha) {
        return sistema.abrirSessao(login, senha);
    }

    /**
     * Obtém valor de atributo de perfil.
     *
     * @param login    login do usuário
     * @param atributo nome do atributo
     * @return valor do atributo
     * @throws UsuarioNaoEncontradoException  se usuário não existir
     * @throws AtributoNaoPreenchidoException se atributo não definido
     */
    public String getAtributoUsuario(String login, String atributo) {
        return sistema.getAtributoUsuario(login, atributo);
    }

    /**
     * Modifica um atributo do perfil do usuário logado.
     *
     * @param idSessao ID da sessão ativa
     * @param atributo nome do atributo
     * @param valor    novo valor
     * @throws UsuarioNaoEncontradoException se sessão inválida
     */
    public void editarPerfil(String idSessao, String atributo, String valor) {
        sistema.editarPerfil(idSessao, atributo, valor);
    }

    /**
     * Inicia ou confirma amizade entre usuários.
     *
     * @param idSessao    ID da sessão solicitante
     * @param amigoLogin  login do usuário a adicionar
     * @throws AutoAmizadeException        se adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException se amigo não existir
     * @throws AmigoJaAdicionadoException  se já houver amizade
     */
    public void adicionarAmigo(String idSessao, String amigoLogin) {
        sistema.adicionarAmigo(idSessao, amigoLogin);
    }

    /**
     * Verifica amizade mútua entre dois usuários.
     *
     * @param login1 login do primeiro usuário
     * @param login2 login do segundo usuário
     * @return true se amigos, false caso contrário
     * @throws UsuarioNaoEncontradoException se algum usuário não existir
     */
    public boolean ehAmigo(String login1, String login2) {
        return sistema.ehAmigo(login1, login2);
    }

    /**
     * Lista amigos de um usuário.
     *
     * @param login login do usuário
     * @return string "{amigo1,amigo2,...}"
     */
    public String getAmigos(String login) {
        return sistema.getAmigos(login);
    }

    /**
     * Envia recado a outro usuário.
     *
     * @param idSessao    ID da sessão do remetente
     * @param destinatario login do destinatário
     * @param mensagem     conteúdo do recado
     * @throws AutoMensagemException       se enviar para si mesmo
     * @throws UsuarioNaoEncontradoException se destinatário não existir
     */
    public void enviarRecado(String idSessao, String destinatario, String mensagem) {
        sistema.enviarRecado(idSessao, destinatario, mensagem);
    }

    /**
     * Lê próximo recado disponível.
     *
     * @param idSessao ID da sessão do usuário
     * @return conteúdo do recado
     * @throws SemRecadosException         se não houver recados
     * @throws UsuarioNaoEncontradoException se sessão inválida
     */
    public String lerRecado(String idSessao) {
        return sistema.lerRecado(idSessao);
    }

    /**
     * Cria comunidade e registra criador como membro inicial.
     *
     * @param sessao    ID da sessão do usuário
     * @param nome      nome único da comunidade
     * @param descricao descrição funcional
     * @throws ComunidadeJaExisteException    se existir nome duplicado
     * @throws UsuarioNaoEncontradoException  se sessão inválida
     */
    public void criarComunidade(String sessao, String nome, String descricao) {
        sistema.criarComunidade(sessao, nome, descricao);
    }

    /**
     * Adiciona usuário a comunidade existente.
     *
     * @param sessao ID da sessão do usuário
     * @param nome   nome da comunidade
     * @throws ComunidadeNaoEncontradaException se comunidade não existir
     * @throws UsuarioJaMembroException        se já membro
     */
    public void adicionarComunidade(String sessao, String nome) {
        sistema.adicionarComunidade(sessao, nome);
    }

    /**
     * Lista comunidades de um usuário.
     *
     * @param login login do usuário
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
     * Obtém descrição de comunidade.
     *
     * @param nome nome da comunidade
     * @return texto descritivo
     */
    public String getDescricaoComunidade(String nome) {
        return sistema.getDescricaoComunidade(nome);
    }

    /**
     * Obtém login do dono da comunidade.
     *
     * @param nome nome da comunidade
     * @return login do proprietário
     */
    public String getDonoComunidade(String nome) {
        return sistema.getDonoComunidade(nome);
    }

    /**
     * Envia mensagem a todos os membros de comunidade.
     *
     * @param id       ID da sessão do remetente
     * @param comunidade nome da comunidade
     * @param mensagem    texto da mensagem
     */
    public void enviarMensagem(String id, String comunidade, String mensagem) {
        sistema.enviarMensagem(id, comunidade, mensagem);
    }

    /**
     * Lê próxima mensagem de comunidade.
     *
     * @param id ID da sessão do usuário
     * @return texto da mensagem
     */
    public String lerMensagem(String id) {
        return sistema.lerMensagem(id);
    }

    /**
     * Adiciona ídolo (fã) ao usuário.
     *
     * @param sessao ID da sessão
     * @param idolo  login do ídolo
     */
    public void adicionarIdolo(String sessao, String idolo) {
        sistema.adicionarIdolo(sessao, idolo);
    }

    /**
     * Adiciona paquera (privado) ao usuário.
     *
     * @param sessao  ID da sessão
     * @param paquera login da paquera
     */
    public void adicionarPaquera(String sessao, String paquera) {
        sistema.adicionarPaquera(sessao, paquera);
    }

    /**
     * Verifica relação fã-ídolo.
     *
     * @param login login do fã
     * @param idolo login do ídolo
     * @return true se for fã
     */
    public boolean ehFa(String login, String idolo) {
        return sistema.ehFa(login, idolo);
    }

    /**
     * Retorna lista de fãs.
     *
     * @param login login do usuário
     * @return string "{fa1,fa2,...}"
     */
    public String getFas(String login) {
        return sistema.getFas(login);
    }

    /**
     * Verifica relação de paquera.
     *
     * @param sessao  ID da sessão
     * @param paquera login da paquera
     * @return true se existir relação
     */
    public boolean ehPaquera(String sessao, String paquera) {
        return sistema.ehPaquera(sessao, paquera);
    }

    /**
     * Retorna lista de paqueras.
     *
     * @param sessao ID da sessão
     * @return string "{p1,p2,...}"
     */
    public String getPaqueras(String sessao) {
        return sistema.getPaqueras(sessao);
    }

    /**
     * Marca usuário como inimigo.
     *
     * @param sessao  ID da sessão
     * @param inimigo login do inimigo
     */
    public void adicionarInimigo(String sessao, String inimigo) {
        sistema.adicionarInimigo(sessao, inimigo);
    }

    /**
     * Remove usuário do sistema, limpando todas referências.
     *
     * @param idSessao ID da sessão do usuário a remover
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
     * Persistência: salva estado em arquivo.
     */
    private void salvarDados() {
        try {
            sistema.salvarEstado(ARQUIVO_DADOS);
        } catch (IOException e) {
            throw new PersistenciaException();
        }
    }

    /**
     * Persistência: carrega estado de arquivo, ou cria sistema novo.
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
