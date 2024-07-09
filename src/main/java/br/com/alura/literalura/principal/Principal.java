package br.com.alura.literalura.principal;

import br.com.alura.literalura.model.*;
import br.com.alura.literalura.repository.AutorRepository;
import br.com.alura.literalura.service.ConsumoApi;
import br.com.alura.literalura.service.ConverteDados;

import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://gutendex.com/books";
    private ConverteDados conversor = new ConverteDados();
    private AutorRepository repositorio;

    public Principal(AutorRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {

        int opcao = -1;
        while(opcao != 0) {
            System.out.println("""
                    >>> Escolha sua opção:
                    1. Buscar livro pelo título
                    2. Listar livros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos em determinado ano
                    5. Listar livros em determinado idioma
                    6. Buscar Top 5 livros registrados com mais downloads
                    
                    0. Sair
                    >>>
                    """);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarLivroPorTitulo();
                    break;
                case 2:
                    buscarTodosLivros();
                    break;
                case 3:
                    buscarTodosAutores();
                    break;
                case 4:
                    buscarAutoresVivosPorAno();
                    break;
                case 5:
                    buscarLivrosPorIdioma();
                    break;
                case 6:
                    buscarTop5Livros();
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }
        }
    }

    private void buscarLivroPorTitulo() {
        System.out.println("Digite o título do livro: ");
        var titulo = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + "/?search=" + URLEncoder.encode(titulo));
        var resposta = conversor.obterDados(json, RespostaLivro.class);

        try {
            DadosLivro dadosLivro = resposta.livros().get(0);
            DadosAutor dadosAutor = dadosLivro.autores().get(0);

            Optional<Autor> optionalAutor = repositorio.findByNomeContainingIgnoreCase(dadosAutor.nome());
            Autor autor;
            if (optionalAutor.isPresent()) {
                autor = optionalAutor.get();
            } else {
                autor = new Autor(dadosAutor);
            }

            var livro = new Livro(dadosLivro);
            livro.setAutor(autor);
            autor.setLivros(List.of(livro));
            System.out.println(livro);
            repositorio.save(autor);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Título inexistente");
        }
    }

    private void buscarTodosLivros() {
        List<Livro> todosLivros = repositorio.buscarTodosLivros();
        todosLivros.forEach(System.out::println);
    }

    private void buscarTodosAutores() {
        List<Autor> todosAutores = repositorio.findAll();
        todosAutores.forEach(System.out::println);
    }

    private void buscarAutoresVivosPorAno() {
        System.out.println("Digite o ano:");
        var ano = leitura.nextInt();
        leitura.nextLine();
        List<Autor> vivos = repositorio.buscarAutoresVivosPorAno(ano);
        vivos.forEach(System.out::println);
    }

    private void buscarLivrosPorIdioma() {
        System.out.println("Escolha um idioma para a busca:");
        for (Idioma idioma: Idioma.values()) {
            System.out.println(idioma + " << " + Idioma.getCompleto(idioma.toString()));
        }
        var idiomaEscolhido = leitura.nextLine();
        try {
            var livros = repositorio.buscarLivrosPorIdioma(Idioma.valueOf(idiomaEscolhido.toUpperCase()));
            if (livros.isEmpty()) {
                System.out.println("Não há livros neste idioma no banco de dados");
                return;
            }
            livros.forEach(System.out::println);

        } catch (IllegalArgumentException e) {
            System.out.println("Idioma não disponível");
        }
    }

    private void buscarTop5Livros() {
        System.out.println("        TOP 5");
        List<Livro> livros = repositorio.top5LivrosDownload();
        livros.forEach(System.out::println);
    }
}
