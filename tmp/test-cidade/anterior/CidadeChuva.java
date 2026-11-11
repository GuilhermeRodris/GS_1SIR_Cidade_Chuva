import java.util.Scanner;
import java.util.Random;

public class CidadeChuva {
    //-------------------- Organiza as etapas da simulacao --------------------//
    public static void main(String[] args) {
        // Equipe: executa a geracao, os passos e o relatorio do bairro.
        Scanner sc = new Scanner(System.in);
        int rm = somaRM(sc);
        if (rm == -1) {
            System.out.println("Entrada encerrada antes de informar todos os RMs.");
            sc.close();
            return;
        }
        int[] parametros = calcularParametros(rm);
        int n = parametros[0], m = parametros[1], k = parametros[2];
        int limite = parametros[3], ordem = parametros[4];
        Random gerador = new Random(rm);
        int[][] altitude = gerarAltitude(gerador, n, m);
        int[][] lamina = gerarMatriz(gerador, n, m, 0, 10);
        int totalInicial = somar(lamina);

        System.out.println("Parametros: N=" + n + " M=" + m + " K=" + k
                + " L=" + limite + " ordem=" + ordem);
        System.out.println("Altitude inicial (cm):");
        imprimir(altitude);
        System.out.println("Lamina inicial (cm):");
        imprimir(lamina);

        for (int passo = 1; passo <= k; passo++) {
            lamina = simularPasso(altitude, lamina, ordem);
            System.out.println("Lamina apos o passo " + passo + ":");
            imprimir(lamina);
            int totalAtual = somar(lamina);
            System.out.println("Agua total: " + totalAtual);
            if (totalAtual != totalInicial) {
                throw new IllegalStateException("A quantidade total de agua mudou.");
            }
        }

        boolean[][] alerta = marcarAlerta(lamina, limite);
        int[] aguaPorLinha = somarPorLinha(lamina);
        int critica = linhaCritica(aguaPorLinha);
        System.out.println("Mapa de alerta (# = alagado; . = normal):");
        imprimirAlerta(alerta);
        for (int i = 0; i < n; i++) {
            System.out.println("Linha " + i + ": " + aguaPorLinha[i] + " cm");
        }
        System.out.println("Recomendacao: priorizar a inspecao da linha " + critica
                + " e sinalizar os trechos em alerta, com rotas acessiveis alternativas.");
        System.out.println("D1 e D2 pendentes: o material complementar nao define seus calculos.");
        sc.close();

        System.out.println("RESUMO RM=" + rm);
        System.out.println("ALERTA=" + contarAlertas(alerta));
        System.out.println("CRITICA=" + critica + " AGUA=" + aguaPorLinha[critica]);
        // Equipe: nao atribui resultados inventados as questoes ausentes do PDF.
        System.out.println("D1=PENDENTE D2=PENDENTE");
    }

    //-------------------- Registra e valida os RMs da equipe --------------------//
    public static int somaRM(Scanner sc) {
        // Equipe: soma apenas RMs positivos e evita ultrapassar o limite de int.
        int quantidade = 0, soma = 0;
        while (quantidade < 1 || quantidade > 3) {
            System.out.println("Informe a quantidade de integrantes (1 a 3):");
            if (!sc.hasNext()) {
                return -1;
            }
            if (sc.hasNextInt()) {
                quantidade = sc.nextInt();
            } else {
                sc.next();
            }
            if (quantidade < 1 || quantidade > 3) {
                System.out.println("Quantidade invalida. Informe novamente!");
            }
        }
        for (int i = 0; i < quantidade;) {
            System.out.println("Informe o " + (i + 1) + " RM:");
            if (!sc.hasNext()) {
                return -1;
            }
            if (sc.hasNextInt()) {
                int rm = sc.nextInt();
                if (rm > 0 && rm <= Integer.MAX_VALUE - soma) {
                    soma += rm;
                    i++;
                } else {
                    System.out.println("RM invalido ou soma acima do limite permitido.");
                }
            } else {
                sc.next();
                System.out.println("Informe um numero inteiro valido.");
            }
        }
        return soma;
    }

    //-------------------- Calcula os parametros definidos pelo RM --------------------//
    public static int[] calcularParametros(int rm) {
        // Equipe: todas as divisoes usadas nas formulas sao inteiras.
        return new int[]{8 + rm % 3, 10 + (rm / 10) % 3,
                4 + (rm / 100) % 3, 10 + 2 * (rm % 4), rm % 2};
    }

