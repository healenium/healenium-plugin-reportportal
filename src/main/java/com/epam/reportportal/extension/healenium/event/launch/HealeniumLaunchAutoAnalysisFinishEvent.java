package com.epam.reportportal.extension.healenium.event.launch;

import com.epam.reportportal.extension.event.LaunchAnalysisFinishEvent;
import com.epam.reportportal.extension.healenium.dao.HealeniumDao;
import com.epam.reportportal.extension.healenium.utils.Constants;
import com.epam.ta.reportportal.dao.ItemAttributeRepository;
import com.epam.ta.reportportal.dao.LaunchRepository;
import com.epam.ta.reportportal.dao.TestItemRepository;
import com.epam.ta.reportportal.entity.ItemAttribute;
import com.epam.ta.reportportal.entity.enums.LogLevel;
import com.epam.ta.reportportal.entity.item.TestItem;
import com.epam.ta.reportportal.entity.launch.Launch;
import com.epam.ta.reportportal.exception.ReportPortalException;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.context.ApplicationListener;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.ta.reportportal.ws.model.ErrorType.LAUNCH_NOT_FOUND;

public class HealeniumLaunchAutoAnalysisFinishEvent implements ApplicationListener<LaunchAnalysisFinishEvent> {

	private static final Map<String, String> HEALING_ATTRIBUTES = ImmutableMap.of(
			Boolean.TRUE.toString(), "Using healed locator",
			Boolean.FALSE.toString(), "New element locator have not been found"
	);

	private final LaunchRepository launchRepository;
	private final TestItemRepository testItemRepository;
	private final ItemAttributeRepository itemAttributeRepository;
	private final HealeniumDao healeniumDao;

	public HealeniumLaunchAutoAnalysisFinishEvent(LaunchRepository launchRepository, TestItemRepository testItemRepository, ItemAttributeRepository itemAttributeRepository, HealeniumDao healeniumDao) {
		this.launchRepository = launchRepository;
		this.testItemRepository = testItemRepository;
		this.itemAttributeRepository = itemAttributeRepository;
		this.healeniumDao = healeniumDao;
	}

	@Override
	public void onApplicationEvent(LaunchAnalysisFinishEvent event) {
		if (!isHealeniumEnabled()) {
			return;
		}
		Launch launch = launchRepository.findById(event.getSource())
				.orElseThrow(() -> new ReportPortalException(LAUNCH_NOT_FOUND, event.getSource()));

		List<Long> allItemIds = testItemRepository.findTestItemsByLaunchId(launch.getId()).stream()
				.map(TestItem::getItemId)
				.collect(Collectors.toList());

		List<TestItem> matchedItems = HEALING_ATTRIBUTES.entrySet().stream()
				.flatMap(attr -> addItemAttribute(allItemIds, attr.getValue(), attr.getKey()).stream())
				.collect(Collectors.toList());

		if (CollectionUtils.isNotEmpty(matchedItems)) {
			addLaunchAttribute(launch);
		}
	}

	private void addLaunchAttribute(Launch launch) {
		ItemAttribute healeniumAttr = new ItemAttribute(null, Constants.HEALENIUM, false);
		healeniumAttr.setLaunch(launch);
		launch.getAttributes().add(healeniumAttr);
		itemAttributeRepository.save(healeniumAttr);
	}

	private Set<TestItem> addItemAttribute(List<Long> allItemIds, String pattern, String attrValue) {
		List<Long> childItemIds = testItemRepository.selectIdsByStringLogMessage(allItemIds, LogLevel.WARN_INT, pattern);
		List<TestItem> childItems = childItemIds.stream()
				.map(testItemRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
		Set<TestItem> testItems = childItemIds.stream()
				.map(testItemRepository::findById)
				.filter(Optional::isPresent)
				.map(child -> testItemRepository.findById(child.get().getParentId()))
				.flatMap(Optional::stream)
				.collect(Collectors.toSet());
		Stream.concat(childItems.stream(), testItems.stream()).forEach(item -> {
			ItemAttribute itemAttribute = new ItemAttribute("healing", attrValue, false);
			itemAttribute.setTestItem(item);
			item.getAttributes().add(itemAttribute);
			itemAttributeRepository.save(itemAttribute);
		});
		return testItems;
	}

	private Boolean isHealeniumEnabled() {
		return healeniumDao.isActive();
	}
}
