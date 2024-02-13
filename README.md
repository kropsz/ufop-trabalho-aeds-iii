# Projeto de AEDS III: Mapeamento de Imagem Bitmap

## Descrição do Projeto
Este projeto foi desenvolvido como parte da disciplina de Algoritmos e Estruturas de Dados III. O objetivo principal é criar um programa capaz de mapear uma imagem em bitmap, onde cada pixel é tratado como um nó, e demonstrar o caminho para se locomover de um ponto a outro na imagem.

## Tecnologias Utilizadas

- Linguagem de Programação: Java
- Bibliotecas: JGrapht
- Ferramentas de Desenvolvimento: Visual Studio Code, Intellij

## Funcionalidades Principais

1. **Leitura da Imagem em Bitmap:**
   - O programa é capaz de ler uma imagem em formato bitmap como entrada. A imagem é fornecida pelo usuário. 
   
   <div>
      <img src="https://github.com/kropsz/ufop-trabalho-aeds-iii/assets/114687669/f9825b31-e447-45ee-8820-75cf960caeca" width="400px" />
   </div>

   - Também é possível o deslocamento em níveis (andares) através de várias imagens
      * Em `resources/assets_trab2/toyFloors` é possível testar utilizando as 3 imagens.
      * Ex: `toy_0.bmp` onde 0 é o **número** do andar

2. **Criação do Grafo:**
   - Cada pixel na imagem é representado como um nó no grafo. Os pixels adjacentes são conectados por arestas, formando o grafo que representa a estrutura da imagem.

   - A imagem é mapeada de acordo com a cor do pixel: 
   * **Inicio**: `VERMELHO`
   * **Destino**: `VERDE`
   * **Caminho Livre**: `BRANCO`
   * **Colisão**: `PRETO`
   * **Próximos de Colisão**: `CINZA_ESCUTO` e `CINZA_CLARO`

3. **Visualização do Caminho:**
   - O programa determina e exibe o caminho mais curto entre dois pontos específicos na imagem.

   <div>
      <img src="https://github.com/kropsz/ufop-trabalho-aeds-iii/assets/114687669/f61cad9d-19c3-4bec-bc6b-2a6ce303139d" width="400px" />
   </div>

## Instruções de Uso

1. **Requisitos:**
   - Clone o Repositório `https://github.com/kropsz/ufop-trabalho-aeds-iii.git`
   - Java Development Kit (JDK) 8 ou superior

2. **Entrada de Dados:**
   - Forneça a imagem em formato bitmap como entrada para o programa. 
   - Em `resources` ja se encontra algumas imagens para testes.


## Autores

| E-Mail                               | Usuário Github |
|--------------------------------------|----------------|
| pedro.esteves@aluno.ufop.edu.br  | kropsz         |
| pedro.hcs1@aluno.ufop.edu.br        | [PHdaCruzSantos](https://github.com/PHdaCruzSantos)    |
