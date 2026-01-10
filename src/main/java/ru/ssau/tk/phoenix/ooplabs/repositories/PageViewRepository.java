package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.phoenix.ooplabs.entities.PageView;

import java.util.UUID;

public interface PageViewRepository extends JpaRepository<PageView, Long> {
    boolean existsBySessionIdAndUrl(UUID sessionId, String url);
}
