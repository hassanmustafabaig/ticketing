package com.venturedive.app.ticketing.entity;

import com.venturedive.app.ticketing.common.enumerations.SettingsType;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Settings {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "settings_key")
    private String settingsKey;

    @Column(name = "settings_value")
    private String settingsValue;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private SettingsType settingsType;

    @Column
    private String description;
}
