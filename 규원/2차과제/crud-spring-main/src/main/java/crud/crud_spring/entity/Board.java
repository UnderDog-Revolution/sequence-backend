package crud.crud_spring.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String title;

    @Column
    private String contents;

    @Column
    private String filename;

    @Column
    private String filepath;
}
