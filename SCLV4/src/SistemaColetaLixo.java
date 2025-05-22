import java.util.Random;

public class SistemaColetaLixo { //a classe principal
    private static final Random rand = new Random();
    CaminhaoPequeno[] caminhoesPequenos;
    EstacaoTransferencia[] estacoes;
    int[] lixoPorZona = new int[5];
    private int totalLixoColetado = 0;

    public static void main(String[] args) {
        new SistemaColetaLixo().simularDia();
    }

    public SistemaColetaLixo() {
        inicializarCaminhoes();
        inicializarEstacoes();
    }

    private void inicializarCaminhoes() { //Algoritmo para inicio de viagem dos caminhões
        caminhoesPequenos = new CaminhaoPequeno[10];
        for (int i = 0; i < caminhoesPequenos.length; i++) {
            int zona = i % 5;
            int capacidade = Config.CAPACIDADES_CAMINHOES_PEQ[i % 4];
            caminhoesPequenos[i] = new CaminhaoPequeno(capacidade, zona);
        }
    }

    private void inicializarEstacoes() { //algoritmo para preparar as estações e inicializa-las
        estacoes = new EstacaoTransferencia[2];
        for (int i = 0; i < estacoes.length; i++) {
            estacoes[i] = new EstacaoTransferencia();
        }
    }

    public void simularDia() { //algoritmo para a duração da simulação e intervalo de relatorio
        for (int minuto = 0; minuto < Config.DURACAO_SIMULACAO; minuto++) {
            boolean horarioPico = verificarHorarioPico(minuto);

            if (minuto % 60 == 0) {
                gerarRelatorioHora(minuto / 60);
            }

            processarCaminhoesPequenos(horarioPico);
            processarEstacoes();
        }
        gerarRelatorioFinal();
    }

    private boolean verificarHorarioPico(int minuto) { //algoritmo para verificar os horarios de picos
        for (int i = 0; i < Config.INICIOS_PICO.length; i++) {
            if (minuto >= Config.INICIOS_PICO[i] && minuto <= Config.FINS_PICO[i]) {
                return true;
            }
        }
        return false;
    }
    void substituirCaminhao(CaminhaoPequeno antigo) { //algoritmo para substituir caminhões pequenos
        for (int i = 0; i < caminhoesPequenos.length; i++) {
            if (caminhoesPequenos[i] == antigo) {
                caminhoesPequenos[i] = new CaminhaoPequeno(antigo.capacidade, antigo.zona);
                System.out.println(Config.VERDE + "[SUBSTITUIÇÃO] Caminhão " + i + " substituído" + Config.RESET);
                break;
            }
        }
    }

    private void processarCaminhoesPequenos(boolean horarioPico) { //algoritmo para processamento de lixo de caminhões pequenos
        for (CaminhaoPequeno caminhao : caminhoesPequenos) {
            if (!caminhao.emViagem && caminhao.podeViajar()) {
                int lixo = gerarLixo(caminhao.zona);
                int duracao = calcularTempoViagem(caminhao.zona, horarioPico);
                caminhao.iniciarViagemColeta(duracao, lixo);
                lixoPorZona[caminhao.zona] += lixo;
                totalLixoColetado += lixo;
            }

            caminhao.atualizarTempo();

            if ((caminhao.cheio() || caminhao.paraSubstituicao) && !caminhao.emViagem) {
                caminhao.irParaEstacao(horarioPico);
                int estacao = new Random().nextInt(estacoes.length);
                estacoes[estacao].receber(caminhao);
            }
        }
    }

    private void processarEstacoes() { //algoritmo de processamento das estações
        for (EstacaoTransferencia estacao : estacoes) {
            estacao.processar(0);
            estacao.substituirCaminhoesVelhos(this);
        }
    }

    private int gerarLixo(int zona) {
        int[] intervalo = Config.INTERVALO_GERACAO_LIXO_ZONAS[zona];
        return intervalo[0] + rand.nextInt(intervalo[1] - intervalo[0] + 1);
    }

    private int calcularTempoViagem(int zona, boolean horarioPico) {
        int[] tempos = horarioPico ?
                Config.TEMPO_VIAGEM_PICO[zona] :
                Config.TEMPO_VIAGEM_NORMAL[zona];
        return tempos[0] + rand.nextInt(tempos[1] - tempos[0] + 1);
    }

