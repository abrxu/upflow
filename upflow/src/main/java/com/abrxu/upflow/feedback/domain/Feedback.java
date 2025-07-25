package com.abrxu.upflow.feedback.domain;

import com.abrxu.upflow.department.domain.Department;
import com.abrxu.upflow.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@Table(name = "tbl_feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_feedback")
    private Long id;

    @Size(max = 255)
    @NotNull(message = "You need to fill the input with a message.")
    @NotBlank(message = "You need to fill the input with a message.")
    @Column(name = "txt_message")
    private String message;

    @NotNull(message = "It occurred an error during the feedback creation. Please try again.")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_department", updatable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @ManyToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id_user", nullable = false, updatable = false)
    private User user;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback that = (Feedback) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
