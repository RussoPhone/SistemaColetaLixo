class EstacaoTransferencia {
    Fila filaPequenos = new Fila();
    Fila filaGrandesDisponiveis = new Fila();
    Fila filaGrandesEmViagem = new Fila();
    int caminhoesAdicionados = 0;
    int totalLixoProcessado = 0;

    void substituirCaminhoesVelhos(SistemaColetaLixo sistema) { //Lógica para a substituição de caminhões pequenos
        Fila novaFila = new Fila();
        while (!filaPequenos.estaVazia()) {
            CaminhaoPequeno cp = (CaminhaoPequeno) filaPequenos.desenfileirar();
            if (cp.paraSubstituicao) {
                sistema.substituirCaminhao(cp);
            } else {
                novaFila.enfileirar(cp);
            }
        }
        filaPequenos = novaFila;
    }

    void processar(int tempoAtual) {
        Fila novosEmViagem = new Fila();
        while (!filaGrandesEmViagem.estaVazia()) {
            CaminhaoGrande g = (CaminhaoGrande) filaGrandesEmViagem.desenfileirar();
            g.atualizarTempo();
            if (g.emViagemAterro) {
                novosEmViagem.enfileirar(g);
            } else {
                filaGrandesDisponiveis.enfileirar(g);
            }
        }
        filaGrandesEmViagem = novosEmViagem;

        if (!filaGrandesDisponiveis.estaVazia()) {
            CaminhaoGrande grande = (CaminhaoGrande) filaGrandesDisponiveis.primeiro();
            grande.tempoEspera++;

            if (grande.tempoEspera >= Config.TOLERANCIA_CAMINHAO_GRANDE) {
                if (grande.carga > 0) {
                    grande.iniciarViagemAterro();
                    filaGrandesEmViagem.enfileirar(grande);
                    filaGrandesDisponiveis.desenfileirar();
                } else {
                    grande.tempoEspera = 0;
                }
            }
        }

        while (!filaPequenos.estaVazia()) {
            CaminhaoPequeno pequeno = (CaminhaoPequeno) filaPequenos.primeiro();
            pequeno.esperarNaEstacao();

            if (pequeno.tempoEsperaEstacao >= Config.TEMPO_MAX_ESPERA_PEQUENO) {
                filaGrandesDisponiveis.enfileirar(new CaminhaoGrande());
                caminhoesAdicionados++;
            }

            if (!filaGrandesDisponiveis.estaVazia()) {
                CaminhaoGrande grande = (CaminhaoGrande) filaGrandesDisponiveis.primeiro();
                int capacidadeRestante = Config.CAPACIDADE_CAMINHAO_GRANDE - grande.carga;
                int cargaTransferida = Math.min(pequeno.cargaAtual, capacidadeRestante);

                grande.carregar(cargaTransferida);
                totalLixoProcessado += cargaTransferida;
                pequeno.cargaAtual -= cargaTransferida;

                if (pequeno.cargaAtual == 0) {
                    pequeno.descarregar();
                    filaPequenos.desenfileirar();
                } else {
                    break;
                }
            }
        }
    }

    void receber(CaminhaoPequeno c) {
        filaPequenos.enfileirar(c);
    }

    String status(int id) {
        StringBuilder sb = new StringBuilder();
        sb.append("Estação ").append(id).append(":\n");
        sb.append("  Caminhões pequenos na fila: ").append(filaPequenos.tamanho()).append("\n");
        sb.append("  Caminhões grandes disponíveis: ").append(filaGrandesDisponiveis.tamanho()).append("\n");
        sb.append("  Caminhões em viagem ao aterro: ").append(filaGrandesEmViagem.tamanho()).append("\n");
        return sb.toString();
    }

    int getTotalLixoProcessado() {
        return totalLixoProcessado;
    }
}