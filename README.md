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
      <img src="https://github-production-user-asset-6210df.s3.amazonaws.com/114687669/304253295-aa6d4037-1021-4fbd-afac-71475e620188.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20240212%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240212T233821Z&X-Amz-Expires=300&X-Amz-Signature=2c55ef4bb2e9d045d94cbe7397b09d68f22370aa8992c11646100aa98c6fa1ca&X-Amz-SignedHeaders=host&actor_id=0&key_id=0&repo_id=673943109" width="400px" />
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
      <img src="https://github-production-user-asset-6210df.s3.amazonaws.com/114687669/304253293-d142d922-cfc2-4855-8dbb-8928d216d8c8.jpg?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20240212%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240212T233900Z&X-Amz-Expires=300&X-Amz-Signature=d5aa497c6e392450aec9a5b7b10f390b7d72c416fae90e2f80a8a66725bf0567&X-Amz-SignedHeaders=host&actor_id=0&key_id=0&repo_id=673943109" width="400px" />
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
