package br.ufal.ic.p2.jackut.services;

import br.ufal.ic.p2.jackut.exceptions.*;
import br.ufal.ic.p2.jackut.models.*;
import java.io.Serializable;
import java.util.*;
import br.ufal.ic.p2.jackut.models.Recado;

/**
 * Gerencia todas as opera��es relacionadas a usu�rios, incluindo cria��o, armazenamento,
 * relacionamentos de amizade e persist�ncia de dados.
 * <p>
 * Respons�vel por manter a integridade das rela��es de amizade e convites entre usu�rios,
 * bem como novos tipos de relacionamento (f�, paquera, inimigo).
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
     * Cria e armazena um novo usu�rio no sistema.
     *
     * @param login identificador �nico do usu�rio
     * @param senha senha de acesso
     * @param nome  nome completo do usu�rio
     * @throws UsuarioJaExisteException se o login j� estiver em uso
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (usuarios.containsKey(login)) {
            throw new UsuarioJaExisteException();
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Recupera um usu�rio pelo seu login.
     *
     * @param login identificador do usu�rio
     * @return inst�ncia do usu�rio correspondente
     * @throws UsuarioNaoEncontradoException se o usu�rio n�o existir
     */
    public Usuario getUsuario(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) throw new UsuarioNaoEncontradoException();
        return usuario;
    }

    /**
     * Estabelece ou confirma uma rela��o de amizade entre dois usu�rios.
     *
     * @param loginUsuario login do usu�rio que inicia ou confirma a amizade
     * @param loginAmigo   login do usu�rio alvo da amizade
     * @throws AutoAmizadeException        se tentar adicionar a si mesmo
     * @throws UsuarioNaoEncontradoException se algum usu�rio n�o existir
     * @throws AmigoJaAdicionadoException  se j� forem amigos ou houver convite pendente
     * @throws InimigoException            se o destinat�rio for inimigo
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
            throw new AmigoJaAdicionadoException("Usu�rio j� est� adicionado como amigo.");
        }
        if (usuario.temConvitePara(loginAmigo)) {
            throw new AmigoJaAdicionadoException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
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
     * Verifica se dois usu�rios t�m amizade m�tua.
     *
     * @param login1 login do primeiro usu�rio
     * @param login2 login do segundo usu�rio
     * @return true se forem amigos m�tuos, false caso contr�rio
     * @throws UsuarioNaoEncontradoException se algum usu�rio n�o existir
     */
    public boolean saoAmigos(String login1, String login2) {
        Usuario u1 = getUsuario(login1);
        Usuario u2 = getUsuario(login2);
        return u1.getAmigos().contains(login2) && u2.getAmigos().contains(login1);
    }

    /**
     * Gera lista de amigos em formato string.
     *
     * @param login login do usu�rio
     * @return string no formato "{amigo1,amigo2}" com amigos em ordem alfab�tica
     * @throws UsuarioNaoEncontradoException se o usu�rio n�o existir
     */
    public String listarAmigos(String login) {
        Usuario usuario = getUsuario(login);
        return "{" + String.join(",", usuario.getAmigosOrdenados()) + "}";
    }

    /**
     * Reinicia o gerenciador removendo todos os usu�rios.
     */
    public void zerar() {
        usuarios.clear();
    }

    /**
     * Adiciona um �dolo para um f�, estabelecendo rela��o de f�-�dolo.
     *
     * @param fa    login do f�
     * @param idolo login do usu�rio a ser idolatrado
     * @throws AutoRelacaoException    se f� e �dolo forem o mesmo usu�rio
     * @throws UsuarioNaoEncontradoException se algum usu�rio n�o existir
     * @throws InimigoException        se o �dolo for inimigo
     */
    public void adicionarIdolo(String fa, String idolo) {
        if (fa.equals(idolo)) {
            throw new AutoRelacaoException("Usu�rio n�o pode ser f� de si mesmo.");
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
     * @param usuario login do usu�rio que paquera
     * @param paquera login da paquera
     * @throws AutoRelacaoException    se usu�rio paquerar a si mesmo
     * @throws UsuarioNaoEncontradoException se algum usu�rio n�o existir
     * @throws InimigoException        se for inimigo
     */
    public void adicionarPaquera(String usuario, String paquera) {
        if (usuario.equals(paquera)) {
            throw new AutoRelacaoException("Usu�rio n�o pode ser paquera de si mesmo.");
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
     * Envia recados autom�ticos do sistema em caso de paquera m�tua.
     *
     * @param usuario1 login do primeiro usu�rio
     * @param usuario2 login do segundo usu�rio
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
        u1.adicionarRecado(new Recado("Sistema", nome2 + " � seu paquera - Recado do Jackut."));
        u2.adicionarRecado(new Recado("Sistema", nome1 + " � seu paquera - Recado do Jackut."));
    }

    /**
     * Marca um usu�rio como inimigo, bloqueando intera��es.
     *
     * @param usuario login do usu�rio que declara inimizade
     * @param inimigo login do inimigo
     * @throws AutoRelacaoException    se usu�rio declarar inimizade consigo mesmo
     * @throws RelacaoExistenteException se j� forem inimigos
     * @throws UsuarioNaoEncontradoException se algum usu�rio n�o existir
     */
    public void adicionarInimigo(String usuario, String inimigo) {
        if (usuario.equals(inimigo)) {
            throw new AutoRelacaoException("Usu�rio n�o pode ser inimigo de si mesmo.");
        }
        Usuario u = getUsuario(usuario);
        Usuario i = getUsuario(inimigo);
        if (u.getInimigos().contains(inimigo)) {
            throw new RelacaoExistenteException("Usu�rio j� est� adicionado como inimigo.");
        }
        u.adicionarInimigo(inimigo);
        i.adicionarInimigo(usuario);
    }

    /**
     * Verifica se um usu�rio � f� de outro.
     *
     * @param loginFa   login do f�
     * @param loginIdolo login do �dolo
     * @return true se for f�, false caso contr�rio
     */
    public boolean ehFa(String loginFa, String loginIdolo) {
        Usuario fa = getUsuario(loginFa);
        return fa.getIdolos().contains(loginIdolo);
    }

    /**
     * Recupera lista de f�s de um usu�rio.
     *
     * @param loginIdolo login do usu�rio idolatrado
     * @return string no formato "{fa1,fa2,...}" dos f�s
     */
    public String getFas(String loginIdolo) {
        Usuario idolo = getUsuario(loginIdolo);
        return "{" + String.join(",", idolo.getFas()) + "}";
    }

    /**
     * Verifica se existe rela��o de paquera entre dois usu�rios.
     *
     * @param loginUsuario login do usu�rio principal
     * @param loginPaquera login da paquera
     * @return true se houver paquera, false caso contr�rio
     */
    public boolean ehPaquera(String loginUsuario, String loginPaquera) {
        Usuario usuario = getUsuario(loginUsuario);
        return usuario.getPaqueras().contains(loginPaquera);
    }

    /**
     * Recupera lista de paqueras de um usu�rio.
     *
     * @param loginUsuario login do usu�rio
     * @return string no formato "{p1,p2,...}" das paqueras
     */
    public String getPaqueras(String loginUsuario) {
        Usuario usuario = getUsuario(loginUsuario);
        return "{" + String.join(",", usuario.getPaqueras()) + "}";
    }

    /**
     * Remove um usu�rio do sistema.
     *
     * @param login login do usu�rio a remover
     */
    public void removerUsuario(String login) {
        usuarios.remove(login);
    }

    /**
     * Remove todas as rela��es e recados associados a um usu�rio.
     *
     * @param loginAlvo login do usu�rio cujas refer�ncias devem ser limpas
     */
    public void removerUsuarioDeRelacionamentos(String loginAlvo) {
        for (Usuario usuario : usuarios.values()) {
            usuario.removerRelacionamentos(loginAlvo);
            usuario.removerRecadosDoUsuario(loginAlvo);
        }
    }

    /**
     * Remove refer�ncias de comunidades de todos os usu�rios.
     *
     * @param comunidades lista de nomes de comunidades a remover
     */
    public void removerComunidadeDeTodosUsuarios(List<String> comunidades) {
        for (Usuario usuario : usuarios.values()) {
            usuario.getComunidades().removeAll(comunidades);
        }
    }
}
