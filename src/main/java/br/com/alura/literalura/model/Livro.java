package br.com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Idioma idioma;

    private int numeroDownloads;
    private String titulo;

    @ManyToOne
    private Autor autor;

    public Livro() {}

    public Livro(DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.idioma = Idioma.valueOf(dadosLivro.idiomas().get(0).toUpperCase());
        this.numeroDownloads = dadosLivro.downloads();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public int getNumeroDownloads() {
        return numeroDownloads;
    }

    public void setNumeroDownloads(int numeroDownloads) {
        this.numeroDownloads = numeroDownloads;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    @Override
    public String toString() {
        return "\n==================" +
                "\n    LIVRO" +
                "\nTitulo: " + this.titulo +
                "\nNúmero de downloads: " + this.numeroDownloads +
                "\nAutor: " + this.autor.getNome() +
                "\nIdioma: " + this.idioma +
                "\n==================\n";
    }
}
