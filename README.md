# Fitcore App 

Aplicativo móvel Android desenvolvido com **Jetpack Compose**, voltado para alunos e funcionários de academias. Oferece uma experiência fluida e moderna para acesso e execução de treinos.

## 🧱 Arquitetura

Este app segue princípios modernos de arquitetura e design de software:

### 🛠 Hexagonal Architecture (Ports and Adapters)
- Separação entre **núcleo de domínio** e **interfaces externas**
- Independência de frameworks e tecnologias específicas
- Fácil de testar, manter e evoluir

### 📚 DDD — Domain-Driven Design
- Organização centrada no **domínio do negócio**
- Casos de uso claros e bem definidos
- Entidades e serviços do domínio modelam o comportamento do sistema

### 🔎 Princípios SOLID
- Código desacoplado e coeso
- Mais fácil de manter, entender e escalar
- Responsabilidades bem definidas entre as camadas

##  Tecnologias Utilizadas

- [Jetpack Compose](https://developer.android.com/jetpack/compose) — UI declarativa moderna para Android
- Kotlin
- Android SDK

##  Funcionalidades Implementadas

-  **Fluxo de login**
-  **Lista de treinos**
-  **Execução guiada de treinos**
-  **Tradução local dos nomes e instruções dos exercícios**
-  **Perfil do usuário**
-  **Conexão com  microserviços**

##  Requisitos

- Android 7.0 (API 24) ou superior
- Conexão com as API's da plataforma Fitcore (Backend e Microservices)

##  Como rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/seu-usuario/fitcore-app.git
   ```
2. Abra o projeto no **Android Studio**.
3. Conecte um dispositivo ou use um emulador.
4. Abra o terminal e digite: "./gradlew installDebug" para compilar e executar.
Obs: Importante ter java instalado e ter configurado o Android Studio com as variáveis de ambiente.

## 📂 Estrutura do Projeto

```
app/
├── core/                # Regras de negócio e casos de uso (Domínio)
├── ui/                  # Telas e componentes visuais (Compose)
├── viewmodel/           # Lógica de apresentação
├── model/               # Modelos de dados
├── repository/          # Implementações dos adaptadores (ex: rede, local)
└── utils/               # Funções auxiliares
```


## 🧑‍💻 Contribuindo

Pull requests são bem-vindos! Para contribuições maiores, por favor, abra uma issue antes para discutir o que você gostaria de mudar.

## 📄 Licença

Este projeto está sob a licença [MIT](LICENSE).

---

Feito com Equipe Fitcore
