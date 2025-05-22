class CaminhaoGrande {
    int carga = 0;
    int tempoEspera = 0;
    int viagensRealizadas = 0;
    boolean emViagemAterro = false;
    int tempoViagemAterro = 0;

    void carregar(int qtd) {
        carga += qtd;
    }

    void iniciarViagemAterro() {
        if (carga > 0) {
            emViagemAterro = true;
            tempoViagemAterro = Config.TEMPO_VIAGEM_ATERRO;
        }
    }

    void atualizarTempo() {
        if (emViagemAterro && tempoViagemAterro > 0) {
            tempoViagemAterro--;
            if (tempoViagemAterro == 0) {
                emViagemAterro = false;
                carga = 0;
                viagensRealizadas++;
            }
        }
    }

    String status() {
        String status;
        if (emViagemAterro) {
            status = Config.VERMELHO + "VIAGEM AO ATERRO (" + tempoViagemAterro + " min)" + Config.RESET;
        } else if (carga > 0) {
            status = Config.AMARELO + "AGUARDANDO (" + tempoEspera + "/" + Config.TOLERANCIA_CAMINHAO_GRANDE + " min)" + Config.RESET;
        } else {
            status = Config.VERDE + "DISPONÍVEL" + Config.RESET;
        }

        return String.format("%sCarga: %-3d/20 ton%s | %s | Viagens: %s%-2d%s",
                Config.CIANO, carga, Config.RESET,
                status,
                Config.AMARELO, viagensRealizadas, Config.RESET);
    }
}