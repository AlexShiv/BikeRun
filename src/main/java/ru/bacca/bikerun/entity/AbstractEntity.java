package ru.bacca.bikerun.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // получают по собственной таблице
public abstract class AbstractEntity implements Serializable {

    protected AbstractEntity() {
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    protected long id = 0L;

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;

    }
}
