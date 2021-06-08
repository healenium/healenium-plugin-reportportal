const itemStyle = {
  display: 'flex',
  alignItems: 'center',
  height: '40px',
  fontSize: '13px'
};

const itemEmptySpaceStyle = {
  width: '10%',
  textIndent: '15px'
};

const itemTitleStyle = {
  width: '15%',
  color: '#999'
};

export const HealeniumPatternItem = (props) => {
  const {title, value, ...rest} = props;
  const {lib: {React}} = rest;

  return (
    <div style={itemStyle}>
      <div style={itemEmptySpaceStyle}/>
      <div style={itemTitleStyle}>
        {title}
      </div>
      <div>
        {value}
      </div>
    </div>
  );
};
