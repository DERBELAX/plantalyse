package repository;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
