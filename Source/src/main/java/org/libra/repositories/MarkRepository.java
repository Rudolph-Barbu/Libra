package org.libra.repositories;

import org.libra.entities.implementation.MarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MarkRepository extends JpaRepository<MarkEntity, Integer>
{
    Optional<MarkEntity> findMarkEntityByPresentationIdAndEmail(Integer presentationId, String email);
}