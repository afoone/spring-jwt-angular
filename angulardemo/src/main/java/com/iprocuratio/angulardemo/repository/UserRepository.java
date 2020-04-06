package com.iprocuratio.angulardemo.repository;





import java.util.List;

import com.iprocuratio.angulardemo.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    public List<User> findByUser(String name);

}