    public static int[][] gerarAltitude(Random gerador, int n, int m) {
        // Equipe: gera o vale por linha e coluna usando o mesmo gerador da chuva.
        int[][] altitude = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                altitude[i][j] = 3 * Math.abs(i - n / 2) + gerador.nextInt(5);
            }
        }
        return altitude;
    }

    public static int[][] gerarMatriz(Random gerador, int n, int m, int minimo, int maximo) {
        // Equipe: inclui os dois extremos do intervalo de valores.
        int[][] matriz = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matriz[i][j] = minimo + gerador.nextInt(maximo - minimo + 1);
            }
        }
        return matriz;
    }

    //-------------------- Utilitarios de matrizes --------------------//
    public static int[][] copiar(int[][] matriz) {
        // Equipe: copia cada valor para impedir alteracoes na matriz original.
        int[][] copia = new int[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                copia[i][j] = matriz[i][j];
            }
        }
        return copia;
    }

    public static boolean dentro(int i, int j, int n, int m) {
        // Equipe: ignora vizinhos que ficam fora das bordas do bairro.
        return i >= 0 && i < n && j >= 0 && j < m;
    }

    public static void imprimir(int[][] matriz) {
        // Equipe: monta cada linha antes de exibir, usando somente println.
        for (int i = 0; i < matriz.length; i++) {
            String linha = "";
            for (int j = 0; j < matriz[i].length; j++) {
                if (j > 0) {
                    linha += "	";
                }
                linha += matriz[i][j];
            }
            System.out.println(linha);
        }
    }

    public static int somar(int[][] matriz) {
        // Equipe: permite conferir a conservacao da agua em cada passo.
        int soma = 0;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                soma += matriz[i][j];
            }
        }
        return soma;
    }

    //-------------------- Simula todas as decisoes ao mesmo tempo --------------------//
    public static int[][] simularPasso(int[][] altitude, int[][] lamina, int ordem) {
        // Equipe: le apenas lamina e acumula todas as transferencias em nova.
        int n = lamina.length, m = lamina[0].length;
        int[][] nova = copiar(lamina);
        int[] deslocamentoLinha, deslocamentoColuna;
        if (ordem == 0) {
            deslocamentoLinha = new int[]{-1, 0, 1, 0};
            deslocamentoColuna = new int[]{0, 1, 0, -1};
        } else {
            deslocamentoLinha = new int[]{0, 1, 0, -1};
            deslocamentoColuna = new int[]{-1, 0, 1, 0};
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int t = lamina[i][j] / 2;
                if (t > 0) {
                    int menor = altitude[i][j] + lamina[i][j];
                    int destinoLinha = -1, destinoColuna = -1;
                    for (int d = 0; d < 4; d++) {
                        int vi = i + deslocamentoLinha[d];
                        int vj = j + deslocamentoColuna[d];
                        if (dentro(vi, vj, n, m)) {
                            int nivel = altitude[vi][vj] + lamina[vi][vj];
                            // Comparacao estrita preserva o primeiro vizinho em empates.
                            if (nivel < menor) {
                                menor = nivel;
                                destinoLinha = vi;
                                destinoColuna = vj;
                            }
                        }
                    }
                    if (destinoLinha != -1) {
                        nova[i][j] -= t;
                        nova[destinoLinha][destinoColuna] += t;
                    }
                }
            }
        }
        return nova;
    }

    //-------------------- Identifica alertas e a linha critica --------------------//
    public static boolean[][] marcarAlerta(int[][] lamina, int limite) {
        // Equipe: o valor igual ao limite tambem representa alerta.
        boolean[][] alerta = new boolean[lamina.length][lamina[0].length];
        for (int i = 0; i < lamina.length; i++) {
            for (int j = 0; j < lamina[i].length; j++) {
                alerta[i][j] = lamina[i][j] >= limite;
            }
        }
        return alerta;
    }

    public static void imprimirAlerta(boolean[][] alerta) {
        // Equipe: separa os simbolos por tabulacao como solicitado.
        for (int i = 0; i < alerta.length; i++) {
            String linha = "";
            for (int j = 0; j < alerta[i].length; j++) {
                if (j > 0) {
                    linha += "	";
                }
                if (alerta[i][j]) {
                    linha += "#";
                } else {
                    linha += ".";
                }
            }
            System.out.println(linha);
        }
    }

    public static int contarAlertas(boolean[][] alerta) {
        // Equipe: conta cada trecho alagado uma unica vez.
        int quantidade = 0;
        for (int i = 0; i < alerta.length; i++) {
            for (int j = 0; j < alerta[i].length; j++) {
                if (alerta[i][j]) {
                    quantidade++;
                }
            }
        }
        return quantidade;
    }

    public static int[] somarPorLinha(int[][] lamina) {
        // Equipe: guarda no vetor a soma de agua de cada linha do bairro.
        int[] aguaPorLinha = new int[lamina.length];
        for (int i = 0; i < lamina.length; i++) {
            for (int j = 0; j < lamina[i].length; j++) {
                aguaPorLinha[i] += lamina[i][j];
            }
        }
        return aguaPorLinha;
    }

    public static int linhaCritica(int[] aguaPorLinha) {
        // Equipe: em empate, mantem a primeira linha, de menor indice.
        int critica = 0;
        for (int i = 1; i < aguaPorLinha.length; i++) {
            if (aguaPorLinha[i] > aguaPorLinha[critica]) {
                critica = i;
            }
        }
        return critica;
    }
}
