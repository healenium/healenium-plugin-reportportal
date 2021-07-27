package com.epam.reportportal.extension.healenium.dao.impl;

import com.epam.reportportal.extension.healenium.dao.HealeniumDao;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class HealeniumDaoImpl implements HealeniumDao {

    private static final String UPDATE_STATUS =
            "UPDATE healenium.healenium_info SET active = :active WHERE healenium_info.id = 1";

    private static final String HEALENIUM_IS_ACTIVE =
            "SELECT healenium_info.active FROM healenium.healenium_info where id = 1";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public HealeniumDaoImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void updateStatus(boolean active) {
        jdbcTemplate.update(UPDATE_STATUS, new MapSqlParameterSource().addValue("active", active));
    }

    @Override
    public Boolean isActive() {
        Boolean aBoolean = jdbcTemplate.queryForObject(HEALENIUM_IS_ACTIVE, new MapSqlParameterSource(), Boolean.class);
        return aBoolean;
    }
}
