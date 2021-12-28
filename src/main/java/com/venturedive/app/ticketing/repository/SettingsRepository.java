package com.venturedive.app.ticketing.repository;

import com.venturedive.app.ticketing.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Settings findSettingsBySettingsKey(String settingsKey);
}