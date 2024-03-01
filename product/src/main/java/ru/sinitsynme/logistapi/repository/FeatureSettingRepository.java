package ru.sinitsynme.logistapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sinitsynme.logistapi.entity.FeatureSetting;

@Repository
public interface FeatureSettingRepository extends JpaRepository<FeatureSetting, String> {

}
