package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Autor;
import br.com.alura.literalura.model.Idioma;
import br.com.alura.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT l FROM Autor a JOIN a.livros l")
    List<Livro> buscarTodosLivros();

    @Query("SELECT a FROM Autor a WHERE :ano BETWEEN a.anoNascimento AND a.anoFalecimento")
    List<Autor> buscarAutoresVivosPorAno(int ano);

    @Query("SELECT l FROM Autor a JOIN a.livros l WHERE :idioma = l.idioma")
    List<Livro> buscarLivrosPorIdioma(Idioma idioma);

    @Query("SELECT l FROM Autor a JOIN a.livros l ORDER BY l.numeroDownloads DESC LIMIT 5")
    List<Livro> top5LivrosDownload();
}
