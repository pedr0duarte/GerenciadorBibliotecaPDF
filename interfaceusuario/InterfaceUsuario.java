package interfaceusuario;

import gerenciador.GerenciadorBiblioteca;
import modelos.*;
import persistencia.Persistencia;
import excecoes.Excecoes;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class InterfaceUsuario {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        GerenciadorBiblioteca gerenciador = null;

        try {
            String caminhoString = Persistencia.carregarCaminho();
            Path caminhoSalvo = (caminhoString != null) ? Path.of(caminhoString) : null;

            if (caminhoSalvo == null || !Files.exists(caminhoSalvo)) {
                System.out
                        .println("Nenhuma biblioteca encontrada. Digite um caminho para criar a primeira biblioteca:");
                String novoCaminho = scanner.nextLine();
                Files.createDirectories(Path.of(novoCaminho));
                Persistencia.salvarCaminho(novoCaminho);
                gerenciador = new GerenciadorBiblioteca(novoCaminho);
            } else {
                gerenciador = new GerenciadorBiblioteca(caminhoSalvo.toString());
            }

            boolean executando = true;
            while (executando) {
                System.out.println("\n--- Menu Biblioteca PDF ---");
                System.out.println("1. Adicionar Livro");
                System.out.println("2. Adicionar Nota de Aula");
                System.out.println("3. Adicionar Slide");
                System.out.println("4. Listar Registros");
                System.out.println("5. Buscar Registros");
                System.out.println("6. Editar Registro");
                System.out.println("7. Deletar Registro");
                System.out.println("8. Criar Nova Biblioteca");
                System.out.println("9. Alternar Biblioteca");
                System.out.println("10. Deletar Biblioteca Atual");
                System.out.println("11. Sair");
                System.out.print("Escolha uma opção: ");

                int opcao = Integer.parseInt(scanner.nextLine());
                switch (opcao) {
                    case 1 -> adicionarLivro(scanner, gerenciador);
                    case 2 -> adicionarNotaDeAula(scanner, gerenciador);
                    case 3 -> adicionarSlide(scanner, gerenciador);
                    case 4 -> listarRegistros(gerenciador);
                    case 5 -> buscarRegistros(scanner, gerenciador);
                    case 6 -> editarRegistro(scanner, gerenciador);
                    case 7 -> deletarRegistro(scanner, gerenciador);
                    case 8 -> {
                        System.out.println("Digite o novo caminho da biblioteca:");
                        String novoCaminho = scanner.nextLine();
                        Files.createDirectories(Path.of(novoCaminho));
                        Persistencia.salvarCaminho(novoCaminho);
                        gerenciador = new GerenciadorBiblioteca(novoCaminho);
                        System.out.println("Nova biblioteca criada e ativada.");
                    }
                    case 9 -> {
                        System.out.println("Digite o caminho da biblioteca existente:");
                        String novoCaminho = scanner.nextLine();
                        if (Files.exists(Path.of(novoCaminho))) {
                            Persistencia.salvarCaminho(novoCaminho);
                            gerenciador = new GerenciadorBiblioteca(novoCaminho);
                            System.out.println("Biblioteca alternada com sucesso.");
                        } else {
                            System.out.println("Caminho não encontrado.");
                        }
                    }
                    case 10 -> {
                        String caminhoAtualStr = Persistencia.carregarCaminho();
                        Path caminhoAtual = (caminhoAtualStr != null) ? Path.of(caminhoAtualStr) : null;
                        if (caminhoAtual != null && Files.exists(caminhoAtual)) {
                            try (var stream = Files.walk(caminhoAtual)) {
                                List<Path> arquivos = stream.sorted(Comparator.reverseOrder()).toList();
                                for (Path p : arquivos) {
                                    Files.deleteIfExists(p);
                                }
                                Persistencia.removerRegistrosPorDiretorio(caminhoAtualStr);
                                Persistencia.removerCaminho(caminhoAtualStr);

                                List<String> restantes = Persistencia.carregarTodosCaminhos();
                                if (!restantes.isEmpty()) {
                                    String novo = restantes.get(restantes.size() - 1);
                                    Persistencia.salvarCaminho(novo);
                                    gerenciador = new GerenciadorBiblioteca(novo);
                                    System.out.println("Biblioteca deletada. Alternado para: " + novo);
                                } else {
                                    gerenciador = null;
                                    System.out.println("Biblioteca deletada. Nenhuma restante.");
                                    System.out.println("Informe o caminho para criar nova biblioteca:");
                                    String novoCaminho = scanner.nextLine();
                                    Files.createDirectories(Path.of(novoCaminho));
                                    Persistencia.salvarCaminho(novoCaminho);
                                    gerenciador = new GerenciadorBiblioteca(novoCaminho);
                                }
                            }
                        } else {
                            System.out.println("Biblioteca atual não encontrada.");
                        }
                    }
                    case 11 -> executando = false;
                    default -> System.out.println("Opção inválida.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static void adicionarLivro(Scanner scanner, GerenciadorBiblioteca gerenciador) {
        try {
            System.out.println("Informe os autores separados por vírgula:");
            List<String> autores = Arrays.asList(scanner.nextLine().split(","));
            System.out.println("Informe o título:");
            String titulo = scanner.nextLine();
            System.out.println("Informe o subtítulo:");
            String subtitulo = scanner.nextLine();
            System.out.println("Informe a área de conhecimento:");
            String area = scanner.nextLine();
            System.out.println("Informe o ano de publicação:");
            int ano = Integer.parseInt(scanner.nextLine());
            System.out.println("Informe o caminho do arquivo PDF:");
            String caminho = scanner.nextLine();

            Livro livro = new Livro(autores, titulo, subtitulo, area, ano, caminho);
            gerenciador.adicionarArquivo(livro);
            System.out.println("Livro adicionado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar livro: " + e.getMessage());
        }
    }

    private static void adicionarNotaDeAula(Scanner scanner, GerenciadorBiblioteca gerenciador) {
        try {
            System.out.println("Informe os autores separados por vírgula:");
            List<String> autores = Arrays.asList(scanner.nextLine().split(","));
            System.out.println("Informe o título:");
            String titulo = scanner.nextLine();
            System.out.println("Informe o subtítulo:");
            String subtitulo = scanner.nextLine();
            System.out.println("Informe a disciplina:");
            String disciplina = scanner.nextLine();
            System.out.println("Informe o caminho do arquivo PDF:");
            String caminho = scanner.nextLine();

            NotaDeAula nota = new NotaDeAula(autores, titulo, subtitulo, disciplina, caminho);
            gerenciador.adicionarArquivo(nota);
            System.out.println("Nota de aula adicionada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar nota de aula: " + e.getMessage());
        }
    }

    private static void adicionarSlide(Scanner scanner, GerenciadorBiblioteca gerenciador) {
        try {
            System.out.println("Informe os autores separados por vírgula:");
            List<String> autores = Arrays.asList(scanner.nextLine().split(","));
            System.out.println("Informe o título:");
            String titulo = scanner.nextLine();
            System.out.println("Informe a disciplina:");
            String disciplina = scanner.nextLine();
            System.out.println("Informe o caminho do arquivo PDF:");
            String caminho = scanner.nextLine();

            Slide slide = new Slide(autores, titulo, disciplina, caminho);
            gerenciador.adicionarArquivo(slide);
            System.out.println("Slide adicionado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao adicionar slide: " + e.getMessage());
        }
    }

    private static void listarRegistros(GerenciadorBiblioteca gerenciador) {
        List<ArquivoPDF> registros = gerenciador.listarRegistrosBibliotecaAtual();
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
        } else {
            for (int i = 0; i < registros.size(); i++) {
                ArquivoPDF registro = registros.get(i);
                System.out.println((i + 1) + " - " + registro + " (" + registro.getCaminhoArquivo() + ")");
            }
        }
    }

    private static void buscarRegistros(Scanner scanner, GerenciadorBiblioteca gerenciador) {
        System.out.println("Digite o termo de busca:");
        String termo = scanner.nextLine();
        List<ArquivoPDF> encontrados = gerenciador.buscarRegistrosBibliotecaAtual(termo);
        if (encontrados.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
        } else {
            encontrados.forEach(System.out::println);
        }
    }

    private static void editarRegistro(Scanner scanner, GerenciadorBiblioteca gerenciador) throws IOException {
        listarRegistros(gerenciador);
        System.out.println("Digite o número do registro a editar:");
        int numero = Integer.parseInt(scanner.nextLine());
        int indice = numero - 1;

        if (indice < 0 || indice >= gerenciador.listarRegistros().size()) {
            System.out.println("Índice inválido.");
            return;
        }

        ArquivoPDF antigo = gerenciador.listarRegistros().get(indice);

        System.out.println("Novo título:");
        String novoTitulo = scanner.nextLine();
        System.out.println("Novos autores (separados por vírgula):");
        List<String> novosAutores = Arrays.asList(scanner.nextLine().split(","));
        String novoCaminho = antigo.getCaminhoArquivo();

        ArquivoPDF novo;
        if (antigo instanceof Livro livro) {
            System.out.println("Novo subtítulo:");
            String novoSubtitulo = scanner.nextLine();
            System.out.println("Nova área de conhecimento:");
            String novaArea = scanner.nextLine();
            System.out.println("Novo ano:");
            int novoAno = Integer.parseInt(scanner.nextLine());
            novo = new Livro(novosAutores, novoTitulo, novoSubtitulo, novaArea, novoAno, novoCaminho);
        } else if (antigo instanceof NotaDeAula nota) {
            System.out.println("Novo subtítulo:");
            String novoSubtitulo = scanner.nextLine();
            System.out.println("Nova disciplina:");
            String novaDisciplina = scanner.nextLine();
            novo = new NotaDeAula(novosAutores, novoTitulo, novoSubtitulo, novaDisciplina, novoCaminho);
        } else if (antigo instanceof Slide slide) {
            System.out.println("Nova disciplina:");
            String novaDisciplina = scanner.nextLine();
            novo = new Slide(novosAutores, novoTitulo, novaDisciplina, novoCaminho);
        } else {
            System.out.println("Tipo de registro desconhecido.");
            return;
        }

        try {
            gerenciador.editarRegistro(indice, novo);
            System.out.println("Registro atualizado com sucesso!");
        } catch (Excecoes e) {
            System.out.println("Erro ao mover o arquivo: " + e.getMessage());
        }
    }

    private static void deletarRegistro(Scanner scanner, GerenciadorBiblioteca gerenciador) throws IOException {
        List<ArquivoPDF> registros = gerenciador.listarRegistros();
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro encontrado.");
            return;
        }

        System.out.println("Selecione o número do registro a ser deletado:");
        for (int i = 0; i < registros.size(); i++) {
            System.out.println((i + 1) + " - " + registros.get(i));
        }

        int numero = Integer.parseInt(scanner.nextLine());
        int indice = numero - 1;

        if (gerenciador.deletarArquivo(indice)) {
            System.out.println("Registro deletado com sucesso.");
        } else {
            System.out.println("Não foi possível deletar o registro.");
        }
    }
}