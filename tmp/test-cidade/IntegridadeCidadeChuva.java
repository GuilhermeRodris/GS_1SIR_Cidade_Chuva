import java.util.Random;
import java.util.Scanner;
public class IntegridadeCidadeChuva {
 static int testes=0;
 static void verificar(boolean ok,String nome){testes++;if(!ok)throw new AssertionError(nome);}
 static void matriz(int[][] a,int[][] b,String nome){verificar(java.util.Arrays.deepEquals(a,b),nome);}
 public static void main(String[] args){
 int[][] a={{6,5,7},{3,1,4},{5,4,6}};
 int[][] l={{4,6,2},{2,0,5},{8,3,1}};
 int[][] p=CidadeChuva.simularPasso(a,l,0);
 matriz(p,new int[][]{{2,3,2},{7,7,3},{4,2,1}},"Exemplo 1 passo 1");
 matriz(CidadeChuva.simularPasso(a,p,0),new int[][]{{5,3,1},{4,4,4},{2,7,1}},"Exemplo 1 passo 2");
 matriz(l,new int[][]{{4,6,2},{2,0,5},{8,3,1}},"Original preservada");
 int[][] copia=CidadeChuva.copiar(l); copia[0][0]=99; verificar(l[0][0]==4,"Copia independente");
 int[][] a2={{3,3,5,6},{4,3,0,3},{6,3,5,3}};
 int[][] l2={{9,3,4,8},{10,6,7,10},{9,2,6,7}};
 int[][] p2=CidadeChuva.simularPasso(a2,l2,1);
 matriz(p2,new int[][]{{5,9,6,4},{5,8,12,5},{5,12,3,7}},"Exemplo 2 passo 1");
 matriz(CidadeChuva.simularPasso(a2,p2,1),new int[][]{{11,5,3,5},{9,4,6,7},{3,6,18,4}},"Exemplo 2 ordem 1");
 matriz(CidadeChuva.simularPasso(a2,p2,0),new int[][]{{11,5,3,5},{9,4,6,16},{3,6,9,4}},"Exemplo 2 ordem 0");
 matriz(CidadeChuva.simularPasso(new int[][]{{0}},new int[][]{{10}},0),new int[][]{{10}},"Celula isolada");
 matriz(CidadeChuva.simularPasso(new int[][]{{0,0}},new int[][]{{2,2}},0),new int[][]{{2,2}},"Niveis iguais");
 matriz(CidadeChuva.simularPasso(new int[][]{{10,0}},new int[][]{{1,0}},0),new int[][]{{1,0}},"Lamina um nao transfere");
 verificar(CidadeChuva.linhaCritica(new int[]{9,9,2})==0,"Empate linha critica");
 verificar(CidadeChuva.contarAlertas(CidadeChuva.marcarAlerta(new int[][]{{4,5,6}},5))==2,"Limiar inclusivo");
 verificar(!CidadeChuva.dentro(-1,0,3,3)&&!CidadeChuva.dentro(0,3,3,3)&&CidadeChuva.dentro(2,2,3,3),"Bordas");
 verificar(java.util.Arrays.equals(CidadeChuva.calcularParametros(1109237),new int[]{10,11,5,12,1}),"Parametros");
 Random gerador=new Random(123456); int[][] alt=CidadeChuva.gerarAltitude(gerador,8,10); int[][] agua=CidadeChuva.gerarMatriz(gerador,8,10,0,10);
 verificar(java.util.Arrays.equals(alt[0],new int[]{15,14,14,12,15,15,14,13,16,14}),"Geracao altitude");
 verificar(java.util.Arrays.equals(agua[0],new int[]{5,0,0,7,4,4,10,4,10,10}),"Geracao agua");
 int[] rms={123456,1109237,1680280}; int[] cont={11,10,16}, crit={4,5,4}, soma={134,115,160};
 for(int x=0;x<rms.length;x++){int[] par=CidadeChuva.calcularParametros(rms[x]);Random r=new Random(rms[x]);int[][] at=CidadeChuva.gerarAltitude(r,par[0],par[1]);int[][] ag=CidadeChuva.gerarMatriz(r,par[0],par[1],0,10);for(int s=0;s<par[2];s++)ag=CidadeChuva.simularPasso(at,ag,par[4]);int[] linhas=CidadeChuva.somarPorLinha(ag);verificar(CidadeChuva.contarAlertas(CidadeChuva.marcarAlerta(ag,par[3]))==cont[x]&&CidadeChuva.linhaCritica(linhas)==crit[x]&&linhas[crit[x]]==soma[x],"RM referencia "+rms[x]);}
 Random r=new Random(42);
 for(int caso=0;caso<200;caso++){int n=1+r.nextInt(10),m=1+r.nextInt(12);int[][] at=CidadeChuva.gerarMatriz(r,n,m,0,30),ag=CidadeChuva.gerarMatriz(r,n,m,0,10);int total=CidadeChuva.somar(ag);int[][] salvo=CidadeChuva.copiar(at);for(int s=0;s<20;s++){int[][] anterior=CidadeChuva.copiar(ag);int[][] nova=CidadeChuva.simularPasso(at,ag,caso%2);matriz(ag,anterior,"Entrada imutavel");verificar(CidadeChuva.somar(nova)==total,"Conservacao");for(int[] linha:nova)for(int v:linha)verificar(v>=0,"Nao negatividade");ag=nova;}matriz(at,salvo,"Altitude fixa");}
 verificar(CidadeChuva.somaRM(new Scanner("1 -3 0 abc 123456"))==123456,"RMs invalidos");
 verificar(CidadeChuva.somaRM(new Scanner("3 570134 573443 572495"))==1716072,"Soma trio");
 verificar(CidadeChuva.somaRM(new Scanner("2 2147483646 2 1"))==Integer.MAX_VALUE,"Protecao overflow");
 verificar(CidadeChuva.somaRM(new Scanner(""))==-1,"EOF quantidade");
 verificar(CidadeChuva.somaRM(new Scanner("2 123"))==-1,"EOF RM");
 System.out.println("PASSOU: "+testes+" verificacoes.");
 }
}