    private void gerarRelatorioHora(int hora) { //logica do relatorio de hora em hora
        String horario = String.format("%02d:00-%02d:59", hora, hora);

        System.out.println(Config.AZUL + "\n╔════════════════════════════════════════════════╗");
        System.out.println("║ " + Config.NEGRITO + "RELATÓRIO HORÁRIO - " + horario + Config.RESET + Config.AZUL + "       ║");
        System.out.println("╚════════════════════════════════════════════════╝" + Config.RESET);

        System.out.println(Config.VERDE + "\n[LIXO COLETADO POR ZONA]" + Config.RESET);
        for (int i = 0; i < lixoPorZona.length; i++) {
            System.out.printf("  %-8s: %s%4d toneladas%s\n",
                    Config.NOMES_ZONAS[i],
                    Config.CIANO,
                    lixoPorZona[i],
                    Config.RESET);
        }
        System.out.println(Config.VERDE + "\n[STATUS DOS CAMINHÕES PEQUENOS]" + Config.RESET);
        System.out.printf("%s%-4s %-8s %-10s %-14s %-24s %s\n",
                Config.AMARELO,
                "ID", "Zona", "Carga", "Status", "Atividade", "Viagens",
                Config.RESET);

        for (int i = 0; i < caminhoesPequenos.length; i++) {
            CaminhaoPequeno c = caminhoesPequenos[i];
            String status = c.emViagem ? Config.VERMELHO + "EM VIAGEM" : Config.VERDE + "DISPONÍVEL";
            System.out.printf("%-4d %-8s %s%-4d/%-4d%s %-14s %-24s %s%-6d%s\n",
                    i,
                    Config.NOMES_ZONAS[c.zona],
                    Config.CIANO, c.cargaAtual, c.capacidade, Config.RESET,
                    status + Config.RESET,
                    c.proposito,
                    Config.AMARELO, c.viagens, Config.RESET);
        }


        System.out.println(Config.VERDE + "\n[ESTAÇÕES DE TRANSFERÊNCIA]" + Config.RESET);
        for (int i = 0; i < estacoes.length; i++) {
            EstacaoTransferencia est = estacoes[i];
            System.out.printf("%sEstação %d:%s\n", Config.NEGRITO, i, Config.RESET);
            System.out.printf("  - Caminhões pequenos aguardando: %s%d%s\n",
                    Config.CIANO, est.filaPequenos.tamanho(), Config.RESET);
            System.out.printf("  - Caminhões grandes ativos: %s%d%s\n",
                    Config.CIANO, est.filaGrandesDisponiveis.tamanho(), Config.RESET);

            if (est.filaGrandesEmViagem.tamanho() > 0) {
                System.out.println("  " + Config.VERMELHO + "CAMINHÕES EM VIAGEM PARA ATERRO:" + Config.RESET);
                for (No no = est.filaGrandesEmViagem.inicio; no != null; no = no.proximo) {
                    CaminhaoGrande g = (CaminhaoGrande) no.valor;
                    System.out.printf("    → %sCarga: %d/20 ton | Tempo restante: %d min%s\n",
                            Config.AMARELO, g.carga, g.tempoViagemAterro, Config.RESET);
                }
            } else {
                System.out.println("  " + Config.VERDE + "Nenhum caminhão em viagem ao aterro" + Config.RESET);
            }
        }
    }

    private void gerarRelatorioFinal() { //Algoritmo com calculos para examinar o relatorio final
        System.out.println(Config.AZUL + "\n╔════════════════════════════════════════════════╗");
        System.out.println("║ " + Config.NEGRITO + "RELATÓRIO FINAL DA SIMULAÇÃO" + Config.RESET + Config.AZUL + "             ║");
        System.out.println("╚════════════════════════════════════════════════╝" + Config.RESET);

        System.out.printf("%sTotal de lixo coletado:%s %s%,d toneladas%s\n\n",
                Config.VERDE, Config.RESET,
                Config.CIANO, totalLixoColetado, Config.RESET);


        System.out.println(Config.VERDE + "[EFICIÊNCIA POR ZONA]" + Config.RESET);
        for (int i = 0; i < lixoPorZona.length; i++) {
            double percentual = (lixoPorZona[i] * 100.0) / totalLixoColetado;
            System.out.printf("  %-8s: %s%4d ton%s (%s%.1f%%%s)\n",
                    Config.NOMES_ZONAS[i],
                    Config.CIANO, lixoPorZona[i], Config.RESET,
                    Config.AMARELO, percentual, Config.RESET);
        }

        System.out.println(Config.VERDE + "\n[DESEMPENHO DAS ESTAÇÕES]" + Config.RESET); //calculo do desempenho das estações durante a simulação
        int totalAdicionados = 0;
        for (int i = 0; i < estacoes.length; i++) {
            System.out.printf("%sEstação %d:%s\n", Config.NEGRITO, i, Config.RESET);
            System.out.printf("  - Caminhões adicionados: %s%d%s\n",
                    Config.CIANO, estacoes[i].caminhoesAdicionados, Config.RESET);
            System.out.printf("  - Lixo processado: %s%,d ton%s\n",
                    Config.CIANO, estacoes[i].getTotalLixoProcessado(), Config.RESET);
            totalAdicionados += estacoes[i].caminhoesAdicionados;
        }

        int totalCaminhoes = estacoes.length + totalAdicionados;
        System.out.printf("\n%sCONCLUSÃO:%s\n", Config.VERDE, Config.RESET);
        System.out.printf("  Teresina precisa de %s%d caminhões de 20 toneladas%s\n", // calculo com a quantidade necessaria para poder processar em estações e cletar lixos
                Config.AMARELO, totalCaminhoes, Config.RESET);
        System.out.printf("  %sEficiência geral do sistema: %.1f%%%s\n", //calculo de eficiencia da simulação no dia com os parametros escolhidos
                Config.CIANO,
                (estacoes[0].getTotalLixoProcessado() + estacoes[1].getTotalLixoProcessado()) * 100.0 / totalLixoColetado,
                Config.RESET);
    }

}