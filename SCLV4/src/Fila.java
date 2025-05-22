class Fila {
    No inicio, fim;
    int tamanho = 0;

    void enfileirar(Object valor) {
        No novo = new No(valor);
        if (fim != null) fim.proximo = novo;
        fim = novo;
        if (inicio == null) inicio = novo;
        tamanho++;
    }

    Object desenfileirar() {
        if (inicio == null) return null;
        Object v = inicio.valor;
        inicio = inicio.proximo;
        if (inicio == null) fim = null;
        tamanho--;
        return v;
    }

    boolean estaVazia() {
        return tamanho == 0;
    }

    int tamanho() {
        return tamanho;
    }

    Object primeiro() {
        return inicio != null ? inicio.valor : null;
    }
}
