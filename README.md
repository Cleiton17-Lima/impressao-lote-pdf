# ImpressaoLotePDF

Aplicação desktop desenvolvida em Java para automatizar a impressão de
vários documentos PDF em sequência, a partir de uma pasta selecionada
pelo usuário.

O objetivo é reduzir etapas manuais, otimizar o tempo gasto nas demandas
de impressão e facilitar o processamento de lotes de documentos.

## Funcionalidades

-   Seleção de uma pasta contendo documentos PDF.
-   Identificação dos arquivos com extensão `.pdf` no diretório
    selecionado.
-   Exibição dos documentos encontrados na interface gráfica.
-   Organização da listagem de arquivos em ordem alfabética.
-   Seleção de uma impressora disponível no computador.
-   Encaminhamento sequencial dos documentos para impressão.
-   Processamento em segundo plano, mantendo a interface responsiva.
-   Exibição do resultado do processamento e de eventuais falhas.

> **Observação:** o envio de um documento para a fila de impressão não
> garante, por si só, que todas as páginas tenham sido fisicamente
> impressas. A conclusão também depende do Windows, da fila de
> impressão, da impressora e da disponibilidade dos recursos de
> impressão.

## Tecnologias utilizadas

  -----------------------------------------------------------------------
  Tecnologia                          Finalidade
  ----------------------------------- -----------------------------------
  Java                                Linguagem utilizada na lógica e no
                                      controle do fluxo da aplicação.

  JavaFX                              Construção da interface gráfica.

  Apache PDFBox                       Carregamento e processamento dos
                                      documentos PDF.

  Java Print Service / `PrinterJob`   Integração com as impressoras
                                      disponíveis no ambiente.

  Apache Maven                        Gerenciamento de dependências e
                                      compilação do projeto.

  JPackage                            Geração do instalador para Windows
                                      e empacotamento do ambiente de
                                      execução.
  -----------------------------------------------------------------------

## Como funciona

1.  **Inicialização:** a aplicação abre a interface gráfica e consulta
    as impressoras disponíveis.
2.  **Seleção da pasta:** o usuário escolhe o diretório que contém os
    documentos.
3.  **Identificação:** a aplicação localiza os arquivos com extensão
    `.pdf` no diretório selecionado.
4.  **Listagem:** os arquivos encontrados são apresentados na interface,
    em ordem alfabética.
5.  **Impressão:** ao iniciar o processo, os documentos são carregados
    pelo PDFBox e encaminhados sequencialmente à impressora selecionada.
6.  **Resultado:** a aplicação apresenta informações sobre o
    processamento e eventuais erros.

## Instalação e execução

### Para utilizar o instalador

1.  Obtenha o arquivo instalador `ImpressaoLotePDF-1.0.exe` por um canal
    autorizado.
2.  Execute o instalador no computador Windows.
3.  Siga as instruções apresentadas pelo instalador.
4.  Abra o ImpressaoLotePDF pelo atalho criado ou pelo menu Iniciar.
5.  Selecione a pasta com os PDFs, escolha a impressora e inicie a
    impressão.

O instalador foi gerado com o JPackage e configurado para incluir o
runtime Java. Portanto, o computador não precisa ter uma instalação
independente do Java, desde que o runtime esteja corretamente incluído
no pacote.

### Requisitos de ambiente

-   Computador com Windows compatível com o instalador.
-   Permissão para instalar e executar o programa.
-   Impressora instalada e configurada no Windows.
-   Acesso à impressora ou ao servidor de impressão, quando aplicável.
-   Acesso de leitura à pasta que contém os documentos PDF.

## Segurança e comunicação

O ImpressaoLotePDF é uma aplicação desktop de execução local. O projeto
não implementa servidor web, API ou funcionalidade própria de envio de
documentos para serviços externos.

Os arquivos são acessados a partir da pasta selecionada pelo usuário e
encaminhados ao sistema de impressão do Windows. A comunicação com a
impressora ou com o servidor de impressão é necessária para realizar a
impressão.

A ausência de funcionalidades de comunicação externa no código da
aplicação não equivale a uma auditoria completa de rede. Para ambientes
corporativos, recomenda-se que a equipe responsável de TI valide o
instalador, as dependências e o tráfego de rede observado durante a
execução.

Boas práticas:

-   Utilize documentos provenientes de fontes confiáveis.
-   Mantenha o Windows e as dependências do projeto atualizados.
-   Execute a aplicação com uma conta de usuário comum, sem privilégios
    administrativos desnecessários.
-   Obtenha o instalador por um canal confiável e valide-o antes de
    utilizá-lo.
-   Em caso de dúvidas sobre conexões de rede, solicite ao TI a
    verificação dos destinos acessados pelo processo.

## Estrutura e desenvolvimento

O projeto é desenvolvido em Java e utiliza o Maven para gerenciar as
dependências e automatizar a compilação.

Para realizar alterações no código-fonte:

1.  Importe o projeto em uma IDE compatível com Maven, como o Spring
    Tool Suite ou Eclipse.
2.  Aguarde a resolução das dependências Maven.
3.  Compile e execute o projeto pela IDE ou por uma instalação do Maven.
4.  Para gerar uma nova versão distribuível, atualize a versão do
    projeto quando necessário, gere o pacote e execute o JPackage com as
    dependências e o runtime adequados ao ambiente de destino.

> A geração do instalador deve ser feita para a plataforma e arquitetura
> de destino compatíveis. Após gerar uma nova versão, teste a instalação
> e a impressão em um computador de validação antes de distribuir o
> pacote.

## Limitações e observações

-   A aplicação processa arquivos PDF encontrados no diretório
    selecionado; não é um sistema de gerenciamento ou edição de
    documentos.
-   A impressão depende das impressoras instaladas e configuradas no
    Windows.
-   A disponibilidade e o resultado físico da impressão dependem também
    da fila de impressão, da rede e do dispositivo.
-   O instalador gerado para Windows não é destinado a macOS ou Linux.

## Objetivo do projeto

O ImpressaoLotePDF surgiu para atender a uma necessidade operacional:
reduzir o trabalho repetitivo de abrir e imprimir documentos PDF
individualmente.

O projeto aplica conceitos de programação Java, interface gráfica,
manipulação de arquivos, processamento em segundo plano, integração com
dispositivos de impressão e empacotamento de aplicações desktop.

------------------------------------------------------------------------

**Projeto:** ImpressaoLotePDF\
**Tipo:** Aplicação desktop\
**Plataforma de distribuição:** Windows\
**Formato do instalador:** `.exe`
