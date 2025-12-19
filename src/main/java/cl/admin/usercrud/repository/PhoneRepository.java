package cl.admin.usercrud.repository;

import cl.admin.usercrud.models.PhoneEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository<PhoneEntity, UUID> {



}
