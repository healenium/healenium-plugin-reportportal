package com.epam.reportportal.extension.healenium.dao;

public interface HealeniumDao {

    void updateStatus(boolean active);

    Boolean isActive();

}
