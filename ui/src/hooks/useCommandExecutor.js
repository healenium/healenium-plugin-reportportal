export const useCommandExecutor = ({
  lib: {useSelector},
  selectors: {activeProjectSelector, globalIntegrationsSelector},
  utils: {fetch, URLS}
}) => {
  const activeProject = useSelector(activeProjectSelector);
  const integrations = useSelector(globalIntegrationsSelector);

  return (command, data = {}, params = {}) => {
    if (!integrations || !integrations[0]) {
      return Promise.reject('No integrations found');
    }
    return fetch(URLS.projectIntegrationByIdCommand(activeProject, integrations[0].id, command),
      {
        method: 'PUT',
        data,
        ...params
      })
  };
};
