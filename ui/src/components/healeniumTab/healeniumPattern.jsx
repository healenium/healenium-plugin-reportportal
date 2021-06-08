import {HealeniumPatternItem} from "components/healeniumTab/healeniumPatternItem";

const patternStyle = {
    backgroundColor: '#f8f8f8',
    border: 'solid 2px #f8f8f8',
    borderRadius: '2px',
    height: '40px',
    display: 'flex',
    alignItems: 'center',
    fontSize: '13px',
    fontFamily: 'OpenSans-Semibold',
    color: '#434343',
    boxShadow: '0 2px 0 #eaeaea'
};

const titleIndexStyle = {
  width: '10%',
  textIndent: '15px'
}

const conditionTitle = 'String';
const attributeTitle = 'Attributes (and)';

export const HealeniumPattern = (props) => {
  const {patternIndex, patternName, patternCondition, attribute, ...rest} = props;
  const {lib: {React}} = rest;

  return (
    <div>
      <div style={patternStyle}>
        <div style={titleIndexStyle}>
          {patternIndex}
        </div>
        <div>
          {patternName}
        </div>
      </div>
      <div>
        <HealeniumPatternItem title={conditionTitle} value={patternCondition} {...props}/>
        <HealeniumPatternItem title={attributeTitle} value={attribute} {...props}/>
      </div>
    </div>
  );
};
