# Testes de estresse — CidadeChuva

Versão SHA-256: b5aac2fc01b8d85705883c4536ea8fab2e86e3710bc883cf905a63538faf657f

- Compilação JDK 21 com -Xlint:all: sem avisos.
- Heap Java limitado a 64 MB.
- APROVADO cenarios=21000 passos=199889 verificacoes=14451441 tempo_ms=661
- Referência independente: calcula transferências em uma matriz de deltas e compara cada passo.
- Verificados: conservação, não negatividade, preservação das entradas, alertas e linha crítica.
- Os 1.000 cenários prolongados usam 100 passos, além dos 4 a 6 exigidos.
- Tempo apenas do laço de simulações, sem console; não é benchmark formal.

## Execuções completas

- RM referencia 123456: saída do processo 0; últimas linhas: Resumo RM = 123456 / Alerta = 11 / Cr�tica = 4| �gua = 134
- RM referencia 1109237: saída do processo 0; últimas linhas: Resumo RM = 1109237 / Alerta = 10 / Cr�tica = 5| �gua = 115
- RM referencia 1680280: saída do processo 0; últimas linhas: Resumo RM = 1680280 / Alerta = 16 / Cr�tica = 4| �gua = 160
- RM maximo: saída do processo 0; últimas linhas: Resumo RM = 2147483647 / Alerta = 6 / Cr�tica = 4| �gua = 125
- Texto na quantidade: saída do processo 1 — Exception in thread "main" java.util.InputMismatchException; últimas linhas: Informe a quantidade de RMs:
- Texto no RM: saída do processo 1 — Exception in thread "main" java.util.InputMismatchException; últimas linhas: Informe a quantidade de RMs: Informe o 1 RM:
- Entrada vazia: saída do processo 1 — Exception in thread "main" java.util.NoSuchElementException; últimas linhas: Informe a quantidade de RMs:
- Entrada incompleta: saída do processo 1 — Exception in thread "main" java.util.NoSuchElementException; últimas linhas: Informe a quantidade de RMs: Informe o 1 RM: Informe o 2 RM:
- Estouro da soma: saída do processo 0; últimas linhas: Resumo RM = -2147483648 / Alerta = 4 / Cr�tica = 3| �gua = 80
- 10 mil quantidades invalidas: saída do processo 0; últimas linhas: Resumo RM = 123456 / Alerta = 11 / Cr�tica = 4| �gua = 134
- 10 mil RMs invalidos: saída do processo 0; últimas linhas: Resumo RM = 123456 / Alerta = 11 / Cr�tica = 4| �gua = 134

## Conclusão

A simulação passou no estresse. A entrada atual lança exceções para texto e fim da entrada. A soma dos RMs pode estourar int e produzir RM negativo. A saída atual também difere do formato do PDF e usa print. Nenhuma alteração foi feita no arquivo principal.
