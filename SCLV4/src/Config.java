class Config {
    static final int[] CAPACIDADES_CAMINHOES_PEQ = {2, 4, 8, 10}; //capacidade de carga dos caminhões pequenos (fixo)
    static int CAPACIDADE_CAMINHAO_GRANDE = 20; //capacidade de carga dos caminhões grandes
    static final int MAX_VIAGENS_CAMINHAO_PEQ = 30; //numero maximo de viagens feita pelos caminhões pequenos
    static final int TEMPO_MAX_ESPERA_PEQUENO = 30; //tempo maximo de espera na estação (em minutos)
    static int TOLERANCIA_CAMINHAO_GRANDE = 20; // tolerancia de espera dos caminhões grandes (caso vazio)
    static final int DURACAO_SIMULACAO = 1440; //1440min = 24hrs
    static final int TEMPO_VIAGEM_ATERRO = 120; //120min = 2 horas
    static final int[][] INTERVALO_GERACAO_LIXO_ZONAS = { // intervalo de geração de lixo nas zonas em matriz (minutos)
            {3, 6}, {4, 7}, {5, 9}, {2, 5}, {3, 6}
    };
    static final int[] INICIOS_PICO = {360, 720, 1080}; //6:00, 12:00, 18:00
    static final int[] FINS_PICO = {420, 810, 1140}; //7:00, 13:00, 19:00
    static final int[][] TEMPO_VIAGEM_PICO = { // O tempo de viagem aumenta em horario de pico
            {8, 12}, {9, 14}, {10, 15}, {7, 13}, {8, 12}
    };
    static final int[][] TEMPO_VIAGEM_NORMAL = { // variaveis de tempo de viagem normal
            {5, 8}, {6, 10}, {7, 11}, {4, 9}, {5, 8}
    };
    static final String[] NOMES_ZONAS = {"Sul", "Norte", "Centro", "Leste", "Sudeste"}; //nome das zonas e cores para o terminal
    public static final String RESET = "\u001B[0m";
    public static final String VERDE = "\u001B[32m";
    public static final String VERMELHO = "\u001B[31m";
    public static final String AZUL = "\u001B[34m";
    public static final String AMARELO = "\u001B[33m";
    public static final String CIANO = "\u001B[36m";
    public static final String NEGRITO = "\u001B[1m";
}