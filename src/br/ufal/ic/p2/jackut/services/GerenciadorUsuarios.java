package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import java.io.Serializable;
import java.util.*;
import br.ufal.ic.p2.jackut.models.Recado;

/**
 * Gerencia todas as operações relacionadas a usuários, incluindo criação, armazenamento,
 * relacionamentos de amizade e persistência de dados.
 * <p>
 * Responsável por manter a integridade das relações de amizade e convites entre usuários,
 * bem como novos tipos de relacionamento (fã, paquera, inimigo).
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 * @see Usuario
 */
public class GerenciadorUsuarios implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Map<String, Usuario> usuarios = new HashMap<>();

    /**
     * Cria e armazena um novo usuário no sistema.
     *
     * @param login identificador único do usuário
     * @param senha senha de acesso
     * @param nome  nome completo do usuário
     * @throws UsuarioJaExisteException se o login já estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (usuarios.containsKey(login)) {
            throw new UsuarioJaExisteException();
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Recupera um usuário pelo seu login.
     *
     * @param login identificador do usuário
     * @return instância do usuário correspondente
     * @throws UsuarioNaoEncontradoException se o usuário não existir
     */
    public Usuario getUsuario(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario;
    }

    /**
     * Estabelece ou confirma uma relação de amizade entre dois usuários.
     *
     * @param loginUsuario login do usuário que inicia ou confirma a amizade
     * @param loginAmigo   login do usuário alvo da amizade
     * @throws AutoAmizadeException        se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException se algum usuário não existir
     * @throws AmigoJaAdicionadoException  se já forem amigos ou houver convite pendente
     * @throws InimigoException            se o destinatário for inimigo
     */
    public void adicionarAmigo(String loginUsuario, String loginAmigo) {
        Usuario usuario = getUsuario(loginUsuario);
        Usuario amigo = getUsuario(loginAmigo);

        if (usuario.getInimigos().contains(loginAmigo)) {
            String nomeInimigo = amigo.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }
        if (loginUsuario.equals(loginAmigo)) {
            throw new AutoAmizadeException();
        }
        if (usuario.getAmigos().contains(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usuário já está adicionado como amigo.");
        }
        if (usuario.temConvitePara(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usuário já está adicionado como amigo, esperando aceitação do convite.");
        }
        if (usuario.temConviteDe(loginAmigo)) {
            usuario.adicionarAmigo(loginAmigo);
            amigo.adicionarAmigo(loginUsuario);
            usuario.removerConvite(loginAmigo);
            amigo.removerConvite(loginUsuario);
        } else {
            usuario.adicionarConvite(loginAmigo, Usuario.ConviteStatus.ENVIADO);
            amigo.adicionarConvite(loginUsuario, Usuario.ConviteStatus.RECEBIDO);
        }
    }

    /**
     * Verifica se dois usuários têm amizade mútua.
     *
     * @param login1 login do primeiro usuário
     * @param login2 login do segundo usuário
     * @return true se forem amigos mútuos, false caso contrário
     * @throws UsuarioNaoEncontradoException se algum usuário não existir
     */
    public boolean saoAmigos(String login1, String login2) {
        Usuario u1 = getUsuario(login1);
        Usuario u2 = getUsuario(login2);
        return u1.getAmigos().contains(login2) && u2.getAmigos().contains(login1);
    }

    /**
     * Gera lista de amigos em formato string.
     *
     * @param login login do usuário
     * @return string no formato "{amigo1,amigo2}" com amigos em ordem alfabética
     * @throws UsuarioNaoEncontradoException se o usuário não existir
     */
    public String listarAmigos(String login) {
        Usuario usuario = getUsuario(login);
        return "{" + String.join(",", usuario.getAmigosOrdenados()) + "}";
    }

    /**
     * Reinicia o gerenciador removendo todos os usuários.
     */
    public void zerar() {
        usuarios.clear();
    }

    /**
     * Adiciona um ídolo para um fã, estabelecendo relação de fã-ídolo.
     *
     * @param fa    login do fã
     * @param idolo login do usuário a ser idolatrado
     * @throws AutoRelacaoException    se fã e ídolo forem o mesmo usuário
     * @throws UsuarioNaoEncontradoException se algum usuário não existir
     * @throws InimigoException        se o ídolo for inimigo
     */
    public void adicionarIdolo(String fa, String idolo) {
        if (fa.equals(idolo)) {
            throw new AutoRelacaoException("Usuário não pode ser fã de si mesmo.");
        }
        Usuario usuarioFa = getUsuario(fa);
        Usuario usuarioIdolo = getUsuario(idolo);
        if (usuarioFa.getInimigos().contains(idolo)) {
            String nomeInimigo = usuarioIdolo.getPerfil().getAtributo("nome");
            throw new InimigoException(nomeInimigo);
        }
        usuarioFa.adicionarIdolo(idolo);
        usuarioIdolo.adicionarFa(fa);
    }

    /**
     * Adiciona uma paquera de forma privada; notifica em caso de reciprocidade.
     *
     * @param usuario login do usuário que paquera
     * @param paquera login da paquera
     * @throws AutoRelacaoException    se usuário paquerar a si mesmo
     * @throws UsuarioNaoEncontradoException se algum usuário não existir
     * @throws InimigoException        se for inimigo
     */
    public void adicionarPaquera(String usuario, String paquera) {
        if (usuario.equals(paquera)) {
            throw new AutoRelacaoException("Usuário não pode ser paquera de si mesmo.");
        }
        Usuario u = getUsuario(usuario);
        Usuario p = getUsuario(paquera);
        if (u.getInimigos().contains(paquera)) {
            throw new InimigoException(p.getPerfil().getAtributo("nome"));
        }
        u.adicionarPaquera(paquera);
        if (p.getPaqueras().contains(usuario)) {
            enviarRecadoSistema(usuario, paquera);
        }
    }

    /**
     * Envia recados automáticos do sistema em caso de paquera mútua.
     *
     * @param usuario1 login do primeiro usuário
     * @param usuario2 login do segundo usuário
     * @throws InimigoException se houver inimizade entre eles
     */
    private void enviarRecadoSistema(String usuario1, String usuario2) {
        Usuario u1 = getUsuario(usuario1);
        Usuario u2 = getUsuario(usuario2);
        if (u1.getInimigos().contains(usuario2) || u2.getInimigos().contains(usuario1)) {
            throw new InimigoException(u2.getPerfil().getAtributo("nome"));
        }
        String nome1 = u1.getPerfil().getAtributo("nome");
        String nome2 = u2.getPerfil().getAtributo("nome");
        u1.adicionarRecado(new Recado("Sistema", nome2 + " é seu paquera - Recado do Jackut."));
        u2.adicionarRecado(new Recado("Sistema", nome1 + " é seu paquera - Recado do Jackut."));
    }

    /**
     * Marca um usuário como inimigo, bloqueando interações.
     *
     * @param usuario login do usuário que declara inimizade
     * @param inimigo login do inimigo
     * @throws AutoRelacaoException    se usuário declarar inimizade consigo mesmo
     * @throws RelacaoExistenteException se já forem inimigos
     * @throws UsuarioNaoEncontradoException se algum usuário não existir
     */
    public void adicionarInimigo(String usuario, String inimigo) {
        if (usuario.equals(inimigo)) {
            throw new AutoRelacaoException("Usuário não pode ser inimigo de si mesmo.");
        }
        Usuario u = getUsuario(usuario);
        Usuario i = getUsuario(inimigo);
        if (u.getInimigos().contains(inimigo)) {
            throw new RelacaoExistenteException("Usuário já está adicionado como inimigo.");
        }
        u.adicionarInimigo(inimigo);
        i.adicionarInimigo(usuario);
    }

    /**
     * Verifica se um usuário é fã de outro.
     *
     * @param loginFa   login do fã
     * @param loginIdolo login do ídolo
     * @return true se for fã, false caso contrário
     */
    public boolean ehFa(String loginFa, String loginIdolo) {
        Usuario fa = getUsuario(loginFa);
        return fa.getIdolos().contains(loginIdolo);
    }

    /**
     * Recupera lista de fãs de um usuário.
     *
     * @param loginIdolo login do usuário idolatrado
     * @return string no formato "{fa1,fa2,...}" dos fãs
     */
    public String getFas(String loginIdolo) {
        Usuario idolo = getUsuario(loginIdolo);
        return "{" + String.join(",", idolo.getFas()) + "}";
    }

    /**
     * Verifica se existe relação de paquera entre dois usuários.
     *
     * @param loginUsuario login do usuário principal
     * @param loginPaquera login da paquera
     * @return true se houver paquera, false caso contrário
     */
    public boolean ehPaquera(String loginUsuario, String loginPaquera) {
        Usuario usuario = getUsuario(loginUsuario);
        return usuario.getPaqueras().contains(loginPaquera);
    }

    /**
     * Recupera lista de paqueras de um usuário.
     *
     * @param loginUsuario login do usuário
     * @return string no formato "{p1,p2,...}" das paqueras
     */
    public String getPaqueras(String loginUsuario) {
        Usuario usuario = getUsuario(loginUsuario);
        return "{" + String.join(",", usuario.getPaqueras()) + "}";
    }

    /**
     * Remove um usuário do sistema.
     *
     * @param login login do usuário a remover
     */
    public void removerUsuario(String login) {
        usuarios.remove(login);
    }

    /**
     * Remove todas as relações e recados associados a um usuário.
     *
     * @param loginAlvo login do usuário cujas referências devem ser limpas
     */
    public void removerUsuarioDeRelacionamentos(String loginAlvo) {
        for (Usuario usuario : usuarios.values()) {
            usuario.removerRelacionamentos(loginAlvo);
            usuario.removerRecadosDoUsuario(loginAlvo);
        }
    }

    /**
     * Remove referências de comunidades de todos os usuários.
     *
     * @param comunidades lista de nomes de comunidades a remover
     */
    public void removerComunidadeDeTodosUsuarios(List<String> comunidades) {
        for (Usuario usuario : usuarios.values()) {
            usuario.getComunidades().removeAll(comunidades);
        }
    }
}
