package ru.otus.lib.service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.jline.reader.LineReader;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import ru.otus.lib.domain.Book;
import ru.otus.lib.domain.BookComment;
import ru.otus.lib.exception.BookCommentNotFoundException;
import ru.otus.lib.exception.BookNotFoundException;
import ru.otus.lib.exception.UserAlreadyCommentedBookException;
import ru.otus.lib.repository.BookCommentRepository;

@Service
public class BookCommentServiceImpl implements BookCommentService {

    private BookCommentRepository bookCommentDao;
    
    private final BookService bookService;
    
    private final MessageSource messageSource;
    
    private final LineReader reader;

    public BookCommentServiceImpl(BookCommentRepository bookCommentDao, @Lazy BookService bookService, MessageSource messageSource, @Lazy LineReader reader) {
        this.bookCommentDao = bookCommentDao;
        this.bookService = bookService;
        this.messageSource = messageSource;
        this.reader = reader;
    }

    @Override
    public BookComment createBookComment() {
        String bookId = reader.readLine(messageSource.getMessage("bookComment.enter.book-id", null, Locale.getDefault()));
        
        Book book = bookService.getById(bookId).orElseThrow(() -> new BookNotFoundException(reader.readLine(
                messageSource.getMessage("book.not-found", new Object[] { bookId }, Locale.getDefault()))));
        
        String userName = reader.readLine(messageSource.getMessage("bookComment.enter.user-name", null, Locale.getDefault()));
        Optional<BookComment> bookComment = bookCommentDao.findByBookIdAndUserLogin(book.getId(), userName);
        if(bookComment.isPresent()) {
            throw new UserAlreadyCommentedBookException(messageSource.getMessage("bookComment.user-already-commented-book", new Object[] {userName, book.getTitle()}, Locale.getDefault())); 
        }
        
        String userComment = reader.readLine(messageSource.getMessage("bookComment.enter.user-comment", null, Locale.getDefault()));
        
        return bookCommentDao.save(BookComment.builder()
                .book(book)
                .userLogin(userName)
                .userComment(userComment)
                .build());
    }

    @Override
    public BookComment updateBookComment() {
        String bookCommentId = reader.readLine(messageSource.getMessage("bookComment.get-id", null, Locale.getDefault()));
        checkBookCommentExists(bookCommentId);

        String bookId = reader.readLine(messageSource.getMessage("bookComment.enter.book-id", null, Locale.getDefault()));
        Book book = bookService.getById(bookId).orElseThrow(() -> new BookNotFoundException(reader.readLine(
                messageSource.getMessage("book.not-found", new Object[] { bookId }, Locale.getDefault()))));

        String userName = reader.readLine(messageSource.getMessage("bookComment.enter.user-name", null, Locale.getDefault()));
        Optional<BookComment> bookComment = bookCommentDao.findByBookIdAndUserLogin(book.getId(), userName);
        if(bookComment.isPresent() && !bookComment.get().getBook().getId().equals(bookId)) {
            throw new UserAlreadyCommentedBookException(messageSource.getMessage("bookComment.user-already-commented-book", new Object[] {userName, book.getTitle()}, Locale.getDefault())); 
        }
        String userComment = reader.readLine(messageSource.getMessage("bookComment.enter.user-comment", null, Locale.getDefault()));
        
        return bookCommentDao.save(BookComment.builder()
                .id(bookCommentId)
                .book(book)
                .userLogin(userName)
                .userComment(userComment)
                .build());
    }

    @Override
    public void deleteBookComment() {
        String bookCommentId = reader.readLine(messageSource.getMessage("bookComment.get-id", null, Locale.getDefault()));
        bookCommentDao.deleteById(bookCommentId);
        System.out.println(messageSource.getMessage("operation.successful", null, Locale.getDefault()));
    }

    @Override
    public Optional<BookComment> getById() {
        String bookCommentId = reader.readLine(messageSource.getMessage("bookComment.get-id", null, Locale.getDefault()));
        return bookCommentDao.findById(bookCommentId);
    }

    @Override
    public List<BookComment> getByBookId() {
        String bookId = reader.readLine(messageSource.getMessage("bookComment.enter.book-id", null, Locale.getDefault()));
        return bookCommentDao.findByBookId(bookId);
    }

    @Override
    public boolean checkBookCommentExists(String bookCommentId) {
        boolean exists = bookCommentDao.existsById(bookCommentId);
        if (exists) {
            return true;
        } else
            throw new BookCommentNotFoundException(messageSource.getMessage("bookComment.not-found",
                    new Object[] { bookCommentId }, Locale.getDefault()));
    }

}
