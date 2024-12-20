# iCook App

O **iCook** é um aplicativo Android projetado para ajudar os usuários a descobrir, adicionar e gerenciar suas receitas favoritas de forma simples e intuitiva. Seja para explorar novos pratos ou compartilhar suas criações culinárias, o iCook oferece uma plataforma amigável para aprimorar sua experiência na cozinha.

## Funcionalidades

### Autenticação de Usuário
- **Login com Google:** Autenticação segura e fácil utilizando sua conta Google.
- **Login com Email e Senha:** Opção de autenticação manual via email e senha.
- **Cadastro de Usuários:** Permite que novos usuários se registrem com nome, email e senha.
- **Firebase Authentication:** Suporte robusto para gerenciamento de sessões e estados de autenticação.
- **Gerenciamento Local de Usuários:** Armazenamento de usuários no banco de dados local SQLite.

### Gerenciamento de Receitas
- **Buscar Receitas:** Pesquisa eficiente através de uma lista abrangente de receitas com filtragem em tempo real conforme você digita.
- **Adicionar Receitas:** Crie e adicione suas próprias receitas, incluindo detalhes como nome, tempo de preparação, avaliação, ingredientes, instruções e imagens.
- **Visualizar Detalhes da Receita:** Clique em qualquer receita para ver informações detalhadas, incluindo ingredientes e etapas de preparação.
- **Armazenamento Local:** Novas receitas são armazenadas no banco de dados local SQLite.

### Interface do Usuário
- **Exibição de Boas-Vindas:** O aplicativo exibe uma mensagem personalizada de boas-vindas com o nome do usuário.
- **Integração com RecyclerView:** Exibição eficiente de listas de receitas com rolagem suave e atualizações dinâmicas de conteúdo.
- **Design Responsivo:** Layouts otimizados para diferentes tamanhos e orientações de tela.

### Manipulação de Imagens
- **Upload de Imagens:** Selecione e faça upload de imagens da galeria do seu dispositivo para acompanhar suas receitas.
- **Imagens Placeholder:** Exibição de imagens padrão quando nenhuma imagem específica é fornecida para uma receita.

### Funcionalidade de Logout
- **Logout Seguro:** Saia facilmente da sua conta, garantindo que seus dados permaneçam protegidos.

## Tecnologias e Recursos Utilizados
- **Linguagem de Programação:** Kotlin
- **Android SDK:** Para desenvolvimento do aplicativo.
- **Firebase Authentication:** Para autenticação segura de usuários.
- **Google Sign-In:** Simplifica o processo de login com contas Google.
- **RecyclerView:** Para exibição eficiente e dinâmica de listas.
- **SQLite:** Gerenciamento de banco de dados local para armazenamento de receitas e usuários.
- **FileProvider:** Compartilhamento seguro de arquivos de imagem entre aplicativos.

## Requisitos
- **Sistema Operacional:** Windows, macOS ou Linux.
- **Android Studio:** De preferência a versão mais atualizada.
- **Android SDK:** Incluindo as ferramentas de build necessárias.
- **Dispositivo Virtual:** Emulador Android configurado (recomenda-se Android 10 ou superior).
- **Google Account:** Para autenticação via Firebase.

## Como Instalar e Utilizar o Projeto

1. **Clone o repositório:**

   ```bash
   git clone https://github.com/HenriqueAX/icook-android-app.git
   ```

2. **Abra o projeto no Android Studio:**

   - No Android Studio, vá em `File -> Open` e selecione o diretório do projeto clonado.

3. **Realize o build do projeto:**

   - Após abrir o projeto, sincronize as dependências e compile o app:
     - Clique em **`File -> Sync Project with Gradle Files`** para sincronizar as dependências do projeto.
     - Em seguida, vá em **`Build -> Make Project`** ou pressione `Ctrl+F9` para compilar o código.

4. **Configure um dispositivo virtual:**

   - Para garantir melhor compatibilidade, crie um emulador com a versão Android 10 no Android Studio.
   - No Android Studio, acesse o **`Device Manager`**, clique em **`Create Virtual Device`**, selecione um dispositivo e, de preferência, escolha a versão Android 10 (API Level 29) para o emulador.

5. **Compile e rode o projeto:**

   - Conecte o dispositivo físico ou emulador criado.
   - Clique no botão **`Run`** (ícone de play verde) na barra de ferramentas ou vá em **`Run -> Run 'app'`** para instalar e rodar o aplicativo.

## Uso do Aplicativo

1. **Inicie o App:**

   - Ao abrir, você será solicitado a fazer login usando sua conta Google ou email e senha.

2. **Cadastrar Usuário:**

   - Caso não tenha uma conta, clique em "Cadastrar-se" e preencha as informações solicitadas.

3. **Buscar Receitas:**

   - Use a barra de busca no topo para digitar o nome de uma receita.
   - A lista atualiza em tempo real conforme você digita, exibindo as receitas correspondentes.

4. **Adicionar uma Nova Receita:**

   - Clique no botão de adicionar (ícone de mais) localizado na parte inferior da tela.
   - Preencha os detalhes da receita, incluindo nome, tempo de preparação, avaliação, ingredientes, instruções e selecione uma imagem.
   - Salve a receita para adicioná-la à sua coleção.

5. **Visualizar Detalhes da Receita:**

   - Clique em qualquer receita na lista para ver suas informações detalhadas, incluindo ingredientes e etapas de preparação.

6. **Logout:**

   - Clique no botão de logout no canto superior direito para sair da sua conta de forma segura.

7. **Visualizar Nome do Usuário:**

   - O nome do usuário logado é exibido no canto superior esquerdo da tela de busca de receitas.

