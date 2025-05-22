import java.util.Random;

class CaminhaoPequeno {
    private static final Random rand = new Random();
    final int capacidade;
    int cargaAtual = 0;
    int viagens = 0;
    final int zona;
    int tempoRestante = 0;
    boolean emViagem = false;
    int tempoEsperaEstacao = 0;
    String proposito = "Disponível";
    boolean paraSubstituicao = false;

    CaminhaoPequeno(int capacidade, int zona) {
        this.capacidade = capacidade;
        this.zona = zona;
    }

    boolean podeViajar() { //Algoritmo para verificar se o tempo de viagem foi alcançado
        return viagens < Config.MAX_VIAGENS_CAMINHAO_PEQ && !emViagem && !paraSubstituicao;
    }

    void iniciarViagemColeta(int duracao, int cargaColetada) { //algoritmo para inicio de viagem
        cargaAtual = Math.min(cargaAtual + cargaColetada, capacidade);
        viagens++;
        tempoRestante = duracao;
        emViagem = true;
        proposito = "Coletando (" + cargaColetada + " ton)";


        if (viagens >= Config.MAX_VIAGENS_CAMINHAO_PEQ) {
            paraSubstituicao = true;
        }
    }

    void irParaEstacao(boolean horarioPico) { //algoritmo para caso alcançar o max de viagens ou a capacidade maxima
        int[] tempos = horarioPico ?
                Config.TEMPO_VIAGEM_PICO[zona] :
                Config.TEMPO_VIAGEM_NORMAL[zona];
        tempoRestante = tempos[0] + rand.nextInt(tempos[1] - tempos[0] + 1);
        emViagem = true;
        proposito = "Indo para estação";
    }

    void atualizarTempo() { //algoritmo para atualizar tempo de viagem
        if (emViagem && tempoRestante > 0) tempoRestante--;
        if (tempoRestante <= 0) emViagem = false;
    }

    boolean cheio() {
        return cargaAtual >= capacidade;
    }

    void descarregar() {
        cargaAtual = 0;
        tempoEsperaEstacao = 0;
        proposito = "Disponível";
    }

    void esperarNaEstacao() {
        tempoEsperaEstacao++;
        proposito = "Aguardando descarga";
    }

    String status() {
        String status = emViagem ?
                Config.VERMELHO + "EM VIAGEM" + Config.RESET :
                Config.VERDE + "DISPONÍVEL" + Config.RESET;

        return String.format("%s%-5s%s | %s%-7s%s | %s%-15s%s | Viagens: %s%-2d%s",
                Config.CIANO, Config.NOMES_ZONAS[zona], Config.RESET,
                Config.AMARELO, cargaAtual + "/" + capacidade, Config.RESET,
                status, proposito, Config.RESET,
                Config.AMARELO, viagens, Config.RESET);
    }
}