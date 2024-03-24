package ru.sinitsynme.logistapi.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.sinitsynme.logistapi.entity.enums.DocumentType;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private DocumentType type;
    private String name;
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(id, document.id) && type == document.type && Objects.equals(name, document.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name);
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", type=" + type +
                ", name='" + name + '\'' +
                '}';
    }
}
