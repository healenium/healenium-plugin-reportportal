import {HealeniumTab} from 'components/healeniumTab';

window.RP.registerPlugin({
  name: 'healenium',
  extensions: [{
    name: 'healenium',
    title: 'healenium',
    type: 'uiExtension:settingsTab',
    component: HealeniumTab
  }]
});
