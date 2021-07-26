import {HealeniumPattern} from 'components/healeniumTab/healeniumPattern';
import {useCommandExecutor} from 'hooks/useCommandExecutor';
import {ENABLE_HEALENIUM_PLUGIN} from 'constants/pluginCommands';
import {GET_HEALENIUM_PLUGIN_STATUS} from 'constants/pluginCommands';
import Parser from 'html-react-parser'

const patternNameNegativeTitle = 'Failed to find an element using locator';
const patternNamePositiveTitle = 'Using healed locator';

const patternConditionNegativeText = 'New element locators have not been found';
const attributeNegativeValue = 'healenium: false';

const patternConditionPositiveText = 'Using healed locator';
const attributePositiveValue = 'healenium: true';

const toggleLabel = 'Pattern-Analysis';
const defaultMessage = 'If ON - analysis starts as soon as any launch finished<br/>If OFF - not automatic, but can be invoked manually';

const list_header = {
  position: 'sticky',
  top: '-1px',
  zIndex: '$Z_INDEX-STICKY-TOOLBAR',
  display: 'flex',
  flexDirection: 'row',
  justifyContent: 'flex-start',
  alignItems: 'center',
  padding: '15px 0',
  marginBottom: '-1px',
  color: '$COLOR--gray-47',
  backgroundColor: '$COLOR--white-two',
  fontSize: '12px',
  fontFamily: '$FONT-REGULAR',
  lineHeight: '27px',
  boxShadow: '0 3px 1px -3px $COLOR--gray-91'
};

const caption = {
  width: '110px',
  paddingRight: '15px',
  paddingLeft: '90px',
  fontSize: '13px',
  color: '$COLOR--charcoal-grey'
};

const description = {
  width: '220px',
  flexGrow: '1',
  flexShrink: '1',
  padding: '0 15px',
  color: '$COLOR--gray-60',
  lineHeight: '14px'
};

export const HealeniumTab = (props) => {

  const {lib: {React},
        components: {InputBigSwitcher}} = props;
  const [paState, setPaState] = React.useState(true);
  const executeCommand = useCommandExecutor(props);

  executeCommand(GET_HEALENIUM_PLUGIN_STATUS, {})
    .then((state) => {
      setPaState(state);
      setLoading(false);
    })
    .catch((err) => {
      dispatch(showErrorNotification({message: err.message}));
      setLoading(false);
    })

  const handleOnChangeSwitcher = (state) => {
    executeCommand(ENABLE_HEALENIUM_PLUGIN, {state: state})
      .then(() => {
        setPaState(state);
        setLoading(false);
      })
      .catch((err) => {
        dispatch(showErrorNotification({message: err.message}));
        setLoading(false);
      })

  };



  return (
    <div style={{display: 'flex', justifyContent: 'center'}}>
      <div style={{maxWidth: '1000px', width: '100%', display: 'flex', flexDirection: 'column'}}>
        <div style={list_header}>
          <span style={caption}>{toggleLabel}</span>
          <InputBigSwitcher
            disabled={false}
            title={''}
            mobileDisabled
            value={paState}
            onChange={handleOnChangeSwitcher}
          />
          <p style={description}>{Parser(defaultMessage)}</p>
        </div>
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
