package ru.job4j.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.domain.Room;

import java.util.Optional;

public interface RoomRepository extends CrudRepository<Room, Integer> {
}
