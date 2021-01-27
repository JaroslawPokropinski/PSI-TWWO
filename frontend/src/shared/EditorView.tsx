import React, { useCallback, useEffect, useMemo, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import { Button, Form } from 'antd';
import { Store } from 'antd/lib/form/interface';
import { CaretLeftOutlined, CaretRightOutlined } from '@ant-design/icons';
import RemoveModal from '../context/RemoveModal';
import Header from './Header';

import './EditorView.less';
import VerifyModal from './VerifyModal';
import { VersionHistory } from './versionHistory';
import { Versioned } from './Versioned';

const EditorView: React.FunctionComponent<{
  name: string;
  onFinish: (results: unknown) => void;
  queryParams: string;
  initialVals: Store;
  header: string;
  isVerified?: boolean;
  isVerifiable?: boolean;
  useArchive?: boolean;
  versionHistory?: VersionHistory<Store> | null;
  onRemove?: () => void;
  onVerify?: () => void;
  onDownload?: () => void | null;
  mapper?: (store: any) => Store;
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
  versionHistory = null,
  onRemove = () => {},
  onVerify = () => {},
  onDownload = null,
  mapper = (a) => a,
}) => {
  const history = useHistory();
  const { state } = useParams<{ state: string }>();
  const mappedVals = useMemo(() => mapper(initialVals), [initialVals, mapper]);

  const handleFinish = useCallback(
    (results) => {
      // history.goBack();
      onFinish(results);
    },
    [onFinish]
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

  const [archiveVals, setArchiveVals] = useState<Versioned<Store> | null>(null);
  const [hasPrevVer, setHasPrevVer] = useState(false);
  const [hasNextVer, setHasNextVer] = useState(false);

  useEffect(() => {
    if (versionHistory == null) return;
    setArchiveVals(versionHistory.getCurrent());
  }, [versionHistory, mapper]);

  useEffect(() => {
    if (versionHistory == null) return;

    setHasPrevVer(!versionHistory.isFirst());
    setHasNextVer(!versionHistory.isLast());
  }, [versionHistory, archiveVals]);

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
        {!useArchive ? null : (
          <Button
            className="controlls-button"
            type="primary"
            onClick={() => showHistory(!isShowingHistory)}
          >
            Historia
          </Button>
        )}

        {isShowingHistory ? (
          <>
            <Button
              className="controlls-button"
              type="primary"
              disabled={!hasPrevVer}
              onClick={() => {
                if (versionHistory == null) return;

                versionHistory.getPrev().then((prev) => {
                  setArchiveVals(null);
                  setArchiveVals(prev);
                });
              }}
            >
              <CaretLeftOutlined />
            </Button>
            <Button
              className="controlls-button"
              type="primary"
              disabled={!hasNextVer}
              onClick={() => {
                if (versionHistory == null) return;

                versionHistory.getNext().then((next) => {
                  setArchiveVals(null);
                  setArchiveVals(next);
                });
              }}
            >
              <CaretRightOutlined />
            </Button>
          </>
        ) : null}

        {onDownload == null ? null : (
          <Button
            className="controlls-button"
            type="primary"
            onClick={() => onDownload()}
          >
            Pobierz
          </Button>
        )}

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
          initialValues={mappedVals}
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
            initialValues={mapper(archiveVals.entity)}
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
