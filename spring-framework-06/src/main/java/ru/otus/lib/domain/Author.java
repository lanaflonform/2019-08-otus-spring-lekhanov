package ru.otus.lib.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
public class Author {

    @Id
    @Column(name = "author_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;
    
    @Column(name = "lastname")
    private String lastname;
    
    @Column(name = "firstname")
    private String firstname;
    
    @Column(name = "middlename")
    private String middlename;
    
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "author", fetch=FetchType.EAGER)
    private Set<Book> books;
}
