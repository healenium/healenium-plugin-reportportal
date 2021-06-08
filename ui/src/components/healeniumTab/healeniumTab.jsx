import {HealeniumPattern} from "components/healeniumTab/healeniumPattern";

const patternNameNegativeTitle = 'Failed to find an element using locator';
const patternNamePositiveTitle = 'Using healed locator';

const patternConditionNegativeText = 'com.epam.healenium.handlers.proxy.BaseHandler - Failed to find an element using locator';
const attributeNegativeValue = 'healenium: false';

const patternConditionPositiveText = 'com.epam.healenium.service.impl.HealingServiceImpl - Using healed locator';
const attributePositiveValue = 'healenium: true';

export const HealeniumTab = (props) => {
  const {lib: {React}} = props;

  return (
    <div style={{display: 'flex', justifyContent: 'center'}}>
      <div style={{maxWidth: '1000px', width: '100%', display: 'flex', flexDirection: 'column'}}>
        <HealeniumPattern patternIndex='1' patternName={patternNameNegativeTitle}
                          patternCondition={patternConditionNegativeText}
                          attribute={attributeNegativeValue}
                          {...props}/>
        <HealeniumPattern patternIndex='2' patternName={patternNamePositiveTitle}
                          patternCondition={patternConditionPositiveText}
                          attribute={attributePositiveValue}
                          {...props}/>
      </div>
    </div>
  );
};
