import easyaccept.EasyAccept;

/**
 * Classe principal responsável por executar os testes de aceitação do sistema Jackut.
 * <p>
 * Esta classe utiliza o framework EasyAccept para validar as funcionalidades implementadas
 * conforme as user stories definidas.
 * </p>
 *
 * @author IuryNogueira
 * @version 1.0
 */
public class Main {
    /**
     * Ponto de entrada do programa. Executa sequencialmente todos os testes de aceitação
     * para as user stories US1 a US4, incluindo testes de persistência.
     *
     * @param args Argumentos de linha de comando (não utilizados neste contexto)
     */
    public static void main(String[] args) {
        // Configuração dos parâmetros para cada conjunto de testes
        String[] argsUS1_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us1_1.txt"};
        String[] argsUS1_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us1_2.txt"};
        String[] argsUS2_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us2_1.txt"};
        String[] argsUS2_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us2_2.txt"};
        String[] argsUS3_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us3_1.txt"};
        String[] argsUS3_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us3_2.txt"};
        String[] argsUS4_1 = {"br.ufal.ic.p2.jackut.Facade", "tests/us4_1.txt"};
        String[] argsUS4_2 = {"br.ufal.ic.p2.jackut.Facade", "tests/us4_2.txt"};

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

        // Execução sequencial dos testes
        EasyAccept.main(argsUS1_1);  // Testes US1 - Criação de conta 8(casos básicos)
        EasyAccept.main(argsUS1_2);  // Testes US1 - Validação de persistência
        EasyAccept.main(argsUS2_1);  // Testes US2 - Perfil (operações básicas)
        EasyAccept.main(argsUS2_2);  // Testes US2 - Validação de persistência
        EasyAccept.main(argsUS3_1);  // Testes US3 - Amizades (casos básicos)
        EasyAccept.main(argsUS3_2);  // Testes US3 - Validação de persistência
        EasyAccept.main(argsUS4_1);  // Testes US4 - Recados (operações básicas)
        EasyAccept.main(argsUS4_2);  // Testes US4 - Validação de persistência

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