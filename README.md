# 📚 Gerenciador de Biblioteca PDF - README - PEDRO HENRIQUE DUARTE DE OLIVEIRA - 20190003968

## 📌 Descrição

Este projeto representa um sistema de gerenciamento de bibliotecas de arquivos PDF operado via linha de comando.
O sistema organiza documentos em bibliotecas independentes, permitindo-se adicionar, listar, buscar, editar e excluir registros de arquivos.
A persistência dos metadados é feita em arquivos CSV.
Todas as funcionalidades seguem princípios da Programação Orientada a Objetos (POO), incluindo encapsulamento, herança e tratamento de exceções.

## 📂 Estrutura de Arquivos

Ao adicionar arquivos PDF, o sistema cria:
- Um diretório para cada biblioteca.
- Subdiretórios por autor dentro da biblioteca.

	src/
	├── interfaceusuario/        -> Interface CLI (menu interativo)
	├── gerenciador/             -> Lógica de negócio (inserção, edição, deleção)
	├── modelos/                 -> Modelos de dados: Livro, NotaDeAula, Slide
	├── persistencia/            -> Leitura/gravação em CSV e configuração
	└── excecoes/                -> Exceções personalizadas


### `InterfaceUsuario.java`
- Ponto de entrada da aplicação (linha de comando).
- Exibe menu interativo.
- Gerencia entrada do usuário e chama métodos do gerenciador.

### `GerenciadorBiblioteca.java`
- Lida com arquivos PDF e os registros da biblioteca ativa.
- Centraliza a lógica de adicionar, editar, listar, buscar, deletar documentos.
- Mantém somente os registros da biblioteca atual em foco.

### `ArquivoPDF.java`
- Classe abstrata base com `autores`, `título`, `caminho`.

### `Livro.java`, `NotaDeAula.java`, `Slide.java`
- Subclasses de `ArquivoPDF` que implementam atributos específicos conforme o tipo de documento.

### `Persistencia.java`
- Lê e grava os dados no arquivo `biblioteca_dados.csv`.
- Controla os caminhos das bibliotecas salvos em `biblioteca_path.txt`.

### `Excecoes.java`
- Classe de exceção customizada para mensagens específicas.

## 💾 Persistência

- `biblioteca_path.txt`: armazena os caminhos das bibliotecas conhecidas.
   * O último caminho é a biblioteca atualmente ativa.
- `biblioteca_dados.csv`: armazena os metadados de todos os documentos registrados.
   * Cada linha representa um registro.
   * O sistema filtra os dados com base no caminho da biblioteca ativa.

## ⚙️ Funcionalidades

 1. CRIAR NOVA BIBLIOTECA
   - Permite criar um novo diretório para armazenar os arquivos PDF e seus registros.
   - A biblioteca criada se torna a ativa e será usada nas demais operações.
   - Persistência automática do caminho no arquivo `biblioteca_path.txt`.

2. ALTERNAR ENTRE BIBLIOTECAS
   - O sistema mantém histórico dos caminhos das bibliotecas acessadas.
   - Ao alternar para outra biblioteca existente, os registros e operações passam a valer somente para ela.

3. ADICIONAR DOCUMENTOS
   - Tipos suportados: Livro, Nota de Aula, Slide.
   - Informações obrigatórias por tipo:
     * Livro: autores, título, subtítulo, área de conhecimento, ano, caminho PDF.
     * Nota de Aula: autores, título, subtítulo, disciplina, caminho PDF.
     * Slide: autores, título, disciplina, caminho PDF.
   - O PDF é copiado para um subdiretório nomeado com o nome do primeiro autor.
   - Os metadados são persistidos no arquivo `biblioteca_dados.csv`.

4. LISTAR REGISTROS
   - Mostra apenas os registros pertencentes à biblioteca ativa.
   - Exibe título, autores, tipo de documento e caminho do arquivo PDF.

5. BUSCAR REGISTROS
   - Busca por palavras-chave no título, subtítulo ou nomes de autores.
   - Apenas registros da biblioteca ativa são considerados.

6. EDITAR REGISTRO
   - Permite editar todos os campos de um registro existente.
   - Ao alterar autores, o arquivo PDF é movido para a pasta do novo autor.
   - A edição substitui o registro antigo, mantendo integridade dos dados.

7. DELETAR REGISTRO
   - Permite deletar registros individualmente.
   - Remove o arquivo PDF do disco e o registro correspondente do CSV.
   - Funciona apenas dentro da biblioteca ativa.

8. DELETAR BIBLIOTECA ATUAL
   - Remove fisicamente o diretório e seus subdiretórios.
   - Remove todos os registros associados do `biblioteca_dados.csv`.
   - Atualiza a lista de bibliotecas no `biblioteca_path.txt`.
   - Se não houver mais bibliotecas, o sistema solicita a criação de uma nova.

## 🔍 Detalhamento de Métodos

### InterfaceUsuario.java
| Método                         | Descrição |
|-------------------------------|-----------|
| `main()`                      | Inicia o programa e menu principal. |
| `adicionarLivro()`            | Coleta dados e adiciona um novo livro. |
| `adicionarNotaDeAula()`       | Adiciona uma nota de aula com metadados. |
| `adicionarSlide()`            | Adiciona um slide com disciplina. |
| `listarRegistros()`           | Lista registros da biblioteca ativa. |
| `buscarRegistros()`           | Busca por termo nos registros ativos. |
| `editarRegistro()`            | Permite editar campos de um registro. |
| `deletarRegistro()`           | Remove registro e PDF correspondente. |

### GerenciadorBiblioteca.java
| Método                                 | Descrição |
|----------------------------------------|-----------|
| `adicionarArquivo(ArquivoPDF)`         | Copia o PDF para a pasta do autor e grava o registro. |
| `listarRegistros()`                    | Retorna todos os registros. |
| `listarRegistrosBibliotecaAtual()`     | Filtra registros da biblioteca atual. |
| `buscarRegistros(String termo)`        | Busca geral entre todos os registros. |
| `buscarRegistrosBibliotecaAtual()`     | Busca restrita à biblioteca ativa. |
| `editarRegistro(int, ArquivoPDF)`      | Atualiza metadados e move o PDF se necessário. |
| `deletarArquivo(int)`                  | Deleta PDF e remove do CSV. |
| `deletarBiblioteca()`                  | Deleta todos os arquivos da biblioteca e atualiza arquivos auxiliares. |

## 🚀 EXECUÇÃO

1. Execute o programa principal:
   	InterfaceUsuario.java

2. Execute o .jar:
	java -jar GerenciadorBiblioteca.jar
