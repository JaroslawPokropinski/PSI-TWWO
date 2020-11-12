import React, { useCallback, useMemo } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import EditorView from '../shared/EditorView';
import useQueryParam from '../shared/useQueryParam';

import './PlansEditor.css';

function PlansEditor(): JSX.Element {
  const history = useHistory();
  const [code] = useQueryParam('code');
  const { state } = useParams<{ state: string }>();
  const modify = useMemo(() => state === 'create' || state === 'edit', [state]);
  const onFinish = useCallback(
    (/* results */) => {
      // history.goBack();
    },
    [
      /* history */
    ]
  );

  return (
    <EditorView
      name="programs"
      initialVals={{
        code,
      }}
      onFinish={onFinish}
      queryParams={`?code=${code}`}
      header="Edycja planów uczenia"
    />
  );
}

export default PlansEditor;
