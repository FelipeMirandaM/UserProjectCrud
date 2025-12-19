package cl.admin.usercrud.repository;

import cl.admin.usercrud.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Boolean existsByEmail(String email);


}
