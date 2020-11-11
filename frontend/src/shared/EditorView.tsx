import { Button, Form } from 'antd';
import React, { useCallback, useMemo, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import RemoveModal from '../context/RemoveModal';
import Header from './Header';

import './EditorView.less';

const EditorView: React.FC<{
  name: string;
  onFinish: (results: unknown) => void;
  queryParams: string;
  initialVals: any;
  header: string;
}> = (props) => {
  const history = useHistory();
  const { state } = useParams<{ state: string }>();
  // eslint-disable-next-line react/prop-types
  const { name, queryParams, onFinish, initialVals, children, header } = props;
  const handleFinish = useCallback(
    (results) => {
      history.goBack();
      onFinish(results);
    },
    [history, onFinish]
  );
  const [isModalVisible, showModal] = useState(false);
  const modify = useMemo(() => state === 'create' || state === 'edit', [state]);
  const onRemove = useCallback(() => {
    showModal(true);
  }, []);

  const onRemoveApprove = useCallback(() => {
    history.goBack();
  }, [history]);

  const onRemoveCancel = useCallback(() => {
    showModal(false);
  }, []);

  const onEdit = useCallback(() => {
    history.push(`/${name}/edit${queryParams}`);
  }, [history, name, queryParams]);

  return (
    <div className="editor">
      <Header title={header} />
      <div className="controlls">
        {state === 'view' || state === 'create' ? (
          <Button
            className="controlls-button"
            type="primary"
            onClick={() => history.goBack()}
          >
            Wstecz
          </Button>
        ) : null}
        {state === 'edit' ? (
          <Button
            className="controlls-button"
            type="primary"
            onClick={() => history.goBack()}
          >
            Anuluj
          </Button>
        ) : null}
        {state === 'view' ? (
          <Button
            className="controlls-button"
            type="primary"
            onClick={onEdit}
            disabled={modify}
          >
            Edytuj
          </Button>
        ) : null}

        {state === 'view' ? (
          <Button className="remove-button" onClick={onRemove}>
            Usuń
          </Button>
        ) : null}
      </div>
      <Form
        className="form"
        name="basic"
        initialValues={initialVals}
        onFinish={handleFinish}
      >
        {children}

        <Form.Item className="form-item">
          {modify ? (
            <Button type="primary" htmlType="submit">
              Zatwierdź
            </Button>
          ) : null}
        </Form.Item>
      </Form>
      <RemoveModal
        visible={isModalVisible}
        onOk={onRemoveApprove}
        onCancel={onRemoveCancel}
      />
    </div>
  );
};

export default EditorView;
