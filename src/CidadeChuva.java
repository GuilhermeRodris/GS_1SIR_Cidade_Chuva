//Guilherme Rodrigues Santos – RM: 570134
//Gustavo Enrique Donoso Collins – RM: 573443
//Mateus Carrilho Soares Silva – RM: 572495

import java.util.Scanner;
import java.util.Random;

public class CidadeChuva {
    //-------------------- Organiza as etapas da simulação --------------------//
    public static void main(String[] args) {
        // Equipe: executa a geração, os passos e o relatório do bairro.
        Scanner sc = new Scanner(System.in);
        int rm = somaRM(sc);
        if (rm == -1) {
            System.out.println("Entrada encerrada antes de informar todos os RMs (0_0)");
        }
        int[] parametros = calcularParametros(rm);
        int n = parametros[0], m = parametros[1], k = parametros[2];
        int limite = parametros[3], ordem = parametros[4];
        Random gerador = new Random(rm);
        int[][] altitude = gerarAltitude(gerador, n, m);
        int[][] lamina = gerarMatriz(gerador, n, m, 0, 10);
        int totalInicial = somar(lamina);

        System.out.println("Parâmetros: N = " + n + " M = " + m + " K = " + k
                + " L = " + limite + " ordem = " + ordem);
        System.out.println("Altitude inicial (cm): ");
        imprimir(altitude);
        System.out.println();
        System.out.println("Lamina inicial (cm): ");
        imprimir(lamina);
        System.out.println();

        for (int passo = 1; passo <= k; passo++) {
            lamina = simularPasso(altitude, lamina, ordem);
            System.out.println("Lamina após o passo " + passo + ": ");
            imprimir(lamina);
            int totalAtual = somar(lamina);
            System.out.println("Água total: " + totalAtual);
            System.out.println();
            if (totalAtual != totalInicial) {
                System.out.println("Erro a quantidade total de água mudou!");
            }
        }

        boolean[][] alerta = marcarAlerta(lamina, limite);
        int[] aguaPorLinha = somarPorLinha(lamina);
        int critica = linhaCritica(aguaPorLinha);
        System.out.println("Mapa de alerta (# = alagado; . = normal): ");
        imprimirAlerta(alerta);
        System.out.println();
        for (int i = 0; i < n; i++) {
            System.out.println("Linha " + i + ": " + aguaPorLinha[i] + " cm");
        }
        System.out.println();
        System.out.println("Linha " + critica);
        System.out.println("Resumo RM = " + rm);
        System.out.println("Alerta = " + contarAlertas(alerta));
        System.out.println("Crítica = " + critica + "| Água = " + aguaPorLinha[critica]);
    }

//--------------------------------- Registra e valida os RMs da equipe --------------------
    public static int somaRM(Scanner sc) {
        int quantidade = 0, soma = 0;
        while (quantidade < 1 || quantidade > 3) {
            System.out.print("Informe a quantidade de RMs: ");
           quantidade = sc.nextInt();
            if (quantidade < 1 || quantidade > 3) {
                System.out.println("Quantidade invalida, informe novamente!");
            }
        }
        for (int i = 0; i < quantidade;) {
            System.out.print("Informe o " + (i + 1) + " RM: " );
            int rm = sc.nextInt();
            if (rm <= 0) {
                System.out.println("Informe um número inteiro válido");

            } else {
                    soma += rm;
                    i++;
            }
        }
        return soma;
    }
//---------------------------- Calcula os parametros definidos pelo RM ----------------------------
    public static int[] calcularParametros(int rm) {
        return new int[]{8 + rm % 3, 10 + (rm / 10) % 3,
                4 + (rm / 100) % 3, 10 + 2 * (rm % 4), rm % 2};
    }

    public static int[][] gerarAltitude(Random gerador, int n, int m) {
        int[][] altitude = new int[n][m];
        for (int i = 0; i < n; i++) {
            int altitudeBase = 3 * Math.abs(i - n / 2);
            for (int j = 0; j < m; j++) {
                altitude[i][j] = altitudeBase + gerador.nextInt(5);
            }
        }
        return altitude;
    }

    public static int[][] gerarMatriz(Random gerador, int n, int m, int minimo, int maximo) {
        // Equipe: inclui os dois extremos do intervalo de valores.
        int[][] matriz = new int[n][m];
        int intervalo = maximo - minimo + 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matriz[i][j] = minimo + gerador.nextInt(intervalo);
            }
        }
        return matriz;
    }

//------------------------------ Utilitários de matrizes -----------------------------
    public static int[][] copiar(int[][] matriz) {
        int[][] copia = new int[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                copia[i][j] = matriz[i][j];
            }
        }
        return copia;
    }

    public static boolean dentro(int i, int j, int n, int m) {
        return i >= 0 && i < n && j >= 0 && j < m;
    }

    public static void imprimir(int[][] matriz) {
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
        int soma = 0;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                soma += matriz[i][j];
            }
        }
        return soma;
    }

//------------------------------------- Simula todas as decisões ao mesmo tempo -------------------------------------
    public static int[][] simularPasso(int[][] altitude, int[][] lamina, int ordem) {
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

//------------------------------- Identifica alertas e a linha critica --------------------------------------
    public static boolean[][] marcarAlerta(int[][] lamina, int limite) {

        boolean[][] alerta = new boolean[lamina.length][lamina[0].length];
        for (int i = 0; i < lamina.length; i++) {
            for (int j = 0; j < lamina[i].length; j++) {
                alerta[i][j] = lamina[i][j] >= limite;
            }
        }
        return alerta;
    }

    public static void imprimirAlerta(boolean[][] alerta) {
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
        int[] aguaPorLinha = new int[lamina.length];
        for (int i = 0; i < lamina.length; i++) {
            int soma = 0;
            for (int j = 0; j < lamina[i].length; j++) {
                soma += lamina[i][j];
            }
            aguaPorLinha[i] = soma;
        }
        return aguaPorLinha;
    }

    public static int linhaCritica(int[] aguaPorLinha) {
        int critica = 0;
        for (int i = 1; i < aguaPorLinha.length; i++) {
            if (aguaPorLinha[i] > aguaPorLinha[critica]) {
                critica = i;
            }
        }
        return critica;
    }
}
