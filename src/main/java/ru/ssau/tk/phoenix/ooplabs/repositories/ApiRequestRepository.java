package ru.ssau.tk.phoenix.ooplabs.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ssau.tk.phoenix.ooplabs.entities.ApiRequest;

public interface ApiRequestRepository extends JpaRepository<ApiRequest, Long> {
}
