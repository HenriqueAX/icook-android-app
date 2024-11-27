
# iCook App

O iCook é um aplicativo de receitas desenvolvido para Android, onde os usuários podem buscar, adicionar e visualizar receitas. Este projeto foi desenvolvido em Kotlin e utiliza um banco de dados SQLite para armazenar receitas.

## Funcionalidades

- **Adicionar Receitas:** Os usuários podem adicionar novas receitas, incluindo nome, tempo de preparo, avaliação, ingredientes, modo de preparo e uma imagem.
- **Buscar Receitas:** É possível buscar receitas com base no nome utilizando a barra de busca.
- **Visualizar Receitas:** As receitas adicionadas podem ser visualizadas com seus detalhes, incluindo uma imagem, ingredientes e instruções.
- **Login Simples:** O login é feito utilizando as credenciais fixas de administrador (`usuário: admin`, `senha: 1234`). Caso o login falhe, o usuário é informado de que o login é inválido.

## Estrutura do Projeto

O projeto está organizado da seguinte forma:

- **MainActivity:** Tela de login do aplicativo.
- **AddRecipeActivity:** Tela onde os usuários podem adicionar uma nova receita ao banco de dados.
- **SearchRecipeActivity:** Tela que permite aos usuários buscar receitas existentes.
- **RecipeResultActivity:** Exibe os resultados da busca.
- **RecipeDetailActivity:** Exibe os detalhes de uma receita selecionada.
- **DatabaseHelper:** Classe que gerencia o banco de dados SQLite para adicionar e recuperar receitas.

## Requisitos

- **Android Studio:** Para rodar o projeto no ambiente de desenvolvimento Android.
- **Kotlin:** Linguagem de programação utilizada.
- **SQLite:** Banco de dados local para armazenar receitas.

## Como Rodar o Projeto

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/HenriqueAX/icook-android-app.git
   ```

2. **Abra o projeto no Android Studio:**
   - No Android Studio, vá em `File -> Open` e selecione o diretório do projeto clonado.

3. **Configure um dispositivo virtual com Android 10:**
   - Para garantir melhor compatibilidade, crie um emulador com a versão Android 10 no Android Studio.
   - No Android Studio, acesse o `Device Manager`, clique em `Create Virtual Device`, selecione um dispositivo e de preferência escolha a versão Android 10 (API Level 29) para o emulador.

4. **Compile e rode o projeto:**
   - Conecte o dispositivo físico ou emulador criado e clique no botão `Run` para rodar o aplicativo.

## Estrutura de Banco de Dados

O banco de dados SQLite armazena as receitas em uma tabela chamada `recipes` com os seguintes campos:

- `id`: ID único da receita (Primary Key).
- `name`: Nome da receita.
- `prep_time`: Tempo de preparo da receita (em minutos).
- `rating`: Avaliação da receita (1 a 5).
- `ingredients`: Ingredientes necessários para a receita.
- `instructions`: Instruções de preparo.
- `image_res_id`: ID do recurso da imagem (opcional).
- `image_uri`: URI da imagem adicionada pelo usuário (opcional).

## Adicionando uma Receita

Para adicionar uma nova receita:
1. Clique no ícone de adição na tela principal.
2. Preencha os campos obrigatórios, como nome, tempo de preparo, avaliação, ingredientes e instruções.
3. Opcionalmente, selecione uma imagem da galeria.
4. Clique no botão "Adicionar Receita" para salvar a receita no banco de dados.

## Credenciais de Login

As credenciais padrão para acessar o aplicativo são:
- **Usuário:** `admin`
- **Senha:** `1234`

Caso as credenciais estejam incorretas, uma mensagem de erro será exibida.
