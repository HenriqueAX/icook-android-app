# iCook App

O **iCook** é um aplicativo Android projetado para ajudar os usuários a descobrir, adicionar e gerenciar suas receitas favoritas de forma simples e intuitiva. Seja para explorar novos pratos ou compartilhar suas criações culinárias, o iCook oferece uma plataforma amigável para aprimorar sua experiência na cozinha.

## Funcionalidades

- **Autenticação de Usuário:**
  - **Login com Google:** Autenticação segura e fácil utilizando sua conta Google.
  - **Firebase Authentication:** Suporte robusto para gerenciamento de sessões e estados de autenticação.

- **Gerenciamento de Receitas:**
  - **Buscar Receitas:** Pesquisa eficiente através de uma lista abrangente de receitas com filtragem em tempo real conforme você digita.
  - **Adicionar Receitas:** Crie e adicione suas próprias receitas, incluindo detalhes como nome, tempo de preparação, avaliação, ingredientes, instruções e imagens.
  - **Visualizar Detalhes da Receita:** Clique em qualquer receita para ver informações detalhadas, incluindo ingredientes e etapas de preparação.

- **Interface do Usuário:**
  - **Integração com RecyclerView:** Exibição eficiente de listas de receitas com rolagem suave e atualizações dinâmicas de conteúdo.
  - **Design Responsivo:** Layouts otimizados para diferentes tamanhos e orientações de tela.

- **Manipulação de Imagens:**
  - **Upload de Imagens:** Selecione e faça upload de imagens da galeria do seu dispositivo para acompanhar suas receitas.
  - **Imagens Placeholder:** Exibição de imagens padrão quando nenhuma imagem específica é fornecida para uma receita.

- **Funcionalidade de Logout:**
  - **Logout Seguro:** Saia facilmente da sua conta, garantindo que seus dados permaneçam protegidos.

## Tecnologias e Recursos Utilizados

- **Linguagem de Programação:** Kotlin
- **Android SDK:** Para desenvolvimento do aplicativo.
- **Firebase Authentication:** Para autenticação segura de usuários.
- **Google Sign-In:** Simplifica o processo de login com contas Google.
- **RecyclerView:** Para exibição eficiente e dinâmica de listas.
- **SQLite:** Gerenciamento de banco de dados local para armazenamento de receitas.
- **FileProvider:** Compartilhamento seguro de arquivos de imagem entre aplicativos.

## Como Instalar e Utilizar o Projeto

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

## Como utilizar o aplicativo

1. **Inicie o App:**
   - Ao abrir, você será solicitado a fazer login usando sua conta Google.

2. **Buscar Receitas:**
   - Use a barra de busca no topo para digitar o nome de uma receita.
   - A lista atualiza em tempo real conforme você digita, exibindo as receitas correspondentes.

3. **Adicionar uma Nova Receita:**
   - Clique no botão de adicionar (ícone de mais) localizado na parte inferior da tela.
   - Preencha os detalhes da receita, incluindo nome, tempo de preparação, avaliação, ingredientes, instruções e selecione uma imagem.
   - Salve a receita para adicioná-la à sua coleção.

4. **Visualizar Detalhes da Receita:**
   - Clique em qualquer receita na lista para ver suas informações detalhadas, incluindo ingredientes e etapas de preparação.

5. **Logout:**
   - Clique no botão de logout no canto superior direito para sair da sua conta Google de forma segura.
