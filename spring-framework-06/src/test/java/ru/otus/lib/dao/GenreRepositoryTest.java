package ru.otus.lib.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

import ru.otus.lib.domain.Genre;
import ru.otus.lib.repository.GenreRepositoryImpl;

@DisplayName("GenreRepository tests")
@DataJpaTest
@Import(GenreRepositoryImpl.class)
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    TransactionDbUnitTestExecutionListener.class
})
@DatabaseSetup("genre-dataset.xml")
public class GenreRepositoryTest {
    
    @Autowired
    private GenreRepositoryImpl genreDao;
    
    private Genre russianClassic = Genre.builder().genreId(1000L).name("Русская классика").build();
    private Genre foreignClassic = Genre.builder().genreId(1001L).name("Зарубежная классика").build();
    private Genre fantasy = Genre.builder().genreId(1002L).name("Фэнтези").build();
    
    @DisplayName("Genre creation")
    @Test
    void testCreateGenre() {
        Genre scienceFiction = Genre.builder().name("Научная фантастика").build();
        Genre createdGenre = genreDao.createGenre(scienceFiction);
        scienceFiction.setGenreId(createdGenre.getGenreId());
        assertThat(createdGenre.getGenreId()).isNotNull();
        assertThat(createdGenre).isEqualTo(scienceFiction);
        
    }

    @DisplayName("Genre modification")
    @Test
    void testUpdateGenre() {
        Genre genreForUpdate = Genre.builder().genreId(1002L).name("Fantasy").build();
        Genre result = genreDao.updateGenre(genreForUpdate);
        assertThat(result).isEqualTo(genreForUpdate);
    }

    @DisplayName("Genre deletion")
    @Test
    void testDeleteGenre() {
        Long genreId = Long.valueOf(1002);
        genreDao.deleteGenre(genreId);
        assertThat(genreDao.getById(genreId)).isEmpty();
    }

    @DisplayName("Search genre by id")
    @Test
    void testGetById() {
        Long genreId = Long.valueOf(1000);
        Optional<Genre> genre = genreDao.getById(genreId);
        assertThat(genre.get()).isEqualTo(russianClassic);
    }

    @DisplayName("Search genre by name")
    @Test
    void testGetByName() {
        Optional<Genre> genre = genreDao.getByName("Русская классика");
        assertThat(genre.get()).isEqualTo(russianClassic);
    }

    @DisplayName("Search of all genres")
    @Test
    void testGetAll() {
        List<Genre> genreList = genreDao.getAll();
        assertThat(genreList).hasSize(3).containsExactly(russianClassic, foreignClassic, fantasy);
    }

}
