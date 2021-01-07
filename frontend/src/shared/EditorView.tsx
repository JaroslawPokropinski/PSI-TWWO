import React, { useCallback, useMemo, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import { Button, Form } from 'antd';
import { Store } from 'antd/lib/form/interface';
import { CaretLeftOutlined, CaretRightOutlined } from '@ant-design/icons';
import RemoveModal from '../context/RemoveModal';
import Header from './Header';

import './EditorView.less';
import VerifyModal from './VerifyModal';

const EditorView: React.FunctionComponent<{
  name: string;
  onFinish: (results: unknown) => void;
  queryParams: string;
  initialVals: Store;
  header: string;
  isVerified?: boolean;
  isVerifiable?: boolean;
  useArchive?: boolean;
  archiveVals?: Store;
  onRemove?: () => void;
  onVerify?: () => void;
}> = ({
  name = '',
  queryParams = '',
  onFinish = () => null,
  initialVals = {},
  header = '',
  children = [],
  isVerified = false,
  isVerifiable = true,
  useArchive = false,
  archiveVals = null,
  onRemove = () => {},
  onVerify = () => {},
}) => {
  const history = useHistory();
  const { state } = useParams<{ state: string }>();
  // eslint-disable-next-line react/prop-types
  // const { name, queryParams, onFinish, initialVals, children, header } = props;
  const handleFinish = useCallback(
    (results) => {
      history.goBack();
      onFinish(results);
    },
    [history, onFinish]
  );
  const [isModalVisible, showModal] = useState(false);
  const [isVerifyModalVisible, showVerifyModal] = useState(false);
  const [isShowingHistory, showHistory] = useState(false);
  const modify = useMemo(() => state === 'create' || state === 'edit', [state]);
  const onRemoveAction = useCallback(() => {
    showModal(true);
  }, []);

  const onRemoveApprove = useCallback(() => {
    onRemove();
    history.goBack();
  }, [history, onRemove]);

  const onRemoveCancel = useCallback(() => {
    showModal(false);
  }, []);

  const onEdit = useCallback(() => {
    history.push(`/${name}/edit${queryParams}`);
  }, [history, name, queryParams]);

  const onVerifyAction = useCallback(() => {
    showVerifyModal(true);
  }, []);

  const onVerifyApprove = useCallback(() => {
    showVerifyModal(false);
    onVerify();
  }, [onVerify]);

  const onVerifyCancel = useCallback(
    () => {
      showVerifyModal(false);
      // history.goBack();
    },
    [
      /* history */
    ]
  );

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

        {state === 'view' && isVerifiable ? (
          <Button
            className="controlls-button"
            type="primary"
            onClick={onVerifyAction}
            disabled={isVerified}
          >
            Weryfikuj
          </Button>
        ) : null}

        <Button
          className="controlls-button"
          type="primary"
          onClick={() => showHistory(!isShowingHistory)}
        >
          Historia
        </Button>

        {isShowingHistory ? (
          <>
            <Button className="controlls-button" type="primary">
              <CaretLeftOutlined />
            </Button>
            <Button className="controlls-button" type="primary">
              <CaretRightOutlined />
            </Button>
          </>
        ) : null}

        {state === 'view' ? (
          <Button className="remove-button" onClick={onRemoveAction}>
            Usuń
          </Button>
        ) : null}
      </div>
      <div className="form-container">
        <Form
          className="form"
          name="basic"
          initialValues={initialVals}
          onFinish={handleFinish}
        >
          {React.Children.map(children, (child, i) => {
            if (i !== 0) return null;
            return child;
          })}

          <Form.Item className="form-item">
            {modify ? (
              <Button type="primary" htmlType="submit">
                Zatwierdź
              </Button>
            ) : null}
          </Form.Item>
        </Form>
        {useArchive && archiveVals != null && isShowingHistory ? (
          <Form
            className="form"
            name="basic"
            initialValues={archiveVals}
            onFinish={handleFinish}
          >
            {React.Children.map(children, (child, i) => {
              if (i !== 1) return null;
              return child;
            })}
          </Form>
        ) : null}
      </div>
      <RemoveModal
        visible={isModalVisible}
        onOk={onRemoveApprove}
        onCancel={onRemoveCancel}
      />
      <VerifyModal
        visible={isVerifyModalVisible}
        onOk={onVerifyApprove}
        onCancel={onVerifyCancel}
      />
    </div>
  );
};

export default EditorView;
