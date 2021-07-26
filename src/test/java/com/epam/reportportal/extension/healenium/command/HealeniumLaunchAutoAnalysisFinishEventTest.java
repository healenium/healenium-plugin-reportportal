package com.epam.reportportal.extension.healenium.command;

import com.epam.reportportal.extension.healenium.dao.HealeniumDao;
import com.epam.reportportal.extension.healenium.dao.impl.HealeniumDaoImpl;
import com.epam.reportportal.extension.healenium.event.launch.HealeniumLaunchAutoAnalysisFinishEvent;
import com.epam.ta.reportportal.dao.ItemAttributeRepository;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.dao.TestItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.mockito.Mockito.mock;

public class HealeniumLaunchAutoAnalysisFinishEventTest {

    private final NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
    private final HealeniumDao healeniumDao = mock(HealeniumDaoImpl.class);
    private final LaunchRepository launchRepository = mock(LaunchRepository.class);
    private final TestItemRepository testItemRepository = mock(TestItemRepository.class);
    private final ItemAttributeRepository itemAttributeRepository = mock(ItemAttributeRepository.class);

    private final HealeniumLaunchAutoAnalysisFinishEvent healeniumLaunchAutoAnalysisFinishEvent =
            new HealeniumLaunchAutoAnalysisFinishEvent(
                    launchRepository,
                    testItemRepository,
                    itemAttributeRepository,
                    healeniumDao);

    @Test
    void firstTest() {

        healeniumLaunchAutoAnalysisFinishEvent.
        Assertions.assertTrue(true);

    }

}
