import { Button, Checkbox, Form, Input, InputNumber, Select } from 'antd';
import React, { useCallback, useMemo, useState } from 'react';
import { useHistory, useParams } from 'react-router-dom';
import RemoveModal from '../context/RemoveModal';
import Header from '../shared/Header';
import useQueryParam from '../shared/useQueryParam';

import './EffectsEditor.css';

function EffectsEditor(): JSX.Element {
  const history = useHistory();
  const { state } = useParams<{ state: string }>();
  const [code] = useQueryParam('code');
  const onFinish = useCallback(
    (/* results */) => {
      // history.goBack();
    },
    []
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
    history.push(`/effects/edit?code=${code}`);
  }, [history, code]);

  return (
    <div className="effects-editor">
      <Header title="Edycja efektu kształcenia" />
      <div className="effects-controlls">
        {state === 'view' || state === 'create' ? (
          <Button
            className="effects-controlls-button"
            type="primary"
            onClick={() => history.goBack()}
          >
            Wstecz
          </Button>
        ) : null}
        {state === 'edit' ? (
          <Button
            className="effects-controlls-button"
            type="primary"
            onClick={() => history.goBack()}
          >
            Anuluj
          </Button>
        ) : null}
        {state === 'view' ? (
          <Button
            className="effects-controlls-button"
            type="primary"
            onClick={onEdit}
            disabled={modify}
          >
            Edytuj
          </Button>
        ) : null}

        {state === 'view' ? (
          <Button className="effects-remove-button" onClick={onRemove}>
            Usuń
          </Button>
        ) : null}
      </div>
      <Form
        className="effects-form"
        name="basic"
        initialValues={{
          code,
          description:
            'zna podstawy dotyczące architektury systemu Linux i jego eksploatacji jako serwera lub stacji roboczej użytkownika w systemach informatycznych opartych o platformę Linux',
          category: 'knowledge',
          prk: '6',
          bachelor: true,
        }}
        onFinish={onFinish}
      >
        <Form.Item
          className="effects-form-item"
          label="Kod"
          labelAlign="left"
          name="code"
          rules={[{ required: true, message: 'Wprowadź kod efektu!' }]}
        >
          <Input disabled={!(state === 'create')} />
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          label="Opis"
          labelAlign="left"
          name="description"
          rules={[{ required: true, message: 'Wprowadź opis efektu!' }]}
        >
          <Input.TextArea disabled={!modify} />
        </Form.Item>

        <Form.Item
          name="category"
          label="Kategoria"
          labelAlign="left"
          hasFeedback
          rules={[{ required: true, message: 'Wybierz kategorie efektu!' }]}
        >
          <Select placeholder="Wybierz kategorie efektu" disabled={!modify}>
            <Select.Option value="knowledge">Wiedza</Select.Option>
            <Select.Option value="skills">Umiejętności</Select.Option>
            <Select.Option value="social">Kompetencje społeczne</Select.Option>
          </Select>
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          label="Poziom PRK"
          labelAlign="left"
          name="prk"
          rules={[{ required: true, message: 'Wprowadź poziom PRK efektu!' }]}
        >
          <InputNumber min={1} max={8} disabled={!modify} />
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          name="bachelor"
          valuePropName="checked"
        >
          <Checkbox disabled={!modify}>Umożliwia inżnyniera</Checkbox>
        </Form.Item>

        <Form.Item
          className="effects-form-item"
          name="language"
          valuePropName="checked"
        >
          <Checkbox disabled={!modify}>Językowy</Checkbox>
        </Form.Item>

        <Form.Item className="effects-form-item">
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
}

export default EffectsEditor;
