import java.util.*;
public class EstresseCidadeChuva {
 static long verificacoes=0,passos=0;
 static void exigir(boolean ok,String nome){verificacoes++;if(!ok)throw new AssertionError(nome);}
 static int[][] referencia(int[][] a,int[][] l,int ordem){
 int n=l.length,m=l[0].length;int[][] delta=new int[n][m],out=new int[n][m];
 int[][] dirs=ordem==0?new int[][]{{-1,0},{0,1},{1,0},{0,-1}}:new int[][]{{0,-1},{1,0},{0,1},{-1,0}};
 for(int i=0;i<n;i++)for(int j=0;j<m;j++){
 int destinoI=i,destinoJ=j,min=a[i][j]+l[i][j];
 for(int[] d:dirs){int x=i+d[0],y=j+d[1];if(x>=0&&x<n&&y>=0&&y<m&&a[x][y]+l[x][y]<min){min=a[x][y]+l[x][y];destinoI=x;destinoJ=y;}}
 int t=l[i][j]/2;delta[i][j]-=t;delta[destinoI][destinoJ]+=t;
 }
 for(int i=0;i<n;i++)for(int j=0;j<m;j++)out[i][j]=l[i][j]+delta[i][j];return out;
 }
 static int[][] passo(int[][] a,int[][] l,int ordem,int total){
 int[][] salvo=CidadeChuva.copiar(l),terreno=CidadeChuva.copiar(a),esperado=referencia(a,l,ordem);
 int[][] nova=CidadeChuva.simularPasso(a,l,ordem);passos++;
 exigir(Arrays.deepEquals(nova,esperado),"Divergencia referencia");
 exigir(Arrays.deepEquals(l,salvo),"Lamina original alterada");
 exigir(Arrays.deepEquals(a,terreno),"Altitude alterada");
 exigir(CidadeChuva.somar(nova)==total,"Perda de agua");
 for(int[] linha:nova)for(int v:linha)exigir(v>=0,"Agua negativa");return nova;
 }
 public static void main(String[] args){long inicio=System.nanoTime();Random sorteio=new Random(20261116);
 for(int caso=0;caso<20000;caso++){
 int rm=caso<10000?caso+1:1+sorteio.nextInt(Integer.MAX_VALUE-1);if(caso==19999)rm=Integer.MAX_VALUE;
 int[] p=CidadeChuva.calcularParametros(rm);exigir(p[0]>=8&&p[0]<=10&&p[1]>=10&&p[1]<=12&&p[2]>=4&&p[2]<=6&&p[3]>=10&&p[3]<=16,"Parametros");
 Random r=new Random(rm);int[][] a=CidadeChuva.gerarAltitude(r,p[0],p[1]),l=CidadeChuva.gerarMatriz(r,p[0],p[1],0,10);int total=CidadeChuva.somar(l);
 for(int k=0;k<p[2];k++)l=passo(a,l,p[4],total);
 int[] linhas=CidadeChuva.somarPorLinha(l);int soma=0,critica=0,alertas=0;
 for(int i=0;i<l.length;i++){int somaLinha=0;for(int v:l[i]){somaLinha+=v;if(v>=p[3])alertas++;}exigir(linhas[i]==somaLinha,"Soma linha");soma+=linhas[i];if(linhas[i]>linhas[critica])critica=i;}
 exigir(soma==total&&CidadeChuva.linhaCritica(linhas)==critica,"Relatorio");exigir(CidadeChuva.contarAlertas(CidadeChuva.marcarAlerta(l,p[3]))==alertas,"Alertas");
 }
 for(int caso=0;caso<1000;caso++){int n=1+sorteio.nextInt(10),m=1+sorteio.nextInt(12);int[][] a=CidadeChuva.gerarMatriz(sorteio,n,m,0,30),l=CidadeChuva.gerarMatriz(sorteio,n,m,0,10);int total=CidadeChuva.somar(l);for(int k=0;k<100;k++)l=passo(a,l,caso%2,total);}
 System.out.println("APROVADO cenarios=21000 passos="+passos+" verificacoes="+verificacoes+" tempo_ms="+(System.nanoTime()-inicio)/1000000);
 }
}
