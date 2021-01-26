package ru.borisov.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.borisov.backend.entity.UserInfoEntity;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfoEntity, Long> {

    UserInfoEntity getByFirstNameAndLastName(String firstName, String lastName);

}
