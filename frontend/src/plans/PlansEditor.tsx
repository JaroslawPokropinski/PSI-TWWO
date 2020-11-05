import React from 'react';
import EditorView from '../shared/EditorView';

function PlansEditor(): JSX.Element {
  return (
    <EditorView
      name="plans"
      initialVals={{}}
      onFinish={() => null}
      queryParams=""
      header="Edycja planów uczenia"
    />
  );
}

export default PlansEditor;
