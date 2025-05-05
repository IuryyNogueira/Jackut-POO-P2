/**
 * Classe principal do sistema Jackut, responsável pela execução dos testes de aceitação
 * definidos pelas user stories US1 a US9, incluindo validações de persistência.
 * <p>
 * Utiliza o framework EasyAccept para carregar e executar os roteiros de teste
 * localizados na pasta <code>tests/</code>.
 * </p>
 *
 * @author Iury
 * @version 1.0
 */
import easyaccept.EasyAccept;

public class Main {
    /**
     * Ponto de entrada da aplicação. Executa sequencialmente todos os roteiros de teste
     * de aceitação fornecidos, agrupados por user story e fase (funcionalidade básica e
     * persistência).
     *
     * @param args argumentos de linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        // Testes US1 - Criação de conta e persistência
        String[] argsUS1_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us1_1.txt"};
        String[] argsUS1_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us1_2.txt"};

        // Testes US2 - Edição de perfil e persistência
        String[] argsUS2_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us2_1.txt"};
        String[] argsUS2_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us2_2.txt"};

        // Testes US3 - Sistema de amizades e persistência
        String[] argsUS3_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us3_1.txt"};
        String[] argsUS3_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us3_2.txt"};

        // Testes US4 - Troca de recados e persistência
        String[] argsUS4_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us4_1.txt"};
        String[] argsUS4_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us4_2.txt"};

        // Testes US5 a US9 - Criação de comunidades, adição, mensagens,
        // novos relacionamentos e remoção de conta, ambos fases básica e persistência
        String[] argsUS5_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us5_1.txt"};
        String[] argsUS5_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us5_2.txt"};

        String[] argsUS6_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us6_1.txt"};
        String[] argsUS6_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us6_2.txt"};

        String[] argsUS7_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us7_1.txt"};
        String[] argsUS7_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us7_2.txt"};

        String[] argsUS8_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us8_1.txt"};
        String[] argsUS8_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us8_2.txt"};

        String[] argsUS9_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us9_1.txt"};
        String[] argsUS9_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us9_2.txt"};

        // Execução principal de todos os testes
        EasyAccept.main(argsUS1_1);
        EasyAccept.main(argsUS1_2);

        EasyAccept.main(argsUS2_1);
        EasyAccept.main(argsUS2_2);

        EasyAccept.main(argsUS3_1);
        EasyAccept.main(argsUS3_2);

        EasyAccept.main(argsUS4_1);
        EasyAccept.main(argsUS4_2);

        EasyAccept.main(argsUS5_1);
        EasyAccept.main(argsUS5_2);

        EasyAccept.main(argsUS6_1);
        EasyAccept.main(argsUS6_2);

        EasyAccept.main(argsUS7_1);
        EasyAccept.main(argsUS7_2);

        EasyAccept.main(argsUS8_1);
        EasyAccept.main(argsUS8_2);

        EasyAccept.main(argsUS9_1);
        EasyAccept.main(argsUS9_2);
    }
}
